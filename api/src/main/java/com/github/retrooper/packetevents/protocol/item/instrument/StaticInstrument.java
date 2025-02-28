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

package com.github.retrooper.packetevents.protocol.item.instrument;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticInstrument extends AbstractMappedEntity implements Instrument {

    private final Sound sound;
    private final float useSeconds;
    private final float range;
    private final Component description;

    @Deprecated // useDuration changed from ticks to seconds in 1.21.2
    public StaticInstrument(Sound sound, int useDuration, float range) {
        this(sound, useDuration * 20f, range, Component.empty());
    }

    public StaticInstrument(Sound sound, float useSeconds, float range, Component description) {
        this(null, sound, useSeconds, range, description);
    }

    @ApiStatus.Internal
    public StaticInstrument(@Nullable TypesBuilderData data, Sound sound, float useSeconds, float range, Component description) {
        super(data);
        this.sound = sound;
        this.useSeconds = useSeconds;
        this.range = range;
        this.description = description;
    }

    @Override
    public Instrument copy(@Nullable TypesBuilderData newData) {
        return new StaticInstrument(newData, this.sound, this.useSeconds, this.range, this.description);
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Override
    public float getUseSeconds() {
        return this.useSeconds;
    }

    @Override
    public float getRange() {
        return this.range;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticInstrument)) return false;
        if (!super.equals(obj)) return false;
        StaticInstrument that = (StaticInstrument) obj;
        if (this.useSeconds != that.useSeconds) return false;
        if (Float.compare(that.range, this.range) != 0) return false;
        if (!this.sound.equals(that.sound)) return false;
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.sound, this.useSeconds, this.range, this.description);
    }
}
