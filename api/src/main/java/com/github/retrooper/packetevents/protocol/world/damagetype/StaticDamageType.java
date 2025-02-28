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

package com.github.retrooper.packetevents.protocol.world.damagetype;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticDamageType extends AbstractMappedEntity implements DamageType {

    private final String messageId;
    private final DamageScaling scaling;
    private final float exhaustion;
    private final DamageEffects effects;
    private final DeathMessageType deathMessageType;

    @ApiStatus.Internal
    public StaticDamageType(
            @Nullable TypesBuilderData data, String messageId, DamageScaling scaling,
            float exhaustion, DamageEffects effects, DeathMessageType deathMessageType
    ) {
        super(data);
        this.messageId = messageId;
        this.scaling = scaling;
        this.exhaustion = exhaustion;
        this.effects = effects;
        this.deathMessageType = deathMessageType;
    }

    @Override
    public DamageType copy(@Nullable TypesBuilderData newData) {
        return new StaticDamageType(newData, this.messageId, this.scaling, this.exhaustion, this.effects,
                this.deathMessageType);
    }

    @Override
    public String getMessageId() {
        return this.messageId;
    }

    @Override
    public DamageScaling getScaling() {
        return this.scaling;
    }

    @Override
    public float getExhaustion() {
        return this.exhaustion;
    }

    @Override
    public DamageEffects getEffects() {
        return this.effects;
    }

    @Override
    public DeathMessageType getDeathMessageType() {
        return this.deathMessageType;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticDamageType)) return false;
        if (!super.equals(obj)) return false;
        StaticDamageType that = (StaticDamageType) obj;
        if (Float.compare(that.exhaustion, this.exhaustion) != 0) return false;
        if (!this.messageId.equals(that.messageId)) return false;
        if (this.scaling != that.scaling) return false;
        if (this.effects != that.effects) return false;
        return this.deathMessageType == that.deathMessageType;
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.messageId, this.scaling, this.exhaustion, this.effects, this.deathMessageType);
    }

    @Override
    public String toString() {
        return "StaticDamageType{messageId='" + this.messageId + '\'' + ", scaling=" + this.scaling + ", exhaustion=" + this.exhaustion + ", effects=" + this.effects + ", deathMessageType=" + this.deathMessageType + '}';
    }
}
