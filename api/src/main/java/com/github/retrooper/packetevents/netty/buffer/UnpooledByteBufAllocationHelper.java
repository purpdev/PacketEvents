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

public class UnpooledByteBufAllocationHelper {
    public static Object wrappedBuffer(byte[] bytes) {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(bytes);
    }

    public static Object copiedBuffer(byte[] bytes) {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().copiedBuffer(bytes);
    }

    public static Object buffer() {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer();
    }

    public static Object directBuffer() {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer();
    }

    public static Object compositeBuffer() {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().compositeBuffer();
    }

    public static Object buffer(int initialCapacity) {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer(initialCapacity);
    }

    public static Object directBuffer(int initialCapacity) {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer(initialCapacity);
    }

    public static Object compositeBuffer(int maxNumComponents) {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().compositeBuffer(maxNumComponents);
    }

    public static Object emptyBuffer() {
        return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().emptyBuffer();
    }
}
