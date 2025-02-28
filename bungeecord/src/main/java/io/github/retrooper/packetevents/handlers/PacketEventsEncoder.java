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
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.EncoderException;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;

// Thanks to ViaVersion for the compression method.
@ChannelHandler.Sharable
public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    private final PacketSide side = PacketSide.SERVER;
    public ProxiedPlayer player;
    public User user;
    public boolean handledCompression;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void handle(ChannelHandlerContext ctx, ByteBuf in, ChannelPromise promise) throws Exception {
        ByteBuf decompressed = this.handleCompressionOrder(ctx, in.retain());
        if (decompressed != null) in = decompressed;

        ProtocolPacketEvent event = PacketEventsImplHelper.handlePacket(ctx.channel(),
                this.user, this.player, in.retain(), false, this.side);
        ByteBuf buf = event != null ? (ByteBuf) event.getByteBuf() : in;

        if (!buf.isReadable()) {
            // cancelled, stop it
            buf.release();
            promise.setSuccess(); // mark as done
            return;
        }

        if (decompressed != null) {
            this.recompress(ctx, buf, promise);
        } else {
            ctx.write(buf, promise);
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            ctx.write(msg, promise);
            return;
        }
        ByteBuf in = (ByteBuf) msg;
        if (!in.isReadable()) {
            in.release();
            promise.setSuccess(); // mark as done
            return;
        }

        try {
            this.handle(ctx, in, promise);
        } finally {
            in.release();
        }
    }

    private @Nullable ByteBuf handleCompressionOrder(ChannelHandlerContext ctx, ByteBuf buffer) {
        ChannelPipeline pipe = ctx.pipeline();
        if (this.handledCompression) {
            return null;
        }
        int encoderIndex = pipe.names().indexOf("compress");
        if (encoderIndex == -1) {
            return null;
        }
        if (encoderIndex > pipe.names().indexOf(PacketEvents.ENCODER_NAME)) {
            // Need to decompress this packet due to bad order
            ChannelHandler decompressor = pipe.get("decompress");
            try {
                ByteBuf out = CustomPipelineUtil.callPacketDecodeByteBuf(decompressor, ctx, buffer.retain());
                //Relocate handlers
                PacketEventsDecoder decoder = (PacketEventsDecoder) pipe.remove(PacketEvents.DECODER_NAME);
                PacketEventsEncoder encoder = (PacketEventsEncoder) pipe.remove(PacketEvents.ENCODER_NAME);
                pipe.addAfter("decompress", PacketEvents.DECODER_NAME, decoder);
                pipe.addAfter("compress", PacketEvents.ENCODER_NAME, encoder);
                this.handledCompression = true;
                return out;
            } catch (InvocationTargetException exception) {
                throw new EncoderException("Error while decompressing bytebuf: " + exception, exception);
            } finally {
                buffer.release();
            }
        }
        return null;
    }

    private void recompress(ChannelHandlerContext ctx, ByteBuf buffer, ChannelPromise promise) {
        try {
            ChannelHandler compressor = ctx.pipeline().get("compress");
            CustomPipelineUtil.callPacketEncodeByteBuf(compressor, ctx, buffer, promise);
        } catch (InvocationTargetException exception) {
            throw new EncoderException("Error while recompressing bytebuf " + exception, exception);
        }
    }
}
