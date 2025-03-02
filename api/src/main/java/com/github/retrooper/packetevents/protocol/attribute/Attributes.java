/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2023 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.attribute;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class Attributes {

    private static final VersionedRegistry<Attribute> REGISTRY = new VersionedRegistry<>("attribute");

    private Attributes() {
    }

    private static Attribute define(
            String key, @Nullable String legacyPrefix,
            double def, double min, double max
    ) {
        return REGISTRY.define(key, data ->
                new StaticAttribute(data, legacyPrefix, def, min, max));
    }

    public static VersionedRegistry<Attribute> getRegistry() {
        return REGISTRY;
    }

    public static Attribute getByName(String name) {
        // "remapping" to support < 1.21.2
        String normedName = ResourceLocation.normString(name);
        if (normedName.startsWith(ResourceLocation.VANILLA_NAMESPACE + ":generic.")
                || normedName.startsWith(ResourceLocation.VANILLA_NAMESPACE + ":player.")
                || normedName.startsWith(ResourceLocation.VANILLA_NAMESPACE + ":zombie.")) {
            normedName = normedName.substring(name.indexOf('.') + 1);
        }
        return REGISTRY.getByName(normedName);
    }

    public static Attribute getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final Attribute ARMOR = define("armor",
            "generic", 0d, 0d, 30d);
    public static final Attribute ARMOR_TOUGHNESS = define("armor_toughness",
            "generic", 0d, 0d, 20d);
    public static final Attribute ATTACK_DAMAGE = define("attack_damage",
            "generic", 2d, 0d, 2048d);
    public static final Attribute ATTACK_KNOCKBACK = define("attack_knockback",
            "generic", 0d, 0d, 5d);
    public static final Attribute ATTACK_SPEED = define("attack_speed",
            "generic", 4d, 0d, 1024d);
    public static final Attribute FLYING_SPEED = define("flying_speed",
            "generic", 0.4d, 0d, 1024d);
    public static final Attribute FOLLOW_RANGE = define("follow_range",
            "generic", 32d, 0d, 2048d);
    public static final Attribute KNOCKBACK_RESISTANCE = define("knockback_resistance",
            "generic", 0d, 0d, 1d);
    public static final Attribute LUCK = define("luck",
            "generic", 0d, -1024d, 1024d);
    public static final Attribute MAX_HEALTH = define("max_health",
            "generic", 20d, 1d, 1024d);
    public static final Attribute MOVEMENT_SPEED = define("movement_speed",
            "generic", 0.7d, 0d, 1024d);
    public static final Attribute SPAWN_REINFORCEMENTS = define("spawn_reinforcements",
            "zombie", 0d, 0d, 1d);

    /**
     * Added with 1.20.2
     */
    public static final Attribute MAX_ABSORPTION = define("max_absorption",
            "generic", 0d, 0d, 2048d);

    /**
     * Added with 1.20.5
     */
    public static final Attribute BLOCK_BREAK_SPEED = define("block_break_speed",
            "player", 1d, 0d, 1024d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute BLOCK_INTERACTION_RANGE = define("block_interaction_range",
            "player", 4.5d, 0d, 64d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute ENTITY_INTERACTION_RANGE = define("entity_interaction_range",
            "player", 3d, 0d, 64d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute FALL_DAMAGE_MULTIPLIER = define("fall_damage_multiplier",
            "generic", 1d, 0d, 100d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute GRAVITY = define("gravity",
            "generic", 0.08d, -1d, 1d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute JUMP_STRENGTH = define("jump_strength",
            "generic", 0.42d, 0d, 32d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute SAFE_FALL_DISTANCE = define("safe_fall_distance",
            "generic", 3d, 0d, 1024d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute SCALE = define("scale",
            "generic", 1d, 1d / 16d, 16d);
    /**
     * Added with 1.20.5
     */
    public static final Attribute STEP_HEIGHT = define("step_height",
            "generic", 0.6d, 0d, 10d);

    /**
     * Added with 1.21
     */
    public static final Attribute BURNING_TIME = define("burning_time",
            "generic", 0d, 1d, 1024d);
    /**
     * Added with 1.21
     */
    public static final Attribute EXPLOSION_KNOCKBACK_RESISTANCE = define("explosion_knockback_resistance",
            "generic", 0d, 0d, 1d);
    /**
     * Added with 1.21
     */
    public static final Attribute MINING_EFFICIENCY = define("mining_efficiency",
            "player", 0d, 0d, 1024d);
    /**
     * Added with 1.21
     */
    public static final Attribute MOVEMENT_EFFICIENCY = define("movement_efficiency",
            "generic", 0d, 0d, 1d);
    /**
     * Added with 1.21
     */
    public static final Attribute OXYGEN_BONUS = define("oxygen_bonus",
            "generic", 0d, 0d, 1024d);
    /**
     * Added with 1.21
     */
    public static final Attribute SNEAKING_SPEED = define("sneaking_speed",
            "player", 0.3d, 0d, 1d);
    /**
     * Added with 1.21
     */
    public static final Attribute SUBMERGED_MINING_SPEED = define("submerged_mining_speed",
            "player", 0.2d, 0d, 20d);
    /**
     * Added with 1.21
     */
    public static final Attribute SWEEPING_DAMAGE_RATIO = define("sweeping_damage_ratio",
            "player", 0d, 0d, 1d);
    /**
     * Added with 1.21
     */
    public static final Attribute WATER_MOVEMENT_EFFICIENCY = define("water_movement_efficiency",
            "generic", 0d, 0d, 1d);

    /**
     * Added with 1.21.2
     */
    public static final Attribute TEMPT_RANGE = define("tempt_range",
            null, 10d, 0d, 2048d);

    /**
     * This attribute has been renamed in 1.20.5 to {@link #JUMP_STRENGTH}
     */
    @ApiStatus.Obsolete
    public static final Attribute HORSE_JUMP_STRENGTH = define("horse.jump_strength",
            null, 0.7d, 0d, 2d);

    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_ARMOR = ARMOR;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_ARMOR_TOUGHNESS = ARMOR_TOUGHNESS;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_ATTACK_DAMAGE = ATTACK_DAMAGE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_ATTACK_KNOCKBACK = ATTACK_KNOCKBACK;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_ATTACK_SPEED = ATTACK_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_FLYING_SPEED = FLYING_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_FOLLOW_RANGE = FOLLOW_RANGE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_KNOCKBACK_RESISTANCE = KNOCKBACK_RESISTANCE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_LUCK = LUCK;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_MAX_HEALTH = MAX_HEALTH;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_MOVEMENT_SPEED = MOVEMENT_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute ZOMBIE_SPAWN_REINFORCEMENTS = SPAWN_REINFORCEMENTS;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_MAX_ABSORPTION = MAX_ABSORPTION;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_BLOCK_BREAK_SPEED = BLOCK_BREAK_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_BLOCK_INTERACTION_RANGE = BLOCK_INTERACTION_RANGE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_ENTITY_INTERACTION_RANGE = ENTITY_INTERACTION_RANGE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_FALL_DAMAGE_MULTIPLIER = FALL_DAMAGE_MULTIPLIER;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_GRAVITY = GRAVITY;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_JUMP_STRENGTH = JUMP_STRENGTH;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_SAFE_FALL_DISTANCE = SAFE_FALL_DISTANCE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_SCALE = SCALE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_STEP_HEIGHT = STEP_HEIGHT;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_BURNING_TIME = BURNING_TIME;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_EXPLOSION_KNOCKBACK_RESISTANCE = EXPLOSION_KNOCKBACK_RESISTANCE;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_MINING_EFFICIENCY = MINING_EFFICIENCY;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_MOVEMENT_EFFICIENCY = MOVEMENT_EFFICIENCY;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_OXYGEN_BONUS = OXYGEN_BONUS;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_SNEAKING_SPEED = SNEAKING_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_SUBMERGED_MINING_SPEED = SUBMERGED_MINING_SPEED;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute PLAYER_SWEEPING_DAMAGE_RATIO = SWEEPING_DAMAGE_RATIO;
    /**
     * All attribute "type" prefixes were removed with 1.21.2
     */
    @Deprecated
    public static final Attribute GENERIC_WATER_MOVEMENT_EFFICIENCY = WATER_MOVEMENT_EFFICIENCY;

    static {
        REGISTRY.unloadMappings();
    }
}
