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

package com.github.retrooper.packetevents.protocol.world.damagetype;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

public final class DamageTypes {

    private static final VersionedRegistry<DamageType> REGISTRY = new VersionedRegistry<>("damage_type");

    private DamageTypes() {
    }

    @ApiStatus.Internal
    public static DamageType define(String key, String messageId, float exhaustion) {
        return define(key, messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, DamageEffects.HURT,
                DeathMessageType.DEFAULT);
    }

    @ApiStatus.Internal
    public static DamageType define(String key, String messageId, float exhaustion, DamageEffects damageEffects) {
        return define(key, messageId, DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, exhaustion, damageEffects,
                DeathMessageType.DEFAULT);
    }

    @ApiStatus.Internal
    public static DamageType define(String key, String messageId, DamageScaling scaling, float exhaustion) {
        return define(key, messageId, scaling, exhaustion, DamageEffects.HURT, DeathMessageType.DEFAULT);
    }

    @ApiStatus.Internal
    public static DamageType define(String key, String messageId, DamageScaling scaling, float exhaustion,
                                    DamageEffects damageEffects, DeathMessageType deathMessageType) {
        return REGISTRY.define(key,
                data -> new StaticDamageType(data, messageId, scaling, exhaustion, damageEffects, deathMessageType));
    }

    public static DamageType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static DamageType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static VersionedRegistry<DamageType> getRegistry() {
        return REGISTRY;
    }

    public static final DamageType ARROW = define("arrow", "arrow", 0.1F);
    public static final DamageType BAD_RESPAWN_POINT = define("bad_respawn_point", "badRespawnPoint",
            DamageScaling.ALWAYS, 0.1F, DamageEffects.HURT, DeathMessageType.INTENTIONAL_GAME_DESIGN);
    public static final DamageType CACTUS = define("cactus", "cactus", 0.1F);
    public static final DamageType CAMPFIRE = define("campfire", "inFire", 0.1F, DamageEffects.BURNING);
    public static final DamageType CRAMMING = define("cramming", "cramming", 0.0F);
    public static final DamageType DRAGON_BREATH = define("dragon_breath", "dragonBreath", 0.0F);
    public static final DamageType DROWN = define("drown", "drown", 0.0F, DamageEffects.DROWNING);
    public static final DamageType DRY_OUT = define("dry_out", "dryout", 0.1F);
    public static final DamageType EXPLOSION = define("explosion", "explosion", DamageScaling.ALWAYS, 0.1F);
    public static final DamageType FALL = define("fall", "fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F,
            DamageEffects.HURT, DeathMessageType.FALL_VARIANTS);
    public static final DamageType FALLING_ANVIL = define("falling_anvil", "anvil", 0.1F);
    public static final DamageType FALLING_BLOCK = define("falling_block", "fallingBlock", 0.1F);
    public static final DamageType FALLING_STALACTITE = define("falling_stalactite", "fallingStalactite", 0.1F);
    public static final DamageType FELL_OUT_OF_WORLD = define("out_of_world", "outOfWorld", 0.0F);
    public static final DamageType FIREBALL = define("fireball", "fireball", 0.1F, DamageEffects.BURNING);
    public static final DamageType FIREWORKS = define("fireworks", "fireworks", 0.1F);
    public static final DamageType FLY_INTO_WALL = define("fly_into_wall", "flyIntoWall", 0.0F);
    public static final DamageType FREEZE = define("freeze", "freeze", 0.0F, DamageEffects.FREEZING);
    public static final DamageType GENERIC = define("generic", "generic", 0.0F);
    public static final DamageType GENERIC_KILL = define("generic_kill", "genericKill", 0.0F);
    public static final DamageType HOT_FLOOR = define("hot_floor", "hotFloor", 0.1F, DamageEffects.BURNING);
    public static final DamageType IN_FIRE = define("in_fire", "inFire", 0.1F, DamageEffects.BURNING);
    public static final DamageType IN_WALL = define("in_wall", "inWall", 0.0F);
    public static final DamageType INDIRECT_MAGIC = define("indirect_magic", "indirectMagic", 0.0F);
    public static final DamageType LAVA = define("lava", "lava", 0.1F, DamageEffects.BURNING);
    public static final DamageType LIGHTNING_BOLT = define("lightning_bolt", "lightningBolt", 0.1F);
    public static final DamageType MAGIC = define("magic", "magic", 0.0F);
    public static final DamageType MOB_ATTACK = define("mob_attack", "mob", 0.1F);
    public static final DamageType MOB_ATTACK_NO_AGGRO = define("mob_attack_no_aggro", "mob", 0.1F);
    public static final DamageType MOB_PROJECTILE = define("mob_projectile", "mob", 0.1F);
    public static final DamageType ON_FIRE = define("on_fire", "onFire", 0.0F, DamageEffects.BURNING);
    public static final DamageType OUTSIDE_BORDER = define("outside_border", "outsideBorder", 0.0F);
    public static final DamageType PLAYER_ATTACK = define("player_attack", "player", 0.1F);
    public static final DamageType PLAYER_EXPLOSION = define("player_explosion", "explosion.player",
            DamageScaling.ALWAYS, 0.1F);
    public static final DamageType SONIC_BOOM = define("sonic_boom", "sonic_boom", DamageScaling.ALWAYS, 0.0F);
    public static final DamageType SPIT = define("spit", "mob", 0.1F);
    public static final DamageType STALAGMITE = define("stalagmite", "stalagmite", 0.0F);
    public static final DamageType STARVE = define("starve", "starve", 0.0F);
    public static final DamageType STING = define("sting", "sting", 0.1F);
    public static final DamageType SWEET_BERRY_BUSH = define("sweet_berry_bush", "sweetBerryBush", 0.1F,
            DamageEffects.POKING);
    public static final DamageType THORNS = define("thorns", "thorns", 0.1F, DamageEffects.THORNS);
    public static final DamageType THROWN = define("thrown", "thrown", 0.1F);
    public static final DamageType TRIDENT = define("trident", "trident", 0.1F);
    public static final DamageType UNATTRIBUTED_FIREBALL = define("unattributed_fireball", "onFire", 0.1F,
            DamageEffects.BURNING);
    public static final DamageType WIND_CHARGE = define("wind_charge", "mob", 0.1F);
    public static final DamageType WITHER = define("wither", "wither", 0.0F);
    public static final DamageType WITHER_SKULL = define("wither_skull", "witherSkull", 0.1F);

    // added with 1.21.2
    public static final DamageType ENDER_PEARL = define("ender_pearl", "fall", DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
            0.0F, DamageEffects.HURT, DeathMessageType.FALL_VARIANTS);
    public static final DamageType MACE_SMASH = define("mace_smash", "mace_smash", 0.1F);

    /**
     * Returns an immutable view of the damagetypes.
     *
     * @return DamageTypes
     */
    public static Collection<DamageType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
