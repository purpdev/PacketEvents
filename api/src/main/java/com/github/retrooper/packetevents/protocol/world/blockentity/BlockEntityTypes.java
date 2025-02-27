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

package com.github.retrooper.packetevents.protocol.world.blockentity;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;

import java.util.Collection;

public final class BlockEntityTypes {

    private static final VersionedRegistry<BlockEntityType> REGISTRY = new VersionedRegistry<>("block_entity_type");

    private BlockEntityTypes() {
    }

    private static BlockEntityType define(String key) {
        return REGISTRY.define(key, StaticBlockEntityType::new);
    }

    public static VersionedRegistry<BlockEntityType> getRegistry() {
        return REGISTRY;
    }

    public static BlockEntityType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static BlockEntityType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final BlockEntityType FURNACE = define("furnace");
    public static final BlockEntityType CHEST = define("chest");
    public static final BlockEntityType TRAPPED_CHEST = define("trapped_chest");
    public static final BlockEntityType ENDER_CHEST = define("ender_chest");
    public static final BlockEntityType JUKEBOX = define("jukebox");
    public static final BlockEntityType DISPENSER = define("dispenser");
    public static final BlockEntityType DROPPER = define("dropper");
    public static final BlockEntityType SIGN = define("sign");
    public static final BlockEntityType HANGING_SIGN = define("hanging_sign");
    public static final BlockEntityType MOB_SPAWNER = define("mob_spawner");
    public static final BlockEntityType PISTON = define("piston");
    public static final BlockEntityType BREWING_STAND = define("brewing_stand");
    public static final BlockEntityType ENCHANTING_TABLE = define("enchanting_table");
    public static final BlockEntityType END_PORTAL = define("end_portal");
    public static final BlockEntityType BEACON = define("beacon");
    public static final BlockEntityType SKULL = define("skull");
    public static final BlockEntityType DAYLIGHT_DETECTOR = define("daylight_detector");
    public static final BlockEntityType HOPPER = define("hopper");
    public static final BlockEntityType COMPARATOR = define("comparator");
    public static final BlockEntityType BANNER = define("banner");
    public static final BlockEntityType STRUCTURE_BLOCK = define("structure_block");
    public static final BlockEntityType END_GATEWAY = define("end_gateway");
    public static final BlockEntityType COMMAND_BLOCK = define("command_block");
    public static final BlockEntityType SHULKER_BOX = define("shulker_box");
    public static final BlockEntityType BED = define("bed");
    public static final BlockEntityType CONDUIT = define("conduit");
    public static final BlockEntityType BARREL = define("barrel");
    public static final BlockEntityType SMOKER = define("smoker");
    public static final BlockEntityType BLAST_FURNACE = define("blast_furnace");
    public static final BlockEntityType LECTERN = define("lectern");
    public static final BlockEntityType BELL = define("bell");
    public static final BlockEntityType JIGSAW = define("jigsaw");
    public static final BlockEntityType CAMPFIRE = define("campfire");
    public static final BlockEntityType BEEHIVE = define("beehive");
    public static final BlockEntityType SCULK_SENSOR = define("sculk_sensor");
    public static final BlockEntityType CALIBRATED_SCULK_SENSOR = define("calibrated_sculk_sensor");
    public static final BlockEntityType SCULK_CATALYST = define("sculk_catalyst");
    public static final BlockEntityType SCULK_SHRIEKER = define("sculk_shrieker");
    public static final BlockEntityType CHISELED_BOOKSHELF = define("chiseled_bookshelf");
    public static final BlockEntityType BRUSHABLE_BLOCK = define("brushable_block");
    public static final BlockEntityType DECORATED_POT = define("decorated_pot");
    public static final BlockEntityType CRAFTER = define("crafter");
    public static final BlockEntityType TRIAL_SPAWNER = define("trial_spawner");
    public static final BlockEntityType VAULT = define("vault");

    // added with 1.21.2
    public static final BlockEntityType CREAKING_HEART = define("creaking_heart");

    /**
     * Returns an immutable view of the block entity types.
     *
     * @return Block Entity Types
     */
    public static Collection<BlockEntityType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
