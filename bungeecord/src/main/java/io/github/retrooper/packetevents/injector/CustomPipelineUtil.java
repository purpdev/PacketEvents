/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2022 ViaVersion and contributors
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

package io.github.retrooper.packetevents.injector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.Recycler;
import io.netty.util.concurrent.PromiseCombiner;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@ApiStatus.Internal
public class CustomPipelineUtil {

    private static final Recycler<OutList> OUT_LIST_RECYCLER = new Recycler<OutList>() {
        @Override
        protected OutList newObject(Handle<OutList> handle) {
            return new OutList(handle);
        }
    };

    private static Method DECODE_METHOD;
    private static Method ENCODE_METHOD;
    private static Method MTM_DECODE;
    private static Method BUNGEE_PACKET_DECODE_BYTEBUF;
    private static Method BUNGEE_PACKET_ENCODE_BYTEBUF;
    private static Method MTM_ENCODE;

    public static void init() {
        try {
            DECODE_METHOD = ByteToMessageDecoder.class
                    .getDeclaredMethod("decode", ChannelHandlerContext.class,
                            Object.class, List.class);
            DECODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            ENCODE_METHOD = MessageToByteEncoder.class
                    .getDeclaredMethod("encode", ChannelHandlerContext.class, Object.class,
                            ByteBuf.class);
            ENCODE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            MTM_DECODE = MessageToMessageDecoder.class
                    .getDeclaredMethod("decode", ChannelHandlerContext.class,
                            Object.class, List.class);
            MTM_DECODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            MTM_ENCODE = MessageToMessageEncoder.class
                    .getDeclaredMethod("encode", ChannelHandlerContext.class,
                            Object.class, List.class);
            MTM_ENCODE.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Call the decode method on a netty ByteToMessageDecoder
     *
     * @param decoder The decoder
     * @param ctx     The current context
     * @param input   The packet to decode
     * @return A list of the decoders output
     * @throws InvocationTargetException If an exception happens while executing
     */
    public static List<Object> callDecode(Object decoder, Object ctx, Object input) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();
        try {
            CustomPipelineUtil.DECODE_METHOD.invoke(decoder, ctx, input, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Call the encode method on a netty MessageToByteEncoder
     *
     * @param encoder The encoder
     * @param ctx     The current context
     * @param msg     The packet to encode
     * @param output  The bytebuf to write the output to
     * @throws InvocationTargetException If an exception happens while executing
     */
    public static void callEncode(Object encoder, Object ctx, Object msg, Object output) throws InvocationTargetException {
        try {
            CustomPipelineUtil.ENCODE_METHOD.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> callMTMEncode(Object encoder, Object ctx, Object msg) {
        List<Object> output = new ArrayList<>();
        try {
            MTM_ENCODE.invoke(encoder, ctx, msg, output);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static List<Object> callMTMDecode(Object decoder, Object ctx, Object msg) throws InvocationTargetException {
        List<Object> output = new ArrayList<>();
        try {
            MTM_DECODE.invoke(decoder, ctx, msg, output);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void callPacketEncodeByteBuf(Object encoder, ChannelHandlerContext ctx, ByteBuf msg, ChannelPromise promise) throws InvocationTargetException {
        if (BUNGEE_PACKET_ENCODE_BYTEBUF == null) {
            try {
                BUNGEE_PACKET_ENCODE_BYTEBUF = encoder.getClass().getDeclaredMethod("encode",
                        ChannelHandlerContext.class, ByteBuf.class, List.class);
                BUNGEE_PACKET_ENCODE_BYTEBUF.setAccessible(true);
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException(exception);
            }
        }
        OutList out = OUT_LIST_RECYCLER.get();
        try {
            BUNGEE_PACKET_ENCODE_BYTEBUF.invoke(encoder, ctx, msg, out.list);
            out.write(ctx, promise);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        } finally {
            msg.release();
            out.list.clear();
            out.handle.recycle(out);
        }
    }

    public static ByteBuf callPacketDecodeByteBuf(Object decoder, ChannelHandlerContext ctx, ByteBuf msg) throws InvocationTargetException {
        if (BUNGEE_PACKET_DECODE_BYTEBUF == null) {
            try {
                BUNGEE_PACKET_DECODE_BYTEBUF = decoder.getClass().getDeclaredMethod("decode",
                        ChannelHandlerContext.class, Object.class, List.class);
                BUNGEE_PACKET_DECODE_BYTEBUF.setAccessible(true);
            } catch (NoSuchMethodException exception) {
                throw new IllegalStateException(exception);
            }
        }
        OutList out = OUT_LIST_RECYCLER.get();
        try {
            BUNGEE_PACKET_DECODE_BYTEBUF.invoke(decoder, ctx, msg, out.list);
            return (ByteBuf) out.list.get(0);
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(exception);
        } finally {
            msg.release();
            out.list.clear();
            out.handle.recycle(out);
        }
    }

    private static final class OutList {

        // the default bungee compression handlers only produces one output bytebuf
        private final List<Object> list = new ArrayList<>(1);
        private final Recycler.Handle<OutList> handle;

        public OutList(Recycler.Handle<OutList> handle) {
            this.handle = handle;
        }

        public void write(ChannelHandlerContext ctx, ChannelPromise promise) {
            List<Object> list = this.list;
            int len = list.size();
            if (len == 1) {
                // should be the only case which
                // happens on default bungeecord
                ctx.write(list.get(0), promise);
                return;
            }

            // copied from MessageToMessageEncoder#writePromiseCombiner
            PromiseCombiner combiner = new PromiseCombiner(ctx.executor());
            for (int i = 0; i < len; ++i) {
                combiner.add(ctx.write(list.get(i)));
            }
            combiner.finish(promise);
        }
    }
}
