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

package com.github.retrooper.packetevents.protocol.item.banner;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

public final class BannerPatterns {

    private static final VersionedRegistry<BannerPattern> REGISTRY = new VersionedRegistry<>("banner_pattern");

    private BannerPatterns() {
    }

    @ApiStatus.Internal
    public static BannerPattern define(String key) {
        ResourceLocation assetId = ResourceLocation.minecraft(key);
        String translationKey = "block.minecraft.banner." + key;
        return define(key, assetId, translationKey);
    }

    @ApiStatus.Internal
    public static BannerPattern define(String key, ResourceLocation assetId, String translationKey) {
        return REGISTRY.define(key, data ->
                new StaticBannerPattern(data, assetId, translationKey));
    }

    public static VersionedRegistry<BannerPattern> getRegistry() {
        return REGISTRY;
    }

    public static BannerPattern getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static BannerPattern getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final BannerPattern SQUARE_BOTTOM_LEFT = define("square_bottom_left");
    public static final BannerPattern STRIPE_BOTTOM = define("stripe_bottom");
    public static final BannerPattern CREEPER = define("creeper");
    public static final BannerPattern HALF_HORIZONTAL = define("half_horizontal");
    public static final BannerPattern STRIPE_MIDDLE = define("stripe_middle");
    public static final BannerPattern BASE = define("base");
    public static final BannerPattern DIAGONAL_UP_RIGHT = define("diagonal_up_right");
    public static final BannerPattern HALF_HORIZONTAL_BOTTOM = define("half_horizontal_bottom");
    public static final BannerPattern SMALL_STRIPES = define("small_stripes");
    public static final BannerPattern GRADIENT_UP = define("gradient_up");
    public static final BannerPattern CIRCLE = define("circle");
    public static final BannerPattern STRIPE_DOWNLEFT = define("stripe_downleft");
    public static final BannerPattern RHOMBUS = define("rhombus");
    public static final BannerPattern TRIANGLES_BOTTOM = define("triangles_bottom");
    public static final BannerPattern STRIPE_CENTER = define("stripe_center");
    public static final BannerPattern SQUARE_BOTTOM_RIGHT = define("square_bottom_right");
    public static final BannerPattern DIAGONAL_RIGHT = define("diagonal_right");
    public static final BannerPattern MOJANG = define("mojang");
    public static final BannerPattern STRIPE_LEFT = define("stripe_left");
    public static final BannerPattern SQUARE_TOP_LEFT = define("square_top_left");
    public static final BannerPattern TRIANGLE_BOTTOM = define("triangle_bottom");
    public static final BannerPattern SKULL = define("skull");
    public static final BannerPattern SQUARE_TOP_RIGHT = define("square_top_right");
    public static final BannerPattern GLOBE = define("globe");
    public static final BannerPattern STRIPE_TOP = define("stripe_top");
    public static final BannerPattern CROSS = define("cross");
    public static final BannerPattern BRICKS = define("bricks");
    public static final BannerPattern HALF_VERTICAL = define("half_vertical");
    public static final BannerPattern STRIPE_DOWNRIGHT = define("stripe_downright");
    public static final BannerPattern TRIANGLES_TOP = define("triangles_top");
    public static final BannerPattern STRIPE_RIGHT = define("stripe_right");
    public static final BannerPattern DIAGONAL_UP_LEFT = define("diagonal_up_left");
    public static final BannerPattern HALF_VERTICAL_RIGHT = define("half_vertical_right");
    public static final BannerPattern TRIANGLE_TOP = define("triangle_top");
    public static final BannerPattern FLOWER = define("flower");
    public static final BannerPattern STRAIGHT_CROSS = define("straight_cross");
    public static final BannerPattern GRADIENT = define("gradient");
    public static final BannerPattern CURLY_BORDER = define("curly_border");
    public static final BannerPattern BORDER = define("border");
    public static final BannerPattern PIGLIN = define("piglin");
    public static final BannerPattern DIAGONAL_LEFT = define("diagonal_left");

    // Added with 1.21
    public static final BannerPattern FLOW = define("flow");
    public static final BannerPattern GUSTER = define("guster");

    /**
     * Returns an immutable view of the banner patterns.
     *
     * @return Banner Patterns
     */
    public static Collection<BannerPattern> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
