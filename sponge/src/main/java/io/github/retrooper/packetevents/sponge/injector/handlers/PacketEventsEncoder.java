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

package io.github.retrooper.packetevents.sponge.injector.handlers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.exception.CancelPacketException;
import com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import com.github.retrooper.packetevents.exception.PacketProcessException;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.ExceptionUtil;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.sponge.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.sponge.util.viaversion.CustomPipelineUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.api.Sponge;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

public class PacketEventsEncoder extends MessageToMessageEncoder<ByteBuf> {

    public User user;
    public UUID player;
    private boolean handledCompression;
    private ChannelPromise promise;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public PacketEventsEncoder(ChannelHandler encoder) {
        user = ((PacketEventsEncoder) encoder).user;
        player = ((PacketEventsEncoder) encoder).player;
        handledCompression = ((PacketEventsEncoder) encoder).handledCompression;
        promise = ((PacketEventsEncoder) encoder).promise;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        boolean needsRecompression = !this.handledCompression && this.handleCompression(ctx, byteBuf);
        byteBuf = this.handleClientBoundPacket(ctx.channel(), this.user, this.player, byteBuf.retain(), this.promise);

        if (needsRecompression) {
            this.compress(ctx, byteBuf);
        }

        // So apparently, this is how ViaVersion hacks around bungeecord not supporting sending empty packets
        if (!ByteBufHelper.isReadable(byteBuf)) {
            throw CancelPacketException.INSTANCE;
        }

        list.add(byteBuf);
    }

    private ByteBuf handleClientBoundPacket(
            Channel channel, User user,
            @Nullable UUID playerId, ByteBuf buffer,
            ChannelPromise promise
    ) throws Exception {
        Object player = playerId == null ? null : Sponge.server().player(playerId).orElse(null);
        PacketSendEvent event = PacketEventsImplHelper.handleClientBoundPacket(channel, user,
                player, buffer, true);
        if (event == null) {
            return buffer;
        }
        if (event.hasTasksAfterSend()) {
            promise.addListener((p) -> {
                for (Runnable task : event.getTasksAfterSend()) {
                    task.run();
                }
            });
        }
        return (ByteBuf) event.getByteBuf();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // We must restore the old promise (in case we are stacking promises such as sending packets on send event)
        // If the old promise was successful, set it to null to avoid memory leaks.
        ChannelPromise oldPromise = this.promise != null && !this.promise.isSuccess() ? this.promise : null;
        promise.addListener(p -> this.promise = oldPromise);

        this.promise = promise;
        super.write(ctx, msg, promise);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // This is a terrible hack (to support bungee), I think we should use something other than a MessageToMessageEncoder
        if (ExceptionUtil.isException(cause, CancelPacketException.class)) {
            return;
        }
        // Ignore how mojang sends DISCONNECT packets in the wrong state
        if (ExceptionUtil.isException(cause, InvalidDisconnectPacketSend.class)) {
            return;
        }

        boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);

        if (didWeCauseThis && user != null && user.getEncoderState() != ConnectionState.HANDSHAKING) {
            // Ignore handshaking exceptions
            cause.printStackTrace();
            return;
        }

        super.exceptionCaught(ctx, cause);
    }

    private void compress(ChannelHandlerContext ctx, ByteBuf input) throws InvocationTargetException {
        ChannelHandler compressor = ctx.pipeline().get("compress");
        ByteBuf temp = ctx.alloc().buffer();
        try {
            if (compressor != null) {
                CustomPipelineUtil.callEncode(compressor, ctx, input, temp);
            }
        } finally {
            input.clear().writeBytes(temp);
            temp.release();
        }
    }

    private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
        ChannelHandler decompressor = ctx.pipeline().get("decompress");
        if (decompressor != null) {
            ByteBuf temp = (ByteBuf) CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);
            try {
                output.clear().writeBytes(temp);
            } finally {
                temp.release();
            }
        }
    }

    private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
        if (handledCompression) return false;
        int compressIndex = ctx.pipeline().names().indexOf("compress");
        if (compressIndex == -1) return false;
        handledCompression = true;
        int peEncoderIndex = ctx.pipeline().names().indexOf(PacketEvents.ENCODER_NAME);
        if (peEncoderIndex == -1) return false;
        if (compressIndex > peEncoderIndex) {
            //We are ahead of the decompression handler (they are added dynamically) so let us relocate.
            //But first we need to compress the data and re-compress it after we do all our processing to avoid issues.
            decompress(ctx, buffer, buffer);
            //Let us relocate and no longer deal with compression.
            PacketEventsDecoder decoder = (PacketEventsDecoder) ctx.pipeline().get(PacketEvents.DECODER_NAME);
            ServerConnectionInitializer.relocateHandlers(ctx.channel(), decoder, user);
            return true;
        }
        return false;
    }
}
