/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2022 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypeRef;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import org.jetbrains.annotations.Nullable;

/**
 * @deprecated use {@link com.github.retrooper.packetevents.protocol.world.dimension.DimensionType instead}
 */
@Deprecated
public class Dimension {
    private int id;
    private NBTCompound attributes;

    @Deprecated
    public Dimension(DimensionType type) {
        this.id = type.getId();
        this.attributes = new NBTCompound();
    }

    public Dimension(int id) {
        this.id = id;
        this.attributes = new NBTCompound();
    }

    public Dimension(NBTCompound attributes) {
        this.attributes = attributes;
    }

    @Deprecated
    public Dimension(int id, NBTCompound attributes) {
        this.id = id;
        this.attributes = attributes;
    }

    public static Dimension fromDimensionTypeRef(DimensionTypeRef ref) {
        if (ref instanceof DimensionTypeRef.NameRef) {
            Dimension dimension = new Dimension(0);
            dimension.setDimensionName(ref.getName().toString());
            return dimension;
        } else if (ref instanceof DimensionTypeRef.IdRef) {
            return new Dimension(ref.getId());
        } else if (ref instanceof DimensionTypeRef.DataRef) {
            return new Dimension((NBTCompound) ref.getData());
        } else if (ref instanceof DimensionTypeRef.DirectRef) {
            return fromDimensionType(((DimensionTypeRef.DirectRef) ref).getDimensionType(),
                    null, ((DimensionTypeRef.DirectRef) ref).getVersion());
        } else {
            throw new UnsupportedOperationException("Unsupported DimensionTypeRef implementation: " + ref);
        }
    }

    public static Dimension fromDimensionType(
            com.github.retrooper.packetevents.protocol.world.dimension.DimensionType dimensionType,
            @Nullable User user, @Nullable ClientVersion version
    ) {
        if (version == null) {
            version = user != null && PacketEvents.getAPI().getInjector().isProxy() ? user.getClientVersion() :
                    PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        }
        NBTCompound encodedType = (NBTCompound) com.github.retrooper.packetevents.protocol.world.dimension.DimensionType.encode(dimensionType, version);
        return new Dimension(dimensionType.getId(version), encodedType);
    }

    public DimensionTypeRef asDimensionTypeRef() {
        if (this.attributes == null) {
            return new DimensionTypeRef.IdRef(this.id);
        } else if (this.attributes.size() > 1) {
            // seems to contain more than just "effects"
            return new DimensionTypeRef.DataRef(this.attributes);
        } else {
            ResourceLocation dimensionName = new ResourceLocation(this.getDimensionName());
            return new DimensionTypeRef.NameRef(dimensionName);
        }
    }

    public com.github.retrooper.packetevents.protocol.world.dimension.DimensionType asDimensionType(
            @Nullable User user, @Nullable ClientVersion version) {
        if (version == null) {
            version = user != null && PacketEvents.getAPI().getInjector().isProxy() ? user.getClientVersion() :
                    PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        }
        IRegistry<com.github.retrooper.packetevents.protocol.world.dimension.DimensionType> registry = user != null
                ? user.getRegistryOr(DimensionTypes.getRegistry(), version) : DimensionTypes.getRegistry();
        String dimName = this.getDimensionName();
        if (!dimName.isEmpty()) {
            return registry.getByName(new ResourceLocation(dimName));
        }
        return registry.getById(version, this.id);
    }

    public String getDimensionName() {
        return getAttributes().getStringTagValueOrDefault("effects", "");
    }

    public void setDimensionName(String name) {
        NBTCompound compound = getAttributes();
        compound.setTag("effects", new NBTString(name));
        setAttributes(compound);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Deprecated
    public DimensionType getType() {
        return DimensionType.getById(id);
    }

    @Deprecated
    public void setType(DimensionType type) {
        this.id = type.getId();
    }

    public NBTCompound getAttributes() {
        return attributes;
    }

    public void setAttributes(NBTCompound attributes) {
        this.attributes = attributes;
    }
}
