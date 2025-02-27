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

package com.github.retrooper.packetevents.protocol.world.dimension;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

import java.util.OptionalLong;

public final class DimensionTypes {

    private static final VersionedRegistry<DimensionType> REGISTRY = new VersionedRegistry<>("dimension_type");

    private DimensionTypes() {
    }

    public static VersionedRegistry<DimensionType> getRegistry() {
        return REGISTRY;
    }

    private static final int PRE118_MIN_Y = 0, PRE118_HEIGHT = 256 - PRE118_MIN_Y;
    private static final int POST118_MIN_Y = -64, POST118_HEIGHT = 256 + 64 - POST118_MIN_Y;

    public static final DimensionType OVERWORLD = REGISTRY.define("overworld", data -> {
        NBTCompound monsterSpawnLightLevel = new NBTCompound();
        monsterSpawnLightLevel.setTag("type", new NBTString("minecraft:uniform"));
        monsterSpawnLightLevel.setTag("min_inclusive", new NBTInt(0));
        monsterSpawnLightLevel.setTag("max_inclusive", new NBTInt(7));

        return new StaticDimensionType(data, OptionalLong.empty(), true, false,
                false, true, 1d, true, false,
                POST118_MIN_Y, POST118_HEIGHT, POST118_HEIGHT, "#minecraft:infiniburn_overworld",
                ResourceLocation.minecraft("overworld"), 0f, false,
                true, monsterSpawnLightLevel, 0) {
            @Override
            public int getMinY(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_MIN_Y : PRE118_MIN_Y;
            }

            @Override
            public int getHeight(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_HEIGHT : PRE118_HEIGHT;
            }

            @Override
            public int getLogicalHeight(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_HEIGHT : PRE118_HEIGHT;
            }
        };
    });

    public static final DimensionType OVERWORLD_CAVES = REGISTRY.define("overworld_caves", data -> {
        NBTCompound monsterSpawnLightLevel = new NBTCompound();
        monsterSpawnLightLevel.setTag("type", new NBTString("minecraft:uniform"));
        monsterSpawnLightLevel.setTag("min_inclusive", new NBTInt(0));
        monsterSpawnLightLevel.setTag("max_inclusive", new NBTInt(7));

        return new StaticDimensionType(data, OptionalLong.empty(), true, true,
                false, true, 1d, true, false,
                POST118_MIN_Y, POST118_HEIGHT, POST118_HEIGHT, "#minecraft:infiniburn_overworld",
                ResourceLocation.minecraft("overworld"), 0f, false,
                true, monsterSpawnLightLevel, 0) {
            @Override
            public int getMinY(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_MIN_Y : PRE118_MIN_Y;
            }

            @Override
            public int getHeight(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_HEIGHT : PRE118_HEIGHT;
            }

            @Override
            public int getLogicalHeight(ClientVersion version) {
                return version.isNewerThanOrEquals(ClientVersion.V_1_18) ? POST118_HEIGHT : PRE118_HEIGHT;
            }
        };
    });

    public static final DimensionType THE_END = REGISTRY.define("the_end", data -> {
        NBTCompound monsterSpawnLightLevel = new NBTCompound();
        monsterSpawnLightLevel.setTag("type", new NBTString("minecraft:uniform"));
        monsterSpawnLightLevel.setTag("min_inclusive", new NBTInt(0));
        monsterSpawnLightLevel.setTag("max_inclusive", new NBTInt(7));

        return new StaticDimensionType(data, OptionalLong.of(6000L), false, false,
                false, false, 1d, false, false,
                0, 256, 256, "#minecraft:infiniburn_end",
                ResourceLocation.minecraft("the_end"), 0f, false, true,
                monsterSpawnLightLevel, 0);
    });

    public static final DimensionType THE_NETHER = REGISTRY.define("the_nether",
            data -> new StaticDimensionType(data, OptionalLong.of(18000L), false,
                    true, true, false, 8d, false,
                    true, 0, 256, 128,
                    "#minecraft:infiniburn_nether", ResourceLocation.minecraft("the_nether"),
                    0.1f, true, false, new NBTInt(7),
                    15));

    static {
        REGISTRY.unloadMappings();
    }
}
