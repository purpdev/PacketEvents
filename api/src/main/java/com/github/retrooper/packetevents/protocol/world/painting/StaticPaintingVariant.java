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

package com.github.retrooper.packetevents.protocol.world.painting;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticPaintingVariant extends AbstractMappedEntity implements PaintingVariant {

    private final int width;
    private final int height;
    private final ResourceLocation assetId;
    private final @Nullable Component title;
    private final @Nullable Component author;

    public StaticPaintingVariant(int width, int height, ResourceLocation assetId) {
        this(null, width, height, assetId, null, null);
    }

    public StaticPaintingVariant(@Nullable TypesBuilderData data, int width, int height, ResourceLocation assetId) {
        this(data, width, height, assetId, null, null);
    }

    public StaticPaintingVariant(int width, int height, ResourceLocation assetId, @Nullable Component title, @Nullable Component author) {
        this(null, width, height, assetId, author, title);
    }

    public StaticPaintingVariant(@Nullable TypesBuilderData data, int width, int height, ResourceLocation assetId, @Nullable Component title, @Nullable Component author) {
        super(data);
        this.width = width;
        this.height = height;
        this.assetId = assetId;
        this.title = title;
        this.author = author;
    }

    @Override
    public PaintingVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticPaintingVariant(newData, this.width, this.height, this.assetId);
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public @Nullable Component getTitle() {
        return this.title;
    }

    @Override
    public @Nullable Component getAuthor() {
        return this.author;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticPaintingVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticPaintingVariant that = (StaticPaintingVariant) obj;
        if (this.width != that.width) return false;
        if (this.height != that.height) return false;
        if (!this.assetId.equals(that.assetId)) return false;
        if (!Objects.equals(this.title, that.title)) return false;
        return Objects.equals(this.author, that.author);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.width, this.height, this.assetId, this.title, this.author);
    }

    @Override
    public String toString() {
        return "StaticPaintingVariant{width=" + this.width + ", height=" + this.height + ", assetId=" + this.assetId + ", title=" + this.title + ", author=" + this.author + '}';
    }
}
