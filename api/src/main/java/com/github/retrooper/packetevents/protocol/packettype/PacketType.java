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

package com.github.retrooper.packetevents.protocol.packettype;

import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_12_1;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_13;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_14_4;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_15_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_16_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_17;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_18;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_1;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_3;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_19_4;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_21_2;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_7_10;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_8;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9;
import com.github.retrooper.packetevents.protocol.packettype.clientbound.ClientboundPacketType_1_9_3;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.config.clientbound.ClientboundConfigPacketType_1_21;
import com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.config.serverbound.ServerboundConfigPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_12_1;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_13;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_14;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_15_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_16_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_17;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_1;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_3;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_19_4;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_3;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_20_5;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_2;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_21_4;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_7_10;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_8;
import com.github.retrooper.packetevents.protocol.packettype.serverbound.ServerboundPacketType_1_9;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.VersionMapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public final class PacketType {

    private static boolean PREPARED = false;

    //TODO UPDATE Update packet type mappings (clientbound pt. 1)
    private static final VersionMapper CLIENTBOUND_PLAY_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_7_10,
            ClientVersion.V_1_8,
            ClientVersion.V_1_9,
            ClientVersion.V_1_10,
            ClientVersion.V_1_12,
            ClientVersion.V_1_12_1,
            ClientVersion.V_1_13,
            ClientVersion.V_1_14,
            ClientVersion.V_1_14_4,
            ClientVersion.V_1_15,
            ClientVersion.V_1_15_2,
            ClientVersion.V_1_16,
            ClientVersion.V_1_16_2,
            ClientVersion.V_1_17,
            ClientVersion.V_1_18,
            ClientVersion.V_1_19,
            ClientVersion.V_1_19_1,
            ClientVersion.V_1_19_3,
            ClientVersion.V_1_19_4,
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21,
            ClientVersion.V_1_21_2);

    //TODO UPDATE Update packet type mappings (serverbound pt. 1)
    private static final VersionMapper SERVERBOUND_PLAY_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_7_10,
            ClientVersion.V_1_8,
            ClientVersion.V_1_9,
            ClientVersion.V_1_12,
            ClientVersion.V_1_12_1,
            ClientVersion.V_1_13,
            ClientVersion.V_1_14,
            ClientVersion.V_1_15_2,
            ClientVersion.V_1_16,
            ClientVersion.V_1_16_2,
            ClientVersion.V_1_17,
            ClientVersion.V_1_19,
            ClientVersion.V_1_19_1,
            ClientVersion.V_1_19_3,
            ClientVersion.V_1_19_4,
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21_2,
            ClientVersion.V_1_21_4);

    // TODO UPDATE Update packet type mappings (config clientbound pt. 1)
    private static final VersionMapper CLIENTBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_3,
            ClientVersion.V_1_20_5,
            ClientVersion.V_1_21);
    // TODO UPDATE Update packet type mappings (config serverbound pt. 1)
    private static final VersionMapper SERVERBOUND_CONFIG_VERSION_MAPPER = new VersionMapper(
            ClientVersion.V_1_20_2,
            ClientVersion.V_1_20_5);

    public static void prepare() {
        PacketType.Play.Client.load();
        PacketType.Play.Server.load();
        PacketType.Configuration.Client.load();
        PacketType.Configuration.Server.load();
        PREPARED = true;
    }

    public static boolean isPrepared() {
        return PREPARED;
    }

    public static PacketTypeCommon getById(PacketSide side, ConnectionState state, ClientVersion version, int packetID) {
        switch (state) {
            case HANDSHAKING:
                if (side == PacketSide.CLIENT) {
                    return PacketType.Handshaking.Client.getById(packetID);
                } else {
                    return PacketType.Handshaking.Server.getById(packetID);
                }
            case STATUS:
                if (side == PacketSide.CLIENT) {
                    return Status.Client.getById(packetID);
                } else {
                    return Status.Server.getById(packetID);
                }
            case LOGIN:
                if (side == PacketSide.CLIENT) {
                    return Login.Client.getById(packetID);
                } else {
                    return Login.Server.getById(packetID);
                }
            case PLAY:
                if (side == PacketSide.CLIENT) {
                    return Play.Client.getById(version, packetID);
                } else {
                    return Play.Server.getById(version, packetID);
                }
            case CONFIGURATION:
                if (side == PacketSide.CLIENT) {
                    return Configuration.Client.getById(version, packetID);
                } else {
                    return Configuration.Server.getById(version, packetID);
                }
            default:
                return null;
        }
    }

    public static class Handshaking {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            HANDSHAKE(0),
            /**
             * Technically not part of the current protocol, but clients older than 1.7 will send this to initiate Server List Ping.
             * 1.8 and newer servers will handle it correctly though.
             */
            LEGACY_SERVER_LIST_PING(0xFE);

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return HANDSHAKE;
                } else if (packetID == 0xFE) {
                    return LEGACY_SERVER_LIST_PING;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            LEGACY_SERVER_LIST_RESPONSE(0xFE);

            private final int id;

            Server(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                return packetID == 0xFE ? LEGACY_SERVER_LIST_RESPONSE : null;
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Status {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            REQUEST(0x00),
            PING(0x01);

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetId) {
                if (packetId == 0) {
                    return REQUEST;
                } else if (packetId == 1) {
                    return PING;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            RESPONSE(0x00),
            PONG(0x01);

            private final int id;

            Server(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                if (packetID == 0) {
                    return RESPONSE;
                } else if (packetID == 1) {
                    return PONG;
                } else {
                    return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Login {

        public enum Client implements PacketTypeConstant, ServerBoundPacket {

            LOGIN_START(0x00),
            ENCRYPTION_RESPONSE(0x01),

            // Added in 1.13
            LOGIN_PLUGIN_RESPONSE(0x02),

            // Added in 1.20.2
            LOGIN_SUCCESS_ACK(0x03),

            // Added in 1.20.5
            COOKIE_RESPONSE(0x04);

            private final int id;

            Client(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                switch (packetID) {
                    case 0x00:
                        return LOGIN_START;
                    case 0x01:
                        return ENCRYPTION_RESPONSE;
                    case 0x02:
                        return LOGIN_PLUGIN_RESPONSE;
                    case 0x03:
                        return LOGIN_SUCCESS_ACK;
                    case 0x04:
                        return COOKIE_RESPONSE;
                    default:
                        return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeConstant, ClientBoundPacket {

            DISCONNECT(0x00),
            ENCRYPTION_REQUEST(0x01),
            LOGIN_SUCCESS(0x02),

            // Added in 1.8
            SET_COMPRESSION(0x03),

            // Added in 1.13
            LOGIN_PLUGIN_REQUEST(0x04),

            // Added in 1.20.5
            COOKIE_REQUEST(0x05);

            private final int id;

            Server(int id) {
                this.id = id;
            }

            @Nullable
            public static PacketTypeCommon getById(int packetID) {
                switch (packetID) {
                    case 0x00:
                        return DISCONNECT;
                    case 0x01:
                        return ENCRYPTION_REQUEST;
                    case 0x02:
                        return LOGIN_SUCCESS;
                    case 0x03:
                        return SET_COMPRESSION;
                    case 0x04:
                        return LOGIN_PLUGIN_REQUEST;
                    case 0x05:
                        return COOKIE_REQUEST;
                    default:
                        return null;
                }
            }

            public int getId() {
                return id;
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    // Added in 1.20.2
    public static class Configuration {

        public enum Client implements PacketTypeCommon, ServerBoundPacket {

            CLIENT_SETTINGS,
            PLUGIN_MESSAGE,
            CONFIGURATION_END_ACK,
            KEEP_ALIVE,
            PONG,
            RESOURCE_PACK_STATUS,

            // Added in 1.20.5
            COOKIE_RESPONSE,
            SELECT_KNOWN_PACKS;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;

            Client() {
                this.ids = new int[SERVERBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
                Arrays.fill(this.ids, -1);
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ServerboundConfigPacketType_1_20_2.values());
                loadPacketIds(ServerboundConfigPacketType_1_20_5.values());
                // TODO UPDATE Update packet type mappings (config serverbound pt. 2)
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Configuration.Client value = Configuration.Client.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static @Nullable PacketTypeCommon getById(int packetId) {
                return getById(ClientVersion.getLatest(), packetId);
            }

            public static @Nullable PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Deprecated
            public int getId() {
                return this.getId(ClientVersion.getLatest());
            }

            @Override
            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                return this.ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeCommon, ClientBoundPacket {

            PLUGIN_MESSAGE,
            DISCONNECT,
            CONFIGURATION_END,
            KEEP_ALIVE,
            PING,
            REGISTRY_DATA,
            RESOURCE_PACK_SEND,
            UPDATE_ENABLED_FEATURES,
            UPDATE_TAGS,

            // Added in 1.20.3
            RESOURCE_PACK_REMOVE,

            // Added in 1.20.5
            COOKIE_REQUEST,
            RESET_CHAT,
            STORE_COOKIE,
            TRANSFER,
            SELECT_KNOWN_PACKS,

            // added in 1.21
            CUSTOM_REPORT_DETAILS,
            SERVER_LINKS;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;

            Server() {
                this.ids = new int[CLIENTBOUND_CONFIG_VERSION_MAPPER.getVersions().length];
                Arrays.fill(this.ids, -1);
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ClientboundConfigPacketType_1_20_2.values());
                loadPacketIds(ClientboundConfigPacketType_1_20_3.values());
                loadPacketIds(ClientboundConfigPacketType_1_20_5.values());
                loadPacketIds(ClientboundConfigPacketType_1_21.values());
                // TODO UPDATE Update packet type mappings (config clientbound pt. 2)
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Configuration.Server value = Configuration.Server.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static @Nullable PacketTypeCommon getById(int packetId) {
                return getById(ClientVersion.getLatest(), packetId);
            }

            public static @Nullable PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Deprecated
            public int getId() {
                return this.getId(ClientVersion.getLatest());
            }

            @Override
            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_CONFIG_VERSION_MAPPER.getIndex(version);
                return this.ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }
        }
    }

    public static class Play {

        public enum Client implements PacketTypeCommon, ServerBoundPacket {

            // Packets which no longer exist on the latest version
            CHAT_PREVIEW,

            TELEPORT_CONFIRM,
            QUERY_BLOCK_NBT,
            SET_DIFFICULTY,
            CHAT_MESSAGE,
            CLIENT_STATUS,
            CLIENT_SETTINGS,
            TAB_COMPLETE,
            WINDOW_CONFIRMATION,
            CLICK_WINDOW_BUTTON,
            CLICK_WINDOW,
            CLOSE_WINDOW,
            PLUGIN_MESSAGE,
            EDIT_BOOK,
            QUERY_ENTITY_NBT,
            INTERACT_ENTITY,
            GENERATE_STRUCTURE,
            KEEP_ALIVE,
            LOCK_DIFFICULTY,
            PLAYER_POSITION,
            PLAYER_POSITION_AND_ROTATION,
            PLAYER_ROTATION,
            PLAYER_FLYING,
            VEHICLE_MOVE,
            STEER_BOAT,
            /**
             * Removed with 1.21.4
             */
            @ApiStatus.Obsolete
            PICK_ITEM,
            CRAFT_RECIPE_REQUEST,
            PLAYER_ABILITIES,
            PLAYER_DIGGING,
            ENTITY_ACTION,
            /**
             * Removed with 1.21.2
             */
            @ApiStatus.Obsolete
            STEER_VEHICLE,
            PONG,
            RECIPE_BOOK_DATA,
            SET_DISPLAYED_RECIPE,
            SET_RECIPE_BOOK_STATE,
            NAME_ITEM,
            RESOURCE_PACK_STATUS,
            ADVANCEMENT_TAB,
            SELECT_TRADE,
            SET_BEACON_EFFECT,
            HELD_ITEM_CHANGE,
            UPDATE_COMMAND_BLOCK,
            UPDATE_COMMAND_BLOCK_MINECART,
            CREATIVE_INVENTORY_ACTION,
            UPDATE_JIGSAW_BLOCK,
            UPDATE_STRUCTURE_BLOCK,
            UPDATE_SIGN,
            ANIMATION,
            SPECTATE,
            PLAYER_BLOCK_PLACEMENT,
            USE_ITEM,

            /**
             * Added with 1.19
             */
            CHAT_COMMAND,

            /**
             * Added with 1.19.1
             */
            CHAT_ACK,

            /**
             * Added with 1.19.3
             */
            CHAT_SESSION_UPDATE,

            /**
             * Added with 1.20.2
             */
            CHUNK_BATCH_ACK,
            /**
             * Added with 1.20.2
             */
            CONFIGURATION_ACK,
            /**
             * Added with 1.20.2
             */
            DEBUG_PING,

            /**
             * Added with 1.20.3
             */
            SLOT_STATE_CHANGE,

            /**
             * Added with 1.20.5
             */
            CHAT_COMMAND_UNSIGNED,
            /**
             * Added with 1.20.5
             */
            COOKIE_RESPONSE,
            /**
             * Added with 1.20.5
             */
            DEBUG_SAMPLE_SUBSCRIPTION,

            /**
             * Added with 1.21.2
             */
            CLIENT_TICK_END,
            /**
             * Added with 1.21.2
             */
            SELECT_BUNDLE_ITEM,
            /**
             * Added with 1.21.2, based on {@link #STEER_VEHICLE}
             */
            PLAYER_INPUT,

            /**
             * Added with 1.21.4
             */
            PICK_ITEM_FROM_BLOCK,
            /**
             * Added with 1.21.4
             */
            PICK_ITEM_FROM_ENTITY,
            /**
             * Added with 1.21.4
             */
            PLAYER_LOADED,
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;

            Client() {
                ids = new int[SERVERBOUND_PLAY_VERSION_MAPPER.getVersions().length];
                Arrays.fill(ids, -1);
            }

            @Nullable
            public static PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                return packetIdMap.get(packetId);
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Client value = Client.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index,
                            k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ServerboundPacketType_1_7_10.values());
                loadPacketIds(ServerboundPacketType_1_8.values());
                loadPacketIds(ServerboundPacketType_1_9.values());
                loadPacketIds(ServerboundPacketType_1_12.values());
                loadPacketIds(ServerboundPacketType_1_12_1.values());
                loadPacketIds(ServerboundPacketType_1_13.values());
                loadPacketIds(ServerboundPacketType_1_14.values());
                loadPacketIds(ServerboundPacketType_1_15_2.values());
                loadPacketIds(ServerboundPacketType_1_16.values());
                loadPacketIds(ServerboundPacketType_1_16_2.values());
                loadPacketIds(ServerboundPacketType_1_17.values());
                loadPacketIds(ServerboundPacketType_1_19.values());
                loadPacketIds(ServerboundPacketType_1_19_1.values());
                loadPacketIds(ServerboundPacketType_1_19_3.values());
                loadPacketIds(ServerboundPacketType_1_19_4.values());
                loadPacketIds(ServerboundPacketType_1_20_2.values());
                loadPacketIds(ServerboundPacketType_1_20_3.values());
                loadPacketIds(ServerboundPacketType_1_20_5.values());
                loadPacketIds(ServerboundPacketType_1_21_2.values());
                loadPacketIds(ServerboundPacketType_1_21_4.values());
                //TODO UPDATE Update packet type mappings (serverbound pt. 2)
            }

            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = SERVERBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                return ids[index];
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.CLIENT;
            }
        }

        public enum Server implements PacketTypeCommon, ClientBoundPacket {

            // Packets which are no longer exist on the latest version
            SET_COMPRESSION,
            MAP_CHUNK_BULK,
            UPDATE_ENTITY_NBT,
            UPDATE_SIGN,
            USE_BED,
            SPAWN_WEATHER_ENTITY,
            TITLE,
            WORLD_BORDER,
            COMBAT_EVENT,
            ENTITY_MOVEMENT,
            SPAWN_LIVING_ENTITY,
            SPAWN_PAINTING,
            SCULK_VIBRATION_SIGNAL,
            ACKNOWLEDGE_PLAYER_DIGGING,
            CHAT_PREVIEW_PACKET,
            CUSTOM_SOUND_EFFECT,
            PLAYER_CHAT_HEADER,
            PLAYER_INFO,
            DISPLAY_CHAT_PREVIEW,
            UPDATE_ENABLED_FEATURES,
            SPAWN_PLAYER,

            // Still existing packets
            WINDOW_CONFIRMATION,
            SPAWN_ENTITY,
            SPAWN_EXPERIENCE_ORB,
            ENTITY_ANIMATION,
            STATISTICS,
            BLOCK_BREAK_ANIMATION,
            BLOCK_ENTITY_DATA,
            BLOCK_ACTION,
            BLOCK_CHANGE,
            BOSS_BAR,
            SERVER_DIFFICULTY,
            CLEAR_TITLES,
            TAB_COMPLETE,
            MULTI_BLOCK_CHANGE,
            DECLARE_COMMANDS,
            CLOSE_WINDOW,
            WINDOW_ITEMS,
            WINDOW_PROPERTY,
            SET_SLOT,
            SET_COOLDOWN,
            PLUGIN_MESSAGE,
            DISCONNECT,
            ENTITY_STATUS,
            EXPLOSION,
            UNLOAD_CHUNK,
            CHANGE_GAME_STATE,
            OPEN_HORSE_WINDOW,
            INITIALIZE_WORLD_BORDER,
            KEEP_ALIVE,
            CHUNK_DATA,
            EFFECT,
            PARTICLE,
            UPDATE_LIGHT,
            JOIN_GAME,
            MAP_DATA,
            MERCHANT_OFFERS,
            ENTITY_RELATIVE_MOVE,
            ENTITY_RELATIVE_MOVE_AND_ROTATION,
            ENTITY_ROTATION,
            VEHICLE_MOVE,
            OPEN_BOOK,
            OPEN_WINDOW,
            OPEN_SIGN_EDITOR,
            PING,
            CRAFT_RECIPE_RESPONSE,
            PLAYER_ABILITIES,
            END_COMBAT_EVENT,
            ENTER_COMBAT_EVENT,
            DEATH_COMBAT_EVENT,
            FACE_PLAYER,
            PLAYER_POSITION_AND_LOOK,
            UNLOCK_RECIPES, // removed in 1.21.2
            DESTROY_ENTITIES,
            REMOVE_ENTITY_EFFECT,
            RESOURCE_PACK_SEND,
            RESPAWN,
            ENTITY_HEAD_LOOK,
            SELECT_ADVANCEMENTS_TAB,
            ACTION_BAR,
            WORLD_BORDER_CENTER,
            WORLD_BORDER_LERP_SIZE,
            WORLD_BORDER_SIZE,
            WORLD_BORDER_WARNING_DELAY,
            WORLD_BORDER_WARNING_REACH,
            CAMERA,
            HELD_ITEM_CHANGE,
            UPDATE_VIEW_POSITION,
            UPDATE_VIEW_DISTANCE,
            SPAWN_POSITION,
            DISPLAY_SCOREBOARD,
            ENTITY_METADATA,
            ATTACH_ENTITY,
            ENTITY_VELOCITY,
            ENTITY_EQUIPMENT,
            SET_EXPERIENCE,
            UPDATE_HEALTH,
            SCOREBOARD_OBJECTIVE,
            SET_PASSENGERS,
            TEAMS,
            UPDATE_SCORE,
            UPDATE_SIMULATION_DISTANCE,
            SET_TITLE_SUBTITLE,
            TIME_UPDATE,
            SET_TITLE_TEXT,
            SET_TITLE_TIMES,
            ENTITY_SOUND_EFFECT,
            SOUND_EFFECT,
            STOP_SOUND,
            PLAYER_LIST_HEADER_AND_FOOTER,
            NBT_QUERY_RESPONSE,
            COLLECT_ITEM,
            ENTITY_TELEPORT,
            UPDATE_ADVANCEMENTS,
            UPDATE_ATTRIBUTES,
            ENTITY_EFFECT,
            DECLARE_RECIPES,
            TAGS,
            CHAT_MESSAGE,

            // Added in 1.19
            ACKNOWLEDGE_BLOCK_CHANGES,
            SERVER_DATA,
            SYSTEM_CHAT_MESSAGE,

            // Added in 1.19.1
            DELETE_CHAT,
            CUSTOM_CHAT_COMPLETIONS,

            // Added in 1.19.3
            DISGUISED_CHAT,
            PLAYER_INFO_REMOVE,
            PLAYER_INFO_UPDATE,

            // Added in 1.19.4
            DAMAGE_EVENT,
            HURT_ANIMATION,
            BUNDLE,
            CHUNK_BIOMES,

            // Added in 1.20.2
            CHUNK_BATCH_END,
            CHUNK_BATCH_BEGIN,
            DEBUG_PONG,
            CONFIGURATION_START,

            // Added in 1.20.3
            RESET_SCORE,
            RESOURCE_PACK_REMOVE,
            TICKING_STATE,
            TICKING_STEP,

            // Added in 1.20.5
            COOKIE_REQUEST,
            DEBUG_SAMPLE,
            STORE_COOKIE,
            TRANSFER,
            PROJECTILE_POWER,

            // added in 1.21
            CUSTOM_REPORT_DETAILS,
            SERVER_LINKS,

            // added in 1.21.2
            MOVE_MINECART,
            SET_CURSOR_ITEM,
            SET_PLAYER_INVENTORY,
            ENTITY_POSITION_SYNC,
            PLAYER_ROTATION,
            RECIPE_BOOK_ADD,
            RECIPE_BOOK_REMOVE,
            RECIPE_BOOK_SETTINGS,
            ;

            private static int INDEX = 0;
            private static final Map<Byte, Map<Integer, PacketTypeCommon>> PACKET_TYPE_ID_MAP = new HashMap<>();
            private final int[] ids;

            Server() {
                ids = new int[CLIENTBOUND_PLAY_VERSION_MAPPER.getVersions().length];
                Arrays.fill(ids, -1);
            }

            public int getId(ClientVersion version) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                return ids[index];
            }

            @Nullable
            public static PacketTypeCommon getById(ClientVersion version, int packetId) {
                if (!PREPARED) {
                    PacketType.prepare();
                }
                int index = CLIENTBOUND_PLAY_VERSION_MAPPER.getIndex(version);
                Map<Integer, PacketTypeCommon> map = PACKET_TYPE_ID_MAP.get((byte) index);
                return map.get(packetId);
            }

            @Override
            public PacketSide getSide() {
                return PacketSide.SERVER;
            }

            private static void loadPacketIds(Enum<?>[] enumConstants) {
                int index = INDEX;
                for (Enum<?> constant : enumConstants) {
                    int id = constant.ordinal();
                    Server value = Server.valueOf(constant.name());
                    value.ids[index] = id;
                    Map<Integer, PacketTypeCommon> packetIdMap = PACKET_TYPE_ID_MAP.computeIfAbsent((byte) index, k -> new HashMap<>());
                    packetIdMap.put(id, value);
                }
                INDEX++;
            }

            public static void load() {
                INDEX = 0;
                loadPacketIds(ClientboundPacketType_1_7_10.values());
                loadPacketIds(ClientboundPacketType_1_8.values());
                loadPacketIds(ClientboundPacketType_1_9.values());
                loadPacketIds(ClientboundPacketType_1_9_3.values());
                loadPacketIds(ClientboundPacketType_1_12.values());
                loadPacketIds(ClientboundPacketType_1_12_1.values());
                loadPacketIds(ClientboundPacketType_1_13.values());
                loadPacketIds(ClientboundPacketType_1_14.values());
                loadPacketIds(ClientboundPacketType_1_14_4.values());
                loadPacketIds(ClientboundPacketType_1_15.values());
                loadPacketIds(ClientboundPacketType_1_15_2.values());
                loadPacketIds(ClientboundPacketType_1_16.values());
                loadPacketIds(ClientboundPacketType_1_16_2.values());
                loadPacketIds(ClientboundPacketType_1_17.values());
                loadPacketIds(ClientboundPacketType_1_18.values());
                loadPacketIds(ClientboundPacketType_1_19.values());
                loadPacketIds(ClientboundPacketType_1_19_1.values());
                loadPacketIds(ClientboundPacketType_1_19_3.values());
                loadPacketIds(ClientboundPacketType_1_19_4.values());
                loadPacketIds(ClientboundPacketType_1_20_2.values());
                loadPacketIds(ClientboundPacketType_1_20_3.values());
                loadPacketIds(ClientboundPacketType_1_20_5.values());
                loadPacketIds(ClientboundPacketType_1_21.values());
                loadPacketIds(ClientboundPacketType_1_21_2.values());
                //TODO UPDATE Update packet type mappings (clientbound pt. 2)
            }
        }
    }
}
