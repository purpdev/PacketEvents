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

package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.wrapper.PacketWrapper;

//PacketEvents is not a math library, we don't plan to have all quaternion functions in here.
// We just store the 4 float components for you.
public class Quaternion4f {
    private float x;
    private float y;
    private float z;
    private float w;

    public Quaternion4f(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public static Quaternion4f read(PacketWrapper<?> wrapper) {
        return new Quaternion4f(wrapper.readFloat(), wrapper.readFloat(),
                wrapper.readFloat(), wrapper.readFloat());
    }

    public static void write(PacketWrapper<?> wrapper, Quaternion4f quaternion) {
        wrapper.writeFloat(quaternion.x);
        wrapper.writeFloat(quaternion.y);
        wrapper.writeFloat(quaternion.z);
        wrapper.writeFloat(quaternion.w);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }


}
