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

package com.github.retrooper.packetevents.protocol.item.mapdecoration;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

import static com.github.retrooper.packetevents.resources.ResourceLocation.minecraft;

public final class MapDecorationTypes {

    private static final VersionedRegistry<MapDecorationType> REGISTRY = new VersionedRegistry<>("map_decoration_type");

    private MapDecorationTypes() {
    }

    @ApiStatus.Internal
    public static MapDecorationType define(String key, boolean showOnItemFrame, boolean trackCount) {
        return define(key, minecraft(key), showOnItemFrame, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(
            String key, ResourceLocation assetId,
            boolean showOnItemFrame, boolean trackCount
    ) {
        return define(key, assetId, showOnItemFrame,
                -1, false, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(
            String key,
            boolean showOnItemFrame, int mapColor,
            boolean explorationMapElement, boolean trackCount
    ) {
        return define(key, minecraft(key),
                showOnItemFrame, mapColor, explorationMapElement, trackCount);
    }

    @ApiStatus.Internal
    public static MapDecorationType define(
            String key, ResourceLocation assetId,
            boolean showOnItemFrame, int mapColor,
            boolean explorationMapElement, boolean trackCount
    ) {
        return REGISTRY.define(key, data -> new StaticMapDecorationType(data,
                assetId, showOnItemFrame, mapColor, explorationMapElement, trackCount));
    }

    public static VersionedRegistry<MapDecorationType> getRegistry() {
        return REGISTRY;
    }

    public static @Nullable MapDecorationType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static @Nullable MapDecorationType getById(int id, ClientVersion version) {
        return REGISTRY.getById(version, id);
    }

    public static @Nullable MapDecorationType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    // color constants used by vanilla
    private static final int LIGHT_GRAY_COLOR = 0x999999;
    private static final int COPPER_COLOR = 0xC26B4C;

    public static final MapDecorationType PLAYER = define("player",
            false, true);
    public static final MapDecorationType FRAME = define("frame",
            true, true);
    public static final MapDecorationType RED_MARKER = define("red_marker",
            false, true);
    public static final MapDecorationType BLUE_MARKER = define("blue_marker",
            false, true);
    public static final MapDecorationType TARGET_X = define("target_x",
            true, false);
    public static final MapDecorationType TARGET_POINT = define("target_point",
            true, false);
    public static final MapDecorationType PLAYER_OFF_MAP = define("player_off_map",
            false, true);
    public static final MapDecorationType PLAYER_OFF_LIMITS = define("player_off_limits",
            false, true);
    public static final MapDecorationType MANSION = define("mansion",
            true, 0x524C44, true, false);
    public static final MapDecorationType MONUMENT = define("monument",
            true, 0x3A7265, true, false);
    public static final MapDecorationType BANNER_WHITE = define("banner_white",
            minecraft("white_banner"), true, true);
    public static final MapDecorationType BANNER_ORANGE = define("banner_orange",
            minecraft("orange_banner"), true, true);
    public static final MapDecorationType BANNER_MAGENTA = define("banner_magenta",
            minecraft("magenta_banner"), true, true);
    public static final MapDecorationType BANNER_LIGHT_BLUE = define("banner_light_blue",
            minecraft("light_blue_banner"), true, true);
    public static final MapDecorationType BANNER_YELLOW = define("banner_yellow",
            minecraft("yellow_banner"), true, true);
    public static final MapDecorationType BANNER_LIME = define("banner_lime",
            minecraft("lime_banner"), true, true);
    public static final MapDecorationType BANNER_PINK = define("banner_pink",
            minecraft("pink_banner"), true, true);
    public static final MapDecorationType BANNER_GRAY = define("banner_gray",
            minecraft("gray_banner"), true, true);
    public static final MapDecorationType BANNER_LIGHT_GRAY = define("banner_light_gray",
            minecraft("light_gray_banner"), true, true);
    public static final MapDecorationType BANNER_CYAN = define("banner_cyan",
            minecraft("cyan_banner"), true, true);
    public static final MapDecorationType BANNER_PURPLE = define("banner_purple",
            minecraft("purple_banner"), true, true);
    public static final MapDecorationType BANNER_BLUE = define("banner_blue",
            minecraft("blue_banner"), true, true);
    public static final MapDecorationType BANNER_BROWN = define("banner_brown",
            minecraft("brown_banner"), true, true);
    public static final MapDecorationType BANNER_GREEN = define("banner_green",
            minecraft("green_banner"), true, true);
    public static final MapDecorationType BANNER_RED = define("banner_red",
            minecraft("red_banner"), true, true);
    public static final MapDecorationType BANNER_BLACK = define("banner_black",
            minecraft("black_banner"), true, true);
    public static final MapDecorationType RED_X = define("red_x",
            true, false);

    // Added with 1.20.2
    public static final MapDecorationType VILLAGE_DESERT = define("village_desert", minecraft("desert_village"),
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType VILLAGE_PLAINS = define("village_plains", minecraft("plains_village"),
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType VILLAGE_SAVANNA = define("village_savanna", minecraft("savanna_village"),
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType VILLAGE_SNOWY = define("village_snowy", minecraft("snowy_village"),
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType VILLAGE_TAIGA = define("village_taiga", minecraft("taiga_village"),
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType JUNGLE_TEMPLE = define("jungle_temple",
            true, LIGHT_GRAY_COLOR, true, false);
    public static final MapDecorationType SWAMP_HUT = define("swamp_hut",
            true, LIGHT_GRAY_COLOR, true, false);

    // Added with 1.20.5
    public static final MapDecorationType TRIAL_CHAMBERS = define("trial_chambers",
            true, COPPER_COLOR, true, false);

    /**
     * Returns an immutable view of the map decoration types.
     *
     * @return Map Decoration Types
     */
    public static Collection<MapDecorationType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
