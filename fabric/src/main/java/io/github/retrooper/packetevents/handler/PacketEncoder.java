/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package io.github.retrooper.packetevents.handler;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.exception.CancelPacketException;
import com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDisconnect;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
public class PacketEncoder extends ChannelOutboundHandlerAdapter {

    private final PacketSide side;
    public User user;
    public Player player;
    private ChannelPromise promise;

    public PacketEncoder(PacketSide side, User user) {
        this.side = side;
        this.user = user;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf in)) {
            ctx.write(msg, promise);
            return;
        }

        // Handle promise management first (matches Spigot)
        ChannelPromise oldPromise = this.promise != null && !this.promise.isSuccess() ? this.promise : null;
        promise.addListener(p -> this.promise = oldPromise);
        this.promise = promise;

        // Process the packet and execute post-send tasks (matches Spigot)
        handlePacket(ctx, in, promise);

        // Check for empty packets last (matches Spigot)
        if (!in.isReadable()) {
            in.release();
            throw CancelPacketException.INSTANCE;
        }

        // Forward the packet if readable
        ctx.write(in, promise);
    }

    private @Nullable ProtocolPacketEvent handlePacket(ChannelHandlerContext ctx, ByteBuf buffer, ChannelPromise promise) throws Exception {
        // Process the packet using PacketEventsImplHelper (similar to Spigot)
        ProtocolPacketEvent protocolPacketEvent = PacketEventsImplHelper.handlePacket(
            ctx.channel(), this.user, this.player, buffer, false, this.side
        );

        // Execute post-send tasks (required for cross-platform support)
        if (protocolPacketEvent instanceof PacketSendEvent packetSendEvent && packetSendEvent.hasTasksAfterSend()) {
            promise.addListener((p) -> {
                for (Runnable task : packetSendEvent.getTasksAfterSend()) {
                    task.run();
                }
            });
        }
        return protocolPacketEvent;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Handle CancelPacketException (similar to Spigot)
        if (ExceptionUtil.isException(cause, CancelPacketException.class)) {
            return;
        }

        // Handle InvalidDisconnectPacketSend (similar to Spigot)
        if (ExceptionUtil.isException(cause, InvalidDisconnectPacketSend.class)) {
            return;
        }

        // Handle PacketProcessException (similar to Spigot)
        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
        if (didWeCauseThis && (user == null || user.getEncoderState() != ConnectionState.HANDSHAKING)) {
            if (!isMinecraftServerInstanceDebugging()) {
                if (PacketEvents.getAPI().getSettings().isFullStackTraceEnabled()) {
                    cause.printStackTrace();
                } else {
                    PacketEvents.getAPI().getLogManager().warn(cause.getMessage());
                }
            }

            if (PacketEvents.getAPI().getSettings().isKickOnPacketExceptionEnabled()) {
                try {
                    if (user != null && player instanceof ServerPlayer) {
                        // Use cross-platform PacketEvents wrapper for disconnect packet
                        WrapperPlayServerDisconnect disconnectPacket = new WrapperPlayServerDisconnect(
                            net.kyori.adventure.text.Component.text("Invalid packet")
                        );
                        user.sendPacket(disconnectPacket);
                    }
                } catch (Exception ignored) {
                    // Ignore exceptions during disconnect (similar to Spigot)
                }
                ctx.channel().close();

                if (player instanceof ServerPlayer serverPlayer) {
                    // Schedule delayed kick (Fabric-specific, using Minecraft's scheduler)
                    serverPlayer.getServer().execute(() -> {
                        serverPlayer.connection.disconnect(Component.literal("Invalid packet"));
                    });
                }

                if (user != null) {
                    PacketEvents.getAPI().getLogManager().warn(
                        "Disconnected " + user.getProfile().getName() + " due to invalid packet!"
                    );
                }
            }
        }

        super.exceptionCaught(ctx, cause);
    }

    // Placeholder for Minecraft server debugging check (Fabric-specific)
    private boolean isMinecraftServerInstanceDebugging() {
        // TODO: Implement Fabric-specific debugging check
        return false;
    }
}