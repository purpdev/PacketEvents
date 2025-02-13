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
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.world.Difficulty;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.protocol.world.WorldType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypeRef;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn.FALLBACK_SEA_LEVEL;

public class WrapperPlayServerJoinGame extends PacketWrapper<WrapperPlayServerJoinGame> {
    private int entityID;
    private boolean hardcore;
    private GameMode gameMode;

    @Nullable
    private GameMode previousGameMode;

    private List<String> worldNames;
    private NBTCompound dimensionCodec;
    private DimensionTypeRef dimensionTypeRef;
    private Difficulty difficulty;
    private String worldName;
    private long hashedSeed;
    private int maxPlayers;
    private int viewDistance;
    private int simulationDistance;
    private boolean reducedDebugInfo;
    private boolean enableRespawnScreen;
    private boolean limitedCrafting;
    private boolean isDebug;
    private boolean isFlat;
    private WorldBlockPosition lastDeathPosition;
    private Integer portalCooldown;
    private int seaLevel;
    private boolean enforcesSecureChat;

    public WrapperPlayServerJoinGame(PacketSendEvent event) {
        super(event);
    }

    @Deprecated
    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, Dimension dimension,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimension.asDimensionTypeRef(),
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo, enableRespawnScreen,
                isDebug, isFlat, lastDeathPosition, portalCooldown);
    }

    @Deprecated
    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, Dimension dimension,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimension.asDimensionTypeRef(), difficulty,
                worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown);
    }

    @Deprecated
    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, Dimension dimension,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown, boolean enforcesSecureChat
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec,
                dimension.asDimensionTypeRef(), difficulty, worldName, hashedSeed, maxPlayers, viewDistance,
                simulationDistance, reducedDebugInfo, enableRespawnScreen, limitedCrafting,
                isDebug, isFlat, lastDeathPosition, portalCooldown, enforcesSecureChat);
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionType dimensionType,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, (DimensionTypeRef) null,
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo, enableRespawnScreen,
                isDebug, isFlat, lastDeathPosition, portalCooldown);
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionType dimensionType,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, (DimensionTypeRef) null, difficulty,
                worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown);
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionType dimensionType,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown, boolean enforcesSecureChat
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimensionType,
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown, FALLBACK_SEA_LEVEL, enforcesSecureChat);
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionType dimensionType,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown, int seaLevel, boolean enforcesSecureChat
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, (DimensionTypeRef) null,
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown, seaLevel, enforcesSecureChat);
        this.dimensionTypeRef = dimensionType.asRef(this.serverVersion.toClientVersion());
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionTypeRef dimensionTypeRef,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimensionTypeRef,
                difficulty, worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo, enableRespawnScreen,
                false, isDebug, isFlat, lastDeathPosition, portalCooldown);
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionTypeRef dimensionTypeRef,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimensionTypeRef, difficulty,
                worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown, false);
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionTypeRef dimensionTypeRef,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown, boolean enforcesSecureChat
    ) {
        this(entityID, hardcore, gameMode, previousGameMode, worldNames, dimensionCodec, dimensionTypeRef, difficulty,
                worldName, hashedSeed, maxPlayers, viewDistance, simulationDistance, reducedDebugInfo,
                enableRespawnScreen, limitedCrafting, isDebug, isFlat, lastDeathPosition,
                portalCooldown, FALLBACK_SEA_LEVEL, enforcesSecureChat);
    }

    public WrapperPlayServerJoinGame(
            int entityID, boolean hardcore, GameMode gameMode,
            @Nullable GameMode previousGameMode, List<String> worldNames,
            NBTCompound dimensionCodec, DimensionTypeRef dimensionTypeRef,
            Difficulty difficulty, String worldName, long hashedSeed,
            int maxPlayers, int viewDistance, int simulationDistance,
            boolean reducedDebugInfo, boolean enableRespawnScreen, boolean limitedCrafting,
            boolean isDebug, boolean isFlat, WorldBlockPosition lastDeathPosition,
            @Nullable Integer portalCooldown, int seaLevel, boolean enforcesSecureChat
    ) {
        super(PacketType.Play.Server.JOIN_GAME);
        this.entityID = entityID;
        this.hardcore = hardcore;
        this.gameMode = gameMode;
        this.previousGameMode = previousGameMode;
        this.worldNames = worldNames;
        this.dimensionCodec = dimensionCodec;
        this.dimensionTypeRef = dimensionTypeRef;
        this.difficulty = difficulty;
        this.worldName = worldName;
        this.hashedSeed = hashedSeed;
        this.maxPlayers = maxPlayers;
        this.viewDistance = viewDistance;
        this.simulationDistance = simulationDistance;
        this.reducedDebugInfo = reducedDebugInfo;
        this.enableRespawnScreen = enableRespawnScreen;
        this.limitedCrafting = limitedCrafting;
        this.isDebug = isDebug;
        this.isFlat = isFlat;
        this.lastDeathPosition = lastDeathPosition;
        this.portalCooldown = portalCooldown;
        this.seaLevel = seaLevel;
        this.enforcesSecureChat = enforcesSecureChat;
    }

    @Override
    public void read() {
        entityID = readInt();
        boolean v1_20_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        boolean v1_18 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
        boolean v1_16_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        boolean v1_16 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_15 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        if (v1_16_2) {
            hardcore = readBoolean();
            if (!v1_20_2) {
                gameMode = readGameMode();
            }
        } else {
            int gameModeId = readUnsignedByte();
            hardcore = (gameModeId & 0x8) == 0x8;
            gameMode = GameMode.getById(gameModeId & ~0x08);
        }
        if (v1_16) {
            if (!v1_20_2) {
                previousGameMode = readGameMode();
            }
            int worldCount = readVarInt();
            worldNames = new ArrayList<>(worldCount);
            for (int i = 0; i < worldCount; i++) {
                worldNames.add(readString());
            }
            if (!v1_20_2) {
                dimensionCodec = readNBT();
                this.dimensionTypeRef = DimensionTypeRef.read(this);
                worldName = readString();
            }
        } else {
            previousGameMode = gameMode;
            dimensionCodec = new NBTCompound();
            this.dimensionTypeRef = DimensionTypeRef.read(this);
            if (!v1_14) {
                difficulty = Difficulty.getById(readByte());
            }
        }
        if (v1_15 && !v1_20_2) {
            hashedSeed = readLong();
        }
        if (v1_16) {
            this.maxPlayers = v1_16_2 ? this.readVarInt() : this.readUnsignedByte();
            viewDistance = readVarInt();
            if (v1_18) simulationDistance = readVarInt();
            reducedDebugInfo = readBoolean();
            enableRespawnScreen = readBoolean();
            if (v1_20_2) {
                limitedCrafting = readBoolean();
                this.dimensionTypeRef = DimensionTypeRef.read(this);
                worldName = readString();
                hashedSeed = readLong();
                gameMode = readGameMode();
                previousGameMode = readGameMode();
            }
            isDebug = readBoolean();
            isFlat = readBoolean();
        } else {
            maxPlayers = readUnsignedByte();
            String levelType = readString(16);
            isFlat = com.github.retrooper.packetevents.protocol.world.DimensionType.isFlat(levelType);
            isDebug = com.github.retrooper.packetevents.protocol.world.DimensionType.isDebug(levelType);
            if (v1_14) {
                viewDistance = readVarInt();
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                reducedDebugInfo = readBoolean();
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
                enableRespawnScreen = readBoolean();
            }
        }
        if (v1_19) {
            lastDeathPosition = readOptional(PacketWrapper::readWorldBlockPosition);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
            portalCooldown = readVarInt();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
                if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                    this.seaLevel = this.readVarInt();
                }
                this.enforcesSecureChat = this.readBoolean();
            }
        }
    }

    @Override
    public void write() {
        writeInt(entityID);
        boolean v1_20_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_2);
        boolean v1_19 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19);
        boolean v1_18 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_18);
        boolean v1_16_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16_2);
        boolean v1_16 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_16);
        boolean v1_14 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14);
        boolean v1_15 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15);
        if (v1_16_2) {
            writeBoolean(hardcore);
            if (!v1_20_2) {
                writeGameMode(gameMode);
            }
        } else {
            int gameModeId = gameMode.getId();
            if (hardcore) {
                gameModeId |= 0x8;
            }
            writeByte(gameModeId);
        }
        if (v1_16) {
            if (previousGameMode == null) {
                previousGameMode = gameMode;
            }
            if (!v1_20_2) {
                writeGameMode(previousGameMode);
            }
            writeVarInt(worldNames.size());
            for (String name : worldNames) {
                writeString(name);
            }
            if (!v1_20_2) {
                writeNBT(dimensionCodec);
                DimensionTypeRef.write(this, this.dimensionTypeRef);
                writeString(worldName);
            }
        } else {
            previousGameMode = gameMode;
            DimensionTypeRef.write(this, this.dimensionTypeRef);
            if (!v1_14) {
                writeByte(difficulty.getId());
            }
        }
        if (v1_15 && !v1_20_2) {
            writeLong(hashedSeed);
        }
        if (v1_16) {
            if (v1_16_2) {
                this.writeVarInt(this.maxPlayers);
            } else {
                this.writeByte(this.maxPlayers);
            }
            writeVarInt(viewDistance);
            if (v1_18) writeVarInt(simulationDistance);
            writeBoolean(reducedDebugInfo);
            writeBoolean(enableRespawnScreen);
            if (v1_20_2) {
                writeBoolean(limitedCrafting);
                DimensionTypeRef.write(this, this.dimensionTypeRef);
                writeString(worldName);
                writeLong(hashedSeed);
                writeGameMode(gameMode);
                writeGameMode(previousGameMode);
            }
            writeBoolean(isDebug);
            writeBoolean(isFlat);
        } else {
            writeByte(maxPlayers);
            String levelType;
            //TODO Proper backwards compatibility for level type
            if (isFlat) {
                levelType = WorldType.FLAT.getName();
            } else if (isDebug) {
                levelType = WorldType.DEBUG_ALL_BLOCK_STATES.getName();
            } else {
                levelType = WorldType.DEFAULT.getName();
            }
            writeString(levelType, 16);
            if (v1_14) {
                writeVarInt(viewDistance);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
                writeBoolean(reducedDebugInfo);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_15)) {
                writeBoolean(enableRespawnScreen);
            }
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
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            writeBoolean(enforcesSecureChat);
        }
    }

    @Override
    public void copy(WrapperPlayServerJoinGame wrapper) {
        entityID = wrapper.entityID;
        hardcore = wrapper.hardcore;
        gameMode = wrapper.gameMode;
        previousGameMode = wrapper.previousGameMode;
        worldNames = wrapper.worldNames;
        dimensionCodec = wrapper.dimensionCodec;
        dimensionTypeRef = wrapper.dimensionTypeRef;
        difficulty = wrapper.difficulty;
        worldName = wrapper.worldName;
        hashedSeed = wrapper.hashedSeed;
        maxPlayers = wrapper.maxPlayers;
        viewDistance = wrapper.viewDistance;
        simulationDistance = wrapper.simulationDistance;
        reducedDebugInfo = wrapper.reducedDebugInfo;
        enableRespawnScreen = wrapper.enableRespawnScreen;
        limitedCrafting = wrapper.limitedCrafting;
        isDebug = wrapper.isDebug;
        isFlat = wrapper.isFlat;
        lastDeathPosition = wrapper.lastDeathPosition;
        portalCooldown = wrapper.portalCooldown;
        seaLevel = wrapper.seaLevel;
        enforcesSecureChat = wrapper.enforcesSecureChat;
    }

    public int getEntityId() {
        return entityID;
    }

    public void setEntityId(int entityID) {
        this.entityID = entityID;
    }

    public boolean isHardcore() {
        return hardcore;
    }

    public void setHardcore(boolean hardcore) {
        this.hardcore = hardcore;
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

    public List<String> getWorldNames() {
        return worldNames;
    }

    public void setWorldNames(List<String> worldNames) {
        this.worldNames = worldNames;
    }

    public NBTCompound getDimensionCodec() {
        return dimensionCodec;
    }

    public void setDimensionCodec(NBTCompound dimensionCodec) {
        this.dimensionCodec = dimensionCodec;
    }

    public DimensionTypeRef getDimensionTypeRef() {
        return this.dimensionTypeRef;
    }

    public void setDimensionTypeRef(DimensionTypeRef dimensionTypeRef) {
        this.dimensionTypeRef = dimensionTypeRef;
    }

    public DimensionType getDimensionType() {
        IRegistry<DimensionType> registry = this.getRegistryHolder().getRegistryOr(DimensionTypes.getRegistry());
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public String getWorldName() {
        return worldName;
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public long getHashedSeed() {
        return hashedSeed;
    }

    public void setHashedSeed(long hashedSeed) {
        this.hashedSeed = hashedSeed;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(int viewDistance) {
        this.viewDistance = viewDistance;
    }

    public int getSimulationDistance() {
        return simulationDistance;
    }

    public void setSimulationDistance(int simulationDistance) {
        this.simulationDistance = simulationDistance;
    }

    public boolean isReducedDebugInfo() {
        return reducedDebugInfo;
    }

    public void setReducedDebugInfo(boolean reducedDebugInfo) {
        this.reducedDebugInfo = reducedDebugInfo;
    }

    public boolean isRespawnScreenEnabled() {
        return enableRespawnScreen;
    }

    public void setRespawnScreenEnabled(boolean enableRespawnScreen) {
        this.enableRespawnScreen = enableRespawnScreen;
    }

    public boolean isLimitedCrafting() {
        return this.limitedCrafting;
    }

    public void setLimitedCrafting(boolean limitedCrafting) {
        this.limitedCrafting = limitedCrafting;
    }

    public boolean isDebug() {
        return isDebug;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public void setFlat(boolean isFlat) {
        this.isFlat = isFlat;
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

    public int getSeaLevel() {
        return this.seaLevel;
    }

    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }

    public boolean isEnforcesSecureChat() {
        return this.enforcesSecureChat;
    }

    public void setEnforcesSecureChat(boolean enforcesSecureChat) {
        this.enforcesSecureChat = enforcesSecureChat;
    }
}
