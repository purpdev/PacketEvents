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

package com.github.retrooper.packetevents.protocol.world.states.type;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StateType {

    private final Mapped mapped;

    private final float blastResistance;
    private final float hardness;
    private final boolean isSolid;
    private final boolean isBlocking;
    private final boolean isAir;
    private final boolean requiresCorrectTool;
    private final boolean exceedsCube;
    private final MaterialType materialType;

    @ApiStatus.Internal
    public StateType(
            TypesBuilderData typeData,
            float blastResistance, float hardness, boolean isSolid,
            boolean isBlocking, boolean isAir, boolean requiresCorrectTool,
            boolean isShapeExceedsCube, MaterialType materialType
    ) {
        this.mapped = new Mapped(typeData);
        this.blastResistance = blastResistance;
        this.hardness = hardness;
        this.isSolid = isSolid;
        this.isBlocking = isBlocking;
        this.isAir = isAir;
        this.requiresCorrectTool = requiresCorrectTool;
        this.exceedsCube = isShapeExceedsCube;
        this.materialType = materialType;
    }

    public Mapped getMapped() {
        return this.mapped;
    }

    public WrappedBlockState createBlockState() {
        return WrappedBlockState.getDefaultState(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), this);
    }

    public WrappedBlockState createBlockState(ClientVersion version) {
        return WrappedBlockState.getDefaultState(version, this);
    }

    public String getName() {
        return this.mapped.getName().getKey();
    }

    public float getBlastResistance() {
        return blastResistance;
    }

    public float getHardness() {
        return hardness;
    }

    public boolean isSolid() {
        return isSolid;
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public boolean isAir() {
        return isAir;
    }

    public boolean isRequiresCorrectTool() {
        return requiresCorrectTool;
    }

    public boolean isReplaceable() {
        switch (getMaterialType()) {
            case AIR:
            case STRUCTURAL_AIR:
            case REPLACEABLE_PLANT:
            case REPLACEABLE_FIREPROOF_PLANT:
            case REPLACEABLE_WATER_PLANT:
            case WATER:
            case BUBBLE_COLUMN:
            case LAVA:
            case TOP_SNOW:
            case FIRE:
                return true;
            default:
                return false;
        }
    }

    public boolean exceedsCube() {
        return exceedsCube;
    }

    public MaterialType getMaterialType() {
        return materialType;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StateType stateType = (StateType) o;
        return Float.compare(blastResistance, stateType.blastResistance) == 0
                && Float.compare(hardness, stateType.hardness) == 0
                && isSolid == stateType.isSolid
                && isBlocking == stateType.isBlocking
                && isAir == stateType.isAir
                && requiresCorrectTool == stateType.requiresCorrectTool
                && exceedsCube == stateType.exceedsCube
                && Objects.equals(getName(), stateType.getName())
                && materialType == stateType.materialType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), blastResistance, hardness, isSolid, isBlocking, isAir, requiresCorrectTool, exceedsCube, materialType);
    }

    public final class Mapped extends AbstractMappedEntity {

        @ApiStatus.Internal
        public Mapped(@Nullable TypesBuilderData data) {
            super(data);
        }

        public StateType getStateType() {
            return StateType.this;
        }
    }
}
