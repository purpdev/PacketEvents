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

package com.github.retrooper.packetevents.protocol.potion;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Potion types are the individually applied potion effects.<br>
 * Some examples are speed, poison and blindness.
 * <p>
 * For potions brewable in survival, see {@link Potions}.
 */
public final class PotionTypes {

    private static final VersionedRegistry<PotionType> REGISTRY = new VersionedRegistry<>("mob_effect");

    private PotionTypes() {
    }

    public static VersionedRegistry<PotionType> getRegistry() {
        return REGISTRY;
    }

    @Deprecated
    public static PotionType define(String name, int ignoredId) {
        return define(name);
    }

    @ApiStatus.Internal
    public static PotionType define(String name) {
        return REGISTRY.define(name, StaticPotionType::new);
    }

    public static @Nullable PotionType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    @Deprecated
    public static @Nullable PotionType getById(int id) {
        return getById(id, PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    @Deprecated
    public static @Nullable PotionType getById(int id, ServerVersion version) {
        return getById(id, version.toClientVersion());
    }

    @Deprecated
    public static @Nullable PotionType getById(int id, ClientVersion version) {
        return getById(version, id);
    }

    public static @Nullable PotionType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    // initial mappings based upon https://minecraft.wiki/w/Effect#History
    // Added in b1.8
    public static final PotionType SPEED = define("speed");
    public static final PotionType SLOWNESS = define("slowness");
    public static final PotionType HASTE = define("haste");
    public static final PotionType MINING_FATIGUE = define("mining_fatigue");
    public static final PotionType STRENGTH = define("strength");
    public static final PotionType INSTANT_HEALTH = define("instant_health");
    public static final PotionType INSTANT_DAMAGE = define("instant_damage");
    public static final PotionType JUMP_BOOST = define("jump_boost");
    public static final PotionType NAUSEA = define("nausea");
    public static final PotionType REGENERATION = define("regeneration");
    public static final PotionType RESISTANCE = define("resistance");
    public static final PotionType FIRE_RESISTANCE = define("fire_resistance");
    public static final PotionType WATER_BREATHING = define("water_breathing");
    public static final PotionType INVISIBILITY = define("invisibility");
    public static final PotionType BLINDNESS = define("blindness");
    public static final PotionType NIGHT_VISION = define("night_vision");
    public static final PotionType HUNGER = define("hunger");
    public static final PotionType WEAKNESS = define("weakness");
    public static final PotionType POISON = define("poison");

    // Added in 1.4.2
    public static final PotionType WITHER = define("wither");

    // Added in 1.6.1
    public static final PotionType HEALTH_BOOST = define("health_boost");
    public static final PotionType ABSORPTION = define("absorption");
    public static final PotionType SATURATION = define("saturation");

    // Added in 1.9
    public static final PotionType GLOWING = define("glowing");
    public static final PotionType LEVITATION = define("levitation");
    public static final PotionType LUCK = define("luck");
    public static final PotionType UNLUCK = define("unluck");

    // Added in 1.13
    public static final PotionType SLOW_FALLING = define("slow_falling");
    public static final PotionType CONDUIT_POWER = define("conduit_power");
    public static final PotionType DOLPHINS_GRACE = define("dolphins_grace");

    // Added in 1.14
    public static final PotionType BAD_OMEN = define("bad_omen");
    public static final PotionType HERO_OF_THE_VILLAGE = define("hero_of_the_village");

    // Added in 1.19
    public static final PotionType DARKNESS = define("darkness");

    // Added in 1.20.5
    public static final PotionType TRIAL_OMEN = define("trial_omen");
    public static final PotionType RAID_OMEN = define("raid_omen");
    public static final PotionType WIND_CHARGED = define("wind_charged");
    public static final PotionType WEAVING = define("weaving");
    public static final PotionType OOZING = define("oozing");
    public static final PotionType INFESTED = define("infested");

    /**
     * Returns an immutable view of the potion types.
     *
     * @return Potion Types
     */
    public static Collection<PotionType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
