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

package io.github.retrooper.packetevents.sponge.netty.buffer;

import com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;
import io.netty.buffer.Unpooled;

public class ByteBufAllocationOperatorModernImpl implements ByteBufAllocationOperator {
    @Override
    public Object wrappedBuffer(byte[] bytes) {
        return Unpooled.wrappedBuffer(bytes);
    }

    @Override
    public Object copiedBuffer(byte[] bytes) {
        return Unpooled.copiedBuffer(bytes);
    }

    @Override
    public Object buffer() {
        return Unpooled.buffer();
    }

    @Override
    public Object buffer(int initialCapacity) {
        return Unpooled.buffer(initialCapacity);
    }

    @Override
    public Object directBuffer() {
        return Unpooled.directBuffer();
    }

    @Override
    public Object directBuffer(int initialCapacity) {
        return Unpooled.directBuffer(initialCapacity);
    }

    @Override
    public Object compositeBuffer() {
        return Unpooled.compositeBuffer();
    }

    @Override
    public Object compositeBuffer(int maxNumComponents) {
        return Unpooled.compositeBuffer(maxNumComponents);
    }

    @Override
    public Object emptyBuffer() {
        return Unpooled.EMPTY_BUFFER;
    }
}
