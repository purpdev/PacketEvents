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

package com.github.retrooper.packetevents.protocol.recipe.display;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class RecipeDisplayTypes {

    private static final VersionedRegistry<RecipeDisplayType<?>> REGISTRY = new VersionedRegistry<>("recipe_display");

    private RecipeDisplayTypes() {
    }

    private static <T extends RecipeDisplay<?>> RecipeDisplayType<T> register(
            String id, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(id, data -> new StaticRecipeDisplayType<>(data, reader, writer));
    }

    public static VersionedRegistry<RecipeDisplayType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final RecipeDisplayType<ShapelessCraftingRecipeDisplay> CRAFTING_SHAPELESS = register(
            "crafting_shapeless", ShapelessCraftingRecipeDisplay::read, ShapelessCraftingRecipeDisplay::write);
    public static final RecipeDisplayType<ShapedCraftingRecipeDisplay> CRAFTING_SHAPED = register(
            "crafting_shaped", ShapedCraftingRecipeDisplay::read, ShapedCraftingRecipeDisplay::write);
    public static final RecipeDisplayType<FurnaceRecipeDisplay> FURNACE = register(
            "furnace", FurnaceRecipeDisplay::read, FurnaceRecipeDisplay::write);
    public static final RecipeDisplayType<StonecutterRecipeDisplay> STONECUTTER = register(
            "stonecutter", StonecutterRecipeDisplay::read, StonecutterRecipeDisplay::write);
    public static final RecipeDisplayType<SmithingRecipeDisplay> SMITHING = register(
            "smithing", SmithingRecipeDisplay::read, SmithingRecipeDisplay::write);

    static {
        REGISTRY.unloadMappings();
    }
}
