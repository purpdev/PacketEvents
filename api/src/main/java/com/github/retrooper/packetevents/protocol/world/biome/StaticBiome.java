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

package com.github.retrooper.packetevents.protocol.world.biome;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticBiome extends AbstractMappedEntity implements Biome {

    private final boolean precipitation;
    private final float temperature;
    private final TemperatureModifier temperatureModifier;
    private final float downfall;
    private final @Nullable Category category;
    private final @Nullable Float depth;
    private final @Nullable Float scale;
    private final BiomeEffects effects;

    public StaticBiome(
            boolean precipitation, float temperature, TemperatureModifier temperatureModifier,
            float downfall, BiomeEffects effects
    ) {
        this(null, precipitation, temperature, temperatureModifier, downfall, effects);
    }

    @ApiStatus.Internal
    @ApiStatus.Obsolete
    public StaticBiome(@Nullable TypesBuilderData data, Precipitation precipitation, float temperature,
                       TemperatureModifier temperatureModifier, float downfall, BiomeEffects effects) {
        this(data, precipitation != Precipitation.NONE, temperature, temperatureModifier, downfall, effects);
    }

    @ApiStatus.Internal
    public StaticBiome(
            @Nullable TypesBuilderData data, boolean precipitation,
            float temperature, TemperatureModifier temperatureModifier,
            float downfall, BiomeEffects effects
    ) {
        this(data, precipitation, temperature, temperatureModifier,
                downfall, null, null, null, effects);
    }

    public StaticBiome(
            boolean precipitation, float temperature, TemperatureModifier temperatureModifier,
            float downfall, @Nullable Category category, @Nullable Float depth,
            @Nullable Float scale, BiomeEffects effects
    ) {
        this(null, precipitation, temperature, temperatureModifier, downfall,
                category, depth, scale, effects);
    }

    @ApiStatus.Internal
    @ApiStatus.Obsolete
    public StaticBiome(@Nullable TypesBuilderData data, Precipitation precipitation, float temperature,
                       TemperatureModifier temperatureModifier, float downfall, @Nullable Category category,
                       @Nullable Float depth, @Nullable Float scale, BiomeEffects effects) {
        this(data, precipitation != Precipitation.NONE, temperature, temperatureModifier,
                downfall, category, depth, scale, effects);
    }

    @ApiStatus.Internal
    public StaticBiome(
            @Nullable TypesBuilderData data, boolean precipitation,
            float temperature, TemperatureModifier temperatureModifier,
            float downfall, @Nullable Category category, @Nullable Float depth,
            @Nullable Float scale, BiomeEffects effects
    ) {
        super(data);
        this.precipitation = precipitation;
        this.temperature = temperature;
        this.temperatureModifier = temperatureModifier;
        this.downfall = downfall;
        this.category = category;
        this.depth = depth;
        this.scale = scale;
        this.effects = effects;
    }

    @Override
    public Biome copy(@Nullable TypesBuilderData newData) {
        return new StaticBiome(newData, this.precipitation, this.temperature, this.temperatureModifier,
                this.downfall, this.category, this.depth, this.scale, this.effects);
    }

    @Override
    public boolean hasPrecipitation() {
        return this.precipitation;
    }

    @Override
    public Precipitation getPrecipitation() {
        if (!this.hasPrecipitation()) {
            return Precipitation.NONE;
        }
        switch (this.getTemperatureModifier()) {
            case NONE:
                return Precipitation.RAIN;
            case FROZEN:
                return Precipitation.SNOW;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public float getTemperature() {
        return this.temperature;
    }

    @Override
    public TemperatureModifier getTemperatureModifier() {
        return this.temperatureModifier;
    }

    @Override
    public float getDownfall() {
        return this.downfall;
    }

    @Override
    public @Nullable Category getCategory() {
        return this.category;
    }

    @Override
    public @Nullable Float getDepth() {
        return this.depth;
    }

    @Override
    public @Nullable Float getScale() {
        return this.scale;
    }

    @Override
    public BiomeEffects getEffects() {
        return this.effects;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticBiome)) return false;
        if (!super.equals(obj)) return false;
        StaticBiome that = (StaticBiome) obj;
        if (this.precipitation != that.precipitation) return false;
        if (Float.compare(that.temperature, this.temperature) != 0) return false;
        if (Float.compare(that.downfall, this.downfall) != 0) return false;
        if (this.temperatureModifier != that.temperatureModifier) return false;
        if (this.category != that.category) return false;
        if (!Objects.equals(this.depth, that.depth)) return false;
        if (!Objects.equals(this.scale, that.scale)) return false;
        return this.effects.equals(that.effects);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.precipitation, this.temperature,
                this.temperatureModifier, this.downfall, this.category, this.depth, this.scale, this.effects);
    }

    @Override
    public String toString() {
        return "StaticBiome{precipitation=" + this.precipitation + ", temperature=" + this.temperature + ", temperatureModifier=" + this.temperatureModifier + ", downfall=" + this.downfall + ", category=" + this.category + ", depth=" + this.depth + ", scale=" + this.scale + ", effects=" + this.effects + '}';
    }
}
