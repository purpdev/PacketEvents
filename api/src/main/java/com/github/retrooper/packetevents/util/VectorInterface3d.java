package com.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTDouble;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public interface VectorInterface3d {
    static void write(PacketWrapper<?> wrapper, VectorInterface3d vector) {
        wrapper.writeDouble(vector.getX());
        wrapper.writeDouble(vector.getY());
        wrapper.writeDouble(vector.getZ());
    }

    static NBT encode(VectorInterface3d vector3d, ClientVersion version) {
        NBTList<NBTDouble> list = new NBTList<>(NBTType.DOUBLE, 3);
        list.addTag(new NBTDouble(vector3d.getX()));
        list.addTag(new NBTDouble(vector3d.getY()));
        list.addTag(new NBTDouble(vector3d.getZ()));
        return list;
    }

    double getX();

    double getY();

    double getZ();

    VectorInterface3d add(double x, double y, double z);

    VectorInterface3d add(VectorInterface3i other);

    VectorInterface3d offset(BlockFace face);

    VectorInterface3d subtract(double x, double y, double z);

    VectorInterface3d subtract(VectorInterface3d other);

    VectorInterface3d multiply(double x, double y, double z);

    VectorInterface3d multiply(VectorInterface3d other);

    VectorInterface3d multiply(double value);

    VectorInterface3d crossProduct(VectorInterface3d other);

    double dot(VectorInterface3d other);

    VectorInterface3d with(Double x, Double y, Double z);

    VectorInterface3d withX(double x);

    VectorInterface3d withY(double y);

    VectorInterface3d withZ(double z);

    double distance(VectorInterface3d other);

    double length();

    double lengthSquared();

    VectorInterface3d normalize();

    double distanceSquared(VectorInterface3d other);

    Vector3i toVector3i();

    int getBlockX();

    int getBlockY();

    int getBlockZ();
}
