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

package com.github.retrooper.packetevents.protocol.world.biome;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class Biomes {

    private static final VersionedRegistry<Biome> REGISTRY = new VersionedRegistry<>("worldgen/biome");

    // load data from file, biomes are too complex to define in code here
    private static final Map<ResourceLocation, NBTCompound> BIOME_DATA;

    static {
        BIOME_DATA = new HashMap<>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/worldgen/biome")) {
            dataTag.skipOne(); // skip version
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound) dataTag.next().getValue()) {
                ResourceLocation biomeKey = new ResourceLocation(entry.getKey());
                BIOME_DATA.put(biomeKey, ((SequentialNBTReader.Compound) entry.getValue()).readFully());
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading biome data", exception);
        }
    }

    private Biomes() {
    }

    @ApiStatus.Internal
    public static Biome define(String key) {
        return REGISTRY.define(key, data -> {
            NBTCompound dataTag = BIOME_DATA.get(data.getName());
            if (dataTag == null) {
                throw new IllegalArgumentException("Can't define biome " + data.getName() + ", no data found");
            }
            return Biome.decode(dataTag, ClientVersion.getLatest(), data);
        });
    }

    public static VersionedRegistry<Biome> getRegistry() {
        return REGISTRY;
    }

    public static final Biome BADLANDS = define("badlands");
    public static final Biome BAMBOO_JUNGLE = define("bamboo_jungle");
    public static final Biome BASALT_DELTAS = define("basalt_deltas");
    public static final Biome BEACH = define("beach");
    public static final Biome BIRCH_FOREST = define("birch_forest");
    public static final Biome CHERRY_GROVE = define("cherry_grove");
    public static final Biome COLD_OCEAN = define("cold_ocean");
    public static final Biome CRIMSON_FOREST = define("crimson_forest");
    public static final Biome DARK_FOREST = define("dark_forest");
    public static final Biome DEEP_COLD_OCEAN = define("deep_cold_ocean");
    public static final Biome DEEP_DARK = define("deep_dark");
    public static final Biome DEEP_FROZEN_OCEAN = define("deep_frozen_ocean");
    public static final Biome DEEP_LUKEWARM_OCEAN = define("deep_lukewarm_ocean");
    public static final Biome DEEP_OCEAN = define("deep_ocean");
    public static final Biome DESERT = define("desert");
    public static final Biome DRIPSTONE_CAVES = define("dripstone_caves");
    public static final Biome END_BARRENS = define("end_barrens");
    public static final Biome END_HIGHLANDS = define("end_highlands");
    public static final Biome END_MIDLANDS = define("end_midlands");
    public static final Biome ERODED_BADLANDS = define("eroded_badlands");
    public static final Biome FLOWER_FOREST = define("flower_forest");
    public static final Biome FOREST = define("forest");
    public static final Biome FROZEN_OCEAN = define("frozen_ocean");
    public static final Biome FROZEN_PEAKS = define("frozen_peaks");
    public static final Biome FROZEN_RIVER = define("frozen_river");
    public static final Biome GROVE = define("grove");
    public static final Biome ICE_SPIKES = define("ice_spikes");
    public static final Biome JAGGED_PEAKS = define("jagged_peaks");
    public static final Biome JUNGLE = define("jungle");
    public static final Biome LUKEWARM_OCEAN = define("lukewarm_ocean");
    public static final Biome LUSH_CAVES = define("lush_caves");
    public static final Biome MANGROVE_SWAMP = define("mangrove_swamp");
    public static final Biome MEADOW = define("meadow");
    public static final Biome MUSHROOM_FIELDS = define("mushroom_fields");
    public static final Biome NETHER_WASTES = define("nether_wastes");
    public static final Biome OCEAN = define("ocean");
    public static final Biome OLD_GROWTH_BIRCH_FOREST = define("old_growth_birch_forest");
    public static final Biome OLD_GROWTH_PINE_TAIGA = define("old_growth_pine_taiga");
    public static final Biome OLD_GROWTH_SPRUCE_TAIGA = define("old_growth_spruce_taiga");
    public static final Biome PLAINS = define("plains");
    public static final Biome RIVER = define("river");
    public static final Biome SAVANNA = define("savanna");
    public static final Biome SAVANNA_PLATEAU = define("savanna_plateau");
    public static final Biome SMALL_END_ISLANDS = define("small_end_islands");
    public static final Biome SNOWY_BEACH = define("snowy_beach");
    public static final Biome SNOWY_PLAINS = define("snowy_plains");
    public static final Biome SNOWY_SLOPES = define("snowy_slopes");
    public static final Biome SNOWY_TAIGA = define("snowy_taiga");
    public static final Biome SOUL_SAND_VALLEY = define("soul_sand_valley");
    public static final Biome SPARSE_JUNGLE = define("sparse_jungle");
    public static final Biome STONY_PEAKS = define("stony_peaks");
    public static final Biome STONY_SHORE = define("stony_shore");
    public static final Biome SUNFLOWER_PLAINS = define("sunflower_plains");
    public static final Biome SWAMP = define("swamp");
    public static final Biome TAIGA = define("taiga");
    public static final Biome THE_END = define("the_end");
    public static final Biome THE_VOID = define("the_void");
    public static final Biome WARM_OCEAN = define("warm_ocean");
    public static final Biome WARPED_FOREST = define("warped_forest");
    public static final Biome WINDSWEPT_FOREST = define("windswept_forest");
    public static final Biome WINDSWEPT_GRAVELLY_HILLS = define("windswept_gravelly_hills");
    public static final Biome WINDSWEPT_HILLS = define("windswept_hills");
    public static final Biome WINDSWEPT_SAVANNA = define("windswept_savanna");
    public static final Biome WOODED_BADLANDS = define("wooded_badlands");

    // added with 1.21.2
    public static final Biome PALE_GARDEN = define("pale_garden");

    static {
        BIOME_DATA.clear();
        REGISTRY.unloadMappings();
    }
}
