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

package com.github.retrooper.packetevents.protocol.entity.frog;

import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

public final class FrogVariants {

    public static final VersionedRegistry<FrogVariant> REGISTRY = new VersionedRegistry<>(
            "frog_variant", "entity/frog_variant_mappings");

    public static final FrogVariant TEMPERATE = define("temperate");
    public static final FrogVariant WARM = define("warm");
    public static final FrogVariant COLD = define("cold");

    private FrogVariants() {
    }

    private static FrogVariant define(String name) {
        return define(name, ResourceLocation.minecraft("textures/entity/frog/" + name + "_frog.png"));
    }

    private static FrogVariant define(String name, ResourceLocation texture) {
        return REGISTRY.define(name, data -> new FrogVariantImpl(data, texture));
    }

    public static VersionedRegistry<FrogVariant> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
