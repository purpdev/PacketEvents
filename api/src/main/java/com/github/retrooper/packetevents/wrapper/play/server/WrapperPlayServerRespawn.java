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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.protocol.world.WorldType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypeRef;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerRespawn extends PacketWrapper<WrapperPlayServerRespawn> {

    public static final byte KEEP_NOTHING = 0;
    public static final byte KEEP_ATTRIBUTES = 0b01;
    public static final byte KEEP_ENTITY_DATA = 0b10;
    public static final byte KEEP_ALL_DATA = KEEP_ATTRIBUTES | KEEP_ENTITY_DATA;

    // used for backwards compatibility in constructors
    static final int FALLBACK_SEA_LEVEL = 62;

    private DimensionTypeRef dimensionTypeRef;
    private Optional<String> worldName;
    private Difficulty difficulty;
    private long hashedSeed;
    private GameMode gameMode;
    private @Nullable GameMode previousGameMode;
    private boolean worldDebug;
    private boolean worldFlat;
    private byte keptData;
    private WorldBlockPosition lastDeathPosition;
    private Integer portalCooldown;
    private int seaLevel;

    //This should not be accessed
    private String levelType;

    public WrapperPlayServerRespawn(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerRespawn(
            Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData,
            @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(dimension.asDimensionTypeRef(), worldName, difficulty, hashedSeed, gameMode, previousGameMode,
                worldDebug, worldFlat, keepingAllPlayerData, deathDimensionName, lastDeathPosition, portalCooldown);
    }

    @Deprecated
    public WrapperPlayServerRespawn(
            Dimension dimension, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
            @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown
    ) {
        this(dimension.asDimensionTypeRef(), worldName, difficulty, hashedSeed, gameMode, previousGameMode,
                worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown);
    }

    public WrapperPlayServerRespawn(
            DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData,
            @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this((DimensionTypeRef) null, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat,
                keepingAllPlayerData, deathDimensionName, lastDeathPosition, portalCooldown);
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    public WrapperPlayServerRespawn(
            DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
            @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown
    ) {
        this(dimensionType, worldName, difficulty, hashedSeed, gameMode, previousGameMode,
                worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, FALLBACK_SEA_LEVEL);
    }

    public WrapperPlayServerRespawn(
            DimensionType dimensionType, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
            @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown, int seaLevel
    ) {
        this((DimensionTypeRef) null, worldName, difficulty, hashedSeed, gameMode, previousGameMode,
                worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, seaLevel);
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    public WrapperPlayServerRespawn(
            DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, boolean keepingAllPlayerData,
            @Nullable ResourceLocation deathDimensionName, @Nullable WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(dimensionTypeRef, worldName, difficulty, hashedSeed, gameMode, previousGameMode, worldDebug, worldFlat,
                keepingAllPlayerData ? KEEP_ALL_DATA : KEEP_NOTHING, lastDeathPosition, portalCooldown);
    }

    public WrapperPlayServerRespawn(
            DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
            @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown
    ) {
        this(dimensionTypeRef, worldName, difficulty, hashedSeed, gameMode, previousGameMode,
                worldDebug, worldFlat, keptData, lastDeathPosition, portalCooldown, FALLBACK_SEA_LEVEL);
    }

    public WrapperPlayServerRespawn(
            DimensionTypeRef dimensionTypeRef, @Nullable String worldName, Difficulty difficulty, long hashedSeed, GameMode gameMode,
            @Nullable GameMode previousGameMode, boolean worldDebug, boolean worldFlat, byte keptData,
            @Nullable WorldBlockPosition lastDeathPosition, @Nullable Integer portalCooldown, int seaLevel
    ) {
        super(PacketType.Play.Server.RESPAWN);
        this.dimensionTypeRef = dimensionTypeRef;
        setWorldName(worldName);
        this.difficulty = difficulty;
        this.hashedSeed = hashedSeed;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldDebug = worldDebug;
        this.worldFlat = worldFlat;
        this.keptData = keptData;
        this.lastDeathPosition = lastDeathPosition;
        this.portalCooldown = portalCooldown;
        this.seaLevel = seaLevel;
    }

    @Override
    public void read() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        boolean v1_19_3 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3);
        boolean v1_20_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);

        this.dimensionTypeRef = DimensionTypeRef.read(this);
        if (v1_16_0) {
            worldName = Optional.of(readString());
            hashedSeed = readLong();
            if (v1_20_2) {
                gameMode = readGameMode();
            } else {
                gameMode = GameMode.getById(readUnsignedByte());
            }
            previousGameMode = readGameMode();
            worldDebug = readBoolean();
            worldFlat = readBoolean();
            if (v1_19_3) {
                if (!v1_20_2) {
                    keptData = readByte();
                }
            } else {
                keptData = readBoolean() ? KEEP_ALL_DATA : KEEP_ENTITY_DATA;
            }
            if (v1_19) {
                lastDeathPosition = readOptional(PacketWrapper::readWorldBlockPosition);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
                portalCooldown = readVarInt();
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                this.seaLevel = this.readVarInt();
            }
            if (v1_20_2) {
                keptData = readByte();
            }
        } else {
            worldName = Optional.empty();
            hashedSeed = 0L;
            if (v1_15_0) {
                hashedSeed = readLong();
            } else if (!v1_14) {
                difficulty = Difficulty.getById(readByte());
            }

            //Note: SPECTATOR will not be expected from a 1.7 client.
            gameMode = GameMode.getById(readByte());
            levelType = readString(16);
            worldFlat = com.github.retrooper.packetevents.protocol.world.DimensionType.isFlat(levelType);
            worldDebug = com.github.retrooper.packetevents.protocol.world.DimensionType.isDebug(levelType);
        }
    }

    @Override
    public void write() {
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_16_0 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        boolean v1_19_3 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3);
        boolean v1_20_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);

        DimensionTypeRef.write(this, this.dimensionTypeRef);
        if (v1_16_0) {
            writeString(worldName.orElse(""));
            writeLong(hashedSeed);
            writeGameMode(gameMode);
            writeGameMode(previousGameMode);
            writeBoolean(worldDebug);
            writeBoolean(worldFlat);
            if (v1_19_3) {
                if (!v1_20_2) {
                    writeByte(keptData);
                }
            } else {
                writeBoolean((keptData & KEEP_ATTRIBUTES) != 0);
            }
            if (v1_19) {
                writeOptional(lastDeathPosition, PacketWrapper::writeWorldBlockPosition);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
                int pCooldown = portalCooldown != null ? portalCooldown : 0;
                writeVarInt(pCooldown);
            }
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                this.writeVarInt(this.seaLevel);
            }
            if (v1_20_2) {
                writeByte(keptData);
            }
        } else {
            if (v1_15_0) {
                writeLong(hashedSeed);
            } else if (!v1_14) {
                //Handle 1.13.2 and below
                int id = difficulty == null ? Difficulty.NORMAL.getId() : difficulty.getId();
                writeByte(id);
            }

            //Note: SPECTATOR will not be expected from a 1.7 client.
            writeByte(gameMode.ordinal());

            if (worldFlat) {
                writeString(WorldType.FLAT.getName());
            } else if (worldDebug) {
                writeString(WorldType.DEBUG_ALL_BLOCK_STATES.getName());
            } else {
                writeString(levelType == null ? WorldType.DEFAULT.getName() : levelType, 16);
            }
        }
    }

    @Override
    public void copy(WrapperPlayServerRespawn wrapper) {
        dimensionTypeRef = wrapper.dimensionTypeRef;
        worldName = wrapper.worldName;
        difficulty = wrapper.difficulty;
        hashedSeed = wrapper.hashedSeed;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        worldDebug = wrapper.worldDebug;
        worldFlat = wrapper.worldFlat;
        keptData = wrapper.keptData;
        lastDeathPosition = wrapper.lastDeathPosition;
        portalCooldown = wrapper.portalCooldown;
        seaLevel = wrapper.seaLevel;
        levelType = wrapper.levelType;
    }

    public DimensionTypeRef getDimensionTypeRef() {
        return this.dimensionTypeRef;
    }

    public void setDimensionTypeRef(DimensionTypeRef dimensionTypeRef) {
        this.dimensionTypeRef = dimensionTypeRef;
    }

    public DimensionType getDimensionType() {
        IRegistry<DimensionType> registry = this.getRegistryHolder().getRegistryOr(
                DimensionTypes.getRegistry(), this.serverVersion.toClientVersion());
        return this.dimensionTypeRef.resolve(registry, this.serverVersion.toClientVersion());
    }

    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    @Deprecated
    public Dimension getDimension() {
        return Dimension.fromDimensionTypeRef(this.dimensionTypeRef);
    }

    @Deprecated
    public void setDimension(Dimension dimension) {
        this.dimensionTypeRef = dimension.asDimensionTypeRef();
    }

    public Optional<String> getWorldName() {
        return worldName;
    }

    public void setWorldName(@Nullable String worldName) {
        this.worldName = Optional.ofNullable(worldName);
    }

    public @Nullable Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    @Nullable
    public GameMode getPreviousGameMode() {
        return previousGameMode;
    }

    public void setPreviousGameMode(@Nullable GameMode previousGameMode) {
        this.previousGameMode = previousGameMode;
    }

    public boolean isWorldDebug() {
        return worldDebug;
    }

    public void setWorldDebug(boolean worldDebug) {
        this.worldDebug = worldDebug;
    }

    public boolean isWorldFlat() {
        return worldFlat;
    }

    public void setWorldFlat(boolean worldFlat) {
        this.worldFlat = worldFlat;
    }

    public boolean isKeepingAllPlayerData() {
        return (keptData & KEEP_ATTRIBUTES) != 0;
    }

    public void setKeepingAllPlayerData(boolean keepAllPlayerData) {
        this.keptData = keepAllPlayerData ? KEEP_ALL_DATA : KEEP_ENTITY_DATA;
    }

    public byte getKeptData() {
        return keptData;
    }

    public void setKeptData(byte keptData) {
        this.keptData = keptData;
    }

    public @Nullable WorldBlockPosition getLastDeathPosition() {
        return lastDeathPosition;
    }

    public void setLastDeathPosition(@Nullable WorldBlockPosition lastDeathPosition) {
        this.lastDeathPosition = lastDeathPosition;
    }

    public Optional<Integer> getPortalCooldown() {
        return Optional.ofNullable(portalCooldown);
    }

    public void setPortalCooldown(int portalCooldown) {
        this.portalCooldown = portalCooldown;
    }
}
