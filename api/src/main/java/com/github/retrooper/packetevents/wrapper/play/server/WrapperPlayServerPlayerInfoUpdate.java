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
import com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.TextureProperty;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class WrapperPlayServerPlayerInfoUpdate extends PacketWrapper<WrapperPlayServerPlayerInfoUpdate> {
    //Specify entries using EnumSet.of()
    private EnumSet<Action> actions;
    private List<PlayerInfo> entries;

    public enum Action {
        ADD_PLAYER,
        INITIALIZE_CHAT,
        UPDATE_GAME_MODE,
        UPDATE_LISTED,
        UPDATE_LATENCY,
        UPDATE_DISPLAY_NAME,
        /**
         * Updates the order in which the player is listed in the tablist.<br>
         * Added with 1.21.2
         */
        UPDATE_LIST_ORDER,
        /**
         * Updates whether the outer skin layer (hat) of the player will be shown in tablist.<br>
         * Added with 1.21.4
         */
        UPDATE_HAT;

        public static final WrapperPlayServerPlayerInfoUpdate.Action[] VALUES = values();
    }

    public static class PlayerInfo {
        private UserProfile profile;
        private boolean listed = true;
        private int latency;
        private GameMode gameMode;
        @Nullable
        private Component displayName;
        @Nullable
        private RemoteChatSession chatSession;
        /**
         * Added with 1.21.2
         */
        private int listOrder;
        /**
         * Added with 1.21.4
         */
        private boolean showHat;

        public PlayerInfo(UUID profileId) {
            this(new UserProfile(profileId, ""));
        }

        public PlayerInfo(UserProfile profile) {
            this.profile = profile;
        }

        public PlayerInfo(
                UserProfile profile, boolean listed,
                int latency, GameMode gameMode,
                @Nullable Component displayName,
                @Nullable RemoteChatSession chatSession
        ) {
            this(profile, listed, latency, gameMode, displayName, chatSession, 0);
        }

        public PlayerInfo(
                UserProfile profile, boolean listed,
                int latency, GameMode gameMode,
                @Nullable Component displayName,
                @Nullable RemoteChatSession chatSession,
                int listOrder
        ) {
            this(profile, listed, latency, gameMode, displayName, chatSession, listOrder, false);
        }

        public PlayerInfo(
                UserProfile profile, boolean listed,
                int latency, GameMode gameMode,
                @Nullable Component displayName,
                @Nullable RemoteChatSession chatSession,
                int listOrder, boolean showHat
        ) {
            this.profile = profile;
            this.listed = listed;
            this.latency = latency;
            this.gameMode = gameMode;
            this.displayName = displayName;
            this.chatSession = chatSession;
            this.listOrder = listOrder;
            this.showHat = showHat;
        }

        public UUID getProfileId() {
            return profile.getUUID();
        }

        public UserProfile getUserProfile() {
            return profile;
        }

        public boolean isListed() {
            return listed;
        }

        public int getLatency() {
            return latency;
        }

        public GameMode getGameMode() {
            return gameMode;
        }

        public @Nullable Component getDisplayName() {
            return displayName;
        }

        public @Nullable RemoteChatSession getChatSession() {
            return chatSession;
        }

        /**
         * Added with 1.21.2
         */
        public int getListOrder() {
            return this.listOrder;
        }

        /**
         * Added with 1.21.4
         */
        public boolean isShowHat() {
            return this.showHat;
        }

        public void setGameProfile(UserProfile gameProfile) {
            this.profile = gameProfile;
        }

        public void setListed(boolean listed) {
            this.listed = listed;
        }

        public void setLatency(int latency) {
            this.latency = latency;
        }

        public void setGameMode(GameMode gameMode) {
            this.gameMode = gameMode;
        }

        public void setDisplayName(@Nullable Component displayName) {
            this.displayName = displayName;
        }

        public void setChatSession(@Nullable RemoteChatSession chatSession) {
            this.chatSession = chatSession;
        }

        /**
         * Added with 1.21.2
         */
        public void setListOrder(int listOrder) {
            this.listOrder = listOrder;
        }

        /**
         * Added with 1.21.4
         */
        public void setShowHat(boolean showHat) {
            this.showHat = showHat;
        }
    }

    public WrapperPlayServerPlayerInfoUpdate(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerPlayerInfoUpdate(EnumSet<Action> actions, List<PlayerInfo> entries) {
        super(PacketType.Play.Server.PLAYER_INFO_UPDATE);
        this.actions = actions;
        this.entries = entries;
    }

    public WrapperPlayServerPlayerInfoUpdate(EnumSet<Action> actions, PlayerInfo... entries) {
        super(PacketType.Play.Server.PLAYER_INFO_UPDATE);
        this.actions = actions;
        this.entries = new ArrayList<>();
        Collections.addAll(this.entries, entries);
    }

    public WrapperPlayServerPlayerInfoUpdate(Action action, List<PlayerInfo> entries) {
        this(EnumSet.of(action), entries);
    }

    public WrapperPlayServerPlayerInfoUpdate(Action action, PlayerInfo... entries) {
        this(EnumSet.of(action), entries);
    }

    @Override
    public void read() {
        this.actions = readEnumSet(Action.class);
        this.entries = readList(wrapper -> {
            UUID uuid = wrapper.readUUID();
            UserProfile gameProfile = new UserProfile(uuid, null);
            GameMode gameMode = GameMode.defaultGameMode();
            boolean listed = false;
            int latency = 0;
            @Nullable RemoteChatSession chatSession = null;
            @Nullable Component displayName = null;
            int listOrder = 0;
            boolean showHat = false;
            for (Action action : actions) {
                switch (action) {
                    case ADD_PLAYER:
                        gameProfile.setUUID(uuid);
                        gameProfile.setName(wrapper.readString(16));
                        int propertyCount = wrapper.readVarInt();
                        for (int j = 0; j < propertyCount; j++) {
                            String propertyName = wrapper.readString();
                            String propertyValue = wrapper.readString();
                            String propertySignature = wrapper.readOptional(PacketWrapper::readString);
                            TextureProperty textureProperty = new TextureProperty(propertyName, propertyValue, propertySignature);
                            gameProfile.getTextureProperties().add(textureProperty);
                        }
                        break;
                    case INITIALIZE_CHAT:
                        chatSession = wrapper.readOptional(PacketWrapper::readRemoteChatSession);
                        break;
                    case UPDATE_GAME_MODE:
                        gameMode = GameMode.getById(wrapper.readVarInt());
                        break;

                    case UPDATE_LISTED:
                        listed = wrapper.readBoolean();
                        break;
                    case UPDATE_LATENCY:
                        latency = wrapper.readVarInt();
                        break;
                    case UPDATE_DISPLAY_NAME:
                        displayName = wrapper.readOptional(PacketWrapper::readComponent);
                        break;
                    case UPDATE_LIST_ORDER:
                        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                            listOrder = wrapper.readVarInt();
                        }
                        break;
                    case UPDATE_HAT:
                        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                            showHat = wrapper.readBoolean();
                        }
                        break;
                }
            }
            return new PlayerInfo(gameProfile, listed, latency, gameMode, displayName, chatSession, listOrder, showHat);
        });
    }

    @Override
    public void write() {
        writeEnumSet(this.actions, Action.class);
        writeList(this.entries, (wrapper, playerInfo) -> {
            wrapper.writeUUID(playerInfo.getProfileId());
            for (Action action : actions) {
                switch (action) {
                    case ADD_PLAYER:
                        wrapper.writeString(playerInfo.getGameProfile().getName(), 16);
                        writeList(playerInfo.getGameProfile().getTextureProperties(), (w, textureProperty) -> {
                            w.writeString(textureProperty.getName());
                            w.writeString(textureProperty.getValue());
                            w.writeOptional(textureProperty.getSignature(), PacketWrapper::writeString);
                        });
                        break;
                    case INITIALIZE_CHAT:
                        wrapper.writeOptional(playerInfo.getChatSession(), PacketWrapper::writeRemoteChatSession);
                        break;
                    case UPDATE_GAME_MODE:
                        wrapper.writeVarInt(playerInfo.getGameMode().getId());
                        break;
                    case UPDATE_LISTED:
                        wrapper.writeBoolean(playerInfo.isListed());
                        break;
                    case UPDATE_LATENCY:
                        wrapper.writeVarInt(playerInfo.getLatency());
                        break;
                    case UPDATE_DISPLAY_NAME:
                        wrapper.writeOptional(playerInfo.getDisplayName(), PacketWrapper::writeComponent);
                        break;
                    case UPDATE_LIST_ORDER:
                        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
                            wrapper.writeVarInt(playerInfo.getListOrder());
                        }
                        break;
                    case UPDATE_HAT:
                        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_4)) {
                            wrapper.writeBoolean(playerInfo.isShowHat());
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void copy(WrapperPlayServerPlayerInfoUpdate wrapper) {
        this.actions = wrapper.actions;
        this.entries = wrapper.entries;
    }

    public EnumSet<Action> getActions() {
        return actions;
    }

    public void setActions(EnumSet<Action> actions) {
        this.actions = actions;
    }

    public List<PlayerInfo> getEntries() {
        return entries;
    }

    public void setEntries(List<PlayerInfo> entries) {
        this.entries = entries;
    }
}
