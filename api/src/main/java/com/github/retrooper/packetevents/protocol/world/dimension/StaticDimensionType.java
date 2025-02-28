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

package com.github.retrooper.packetevents.protocol.world.dimension;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.OptionalLong;

public class StaticDimensionType extends AbstractMappedEntity implements DimensionType {

    private final OptionalLong fixedTime;
    private final boolean hasSkyLight;
    private final boolean hasCeiling;
    private final boolean ultraWarm;
    private final boolean natural;
    private final double coordinateScale;
    private final boolean bedWorking;
    private final boolean respawnAnchorWorking;
    private final int minY;
    private final int height;
    private final int logicalHeight;
    private final String infiniburnTag;
    private final @Nullable ResourceLocation effectsLocation;
    private final float ambientLight;
    private final boolean piglinSafe;
    private final boolean hasRaids;
    private final @Nullable NBT monsterSpawnLightLevel;
    private final int monsterSpawnBlockLightLimit;

    public StaticDimensionType(
            OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, @Nullable NBT monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        this(null, fixedTime, hasSkyLight, hasCeiling, ultraWarm, natural, coordinateScale, bedWorking, respawnAnchorWorking,
                minY, height, logicalHeight, infiniburnTag, effectsLocation, ambientLight, piglinSafe, hasRaids,
                monsterSpawnLightLevel, monsterSpawnBlockLightLimit);
    }

    @ApiStatus.Internal
    public StaticDimensionType(
            @Nullable TypesBuilderData data, OptionalLong fixedTime, boolean hasSkyLight, boolean hasCeiling,
            boolean ultraWarm, boolean natural, double coordinateScale, boolean bedWorking, boolean respawnAnchorWorking,
            int minY, int height, int logicalHeight, String infiniburnTag, @Nullable ResourceLocation effectsLocation,
            float ambientLight, boolean piglinSafe, boolean hasRaids, @Nullable NBT monsterSpawnLightLevel,
            int monsterSpawnBlockLightLimit
    ) {
        super(data);
        this.fixedTime = fixedTime;
        this.hasSkyLight = hasSkyLight;
        this.hasCeiling = hasCeiling;
        this.ultraWarm = ultraWarm;
        this.natural = natural;
        this.coordinateScale = coordinateScale;
        this.bedWorking = bedWorking;
        this.respawnAnchorWorking = respawnAnchorWorking;
        this.minY = minY;
        this.height = height;
        this.logicalHeight = logicalHeight;
        this.infiniburnTag = infiniburnTag;
        this.effectsLocation = effectsLocation;
        this.ambientLight = ambientLight;
        this.piglinSafe = piglinSafe;
        this.hasRaids = hasRaids;
        this.monsterSpawnLightLevel = monsterSpawnLightLevel;
        this.monsterSpawnBlockLightLimit = monsterSpawnBlockLightLimit;
    }

    @Override
    public DimensionType copy(@Nullable TypesBuilderData newData) {
        return new StaticDimensionType(newData, this.fixedTime, this.hasSkyLight, this.hasCeiling, this.ultraWarm,
                this.natural, this.coordinateScale, this.bedWorking, this.respawnAnchorWorking, this.minY, this.height,
                this.logicalHeight, this.infiniburnTag, this.effectsLocation, this.ambientLight, this.piglinSafe,
                this.hasRaids, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit);
    }

    @Override
    public OptionalLong getFixedTime() {
        return this.fixedTime;
    }

    @Override
    public boolean hasCeiling() {
        return this.hasCeiling;
    }

    @Override
    public boolean hasSkyLight() {
        return this.hasSkyLight;
    }

    @Override
    public boolean isUltraWarm() {
        return this.ultraWarm;
    }

    @Override
    public boolean isNatural() {
        return this.natural;
    }

    @Override
    public double getCoordinateScale() {
        return this.coordinateScale;
    }

    @Override
    public boolean isBedWorking() {
        return this.bedWorking;
    }

