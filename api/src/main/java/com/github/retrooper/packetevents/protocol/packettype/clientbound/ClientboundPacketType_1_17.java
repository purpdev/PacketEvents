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

package com.github.retrooper.packetevents.protocol.packettype.clientbound;

public enum ClientboundPacketType_1_17 {
    SPAWN_ENTITY,
    SPAWN_EXPERIENCE_ORB,
    SPAWN_LIVING_ENTITY,
    SPAWN_PAINTING,
    SPAWN_PLAYER,

    //This packet was added
    SCULK_VIBRATION_SIGNAL,

    ENTITY_ANIMATION,
    STATISTICS,
    ACKNOWLEDGE_PLAYER_DIGGING,
    BLOCK_BREAK_ANIMATION,
    BLOCK_ENTITY_DATA,
    BLOCK_ACTION,
    BLOCK_CHANGE,
    BOSS_BAR,
    SERVER_DIFFICULTY,
    CHAT_MESSAGE,

    //This packet was added
    CLEAR_TITLES,

    TAB_COMPLETE,

    DECLARE_COMMANDS,

    //This packet was removed and replaced with the PING packet
    //WINDOW_CONFIRMATION,

    CLOSE_WINDOW,
    WINDOW_ITEMS,
    WINDOW_PROPERTY,
    SET_SLOT,
    SET_COOLDOWN,
    PLUGIN_MESSAGE,
    CUSTOM_SOUND_EFFECT,
    DISCONNECT,
    ENTITY_STATUS,
    EXPLOSION,
    UNLOAD_CHUNK,
    CHANGE_GAME_STATE,
    OPEN_HORSE_WINDOW,

    //This packet was added
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

    //This packet was removed
    //ENTITY_MOVEMENT,

    VEHICLE_MOVE,
    OPEN_BOOK,
    OPEN_WINDOW,
    OPEN_SIGN_EDITOR,

    //This packet was added
    PING,

    CRAFT_RECIPE_RESPONSE,
    PLAYER_ABILITIES,

    //This packet was removed and split up into 3 new packets
    //COMBAT_EVENT,

    //These 3 new packets were added
    END_COMBAT_EVENT,
    ENTER_COMBAT_EVENT,
    DEATH_COMBAT_EVENT,

    PLAYER_INFO,
    FACE_PLAYER,
    PLAYER_POSITION_AND_LOOK,
    UNLOCK_RECIPES,

    //In 1.17 you can only destroy ONE ENTITY now
    DESTROY_ENTITIES,

    REMOVE_ENTITY_EFFECT,
    RESOURCE_PACK_SEND,
    RESPAWN,
    ENTITY_HEAD_LOOK,
    MULTI_BLOCK_CHANGE,
    SELECT_ADVANCEMENTS_TAB,

    //These 5 new packets were added
    ACTION_BAR,
    WORLD_BORDER_CENTER,
    WORLD_BORDER_LERP_SIZE,
    WORLD_BORDER_SIZE,
    WORLD_BORDER_WARNING_DELAY,
    WORLD_BORDER_WARNING_REACH,

    //This packet was removed
    //WORLD_BORDER,

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

    //This new packet was added
    SET_TITLE_SUBTITLE,

    TIME_UPDATE,

    //These 2 new packets were added
    SET_TITLE_TEXT,
    SET_TITLE_TIMES,


    //This packet was removed and split up into 3 new packets
    //TITLE,

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
    TAGS
}
