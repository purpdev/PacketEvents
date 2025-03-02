/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2021 ViaVersion and contributors
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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.github.retrooper.packetevents.injector.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

// Thanks to ViaVersion for the compression method.
@ChannelHandler.Sharable
public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    public ProxiedPlayer player;
    public User user;
    public boolean handledCompression;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext originalCtx, ByteBuf buffer, ChannelPromise promise) {
        ChannelHandlerContext ctx = this.tryFixCompressorOrder(originalCtx, buffer);
        int firstReaderIndex = buffer.readerIndex();
        PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(ctx.channel(), user, player,
                buffer, false);
        int readerIndex = buffer.readerIndex();
        PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> buffer.readerIndex(readerIndex));
        if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
                ByteBufHelper.clear(packetSendEvent.getByteBuf());
                packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
                packetSendEvent.getLastUsedWrapper().write();
            } else {
                buffer.readerIndex(firstReaderIndex);
            }
            ctx.write(buffer, promise);
        } else {
            ReferenceCountUtil.release(packetSendEvent.getByteBuf());
        }
        if (packetSendEvent.hasPostTasks()) {
            for (Runnable task : packetSendEvent.getPostTasks()) {
                task.run();
            }
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            super.write(ctx, msg, promise);
            return;
        }
        ByteBuf buf = (ByteBuf) msg;
        if (!buf.isReadable()) {
            buf.release();
        } else {
            this.read(ctx, buf, promise);
        }
    }

    private ChannelHandlerContext tryFixCompressorOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (this.handledCompression) {
            return ctx;
        }
        ChannelPipeline pipe = ctx.pipeline();
        List<String> pipeNames = pipe.names();
        if (pipeNames.contains("frame-prepender-compress")) {
            // "modern" version, no need to handle this here
            this.handledCompression = true;
            return ctx;
        }
        int compressorIndex = pipeNames.indexOf("compress");
        if (compressorIndex == -1) {
            return ctx;
        }
        this.handledCompression = true;
        if (compressorIndex <= pipeNames.indexOf(PacketEvents.ENCODER_NAME)) {
            return ctx; // order already seems to be correct
        }
        // relocate handlers
        PacketEventsDecoder decoder = (PacketEventsDecoder) pipe.remove(PacketEvents.DECODER_NAME);
        PacketEventsEncoder encoder = (PacketEventsEncoder) pipe.remove(PacketEvents.ENCODER_NAME);
        pipe.addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
        pipe.addAfter("compress", PacketEvents.ENCODER_NAME, encoder);

        // manually decompress packet and update context,
        // so we don't need to additionally manually re-compress the packet
        this.decompress(pipe, buffer);
        return pipe.context(PacketEvents.ENCODER_NAME);
    }

    private void decompress(ChannelPipeline pipe, ByteBuf buffer) {
        ChannelHandler decompressor = pipe.get("decompress");
        ChannelHandlerContext decompressorCtx = pipe.context("decompress");

        ByteBuf decompressed = null;
        try {
            decompressed = (ByteBuf) CustomPipelineUtil.callPacketDecodeByteBuf(
                    decompressor, decompressorCtx, buffer).get(0);
            if (buffer != decompressed) {
                buffer.clear().writeBytes(decompressed);
            }
        } catch (InvocationTargetException exception) {
            throw new RuntimeException(exception);
        } finally {
            ReferenceCountUtil.release(decompressed);
        }
    }
}
