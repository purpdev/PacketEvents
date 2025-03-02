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

package com.github.retrooper.packetevents.protocol.sound;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticSound extends AbstractMappedEntity implements Sound {

    private final ResourceLocation soundId;
    private final @Nullable Float range;

    public StaticSound(ResourceLocation soundId, @Nullable Float range) {
        this(null, soundId, range);
    }

    @ApiStatus.Internal
    public StaticSound(@Nullable TypesBuilderData data, ResourceLocation soundId, @Nullable Float range) {
        super(data);
        this.soundId = soundId;
        this.range = range;
    }

    @Override
    public ResourceLocation getSoundId() {
        return this.soundId;
    }

    @Override
    public @Nullable Float getRange() {
        return this.range;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticSound)) return false;
        StaticSound that = (StaticSound) obj;
        if (this.isRegistered()) {
            return super.equals(obj);
        }
        if (!this.soundId.equals(that.soundId)) return false;
        return Objects.equals(this.range, that.range);
    }

    @Override
    public int hashCode() {
        return this.isRegistered() ? super.hashCode() : Objects.hash(this.soundId, this.range);
    }
}
