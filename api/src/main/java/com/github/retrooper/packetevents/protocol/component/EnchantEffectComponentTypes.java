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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.protocol.component.ComponentType.Decoder;
import com.github.retrooper.packetevents.protocol.component.ComponentType.Encoder;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Contains all enchantment effect component types.
 *
 * @see ComponentTypes
 */
public final class EnchantEffectComponentTypes {

    private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry<>("enchantment_effect_component_type");

    private EnchantEffectComponentTypes() {
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key) {
        return define(key, null, null);
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable Decoder<T> reader, @Nullable Encoder<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentType<>(data, reader, writer));
    }

    public static VersionedRegistry<ComponentType<?>> getRegistry() {
        return REGISTRY;
    }

    // just pass everything through for now
    public static ComponentType<NBT> DAMAGE_PROTECTION = define("damage_protection",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> DAMAGE_IMMUNITY = define("damage_immunity",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> DAMAGE = define("damage",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> SMASH_DAMAGE_PER_FALLEN_BLOCK = define("smash_damage_per_fallen_block",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> KNOCKBACK = define("knockback",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ARMOR_EFFECTIVENESS = define("armor_effectiveness",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> POST_ATTACK = define("post_attack",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> HIT_BLOCK = define("hit_block",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ITEM_DAMAGE = define("item_damage",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> ATTRIBUTES = define("attributes",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> EQUIPMENT_DROPS = define("equipment_drops",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> LOCATION_CHANGED = define("location_changed",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TICK = define("tick",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> AMMO_USE = define("ammo_use",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_PIERCING = define("projectile_piercing",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_SPAWNED = define("projectile_spawned",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_SPREAD = define("projectile_spread",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PROJECTILE_COUNT = define("projectile_count",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_RETURN_ACCELERATION = define("trident_return_acceleration",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> FISHING_TIME_REDUCTION = define("fishing_time_reduction",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> FISHING_LUCK_BONUS = define("fishing_luck_bonus",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> BLOCK_EXPERIENCE = define("block_experience",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> MOB_EXPERIENCE = define("mob_experience",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> REPAIR_WITH_XP = define("repair_with_xp",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> CROSSBOW_CHARGE_TIME = define("crossbow_charge_time",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> CROSSBOW_CHARGING_SOUNDS = define("crossbow_charging_sounds",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_SOUND = define("trident_sound",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PREVENT_EQUIPMENT_DROP = define("prevent_equipment_drop",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> PREVENT_ARMOR_CHANGE = define("prevent_armor_change",
            (nbt, version) -> nbt, (val, version) -> val);
    public static ComponentType<NBT> TRIDENT_SPIN_ATTACK_STRENGTH = define("trident_spin_attack_strength",
            (nbt, version) -> nbt, (val, version) -> val);

    static {
        REGISTRY.unloadMappings();
    }
}
