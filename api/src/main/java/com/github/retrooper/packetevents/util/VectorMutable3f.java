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

import com.github.retrooper.packetevents.protocol.world.BlockFace;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * 3D float Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 *
 * @author retrooper
 * @since 2.7.1
 */
public class VectorMutable3f implements VectorInterface3f {
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public float x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public float y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public float z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public VectorMutable3f() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    /**
     * Constructor allowing you to set the values.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public VectorMutable3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructor allowing you to specify an array.
     * X will be set to the first index of an array(if it exists, otherwise 0).
     * Y will be set to the second index of an array(if it exists, otherwise 0).
     * Z will be set to the third index of an array(if it exists, otherwise 0).
     *
     * @param array Array.
     */
    public VectorMutable3f(float[] array) {
        if (array.length > 0) {
            x = array[0];
        } else {
            x = 0;
            y = 0;
            z = 0;
            return;
        }

        if (array.length > 1) {
            y = array[1];
        } else {
            y = 0;
            z = 0;
            return;
        }

        if (array.length > 2) {
            z = array[2];
        } else {
            z = 0;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public boolean equals(VectorMutable3f other) {
        return this.x == other.x && this.y == other.y && this.z == other.z;
    }

    /**
     * Is the object we are comparing to equal to us?
     * It must implement VectorInterface3i, VectorInterface3d, or VectorInterface3f
     * and all values must be equal to the values in this class.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VectorInterface3f) {
            VectorInterface3f vec = (VectorInterface3f) obj;
            return x == vec.getX() && y == vec.getY() && z == vec.getZ();
        } else if (obj instanceof VectorInterface3d) {
            VectorMutable3d vec = (VectorMutable3d) obj;
            return x == vec.getX() && y == vec.getY() && z == vec.getZ();
        } else if (obj instanceof VectorInterface3i) {
            VectorInterface3i vec = (VectorInterface3i) obj;
            return x == (double) vec.getX() && y == (double) vec.getY() && z == (double) vec.getZ();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public VectorMutable3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public VectorMutable3f add(VectorMutable3f other) {
        return add(other.x, other.y, other.z);
    }

    public VectorMutable3f offset(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }

    public VectorMutable3f subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public VectorMutable3f subtract(VectorMutable3f other) {
        return subtract(other.x, other.y, other.z);
    }

    public VectorMutable3f multiply(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= y;
        return this;
    }

    public VectorMutable3f multiply(VectorMutable3f other) {
        return multiply(other.x, other.y, other.z);
    }

    public VectorMutable3f multiply(float value) {
        return multiply(value, value, value);
    }

    public VectorMutable3f crossProduct(VectorMutable3f other) {
        float oldX = this.x;
        float oldY = this.y;
        this.x = this.y * other.z - other.y * this.z;
        this.y = this.z * other.x - other.z * oldX;
        this.z = oldX * other.y - this.x * oldY;
        return this;
    }

    public float dot(VectorMutable3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }

    public static VectorMutable3f zero() {
        return new VectorMutable3f();
    }

    @NotNull
    public VectorMutable3f setX(float x) {
        this.x = x;
        return this;
    }

    @NotNull
    public VectorMutable3f setY(float y) {
        this.y = y;
        return this;
    }

    @NotNull
    public VectorMutable3f setZ(int z) {
        this.z = z;
        return this;
    }

    @NotNull
    public VectorMutable3f clone() {
        try {
            return (VectorMutable3f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}

