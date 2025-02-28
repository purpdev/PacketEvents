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

package com.github.retrooper.packetevents.protocol.mapper;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

@Deprecated
public class GenericMappedEntity implements StaticMappedEntity {

    private final int id;

    public GenericMappedEntity(ClientVersion ignoredVersion, int id) {
        this(id);
    }

    public GenericMappedEntity(int id) {
        this.id = id;
    }

    public static GenericMappedEntity getById(ClientVersion version, int id) {
        return new GenericMappedEntity(version, id);
    }

    public static GenericMappedEntity read(PacketWrapper<?> wrapper) {
        return wrapper.readMappedEntity(GenericMappedEntity::new);
    }

    public static void write(PacketWrapper<?> wrapper, GenericMappedEntity entity) {
        wrapper.writeMappedEntity(entity);
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public ResourceLocation getName() {
        throw new UnsupportedOperationException();
    }
}
