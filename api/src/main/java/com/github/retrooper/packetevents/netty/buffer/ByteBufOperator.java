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

import java.nio.charset.Charset;
import java.util.Objects;

public interface ByteBufOperator {
    int capacity(Object buffer);
    Object capacity(Object buffer, int capacity);
    int readerIndex(Object buffer);
    Object readerIndex(Object buffer, int readerIndex);

    int writerIndex(Object buffer);
    Object writerIndex(Object buffer, int writerIndex);

    int readableBytes(Object buffer);
    int writableBytes(Object buffer);
    Object clear(Object buffer);

    byte readByte(Object buffer);
    short readShort(Object buffer);
    int readMedium(Object buffer);
    int readInt(Object buffer);
    long readUnsignedInt(Object buffer);
    long readLong(Object buffer);

    void writeByte(Object buffer, int value);
    void writeShort(Object buffer, int value);
    void writeMedium(Object buffer, int value);
    void writeInt(Object buffer, int value);
    void writeLong(Object buffer, long value);

    Object getBytes(Object buffer, int index, byte[] destination);
    short getUnsignedByte(Object buffer, int index);

    int getIntLE(Object buffer, int readerIndex);
    int readVarInt(Object buffer);

    boolean isReadable(Object buffer);
    Object copy(Object buffer);
    Object duplicate(Object buffer);
    boolean hasArray(Object buffer);
    byte[] array(Object buffer);
    Object retain(Object buffer);
    Object retainedDuplicate(Object buffer);
    Object readSlice(Object buffer, int length);
    Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length);
    Object readBytes(Object buffer, int length);
    void readBytes(Object buffer, byte[] bytes);

    Object writeBytes(Object buffer, Object src);
    Object writeBytes(Object buffer, byte[] bytes);
    Object writeBytes(Object buffer, byte[] bytes, int offset, int length);

    boolean release(Object buffer);
    int refCnt(Object buffer);
    Object skipBytes(Object buffer, int length);
    String toString(Object buffer, int index, int length, Charset charset);
    Object markReaderIndex(Object buffer);
    Object resetReaderIndex(Object buffer);
    Object markWriterIndex(Object buffer);
    Object resetWriterIndex(Object buffer);

    default float readFloat(Object buffer) {
        return Float.intBitsToFloat(readInt(buffer));
    }

    default void writeFloat(Object buffer, float value) {
        writeInt(buffer, Float.floatToIntBits(value));
    }

    default double readDouble(Object buffer) {
        return Double.longBitsToDouble(readLong(buffer));
    }

    default void writeDouble(Object buffer, double value) {
        writeLong(buffer, Double.doubleToLongBits(value));
    }

    default char readChar(Object buffer) {
        return (char) readShort(buffer);
    }

    default void writeChar(Object buffer, int value) {
        writeShort(buffer, value);
    }

    //Use writeShort to write
    default int readUnsignedShort(Object buffer) {
        return readShort(buffer) & '\uffff';
    }

    //Use writeByte to write
    default short readUnsignedByte(Object buffer) {
        return (short) (this.readByte(buffer) & 255);
    }

    default boolean readBoolean(Object buffer) {
        return readByte(buffer) != 0;
    }

    default void writeBoolean(Object buffer, boolean value) {
        writeByte(buffer, value ? 1 : 0);
    }
}
