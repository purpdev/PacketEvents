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

import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.biome.Biome;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

public interface WolfVariant extends MappedEntity, CopyableEntity<WolfVariant>, DeepComparableEntity {

    ResourceLocation getWildTexture();

    ResourceLocation getTameTexture();

    ResourceLocation getAngryTexture();

    MappedEntitySet<Biome> getBiomes();

    static WolfVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(WolfVariants.getRegistry(), WolfVariant::readDirect);
    }

    static void write(PacketWrapper<?> wrapper, WolfVariant variant) {
        wrapper.writeMappedEntityOrDirect(variant, WolfVariant::writeDirect);
    }

    static WolfVariant readDirect(PacketWrapper<?> wrapper) {
        ResourceLocation wildTexture = wrapper.readIdentifier();
        ResourceLocation tameTexture = wrapper.readIdentifier();
        ResourceLocation angryTexture = wrapper.readIdentifier();
        MappedEntitySet<Biome> biomes = MappedEntitySet.read(wrapper, Biomes.getRegistry());
        return new StaticWolfVariant(wildTexture, tameTexture, angryTexture, biomes);
    }

    static void writeDirect(PacketWrapper<?> wrapper, WolfVariant variant) {
        wrapper.writeIdentifier(variant.getWildTexture());
        wrapper.writeIdentifier(variant.getTameTexture());
        wrapper.writeIdentifier(variant.getAngryTexture());
        MappedEntitySet.write(wrapper, variant.getBiomes());
    }

    static WolfVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        ResourceLocation wildTexture = new ResourceLocation(compound.getStringTagValueOrThrow("wild_texture"));
        ResourceLocation tameTexture = new ResourceLocation(compound.getStringTagValueOrThrow("tame_texture"));
        ResourceLocation angryTexture = new ResourceLocation(compound.getStringTagValueOrThrow("angry_texture"));
        MappedEntitySet<Biome> biomes = MappedEntitySet.decode(
                compound.getTagOrThrow("biomes"), version, Biomes.getRegistry());
        return new StaticWolfVariant(data, wildTexture, tameTexture, angryTexture, biomes);
    }

    static NBT encode(WolfVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("wild_texture", new NBTString(variant.getWildTexture().toString()));
        compound.setTag("tame_texture", new NBTString(variant.getTameTexture().toString()));
        compound.setTag("angry_texture", new NBTString(variant.getAngryTexture().toString()));
        compound.setTag("biomes", MappedEntitySet.encode(variant.getBiomes(), version));
        return compound;
    }
}
