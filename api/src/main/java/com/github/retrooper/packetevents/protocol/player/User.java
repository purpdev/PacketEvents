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

package com.github.retrooper.packetevents.protocol.player;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessageLegacy;
import com.github.retrooper.packetevents.protocol.chat.message.ChatMessage_v1_16;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleSubtitle;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleText;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetTitleTimes;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerTitle;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class User implements IRegistryHolder {

    private final Object channel;
    private ConnectionState decoderState;
    private ConnectionState encoderState;
    private ClientVersion clientVersion;
    private final UserProfile profile;
    private int entityId = -1;

    private DimensionType dimensionType = DimensionTypes.OVERWORLD;
    private final Map<ResourceLocation, IRegistry<?>> registries = new HashMap<>();

    public User(Object channel,
                ConnectionState connectionState, ClientVersion clientVersion,
                UserProfile profile) {
        this.channel = channel;
        this.decoderState = connectionState;
        this.encoderState = connectionState;
        this.clientVersion = clientVersion;
        this.profile = profile;
    }

    @ApiStatus.Internal
    @Override
    public @Nullable IRegistry<?> getRegistry(ResourceLocation registryKey, ClientVersion version) {
        return this.registries.get(registryKey);
    }

    @ApiStatus.Internal
    public void putRegistry(IRegistry<?> registry) {
        this.registries.put(registry.getRegistryKey(), registry);
    }

    public Object getChannel() {
        return channel;
    }

    public InetSocketAddress getAddress() {
        return (InetSocketAddress) ChannelHelper.remoteAddress(channel);
    }

    public ConnectionState getConnectionState() {
        ConnectionState decoderState = this.decoderState;
        ConnectionState encoderState = this.encoderState;
        if (decoderState != encoderState) {
            throw new IllegalArgumentException("Can't get common connection state: " + decoderState + " != " + encoderState);
        }
        return decoderState;
    }

    public void setConnectionState(ConnectionState connectionState) {
        this.setDecoderState(connectionState);
        this.setEncoderState(connectionState);
    }

    public ConnectionState getDecoderState() {
        return this.decoderState;
    }

    public void setDecoderState(ConnectionState decoderState) {
        this.decoderState = decoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s decoder into " + decoderState + " state!");
    }

    public ConnectionState getEncoderState() {
        return this.encoderState;
    }

    public void setEncoderState(ConnectionState encoderState) {
        this.encoderState = encoderState;
        PacketEvents.getAPI().getLogManager().debug(
                "Transitioned " + this.getName() + "'s encoder into " + encoderState + " state!");
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public String getName() {
        return profile.getName();
    }

    public UUID getUUID() {
        return profile.getUUID();
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public void sendPacket(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, buffer);
    }

    public void sendPacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, wrapper);
    }

    public void sendPacketSilently(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, buffer);
    }

    public void sendPacketSilently(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().sendPacketSilently(channel, wrapper);
    }

    public void writePacket(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().writePacket(channel, buffer);
    }

    public void writePacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacket(channel, wrapper);
    }

    public void writePacketSilently(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(channel, buffer);
    }

    public void writePacketSilently(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().writePacketSilently(channel, wrapper);
    }

    public void receivePacket(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, buffer);
    }

    public void receivePacket(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, wrapper);
    }

    public void receivePacketSilently(Object buffer) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(channel, buffer);
    }

    public void receivePacketSilently(PacketWrapper<?> wrapper) {
        PacketEvents.getAPI().getProtocolManager().receivePacketSilently(channel, wrapper);
    }

    public void flushPackets() {
        ChannelHelper.flush(channel);
    }

    public void closeConnection() {
        ChannelHelper.close(channel);
    }

    //Might be tough with the message signing
   /* public void chat(String message) {
        //Fake an incoming chat packet
        WrapperPlayClientChatMessage chatMessage = new WrapperPlayClientChatMessage(message);
        PacketEvents.getAPI().getProtocolManager().receivePacket(channel, chatMessage);
    }*/

    public void closeInventory() {
        WrapperPlayServerCloseWindow closeWindow = new WrapperPlayServerCloseWindow(0);
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, closeWindow);
    }

    public void sendMessage(String legacyMessage) {
        Component component = AdventureSerializer.fromLegacyFormat(legacyMessage);
        sendMessage(component);
    }

    public void sendMessage(Component component) {
        sendMessage(component, ChatTypes.CHAT);
    }

    public void sendMessage(Component component, ChatType type) {
        ServerVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion().toServerVersion() :
                PacketEvents.getAPI().getServerManager().getVersion();
        PacketWrapper<?> chatPacket;
        if (version.isNewerThanOrEquals(ServerVersion.V_1_19)) {
            chatPacket = new WrapperPlayServerSystemChatMessage(false, component);
        } else {
            ChatMessage message;
            if (version.isNewerThanOrEquals(ServerVersion.V_1_16)) {
                message = new ChatMessage_v1_16(component, type, new UUID(0L, 0L));
            } else {
                message = new ChatMessageLegacy(component, type);
            }
            chatPacket = new WrapperPlayServerChatMessage(message);
        }
        PacketEvents.getAPI().getProtocolManager().sendPacket(channel, chatPacket);
    }

    public void sendTitle(String legacyTitle, String legacySubtitle,
                          int fadeInTicks, int stayTicks, int fadeOutTicks) {
        Component title = AdventureSerializer.fromLegacyFormat(legacyTitle);
        Component subtitle = AdventureSerializer.fromLegacyFormat(legacySubtitle);
        sendTitle(title, subtitle, fadeInTicks, stayTicks, fadeOutTicks);
    }

    public void sendTitle(Component title, Component subtitle, int fadeInTicks, int stayTicks, int fadeOutTicks) {
        ClientVersion version = PacketEvents.getAPI().getInjector().isProxy() ? getClientVersion() :
                PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        boolean modern = version.isNewerThanOrEquals(ClientVersion.V_1_17);
        PacketWrapper<?> animation;
        PacketWrapper<?> setTitle = null;
        PacketWrapper<?> setSubtitle = null;
        if (modern) {
            animation = new WrapperPlayServerSetTitleTimes(fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerSetTitleText(title);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerSetTitleSubtitle(subtitle);
            }
        } else {
            animation = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                    TitleAction.SET_TIMES_AND_DISPLAY, (Component) null, null, null,
                    fadeInTicks, stayTicks, fadeOutTicks);
            if (title != null) {
                setTitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_TITLE, title, null, null,
                        0, 0, 0);
            }
            if (subtitle != null) {
                setSubtitle = new WrapperPlayServerTitle(WrapperPlayServerTitle.
                        TitleAction.SET_SUBTITLE, null, subtitle, null,
                        0, 0, 0);
            }
        }
        sendPacket(animation);
        if (setTitle != null) {
            sendPacket(setTitle);
        }
        if (setSubtitle != null) {
            sendPacket(setSubtitle);
        }
    }

    //TODO sendTitle that is cross-version

    // dimension type related methods

    public int getMinWorldHeight() {
        return this.getMinWorldHeight(null);
    }

    public int getMinWorldHeight(@Nullable ClientVersion version) {
        if (version == null) {
            version = PacketEvents.getAPI().getInjector().isProxy() ? this.getClientVersion() :
                    PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        }
        return this.dimensionType.getMinY(version);
    }

    public int getTotalWorldHeight() {
        return this.getTotalWorldHeight(null);
    }

    public int getTotalWorldHeight(@Nullable ClientVersion version) {
        if (version == null) {
            version = PacketEvents.getAPI().getInjector().isProxy() ? this.getClientVersion() :
                    PacketEvents.getAPI().getServerManager().getVersion().toClientVersion();
        }
        return this.dimensionType.getHeight(version);
    }

    public DimensionType getDimensionType() {
        return this.dimensionType;
    }

    @ApiStatus.Internal
    public void setDimensionType(DimensionType dimensionType) {
        this.dimensionType = dimensionType;
    }

    // legacy dimension type related methods

    @Deprecated
    public void setMinWorldHeight(int minWorldHeight) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setTotalWorldHeight(int totalWorldHeight) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void switchDimensionType(ServerVersion version, Dimension dimension) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setDefaultWorldHeights(ServerVersion version, Dimension dimension) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setDefaultWorldHeights(boolean extended) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void setWorldNBT(NBTList<NBTCompound> worldNBT) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public Dimension getDimension() {
        return Dimension.fromDimensionType(this.dimensionType, this, null);
    }

    @Deprecated
    public void setDimension(Dimension dimension) {
        this.dimensionType = dimension.asDimensionType(this, null);
    }

    @Deprecated
    public @Nullable NBTCompound getWorldNBT(String worldName) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public @Nullable NBTCompound getWorldNBT(int worldId) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public @Nullable NBTCompound getWorldNBT(Dimension dimension) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public @Nullable String getWorldName(int worldId) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public String getWorldName(Dimension dimension) {
        throw new UnsupportedOperationException();
    }
}
