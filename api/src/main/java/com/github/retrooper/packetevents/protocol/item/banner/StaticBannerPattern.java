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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticBannerPattern extends AbstractMappedEntity implements BannerPattern {

    private final ResourceLocation assetId;
    private final String translationKey;

    public StaticBannerPattern(ResourceLocation assetId, String translationKey) {
        this(null, assetId, translationKey);
    }

    @ApiStatus.Internal
    public StaticBannerPattern(@Nullable TypesBuilderData data, ResourceLocation assetId, String translationKey) {
        super(data);
        this.assetId = assetId;
        this.translationKey = translationKey;
    }

    @Override
    public BannerPattern copy(@Nullable TypesBuilderData newData) {
        return new StaticBannerPattern(newData, this.assetId, this.translationKey);
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public String getTranslationKey() {
        return this.translationKey;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticBannerPattern)) return false;
        if (!super.equals(obj)) return false;
        StaticBannerPattern that = (StaticBannerPattern) obj;
        if (!this.assetId.equals(that.assetId)) return false;
        return this.translationKey.equals(that.translationKey);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.assetId, this.translationKey);
    }

    @Override
    public String toString() {
        return "StaticBannerPattern{assetId=" + this.assetId + ", translationKey='" + this.translationKey + '\'' + '}';
    }
}
