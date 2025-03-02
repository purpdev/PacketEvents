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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collections;

public final class WolfVariants {

    private static final VersionedRegistry<WolfVariant> REGISTRY = new VersionedRegistry<>("wolf_variant");

    private WolfVariants() {
    }

    @ApiStatus.Internal
    public static WolfVariant define(String name, MappedEntitySet<Biome> biomes) {
        return define(name, "wolf_" + name, biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(String name, String assetId, MappedEntitySet<Biome> biomes) {
        return define(name, ResourceLocation.minecraft("entity/wolf/" + assetId),
                ResourceLocation.minecraft("entity/wolf/" + assetId + "_tame"),
                ResourceLocation.minecraft("entity/wolf/" + assetId + "_angry"), biomes);
    }

    @ApiStatus.Internal
    public static WolfVariant define(
            String name,
            ResourceLocation wildTexture,
            ResourceLocation tameTexture,
            ResourceLocation angryTexture,
            MappedEntitySet<Biome> biomes
    ) {
        return REGISTRY.define(name, data -> new StaticWolfVariant(
                data, wildTexture, tameTexture, angryTexture, biomes));
    }

    public static VersionedRegistry<WolfVariant> getRegistry() {
        return REGISTRY;
    }

    public static final WolfVariant PALE = define("pale", "wolf",
            new MappedEntitySet<>(Collections.singletonList(Biomes.TAIGA)));
    public static final WolfVariant SPOTTED = define("spotted",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_savanna")));
    public static final WolfVariant SNOWY = define("snowy",
            new MappedEntitySet<>(Collections.singletonList(Biomes.GROVE)));
    public static final WolfVariant BLACK = define("black",
            new MappedEntitySet<>(Collections.singletonList(Biomes.OLD_GROWTH_PINE_TAIGA)));
    public static final WolfVariant ASHEN = define("ashen",
            new MappedEntitySet<>(Collections.singletonList(Biomes.SNOWY_TAIGA)));
    public static final WolfVariant RUSTY = define("rusty",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_jungle")));
    public static final WolfVariant WOODS = define("woods",
            new MappedEntitySet<>(Collections.singletonList(Biomes.FOREST)));
    public static final WolfVariant CHESTNUT = define("chestnut",
            new MappedEntitySet<>(Collections.singletonList(Biomes.OLD_GROWTH_SPRUCE_TAIGA)));
    public static final WolfVariant STRIPED = define("striped",
            new MappedEntitySet<>(ResourceLocation.minecraft("is_badlands")));

    static {
        REGISTRY.unloadMappings();
    }
}
