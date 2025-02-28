/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.event.UserDisconnectEvent;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@ApiStatus.Internal
public final class PacketEventsImplHelper {

    private PacketEventsImplHelper() {
    }

    private static void tryRewriteBuffer(ProtocolPacketEvent event, int preProcessIndex) {
        Object buf = event.getByteBuf();
        if (event.isCancelled()) {
            // completely clear packet data if event gets cancelled
            ByteBufHelper.clear(buf);
            return;
        }

        // if the event didn't get cancelled, try to look up the packet wrapper which
        // was used last to access the packet event contents
        PacketWrapper<?> lastWrapper = event.getLastUsedWrapper();
        if (lastWrapper == null) {
            // no wrapper found, reset reader index and pass through the pipeline
            ByteBufHelper.readerIndex(buf, preProcessIndex);
            return;
        }

        // some plugins/servers modify the netty pipeline and pass down pooled byte buffers,
        // which don't allow writing data over the initial capacity
        if (ByteBufHelper.maxCapacity(buf) == Integer.MAX_VALUE) {
            // enough capacity found, simply clear the existing buffer
            ByteBufHelper.clear(buf);
        } else {
            // slower version, create a completely new bytebuf
            event.setByteBuf(ByteBufHelper.newBuffer(buf, ByteBufHelper.readerIndex(buf)));
            lastWrapper.buffer = event.getByteBuf();
            ByteBufHelper.release(buf);
        }

        // write contents of last used wrapper to empty
        // buffer and pass it through the pipeline
        lastWrapper.writeVarInt(event.getPacketId());
        lastWrapper.write();
    }

    public static @Nullable ProtocolPacketEvent handlePacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation, PacketSide side
    ) throws Exception {
        if (side == PacketSide.SERVER) {
            return handleClientBoundPacket(channel, user, player, buffer, autoProtocolTranslation);
        } else {
            return handleServerBoundPacket(channel, user, player, buffer, autoProtocolTranslation);
        }
    }

    public static @Nullable PacketSendEvent handleClientBoundPacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation
    ) throws Exception {
        if (!ByteBufHelper.isReadable(buffer)) {
            return null;
        }

        // create packet event based on state and packet id
        int preProcessIndex = ByteBufHelper.readerIndex(buffer);
        PacketSendEvent event = EventCreationUtil.createSendEvent(channel, user, player, buffer, autoProtocolTranslation);

        // call events of listeners, reset reader index to start of content
        int processIndex = ByteBufHelper.readerIndex(buffer);
        PacketEvents.getAPI().getEventManager().callEvent(event,
                () -> ByteBufHelper.readerIndex(buffer, processIndex));

        try {
            // rewrite the buffer if the event was mutated
            tryRewriteBuffer(event, preProcessIndex);

            // call potential post-handling tasks
            if (event.hasPostTasks()) {
                for (Runnable task : event.getPostTasks()) {
                    task.run();
                }
            }
        } catch (Exception exception) {
            throw new PacketProcessException("Error while processing packet of " + user + ": " + exception, exception);
        }

        return event;
    }

    public static @Nullable PacketReceiveEvent handleServerBoundPacket(
            Object channel, User user, Object player, Object buffer,
            boolean autoProtocolTranslation
    ) throws Exception {
        if (!ByteBufHelper.isReadable(buffer)) {
            return null;
        }

        // create packet event based on state and packet id
        int preProcessIndex = ByteBufHelper.readerIndex(buffer);
        PacketReceiveEvent event = EventCreationUtil.createReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);

        // call events of listeners, reset reader index to start of content
        int processIndex = ByteBufHelper.readerIndex(buffer);
        PacketEvents.getAPI().getEventManager().callEvent(event,
                () -> ByteBufHelper.readerIndex(buffer, processIndex));

        try {
            // rewrite the buffer if the event was mutated
            tryRewriteBuffer(event, preProcessIndex);

            // call potential post-handling tasks
            if (event.hasPostTasks()) {
                for (Runnable task : event.getPostTasks()) {
                    task.run();
                }
            }
        } catch (Exception exception) {
            throw new PacketProcessException("Error while processing packet of " + user + ": " + exception, exception);
        }

        return event;
    }

    public static void handleDisconnection(Object channel, @Nullable UUID uuid) {
        synchronized (channel) {
            User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);

            if (user != null) {
                UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
                PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
                PacketEvents.getAPI().getProtocolManager().removeUser(user.getChannel());
            }

            if (uuid == null) {
                // Only way to be sure of removing a channel
                ProtocolManager.CHANNELS.entrySet().removeIf(pair -> pair.getValue() == channel);
            } else {
                // This is the efficient way that we should prefer
                ProtocolManager.CHANNELS.remove(uuid);
            }
        }
    }
}
