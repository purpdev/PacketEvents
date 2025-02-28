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

package com.github.retrooper.packetevents.netty.buffer;

import com.github.retrooper.packetevents.PacketEvents;

import java.nio.charset.Charset;

public class ByteBufHelper {
    public static int capacity(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().capacity(buffer);
    }

    public static Object capacity(Object buffer, int capacity) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().capacity(buffer, capacity);
    }

    public static int maxCapacity(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().maxCapacity(buffer);
    }

    public static int readerIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readerIndex(buffer);
    }

    public static Object readerIndex(Object buffer, int readerIndex) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readerIndex(buffer, readerIndex);
    }

    public static int writerIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().writerIndex(buffer);
    }

    public static Object writerIndex(Object buffer, int writerIndex) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().writerIndex(buffer, writerIndex);
    }

    public static int readableBytes(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readableBytes(buffer);
    }

    public static int writableBytes(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().writableBytes(buffer);
    }

    public static Object clear(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().clear(buffer);
    }

    public static String toString(Object buffer, int index, int length, Charset charset) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().toString(buffer, index, length, charset);
    }

    public static byte readByte(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readByte(buffer);
    }

    public static void writeByte(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeByte(buffer, value);
    }

    public static boolean readBoolean(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readBoolean(buffer);
    }

    public static void writeBoolean(Object buffer, boolean value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeBoolean(buffer, value);
    }

    public static short readUnsignedByte(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readUnsignedByte(buffer);
    }

    public static char readChar(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readChar(buffer);
    }

    public static void writeChar(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeChar(buffer, value);
    }

    public static short readShort(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readShort(buffer);
    }

    public static int readUnsignedShort(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readUnsignedShort(buffer);
    }

    public static void writeShort(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeShort(buffer, value);
    }

    public static int readMedium(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readMedium(buffer);
    }

    public static void writeMedium(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeMedium(buffer, value);
    }

    public static int readInt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readInt(buffer);
    }

    public static void writeInt(Object buffer, int value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeInt(buffer, value);
    }

    public static long readUnsignedInt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readUnsignedInt(buffer);
    }

    public static long readLong(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readLong(buffer);
    }

    public static void writeLong(Object buffer, long value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeLong(buffer, value);
    }

    public static float readFloat(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readFloat(buffer);
    }

    public static void writeFloat(Object buffer, float value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeFloat(buffer, value);
    }

    public static double readDouble(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readDouble(buffer);
    }

    public static void writeDouble(Object buffer, double value) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeDouble(buffer, value);
    }

    public static Object getBytes(Object buffer, int index, byte[] destination) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().getBytes(buffer, index, destination);
    }

    public static short getUnsignedByte(Object buffer, int index) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().getUnsignedByte(buffer, index);
    }

    public static boolean isReadable(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().isReadable(buffer);
    }

    public static Object copy(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().copy(buffer);
    }

    public static Object duplicate(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().duplicate(buffer);
    }

    public static boolean hasArray(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().hasArray(buffer);
    }

    public static byte[] array(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().array(buffer);
    }

    public static Object retain(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().retain(buffer);
    }

    public static Object retainedDuplicate(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().retainedDuplicate(buffer);
    }

    public static Object readSlice(Object buffer, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readSlice(buffer, length);
    }

    public static Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readBytes(buffer, destination, destinationIndex, length);
    }

    public static Object readBytes(Object buffer, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().readBytes(buffer, length);
    }

    public static Object writeBytes(Object buffer, Object src) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeBytes(buffer, src);
    }

    public static void readBytes(Object buffer, byte[] bytes) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().readBytes(buffer, bytes);
    }

    public static void writeBytes(Object buffer, byte[] bytes) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeBytes(buffer, bytes);
    }

    public static void writeBytes(Object buffer, byte[] bytes, int offset, int length) {
        PacketEvents.getAPI().getNettyManager().getByteBufOperator().writeBytes(buffer, bytes, offset, length);
    }

    public static boolean release(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().release(buffer);
    }

    public static int refCnt(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().refCnt(buffer);
    }

    public static Object skipBytes(Object buffer, int length) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().skipBytes(buffer, length);
    }

    public static Object markReaderIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().markReaderIndex(buffer);
    }

    public static Object resetReaderIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().resetReaderIndex(buffer);
    }

    public static Object markWriterIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().markWriterIndex(buffer);
    }

    public static Object resetWriterIndex(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().resetWriterIndex(buffer);
    }

    public static Object newBuffer(Object buffer) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().newBuffer(buffer);
    }

    public static Object newBuffer(Object buffer, int initialCapacity) {
        return PacketEvents.getAPI().getNettyManager().getByteBufOperator().newBuffer(buffer, initialCapacity);
    }

    public static int readVarInt(Object buffer) {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte(buffer);
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public static void writeVarInt(Object buffer, int value) {
        while (true) {
            if ((value & ~0x7F) == 0) {
                writeByte(buffer, value);
                return;
            }
            writeByte(buffer, (value & 0x7F) | 0x80);
            value >>>= 7;
        }
    }

    public static byte[] copyBytes(Object buffer) {
        byte[] bytes = new byte[readableBytes(buffer)];
        getBytes(buffer, readerIndex(buffer), bytes);
        return bytes;
    }
}
