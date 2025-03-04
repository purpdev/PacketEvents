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

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.mapper.CopyableEntity;
import com.github.retrooper.packetevents.protocol.mapper.DeepComparableEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface PaintingVariant extends MappedEntity, CopyableEntity<PaintingVariant>, DeepComparableEntity {

    int getWidth();

    int getHeight();

    ResourceLocation getAssetId();

    @Nullable Component getTitle();

    @Nullable Component getAuthor();

    static PaintingVariant read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntityOrDirect(PaintingVariants.getRegistry(), PaintingVariant::readDirect);
    }

    static void write(PacketWrapper<?> wrapper, PaintingVariant variant) {
        wrapper.writeMappedEntityOrDirect(variant, PaintingVariant::writeDirect);
    }

    static PaintingVariant readDirect(PacketWrapper<?> wrapper) {
        int width = wrapper.readVarInt();
        int height = wrapper.readVarInt();
        ResourceLocation assetId = wrapper.readIdentifier();
        Component title = null, author = null;
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            title = wrapper.readOptional(PacketWrapper::readComponent);
            author = wrapper.readOptional(PacketWrapper::readComponent);
        }
        return new StaticPaintingVariant(width, height, assetId, title, author);
    }

    static void writeDirect(PacketWrapper<?> wrapper, PaintingVariant variant) {
        wrapper.writeVarInt(variant.getWidth());
        wrapper.writeVarInt(variant.getHeight());
        wrapper.writeIdentifier(variant.getAssetId());
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            wrapper.writeOptional(variant.getTitle(), PacketWrapper::writeComponent);
            wrapper.writeOptional(variant.getAuthor(), PacketWrapper::writeComponent);
        }
    }

    static PaintingVariant decode(NBT nbt, ClientVersion version, @Nullable TypesBuilderData data) {
        NBTCompound compound = (NBTCompound) nbt;
        int width = compound.getNumberTagOrThrow("width").getAsInt();
        int height = compound.getNumberTagOrThrow("height").getAsInt();
        ResourceLocation assetId = new ResourceLocation(compound.getStringTagValueOrThrow("asset_id"));
        Component title = null, author = null;
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            NBT titleTag = compound.getTagOrNull("title");
            if (titleTag != null) {
                title = AdventureSerializer.fromNbt(titleTag);
            }
            NBT authorTag = compound.getTagOrNull("author");
            if (authorTag != null) {
                author = AdventureSerializer.fromNbt(authorTag);
            }
        }
        return new StaticPaintingVariant(data, width, height, assetId, title, author);
    }

    static NBT encode(PaintingVariant variant, ClientVersion version) {
        NBTCompound compound = new NBTCompound();
        compound.setTag("width", new NBTInt(variant.getWidth()));
        compound.setTag("height", new NBTInt(variant.getHeight()));
        compound.setTag("asset_id", new NBTString(variant.getAssetId().toString()));
        if (version.isNewerThanOrEquals(ClientVersion.V_1_21_2)) {
            if (variant.getTitle() != null) {
                compound.setTag("title", AdventureSerializer.toNbt(variant.getTitle()));
            }
            if (variant.getAuthor() != null) {
                compound.setTag("author", AdventureSerializer.toNbt(variant.getAuthor()));
            }
        }
        return compound;
    }
}