    @Override
    public boolean isRespawnAnchorWorking() {
        return this.respawnAnchorWorking;
    }

    @Override
    public int getMinY(ClientVersion version) {
        return this.minY;
    }

    @Override
    public int getHeight(ClientVersion version) {
        return this.height;
    }

    @Override
    public int getLogicalHeight(ClientVersion version) {
        return this.logicalHeight;
    }

    @Override
    public String getInfiniburnTag() {
        return this.infiniburnTag;
    }

    @Override
    public @Nullable ResourceLocation getEffectsLocation() {
        return this.effectsLocation;
    }

    @Override
    public float getAmbientLight() {
        return this.ambientLight;
    }

    @Override
    public boolean isPiglinSafe() {
        return this.piglinSafe;
    }

    @Override
    public boolean hasRaids() {
        return this.hasRaids;
    }

    @Override
    public @Nullable NBT getMonsterSpawnLightLevel() {
        return this.monsterSpawnLightLevel;
    }

    @Override
    public int getMonsterSpawnBlockLightLimit() {
        return this.monsterSpawnBlockLightLimit;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticDimensionType)) return false;
        if (!super.equals(obj)) return false;
        StaticDimensionType that = (StaticDimensionType) obj;
        if (this.hasSkyLight != that.hasSkyLight) return false;
        if (this.hasCeiling != that.hasCeiling) return false;
        if (this.ultraWarm != that.ultraWarm) return false;
        if (this.natural != that.natural) return false;
        if (Double.compare(that.coordinateScale, this.coordinateScale) != 0) return false;
        if (this.bedWorking != that.bedWorking) return false;
        if (this.respawnAnchorWorking != that.respawnAnchorWorking) return false;
        if (this.minY != that.minY) return false;
        if (this.height != that.height) return false;
        if (this.logicalHeight != that.logicalHeight) return false;
        if (Float.compare(that.ambientLight, this.ambientLight) != 0) return false;
        if (this.piglinSafe != that.piglinSafe) return false;
        if (this.hasRaids != that.hasRaids) return false;
        if (this.monsterSpawnBlockLightLimit != that.monsterSpawnBlockLightLimit) return false;
        if (!this.fixedTime.equals(that.fixedTime)) return false;
        if (!this.infiniburnTag.equals(that.infiniburnTag)) return false;
        if (!Objects.equals(this.effectsLocation, that.effectsLocation)) return false;
        return Objects.equals(this.monsterSpawnLightLevel, that.monsterSpawnLightLevel);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.fixedTime, this.hasSkyLight, this.hasCeiling, this.ultraWarm, this.natural, this.coordinateScale, this.bedWorking, this.respawnAnchorWorking, this.minY, this.height, this.logicalHeight, this.infiniburnTag, this.effectsLocation, this.ambientLight, this.piglinSafe, this.hasRaids, this.monsterSpawnLightLevel, this.monsterSpawnBlockLightLimit);
    }

    @Override
    public String toString() {
        return "StaticDimensionType{fixedTime=" + this.fixedTime + ", hasSkyLight=" + this.hasSkyLight + ", hasCeiling=" + this.hasCeiling + ", ultraWarm=" + this.ultraWarm + ", natural=" + this.natural + ", coordinateScale=" + this.coordinateScale + ", bedWorking=" + this.bedWorking + ", respawnAnchorWorking=" + this.respawnAnchorWorking + ", minY=" + this.minY + ", height=" + this.height + ", logicalHeight=" + this.logicalHeight + ", infiniburnTag='" + this.infiniburnTag + '\'' + ", effectsLocation=" + this.effectsLocation + ", ambientLight=" + this.ambientLight + ", piglinSafe=" + this.piglinSafe + ", hasRaids=" + this.hasRaids + ", monsterSpawnLightLevel=" + this.monsterSpawnLightLevel + ", monsterSpawnBlockLightLimit=" + this.monsterSpawnBlockLightLimit + '}';
    }
}
