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

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;

/**
 * 3D double Vector.
 * This vector can represent coordinates, angles, or anything you want.
 * You can use this to represent an array if you really want.
 *
 * @author retrooper
 * @since 2.7.1
 */
public class VectorMutable3d implements VectorInterface3d {
    /**
     * X (coordinate/angle/whatever you wish)
     */
    public double x;
    /**
     * Y (coordinate/angle/whatever you wish)
     */
    public double y;
    /**
     * Z (coordinate/angle/whatever you wish)
     */
    public double z;

    /**
     * Default constructor setting all coordinates/angles/values to their default values (=0).
     */
    public VectorMutable3d() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }

    /**
     * Constructor allowing you to set the values.
     *
     * @param x X
     * @param y Y
     * @param z Z
     */
    public VectorMutable3d(double x, double y, double z) {
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
    public VectorMutable3d(double[] array) {
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

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    public boolean equals(VectorMutable3d other) {
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
        if (obj instanceof VectorInterface3d) {
            VectorInterface3d vec = (VectorInterface3d) obj;
            return x == vec.getX() && y == vec.getY() && z == vec.getZ();
        } else if (obj instanceof VectorInterface3f) {
            VectorInterface3f vec = (VectorInterface3f) obj;
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

    @Override
    public VectorMutable3d add(double x, double y, double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    @Override
    public VectorMutable3d add(VectorInterface3i other) {
        return add(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public VectorMutable3d offset(BlockFace face) {
        return add(face.getModX(), face.getModY(), face.getModZ());
    }

    @Override
    public VectorMutable3d subtract(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    @Override
    public VectorMutable3d subtract(VectorInterface3d other) {
        return subtract(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public VectorMutable3d multiply(double x, double y, double z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    @Override
    public VectorMutable3d multiply(VectorInterface3d other) {
        return multiply(other.getX(), other.getY(), other.getZ());
    }

    @Override
    public VectorMutable3d multiply(double value) {
        return multiply(value, value, value);
    }

    @Override
    public VectorMutable3d crossProduct(VectorInterface3d other) {
        double oldX = this.x;
        double oldY = this.y;
        this.x = this.y * other.getZ() - other.getY() * this.z;
        this.y = this.z * other.getX() - other.getZ() * oldX;
        this.z = oldX * other.getY() - this.x * oldY;
        return this;
    }

    @Override
    public double dot(VectorInterface3d other) {
        return this.x * other.getX() + this.y * other.getY() + this.z * other.getZ();
    }

    @Override
    public VectorMutable3d with(Double x, Double y, Double z) {
        this.x = x == null ? this.x : x;
        this.y = y == null ? this.y : y;
        this.z = z == null ? this.z : z;
        return this;
    }

    @Override
    public double distance(VectorInterface3d other) {
        return Math.sqrt(distanceSquared(other));
    }

    @Override
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    @Override
    public double lengthSquared() {
        return (x * x) + (y * y) + (z * z);
    }

    @Override
    public VectorMutable3d normalize() {
        double length = length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }

    @Override
    public double distanceSquared(VectorInterface3d other) {
        double distX = (x - other.getX()) * (x - other.getX());
        double distY = (y - other.getY()) * (y - other.getY());
        double distZ = (z - other.getZ()) * (z - other.getZ());
        return distX + distY + distZ;
    }

    public Vector3i toVector3i() {
        return new Vector3i((int) x, (int) y, (int) z);
    }

    @Override
    public String toString() {
        return "X: " + x + ", Y: " + y + ", Z: " + z;
    }

    @Override @NotNull
    public VectorMutable3d withX(double x) {
        this.x = x;
        return this;
    }

    @Override @NotNull
    public VectorMutable3d withY(double y) {
        this.y = y;
        return this;
    }

    @Override @NotNull
    public VectorMutable3d withZ(double z) {
        this.z = z;
        return this;
    }

    @Override
    public int getBlockX() {
        return MathUtil.floor(x);
    }

    @Override
    public int getBlockY() {
        return MathUtil.floor(y);
    }

    @Override
    public int getBlockZ() {
        return MathUtil.floor(z);
    }

    @NotNull
    public VectorMutable3d clone() {
        try {
            return (VectorMutable3d) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public static VectorMutable3d read(PacketWrapper<?> wrapper) {
        double x = wrapper.readDouble();
        double y = wrapper.readDouble();
        double z = wrapper.readDouble();
        return new VectorMutable3d(x, y, z);
    }

    public static void write(PacketWrapper<?> wrapper, VectorInterface3d vector) {
        VectorInterface3d.write(wrapper, vector);
    }

    public static VectorMutable3d decode(NBT tag, ClientVersion version) {
        NBTList<?> list = (NBTList<?>) tag;
        double x = ((NBTNumber) list.getTag(0)).getAsDouble();
        double y = ((NBTNumber) list.getTag(1)).getAsDouble();
        double z = ((NBTNumber) list.getTag(2)).getAsDouble();
        return new VectorMutable3d(x, y, z);
    }

    public static NBT encode(VectorInterface3d vector3d, ClientVersion version) {
        return VectorInterface3d.encode(vector3d, version);
    }

    public static VectorMutable3d zero() {
        return new VectorMutable3d();
    }
}
