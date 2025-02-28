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

package com.github.retrooper.packetevents.protocol.entity.wolfvariant;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticWolfVariant extends AbstractMappedEntity implements WolfVariant {

    private final ResourceLocation wildTexture;
    private final ResourceLocation tameTexture;
    private final ResourceLocation angryTexture;
    private final MappedEntitySet<Biome> biomes;

    public StaticWolfVariant(
            ResourceLocation wildTexture,
            ResourceLocation tameTexture,
            ResourceLocation angryTexture,
            MappedEntitySet<Biome> biomes
    ) {
        this(null, wildTexture, tameTexture, angryTexture, biomes);
    }

    @ApiStatus.Internal
    public StaticWolfVariant(
            @Nullable TypesBuilderData data,
            ResourceLocation wildTexture,
            ResourceLocation tameTexture,
            ResourceLocation angryTexture,
            MappedEntitySet<Biome> biomes
    ) {
        super(data);
        this.wildTexture = wildTexture;
        this.tameTexture = tameTexture;
        this.angryTexture = angryTexture;
        this.biomes = biomes;
    }

    @Override
    public WolfVariant copy(@Nullable TypesBuilderData newData) {
        return new StaticWolfVariant(newData, this.wildTexture, this.tameTexture, this.angryTexture, this.biomes);
    }

    @Override
    public ResourceLocation getWildTexture() {
        return this.wildTexture;
    }

    @Override
    public ResourceLocation getTameTexture() {
        return this.tameTexture;
    }

    @Override
    public ResourceLocation getAngryTexture() {
        return this.angryTexture;
    }

    @Override
    public MappedEntitySet<Biome> getBiomes() {
        return this.biomes;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticWolfVariant)) return false;
        if (!super.equals(obj)) return false;
        StaticWolfVariant that = (StaticWolfVariant) obj;
        if (!this.wildTexture.equals(that.wildTexture)) return false;
        if (!this.tameTexture.equals(that.tameTexture)) return false;
        if (!this.angryTexture.equals(that.angryTexture)) return false;
        return this.biomes.equals(that.biomes);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.wildTexture, this.tameTexture, this.angryTexture, this.biomes);
    }

    @Override
    public String toString() {
        return "StaticWolfVariant{wildTexture=" + this.wildTexture + ", tameTexture=" + this.tameTexture + ", angryTexture=" + this.angryTexture + ", biomes=" + this.biomes + '}';
    }
}
