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
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticTrimPattern extends AbstractMappedEntity implements TrimPattern {

    private final ResourceLocation assetId;
    private final ItemType templateItem;
    private final Component description;
    private final boolean decal;

    public StaticTrimPattern(
            ResourceLocation assetId, ItemType templateItem,
            Component description, boolean decal
    ) {
        this(null, assetId, templateItem, description, decal);
    }

    @ApiStatus.Internal
    public StaticTrimPattern(
            @Nullable TypesBuilderData data,
            ResourceLocation assetId, ItemType templateItem,
            Component description, boolean decal
    ) {
        super(data);
        this.assetId = assetId;
        this.templateItem = templateItem;
        this.description = description;
        this.decal = decal;
    }

    @Override
    public TrimPattern copy(@Nullable TypesBuilderData newData) {
        return new StaticTrimPattern(newData, this.assetId,
                this.templateItem, this.description, this.decal);
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public ItemType getTemplateItem() {
        return this.templateItem;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean isDecal() {
        return this.decal;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticTrimPattern)) return false;
        if (!super.equals(obj)) return false;
        StaticTrimPattern that = (StaticTrimPattern) obj;
        if (this.decal != that.decal) return false;
        if (!this.assetId.equals(that.assetId)) return false;
        if (!this.templateItem.equals(that.templateItem)) return false;
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.assetId, this.templateItem, this.description, this.decal);
    }

    @Override
    public String toString() {
        return "StaticTrimPattern{assetId=" + this.assetId + ", templateItem=" + this.templateItem + ", description=" + this.description + ", decal=" + this.decal + '}';
    }
}
