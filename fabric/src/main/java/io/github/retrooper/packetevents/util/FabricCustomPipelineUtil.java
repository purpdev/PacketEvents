package io.github.retrooper.packetevents.util;

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

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;
import net.minecraft.network.CompressionDecoder;

public class FabricCustomPipelineUtil {
    private static MethodHandle FABRIC_PACKET_DECODE_BYTEBUF;

    static {
        try {
            // Get the mapping resolver to handle obfuscated names
            MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();

            // Get the runtime (potentially obfuscated) class for CompressionDecoder
            Class<?> compressionDecoderClass = CompressionDecoder.class;

            // Map the method name from intermediary to runtime (obfuscated) names
            String intermediaryMethodName = "decode"; // Intermediary method name
            String intermediaryClassName = compressionDecoderClass.getName(); // Intermediary class name

            // Define the method descriptor in intermediary mappings
            // Parameters: ChannelHandlerContext, ByteBuf, List<Object>
            // Return type: void
            String methodDescriptor = "(Lio/netty/channel/ChannelHandlerContext;Lio/netty/buffer/ByteBuf;Ljava/util/List;)V";

            // Map the method name to the runtime (obfuscated) name
            String mappedMethodName = resolver.mapMethodName(
                "intermediary",
                intermediaryClassName,
                intermediaryMethodName,
                methodDescriptor
            );

            // Create a MethodHandles.Lookup with private access
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(compressionDecoderClass, MethodHandles.lookup());

            // Define the method type for the decode method
            MethodType methodType = MethodType.methodType(
                void.class,                    // Return type
                ChannelHandlerContext.class,   // Parameter 1
                ByteBuf.class,                 // Parameter 2
                List.class                     // Parameter 3
            );

            // Find the MethodHandle for the decode method
            FABRIC_PACKET_DECODE_BYTEBUF = lookup.findVirtual(
                compressionDecoderClass,
                mappedMethodName,
                methodType
            );
        } catch (IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> callPacketDecodeByteBuf(Object decoder, Object ctx, Object msg) throws InvocationTargetException {
        List<Object> output = new ArrayList<>(1);
        try {
            FABRIC_PACKET_DECODE_BYTEBUF.invoke(decoder, ctx, msg, output);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return output;
    }
}


