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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Potions are all possible brewable drinks.<br>
 * Some examples are the potion of leaping, the extended potion of leaping
 * or the strong potion of leaping.
 * <p>
 * For individual potion effects (also called "mob effects"), see {@link PotionTypes}.
 */
public final class Potions {

    private static final VersionedRegistry<Potion> REGISTRY = new VersionedRegistry<>("potion");

    private Potions() {
    }

    public static VersionedRegistry<Potion> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static Potion define(String name) {
        return REGISTRY.define(name, StaticPotion::new);
    }

    public static @Nullable Potion getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static @Nullable Potion getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    // initial mappings based upon https://minecraft.wiki/w/Potion#History
    public static final Potion WATER = define("water");
    public static final Potion MUNDANE = define("mundane");
    public static final Potion THICK = define("thick");
    public static final Potion AWKWARD = define("awkward");
    public static final Potion NIGHT_VISION = define("night_vision");
    public static final Potion LONG_NIGHT_VISION = define("long_night_vision");
    public static final Potion INVISIBILITY = define("invisibility");
    public static final Potion LONG_INVISIBILITY = define("long_invisibility");
    public static final Potion LEAPING = define("leaping");
    public static final Potion LONG_LEAPING = define("long_leaping");
    public static final Potion STRONG_LEAPING = define("strong_leaping");
    public static final Potion FIRE_RESISTANCE = define("fire_resistance");
    public static final Potion LONG_FIRE_RESISTANCE = define("long_fire_resistance");
    public static final Potion SWIFTNESS = define("swiftness");
    public static final Potion LONG_SWIFTNESS = define("long_swiftness");
    public static final Potion STRONG_SWIFTNESS = define("strong_swiftness");
    public static final Potion SLOWNESS = define("slowness");
    public static final Potion LONG_SLOWNESS = define("long_slowness");
    public static final Potion STRONG_SLOWNESS = define("strong_slowness");
    public static final Potion TURTLE_MASTER = define("turtle_master");
    public static final Potion LONG_TURTLE_MASTER = define("long_turtle_master");
    public static final Potion STRONG_TURTLE_MASTER = define("strong_turtle_master");
    public static final Potion WATER_BREATHING = define("water_breathing");
    public static final Potion LONG_WATER_BREATHING = define("long_water_breathing");
    public static final Potion HEALING = define("healing");
    public static final Potion STRONG_HEALING = define("strong_healing");
    public static final Potion HARMING = define("harming");
    public static final Potion STRONG_HARMING = define("strong_harming");
    public static final Potion POISON = define("poison");
    public static final Potion LONG_POISON = define("long_poison");
    public static final Potion STRONG_POISON = define("strong_poison");
    public static final Potion REGENERATION = define("regeneration");
    public static final Potion LONG_REGENERATION = define("long_regeneration");
    public static final Potion STRONG_REGENERATION = define("strong_regeneration");
    public static final Potion STRENGTH = define("strength");
    public static final Potion LONG_STRENGTH = define("long_strength");
    public static final Potion STRONG_STRENGTH = define("strong_strength");
    public static final Potion WEAKNESS = define("weakness");
    public static final Potion LONG_WEAKNESS = define("long_weakness");
    public static final Potion LUCK = define("luck");
    public static final Potion SLOW_FALLING = define("slow_falling");
    public static final Potion LONG_SLOW_FALLING = define("long_slow_falling");
    public static final Potion WIND_CHARGED = define("wind_charged");
    public static final Potion WEAVING = define("weaving");
    public static final Potion OOZING = define("oozing");
    public static final Potion INFESTED = define("infested");

    static {
        REGISTRY.unloadMappings();
    }
}
