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

package com.github.retrooper.packetevents.protocol.item.trimpattern;

import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

public final class TrimPatterns {

    private static final VersionedRegistry<TrimPattern> REGISTRY = new VersionedRegistry<>("trim_pattern");

    private TrimPatterns() {
    }

    @ApiStatus.Internal
    public static TrimPattern define(String name) {
        ResourceLocation assetId = ResourceLocation.minecraft(name);
        ItemType templateItem = ItemTypes.getByName(assetId + "_armor_trim_smithing_template");
        Component description = Component.translatable("trim_pattern.minecraft." + name);
        boolean decal = false;
        return define(name, assetId, templateItem, description, decal);
    }

    @ApiStatus.Internal
    public static TrimPattern define(
            String name, ResourceLocation assetId, ItemType templateItem,
            Component description, boolean decal
    ) {
        return REGISTRY.define(name, data ->
                new StaticTrimPattern(data, assetId, templateItem, description, decal));
    }

    public static VersionedRegistry<TrimPattern> getRegistry() {
        return REGISTRY;
    }

    public static TrimPattern getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static TrimPattern getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    // Added in 1.19.4
    public static final TrimPattern COAST = define("coast");
    public static final TrimPattern DUNE = define("dune");
    public static final TrimPattern EYE = define("eye");
    public static final TrimPattern RIB = define("rib");
    public static final TrimPattern SENTRY = define("sentry");
    public static final TrimPattern SNOUT = define("snout");
    public static final TrimPattern SPIRE = define("spire");
    public static final TrimPattern TIDE = define("tide");
    public static final TrimPattern VEX = define("vex");
    public static final TrimPattern WARD = define("ward");
    public static final TrimPattern WILD = define("wild");

    // Added in 1.20
    public static final TrimPattern RAISER = define("raiser");
    public static final TrimPattern HOST = define("host");
    public static final TrimPattern SILENCE = define("silence");
    public static final TrimPattern SHAPER = define("shaper");
    public static final TrimPattern WAYFINDER = define("wayfinder");

    // Added in 1.20.5
    public static final TrimPattern BOLT = define("bolt");
    public static final TrimPattern FLOW = define("flow");

    static {
        REGISTRY.unloadMappings();
    }
}
