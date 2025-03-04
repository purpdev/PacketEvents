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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.NotNull;

public class WorldBlockPosition {

    private ResourceLocation world;
    private Vector3i blockPosition;

    public WorldBlockPosition(@NotNull ResourceLocation world, @NotNull Vector3i blockPosition) {
        this.world = world;
        this.blockPosition = blockPosition;
    }

    public WorldBlockPosition(@NotNull ResourceLocation world, int x, int y, int z) {
        this.world = world;
        this.blockPosition = new Vector3i(x, y, z);
    }

    @Deprecated
    public WorldBlockPosition(@NotNull Dimension dimension, @NotNull Vector3i blockPosition) {
        this(new ResourceLocation(dimension.getDimensionName()), blockPosition);
    }

    /**
     * @deprecated Multiple dimensions can have the same dimension type, please reference the specific dimension using a {@link ResourceLocation}.
     */
    @Deprecated
    public WorldBlockPosition(@NotNull DimensionType dimensionType, @NotNull Vector3i blockPosition) {
        this(dimensionType.getName(), blockPosition);
    }

    public static WorldBlockPosition read(PacketWrapper<?> wrapper) {
        ResourceLocation dimension = wrapper.readIdentifier();
        Vector3i pos = wrapper.readBlockPosition();
        return new WorldBlockPosition(dimension, pos);
    }

    public static void write(PacketWrapper<?> wrapper, WorldBlockPosition pos) {
        wrapper.writeIdentifier(pos.world);
        wrapper.writeBlockPosition(pos.blockPosition);
    }

    public ResourceLocation getWorld() {
        return world;
    }

    public void setWorld(ResourceLocation world) {
        this.world = world;
    }

    public Vector3i getBlockPosition() {
        return blockPosition;
    }

    public void setBlockPosition(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }
}
