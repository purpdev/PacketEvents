/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.entity.type;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class StaticEntityType extends AbstractMappedEntity implements EntityType {

    private final Optional<EntityType> parent;
    private final Map<EntityType, Boolean> parents;

    private @Nullable TypesBuilderData legacyData;

    @ApiStatus.Internal
    public StaticEntityType(@Nullable TypesBuilderData data, @Nullable EntityType parent) {
        super(data);
        this.parent = Optional.ofNullable(parent);

        // iterate through all parents and save them for faster access
        this.parents = new IdentityHashMap<>();
        this.parents.put(this, true);
        while (parent != null) {
            this.parents.put(parent, true);
            parent = parent.getParent().orElse(null);
        }
    }

    StaticEntityType setLegacyData(@Nullable TypesBuilderData legacyData) {
        this.legacyData = legacyData;
        return this;
    }

    @Override
    public boolean isInstanceOf(EntityType parent) {
        return parent != null && this.parents.containsKey(parent);
    }

    @Override
    public Optional<EntityType> getParent() {
        return this.parent;
    }

    @Override
    public int getLegacyId(ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
            return -1;
        } else if (this.legacyData != null) {
            return this.legacyData.getId(version);
        }
        throw new UnsupportedOperationException();
    }
}
