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

package io.github.retrooper.packetevents.netty.buffer;

import com.github.retrooper.packetevents.netty.buffer.ByteBufOperator;
import io.github.retrooper.packetevents.impl.netty.buffer.FastNettyUtils;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;

public class ByteBufOperatorModernImpl implements ByteBufOperator {
    @Override
    public int capacity(Object buffer) {
        return ((ByteBuf) buffer).capacity();
    }

    @Override
    public Object capacity(Object buffer, int capacity) {
        return ((ByteBuf) buffer).capacity(capacity);
    }

    @Override
    public int readerIndex(Object buffer) {
        return ((ByteBuf) buffer).readerIndex();
    }

    @Override
    public Object readerIndex(Object buffer, int readerIndex) {
        return ((ByteBuf) buffer).readerIndex(readerIndex);
    }

    @Override
    public int writerIndex(Object buffer) {
        return ((ByteBuf) buffer).writerIndex();
    }

    @Override
    public Object writerIndex(Object buffer, int writerIndex) {
        return ((ByteBuf) buffer).writerIndex(writerIndex);
    }

    @Override
    public int readableBytes(Object buffer) {
        return ((ByteBuf) buffer).readableBytes();
    }

    @Override
    public int writableBytes(Object buffer) {
        return ((ByteBuf) buffer).writableBytes();
    }

    @Override
    public Object clear(Object buffer) {
        return ((ByteBuf) buffer).clear();
    }

    @Override
    public byte readByte(Object buffer) {
        return ((ByteBuf) buffer).readByte();
    }

    @Override
    public short readShort(Object buffer) {
        return ((ByteBuf) buffer).readShort();
    }

    @Override
    public int readMedium(Object buffer) {
        return ((ByteBuf) buffer).readMedium();
    }

    @Override
    public int readInt(Object buffer) {
        return ((ByteBuf) buffer).readInt();
    }

    @Override
    public long readUnsignedInt(Object buffer) {
        return ((ByteBuf) buffer).readUnsignedInt();
    }

    @Override
    public long readLong(Object buffer) {
        return ((ByteBuf) buffer).readLong();
    }

    @Override
    public int getIntLE(Object buffer, int readerIndex) {
        return ((ByteBuf) buffer).getIntLE(readerIndex);
    }

    @Override
    public int readVarInt(Object buffer) {
        return FastNettyUtils.readVarInt((ByteBuf) buffer);
    }

    @Override
    public void writeByte(Object buffer, int value) {
        ((ByteBuf) buffer).writeByte(value);
    }

    @Override
    public void writeShort(Object buffer, int value) {
        ((ByteBuf) buffer).writeShort(value);
    }


    @Override
    public void writeMedium(Object buffer, int value) {
        ((ByteBuf) buffer).writeMedium(value);
    }

    @Override
    public void writeInt(Object buffer, int value) {
        ((ByteBuf) buffer).writeInt(value);
    }

    @Override
    public void writeLong(Object buffer, long value) {
        ((ByteBuf) buffer).writeLong(value);
    }

    @Override
    public Object getBytes(Object buffer, int index, byte[] destination) {
        return ((ByteBuf) buffer).getBytes(index, destination);
    }

    @Override
    public short getUnsignedByte(Object buffer, int index) {
        return ((ByteBuf) buffer).getUnsignedByte(index);
    }

    @Override
    public boolean isReadable(Object buffer) {
        return ((ByteBuf) buffer).isReadable();
    }

    @Override
    public Object copy(Object buffer) {
        return ((ByteBuf) buffer).copy();
    }

    @Override
    public Object duplicate(Object buffer) {
        return ((ByteBuf) buffer).duplicate();
    }

    @Override
    public boolean hasArray(Object buffer) {
        return ((ByteBuf) buffer).hasArray();
    }

    @Override
    public byte[] array(Object buffer) {
        return ((ByteBuf) buffer).array();
    }

    @Override
    public Object retain(Object buffer) {
        return ((ByteBuf) buffer).retain();
    }

    @Override
    public Object retainedDuplicate(Object buffer) {
        return ((ByteBuf) buffer).duplicate().retain();
    }

    @Override
    public Object readSlice(Object buffer, int length) {
        return ((ByteBuf) buffer).readSlice(length);
    }

    @Override
    public Object readBytes(Object buffer, byte[] destination, int destinationIndex, int length) {
        return ((ByteBuf) buffer).readBytes(destination, destinationIndex, length);
    }

    @Override
    public Object readBytes(Object buffer, int length) {
        return ((ByteBuf) buffer).readBytes(length);
    }

    @Override
    public Object writeBytes(Object buffer, Object src) {
        return ((ByteBuf) buffer).writeBytes((ByteBuf) src);
    }

    @Override
    public Object writeBytes(Object buffer, byte[] bytes) {
        return ((ByteBuf) buffer).writeBytes(bytes);
    }

    @Override
    public Object writeBytes(Object buffer, byte[] bytes, int offset, int length) {
        return ((ByteBuf) buffer).writeBytes(bytes, offset, length);
    }

    @Override
    public void readBytes(Object buffer, byte[] bytes) {
        ((ByteBuf) buffer).readBytes(bytes);
    }

    @Override
    public boolean release(Object buffer) {
        return ((ByteBuf) buffer).release();
    }

    @Override
    public int refCnt(Object buffer) {
        return ((ByteBuf) buffer).refCnt();
    }

    @Override
    public Object skipBytes(Object buffer, int length) {
        return ((ByteBuf) buffer).skipBytes(length);
    }

    @Override
    public String toString(Object buffer, int index, int length, Charset charset) {
        return ((ByteBuf) buffer).toString(index, length, charset);
    }

    @Override
    public Object markReaderIndex(Object buffer) {
        return ((ByteBuf) buffer).markReaderIndex();
    }

    @Override
    public Object resetReaderIndex(Object buffer) {
        return ((ByteBuf) buffer).resetReaderIndex();
    }

    @Override
    public Object markWriterIndex(Object buffer) {
        return ((ByteBuf) buffer).markWriterIndex();
    }

    @Override
    public Object resetWriterIndex(Object buffer) {
        return ((ByteBuf) buffer).resetWriterIndex();
    }
}
