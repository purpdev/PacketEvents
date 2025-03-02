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

package com.github.retrooper.packetevents.protocol.recipe.category;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

public final class RecipeBookCategories {

    private static final VersionedRegistry<RecipeBookCategory> REGISTRY = new VersionedRegistry<>("recipe_book_category");

    private RecipeBookCategories() {
    }

    private static RecipeBookCategory register(String id) {
        return REGISTRY.define(id, StaticRecipeBookCategory::new);
    }

    public static VersionedRegistry<RecipeBookCategory> getRegistry() {
        return REGISTRY;
    }

    public static final RecipeBookCategory CRAFTING_BUILDING_BLOCKS = register("crafting_building_blocks");
    public static final RecipeBookCategory CRAFTING_REDSTONE = register("crafting_redstone");
    public static final RecipeBookCategory CRAFTING_EQUIPMENT = register("crafting_equipment");
    public static final RecipeBookCategory CRAFTING_MISC = register("crafting_misc");
    public static final RecipeBookCategory FURNACE_FOOD = register("furnace_food");
    public static final RecipeBookCategory FURNACE_BLOCKS = register("furnace_blocks");
    public static final RecipeBookCategory FURNACE_MISC = register("furnace_misc");
    public static final RecipeBookCategory BLAST_FURNACE_BLOCKS = register("blast_furnace_blocks");
    public static final RecipeBookCategory BLAST_FURNACE_MISC = register("blast_furnace_misc");
    public static final RecipeBookCategory SMOKER_FOOD = register("smoker_food");
    public static final RecipeBookCategory STONECUTTER = register("stonecutter");
    public static final RecipeBookCategory SMITHING = register("smithing");
    public static final RecipeBookCategory CAMPFIRE = register("campfire");

    static {
        REGISTRY.unloadMappings();
    }
}
