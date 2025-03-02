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

/**
 * 3D float Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 *
 * @author retrooper
 * @since 1.8
 */
public class Vector3f {
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public final float x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public final float y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public final float z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public Vector3f() {
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
    public Vector3f(float x, float y, float z) {
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
    public Vector3f(float[] array) {
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

    /**
     * Is the object we are comparing to equal to us?
     * It must be of type Vector3d or Vector3i and all values must be equal to the values in this class.
     *
     * @param obj Compared object.
     * @return Are they equal?
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3f) {
            Vector3f vec = (Vector3f) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3d) {
            Vector3d vec = (Vector3d) obj;
            return x == vec.x && y == vec.y && z == vec.z;
        } else if (obj instanceof Vector3i) {
            Vector3i vec = (Vector3i) obj;
            return x == (double) vec.x && y == (double) vec.y && z == (double) vec.z;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public Vector3f add(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public Vector3f add(Vector3f other) {
        return add(other.x, other.y, other.z);
    }

    public Vector3f offset(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }

    public Vector3f subtract(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    public Vector3f subtract(Vector3f other) {
        return subtract(other.x, other.y, other.z);
    }

    public Vector3f multiply(float x, float y, float z) {
        return new Vector3f(this.x * x, this.y * y, this.z * z);
    }

    public Vector3f multiply(Vector3f other) {
        return multiply(other.x, other.y, other.z);
    }

    public Vector3f multiply(float value) {
        return multiply(value, value, value);
    }

    public Vector3f crossProduct(Vector3f other) {
        float newX = this.y * other.z - other.y * this.z;
        float newY = this.z * other.x - other.z * this.x;
        float newZ = this.x * other.y - other.x * this.y;
        return new Vector3f(newX, newY, newZ);
    }

    public float dot(Vector3f other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3f with(Float x, Float y, Float z) {
        return new Vector3f(x == null ? this.x : x, y == null ? this.y : y, z == null ? this.z : z);
    }

    public Vector3f withX(float x) {
        return new Vector3f(x, this.y, this.z);
    }

    public Vector3f withY(float y) {
        return new Vector3f(this.x, y, this.z);
    }

    public Vector3f withZ(float z) {
        return new Vector3f(this.x, this.y, z);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }

    public String toUnlabledString() {
        return x + ", " + y + ", " + z;
    }

    public static Vector3f zero() {
        return new Vector3f();
    }
}

