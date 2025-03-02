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

package com.github.retrooper.packetevents.protocol.item.trimmaterial;

import com.github.retrooper.packetevents.protocol.item.armormaterial.ArmorMaterial;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public class StaticTrimMaterial extends AbstractMappedEntity implements TrimMaterial {

    private final String assetName;
    private final ItemType ingredient;
    private final float itemModelIndex;
    private final Map<ArmorMaterial, String> overrideArmorMaterials;
    private final Component description;

    public StaticTrimMaterial(
            String assetName, ItemType ingredient,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        this(null, assetName, ingredient, FALLBACK_ITEM_MODEL_INDEX, overrideArmorMaterials, description);
    }

    @ApiStatus.Internal
    public StaticTrimMaterial(
            @Nullable TypesBuilderData data,
            String assetName, ItemType ingredient,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        this(data, assetName, ingredient, FALLBACK_ITEM_MODEL_INDEX, overrideArmorMaterials, description);
    }

    public StaticTrimMaterial(
            String assetName, ItemType ingredient, float itemModelIndex,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        this(null, assetName, ingredient, itemModelIndex, overrideArmorMaterials, description);
    }

    @ApiStatus.Internal
    public StaticTrimMaterial(
            @Nullable TypesBuilderData data,
            String assetName, ItemType ingredient, float itemModelIndex,
            Map<ArmorMaterial, String> overrideArmorMaterials, Component description
    ) {
        super(data);
        this.assetName = assetName;
        this.ingredient = ingredient;
        this.itemModelIndex = itemModelIndex;
        this.overrideArmorMaterials = overrideArmorMaterials;
        this.description = description;
    }

    @Override
    public TrimMaterial copy(@Nullable TypesBuilderData newData) {
        return new StaticTrimMaterial(newData, this.assetName, this.ingredient, this.itemModelIndex,
                this.overrideArmorMaterials, this.description);
    }

    @Override
    public String getAssetName() {
        return this.assetName;
    }

    @Override
    public ItemType getIngredient() {
        return this.ingredient;
    }

    @Override
    public float getItemModelIndex() {
        return this.itemModelIndex;
    }

    @Override
    public Map<ArmorMaterial, String> getOverrideArmorMaterials() {
        return this.overrideArmorMaterials;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticTrimMaterial)) return false;
        if (!super.equals(obj)) return false;
        StaticTrimMaterial that = (StaticTrimMaterial) obj;
        if (Float.compare(that.itemModelIndex, this.itemModelIndex) != 0) return false;
        if (!this.assetName.equals(that.assetName)) return false;
        if (!this.ingredient.equals(that.ingredient)) return false;
        if (!this.overrideArmorMaterials.equals(that.overrideArmorMaterials)) return false;
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.assetName, this.ingredient, this.itemModelIndex, this.overrideArmorMaterials, this.description);
    }

    @Override
    public String toString() {
        return "StaticTrimMaterial{assetName='" + this.assetName + '\'' + ", ingredient=" + this.ingredient + ", itemModelIndex=" + this.itemModelIndex + ", overrideArmorMaterials=" + this.overrideArmorMaterials + ", description=" + this.description + '}';
    }
}
