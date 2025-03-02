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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.EventCreationUtil;
import io.github.retrooper.packetevents.injector.ServerConnectionInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsDecoder extends MessageToMessageDecoder<ByteBuf> {

    public User user;
    public ProxiedPlayer player;
    public boolean handledCompression;

    public PacketEventsDecoder(User user) {
        this.user = user;
    }

    public void read(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> output) throws Exception {
        if (this.tryFixCompressorOrder(ctx, byteBuf)) {
            return; // skip handling of buffer
        }

        ByteBuf transformed = ctx.alloc().buffer().writeBytes(byteBuf);
        try {
            int firstReaderIndex = transformed.readerIndex();
            PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(ctx.channel(),
                    user, player, transformed, false);
            int readerIndex = transformed.readerIndex();
            PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> transformed.readerIndex(readerIndex));
            if (!packetReceiveEvent.isCancelled()) {
                if (packetReceiveEvent.getLastUsedWrapper() != null) {
                    ByteBufHelper.clear(packetReceiveEvent.getByteBuf());
                    packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
                    packetReceiveEvent.getLastUsedWrapper().write();
                } else {
                    transformed.readerIndex(firstReaderIndex);
                }
                output.add(transformed.retain());
            }
            if (packetReceiveEvent.hasPostTasks()) {
                for (Runnable task : packetReceiveEvent.getPostTasks()) {
                    task.run();
                }
            }
        } finally {
            transformed.release();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        if (byteBuf.isReadable()) {
            read(ctx, byteBuf, out);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        ServerConnectionInitializer.destroyChannel(ctx.channel());
        super.channelInactive(ctx);
    }

    private boolean tryFixCompressorOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        if (this.handledCompression) {
            return false;
        }
        ChannelPipeline pipe = ctx.pipeline();
        List<String> pipeNames = pipe.names();
        int decompressorIndex = pipeNames.indexOf("decompress");
        if (decompressorIndex == -1) {
            return false;
        }
        this.handledCompression = true;
        if (!pipeNames.contains("frame-prepender-compress")) {
            // before "modern" version, no need to handle this here
            return false;
        } else if (decompressorIndex <= pipeNames.indexOf(PacketEvents.DECODER_NAME)) {
            return false; // order already seems to be correct
        }
        // relocate handler - encoder doesn't need relocation
        PacketEventsDecoder decoder = (PacketEventsDecoder) pipe.remove(PacketEvents.DECODER_NAME);
        pipe.addAfter("decompress", PacketEvents.DECODER_NAME, decoder);

        // re-fire packet
        ChannelHandlerContext frameDecoderCtx = pipe.context("frame-decoder");
        frameDecoderCtx.fireChannelRead(buffer.retain());
        return true; // skip further handling
    }
}
