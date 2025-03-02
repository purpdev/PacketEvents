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

package com.github.retrooper.packetevents.protocol.recipe;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.recipe.data.CookedRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.RecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.ShapedRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.ShapelessRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.SimpleRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.SmithingRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.SmithingTrimRecipeData;
import com.github.retrooper.packetevents.protocol.recipe.data.StoneCuttingRecipeData;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * <strong>WARNING:</strong> No longer exists since 1.21.2, network recipe data was rewritten.
 */
@ApiStatus.Obsolete
public final class RecipeSerializers {

    private static final VersionedRegistry<RecipeSerializer<?>> REGISTRY = new VersionedRegistry<>("legacy_recipe_serializer");

    private RecipeSerializers() {
    }

    public static VersionedRegistry<RecipeSerializer<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static <T extends RecipeData> RecipeSerializer<T> define(
            String name,
            PacketWrapper.Reader<T> reader,
            PacketWrapper.Writer<T> writer
    ) {
        return define(name, reader, writer, null);
    }

    @ApiStatus.Internal
    public static <T extends RecipeData> RecipeSerializer<T> define(
            String name,
            PacketWrapper.Reader<T> reader,
            PacketWrapper.Writer<T> writer,
            @Nullable RecipeType legacyType
    ) {
        return REGISTRY.define(name, data -> new StaticRecipeSerializer<>(
                data, reader, writer, legacyType));
    }

    public static RecipeSerializer<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static RecipeSerializer<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final RecipeSerializer<ShapedRecipeData> CRAFTING_SHAPED = define("crafting_shaped",
            ShapedRecipeData::read, ShapedRecipeData::write, RecipeType.CRAFTING_SHAPED);
    public static final RecipeSerializer<ShapelessRecipeData> CRAFTING_SHAPELESS = define("crafting_shapeless",
            ShapelessRecipeData::read, ShapelessRecipeData::write, RecipeType.CRAFTING_SHAPELESS);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_ARMORDYE = define("crafting_special_armordye",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_ARMORDYE);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_BOOKCLONING = define("crafting_special_bookcloning",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_BOOKCLONING);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_MAPCLONING = define("crafting_special_mapcloning",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_MAPCLONING);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_MAPEXTENDING = define("crafting_special_mapextending",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_MAPEXTENDING);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_ROCKET = define("crafting_special_firework_rocket",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_ROCKET);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_STAR = define("crafting_special_firework_star",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_FIREWORK_STAR_FADE = define("crafting_special_firework_star_fade",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_FIREWORK_STAR_FADE);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_TIPPEDARROW = define("crafting_special_tippedarrow",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_TIPPEDARROW);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_BANNERDUPLICATE = define("crafting_special_bannerduplicate",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_BANNERDUPLICATE);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SHIELDDECORATION = define("crafting_special_shielddecoration",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SHIELDDECORATION);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SHULKERBOXCOLORING = define("crafting_special_shulkerboxcoloring",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SHULKERBOXCOLORING);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_SUSPICIOUSSTEW = define("crafting_special_suspiciousstew",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_SUSPICIOUSSTEW);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_SPECIAL_REPAIRITEM = define("crafting_special_repairitem",
            SimpleRecipeData::read, SimpleRecipeData::write, RecipeType.CRAFTING_SPECIAL_REPAIRITEM);
    public static final RecipeSerializer<CookedRecipeData> SMELTING = define("smelting",
            CookedRecipeData::read, CookedRecipeData::write, RecipeType.SMELTING);
    public static final RecipeSerializer<CookedRecipeData> BLASTING = define("blasting",
            CookedRecipeData::read, CookedRecipeData::write, RecipeType.BLASTING);
    public static final RecipeSerializer<CookedRecipeData> SMOKING = define("smoking",
            CookedRecipeData::read, CookedRecipeData::write, RecipeType.SMOKING);
    public static final RecipeSerializer<CookedRecipeData> CAMPFIRE_COOKING = define("campfire_cooking",
            CookedRecipeData::read, CookedRecipeData::write, RecipeType.CAMPFIRE_COOKING);
    public static final RecipeSerializer<StoneCuttingRecipeData> STONECUTTING = define("stonecutting",
            StoneCuttingRecipeData::read, StoneCuttingRecipeData::write, RecipeType.STONECUTTING);
    @ApiStatus.Obsolete
    public static final RecipeSerializer<SmithingRecipeData> SMITHING = define("smithing",
            ew -> SmithingRecipeData.read(ew, true),
            (ew, data) -> SmithingRecipeData.write(ew, data, true),
            RecipeType.SMITHING);
    public static final RecipeSerializer<SmithingRecipeData> SMITHING_TRANSFORM = define("smithing_transform",
            SmithingRecipeData::read, SmithingRecipeData::write, RecipeType.SMITHING);
    public static final RecipeSerializer<SmithingTrimRecipeData> SMITHING_TRIM = define("smithing_trim",
            SmithingTrimRecipeData::read, SmithingTrimRecipeData::write);
    public static final RecipeSerializer<SimpleRecipeData> CRAFTING_DECORATED_POT = define("crafting_decorated_pot",
            SimpleRecipeData::read, SimpleRecipeData::write);

    /**
     * Returns an immutable view of the banner patterns.
     *
     * @return Banner Patterns
     */
    public static Collection<RecipeSerializer<?>> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
