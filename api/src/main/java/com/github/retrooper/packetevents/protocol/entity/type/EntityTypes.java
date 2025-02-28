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

package com.github.retrooper.packetevents.protocol.entity.type;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class EntityTypes {

    private static final VersionedRegistry<EntityType> REGISTRY = new VersionedRegistry<>("entity_type");
    private static final VersionedRegistry<EntityType> LEGACY_SPAWN_REGISTRY = new VersionedRegistry<>("legacy_spawn_entity_type");

    private EntityTypes() {
    }

    public static VersionedRegistry<EntityType> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Obsolete
    public static VersionedRegistry<EntityType> getLegacySpawnRegistry() {
        return LEGACY_SPAWN_REGISTRY;
    }

    @ApiStatus.Internal
    public static EntityType define(String name, @Nullable EntityType parent) {
        StaticEntityType type = REGISTRY.define(name, data ->
                new StaticEntityType(data, parent));
        return LEGACY_SPAWN_REGISTRY.define(name, type::setLegacyData);
    }

    public static boolean isTypeInstanceOf(EntityType type, EntityType parent) {
        return type != null && type.isInstanceOf(parent);
    }

    public static EntityType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static EntityType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    @ApiStatus.Obsolete
    public static EntityType getByLegacyId(ClientVersion version, int id) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_14)) {
            return null;
        }
        return LEGACY_SPAWN_REGISTRY.getById(version, id);
    }

    // Credit to ViaVersion for these categories
    public static final EntityType ENTITY = define("entity", null);
    public static final EntityType LIVINGENTITY = define("livingentity", ENTITY);
    public static final EntityType ABSTRACT_INSENTIENT = define("abstract_insentient", LIVINGENTITY);
    public static final EntityType ABSTRACT_CREATURE = define("abstract_creature", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AGEABLE = define("abstract_ageable", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_ANIMAL = define("abstract_animal", ABSTRACT_AGEABLE);
    public static final EntityType ABSTRACT_TAMEABLE_ANIMAL = define("abstract_tameable_animal", ABSTRACT_ANIMAL);
    public static final EntityType ABSTRACT_PARROT = define("abstract_parrot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ABSTRACT_HORSE = define("abstract_horse", ABSTRACT_ANIMAL);
    public static final EntityType CHESTED_HORSE = define("chested_horse", ABSTRACT_HORSE);
    public static final EntityType ABSTRACT_GOLEM = define("abstract_golem", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_FISHES = define("abstract_fishes", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_MONSTER = define("abstract_monster", ABSTRACT_CREATURE);
    public static final EntityType ABSTRACT_PIGLIN = define("abstract_piglin", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_ILLAGER_BASE = define("abstract_illager_base", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_EVO_ILLU_ILLAGER = define("abstract_evo_illu_illager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType ABSTRACT_SKELETON = define("abstract_skeleton", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_FLYING = define("abstract_flying", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_AMBIENT = define("abstract_ambient", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_WATERMOB = define("abstract_watermob", ABSTRACT_INSENTIENT);
    public static final EntityType ABSTRACT_HANGING = define("abstract_hanging", ENTITY);
    public static final EntityType ABSTRACT_LIGHTNING = define("abstract_lightning", ENTITY);
    public static final EntityType ABSTRACT_ARROW = define("abstract_arrow", ENTITY);
    public static final EntityType ABSTRACT_FIREBALL = define("abstract_fireball", ENTITY);
    public static final EntityType PROJECTILE_ABSTRACT = define("projectile_abstract", ENTITY);
    public static final EntityType MINECART_ABSTRACT = define("minecart_abstract", ENTITY);
    public static final EntityType CHESTED_MINECART_ABSTRACT = define("chested_minecart_abstract", MINECART_ABSTRACT);
    public static final EntityType AREA_EFFECT_CLOUD = define("area_effect_cloud", ENTITY);
    public static final EntityType ARMOR_STAND = define("armor_stand", LIVINGENTITY);
    public static final EntityType ALLAY = define("allay", ABSTRACT_CREATURE);
    public static final EntityType ARROW = define("arrow", ABSTRACT_ARROW);
    public static final EntityType AXOLOTL = define("axolotl", ABSTRACT_ANIMAL);
    public static final EntityType BAT = define("bat", ABSTRACT_AMBIENT);
    public static final EntityType BEE = define("bee", ABSTRACT_INSENTIENT);
    public static final EntityType BLAZE = define("blaze", ABSTRACT_MONSTER);
    /**
     * <strong>WARNING:</strong> Does not exist itself anymore since 1.21.2
     */
    public static final EntityType BOAT = define("boat", ENTITY);
    /**
     * <strong>WARNING:</strong> Does not exist itself anymore since 1.21.2
     */
    public static final EntityType CHEST_BOAT = define("chest_boat", BOAT);
    public static final EntityType CAT = define("cat", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType CAMEL = define("camel", ABSTRACT_HORSE);
    public static final EntityType SPIDER = define("spider", ABSTRACT_MONSTER);
    public static final EntityType CAVE_SPIDER = define("cave_spider", SPIDER);
    public static final EntityType CHICKEN = define("chicken", ABSTRACT_ANIMAL);
    public static final EntityType COD = define("cod", ABSTRACT_FISHES);
    public static final EntityType COW = define("cow", ABSTRACT_ANIMAL);
    public static final EntityType CREEPER = define("creeper", ABSTRACT_MONSTER);
    public static final EntityType DOLPHIN = define("dolphin", ABSTRACT_INSENTIENT);
    public static final EntityType DONKEY = define("donkey", CHESTED_HORSE);
    public static final EntityType DRAGON_FIREBALL = define("dragon_fireball", ABSTRACT_FIREBALL);
    public static final EntityType ZOMBIE = define("zombie", ABSTRACT_MONSTER);
    public static final EntityType DROWNED = define("drowned", ZOMBIE);
    public static final EntityType GUARDIAN = define("guardian", ABSTRACT_MONSTER);
    public static final EntityType ELDER_GUARDIAN = define("elder_guardian", GUARDIAN);
    public static final EntityType END_CRYSTAL = define("end_crystal", ENTITY);
    public static final EntityType ENDER_DRAGON = define("ender_dragon", ABSTRACT_INSENTIENT);
    public static final EntityType ENDERMAN = define("enderman", ABSTRACT_MONSTER);
    public static final EntityType ENDERMITE = define("endermite", ABSTRACT_MONSTER);
    public static final EntityType EVOKER = define("evoker", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType EVOKER_FANGS = define("evoker_fangs", ENTITY);
    public static final EntityType EXPERIENCE_ORB = define("experience_orb", ENTITY);
    public static final EntityType EYE_OF_ENDER = define("eye_of_ender", ENTITY);
    public static final EntityType FALLING_BLOCK = define("falling_block", ENTITY);
    public static final EntityType FIREWORK_ROCKET = define("firework_rocket", ENTITY);
    public static final EntityType FOX = define("fox", ABSTRACT_ANIMAL);
    public static final EntityType FROG = define("frog", ABSTRACT_ANIMAL);
    public static final EntityType GHAST = define("ghast", ABSTRACT_FLYING);
    public static final EntityType GIANT = define("giant", ABSTRACT_MONSTER);
    public static final EntityType ITEM_FRAME = define("item_frame", ABSTRACT_HANGING);
    public static final EntityType GLOW_ITEM_FRAME = define("glow_item_frame", ITEM_FRAME);
    public static final EntityType SQUID = define("squid", ABSTRACT_WATERMOB);
    public static final EntityType GLOW_SQUID = define("glow_squid", SQUID);
    public static final EntityType GOAT = define("goat", ABSTRACT_ANIMAL);
    public static final EntityType HOGLIN = define("hoglin", ABSTRACT_ANIMAL);
    public static final EntityType HORSE = define("horse", ABSTRACT_HORSE);
    public static final EntityType HUSK = define("husk", ZOMBIE);
    public static final EntityType ILLUSIONER = define("illusioner", ABSTRACT_EVO_ILLU_ILLAGER);
    public static final EntityType IRON_GOLEM = define("iron_golem", ABSTRACT_GOLEM);
    public static final EntityType ITEM = define("item", ENTITY);
    public static final EntityType FIREBALL = define("fireball", ABSTRACT_FIREBALL);
    public static final EntityType LEASH_KNOT = define("leash_knot", ABSTRACT_HANGING);
    public static final EntityType LIGHTNING_BOLT = define("lightning_bolt", ABSTRACT_LIGHTNING);
    public static final EntityType LLAMA = define("llama", CHESTED_HORSE);
    public static final EntityType LLAMA_SPIT = define("llama_spit", ENTITY);
    public static final EntityType SLIME = define("slime", ABSTRACT_INSENTIENT);
    public static final EntityType MAGMA_CUBE = define("magma_cube", SLIME);
    public static final EntityType MARKER = define("marker", ENTITY);
    public static final EntityType MINECART = define("minecart", MINECART_ABSTRACT);
    public static final EntityType CHEST_MINECART = define("chest_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType COMMAND_BLOCK_MINECART = define("command_block_minecart", MINECART_ABSTRACT);
    public static final EntityType FURNACE_MINECART = define("furnace_minecart", MINECART_ABSTRACT);
    public static final EntityType HOPPER_MINECART = define("hopper_minecart", CHESTED_MINECART_ABSTRACT);
    public static final EntityType SPAWNER_MINECART = define("spawner_minecart", MINECART_ABSTRACT);
    public static final EntityType TNT_MINECART = define("tnt_minecart", MINECART_ABSTRACT);
    public static final EntityType MULE = define("mule", CHESTED_HORSE);
    public static final EntityType MOOSHROOM = define("mooshroom", COW);
    public static final EntityType OCELOT = define("ocelot", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType PAINTING = define("painting", ABSTRACT_HANGING);
    public static final EntityType PANDA = define("panda", ABSTRACT_INSENTIENT);
    public static final EntityType PARROT = define("parrot", ABSTRACT_PARROT);
    public static final EntityType PHANTOM = define("phantom", ABSTRACT_FLYING);
    public static final EntityType PIG = define("pig", ABSTRACT_ANIMAL);
    public static final EntityType PIGLIN = define("piglin", ABSTRACT_PIGLIN);
    public static final EntityType PIGLIN_BRUTE = define("piglin_brute", ABSTRACT_PIGLIN);
    public static final EntityType PILLAGER = define("pillager", ABSTRACT_ILLAGER_BASE);
    public static final EntityType POLAR_BEAR = define("polar_bear", ABSTRACT_ANIMAL);
    public static final EntityType TNT = define("tnt", ENTITY);
    public static final EntityType PUFFERFISH = define("pufferfish", ABSTRACT_FISHES);
    public static final EntityType RABBIT = define("rabbit", ABSTRACT_ANIMAL);
    public static final EntityType RAVAGER = define("ravager", ABSTRACT_MONSTER);
    public static final EntityType SALMON = define("salmon", ABSTRACT_FISHES);
    public static final EntityType SHEEP = define("sheep", ABSTRACT_ANIMAL);
    public static final EntityType SHULKER = define("shulker", ABSTRACT_GOLEM); // yes this is correct
    public static final EntityType SHULKER_BULLET = define("shulker_bullet", ENTITY);
    public static final EntityType SILVERFISH = define("silverfish", ABSTRACT_MONSTER);
    public static final EntityType SKELETON = define("skeleton", ABSTRACT_SKELETON);
    public static final EntityType SKELETON_HORSE = define("skeleton_horse", ABSTRACT_HORSE);
    public static final EntityType SMALL_FIREBALL = define("small_fireball", ABSTRACT_FIREBALL);
    public static final EntityType SNOW_GOLEM = define("snow_golem", ABSTRACT_GOLEM);
    public static final EntityType SNOWBALL = define("snowball", PROJECTILE_ABSTRACT);
    public static final EntityType SPECTRAL_ARROW = define("spectral_arrow", ABSTRACT_ARROW);
    public static final EntityType STRAY = define("stray", ABSTRACT_SKELETON);
    public static final EntityType STRIDER = define("strider", ABSTRACT_ANIMAL);
    public static final EntityType EGG = define("egg", PROJECTILE_ABSTRACT);
    public static final EntityType ENDER_PEARL = define("ender_pearl", PROJECTILE_ABSTRACT);
    public static final EntityType EXPERIENCE_BOTTLE = define("experience_bottle", PROJECTILE_ABSTRACT);
    public static final EntityType POTION = define("potion", PROJECTILE_ABSTRACT);
    public static final EntityType TADPOLE = define("tadpole", ABSTRACT_FISHES);
    @Deprecated // Exists only in 1.9 and 1.10
    public static final EntityType TIPPED_ARROW = define("tipped_arrow", ARROW);
    public static final EntityType TRIDENT = define("trident", ABSTRACT_ARROW);
    public static final EntityType TRADER_LLAMA = define("trader_llama", CHESTED_HORSE);
    public static final EntityType TROPICAL_FISH = define("tropical_fish", ABSTRACT_FISHES);
    public static final EntityType TURTLE = define("turtle", ABSTRACT_ANIMAL);
    public static final EntityType VEX = define("vex", ABSTRACT_MONSTER);
    public static final EntityType VILLAGER = define("villager", ABSTRACT_AGEABLE);
    public static final EntityType VINDICATOR = define("vindicator", ABSTRACT_ILLAGER_BASE);
    public static final EntityType WANDERING_TRADER = define("wandering_trader", ABSTRACT_AGEABLE);
    public static final EntityType WARDEN = define("warden", ABSTRACT_MONSTER);
    public static final EntityType WITCH = define("witch", ABSTRACT_MONSTER);
    public static final EntityType WITHER = define("wither", ABSTRACT_MONSTER);
    public static final EntityType WITHER_SKELETON = define("wither_skeleton", ABSTRACT_SKELETON);
    public static final EntityType WITHER_SKULL = define("wither_skull", ABSTRACT_FIREBALL);
    public static final EntityType WOLF = define("wolf", ABSTRACT_TAMEABLE_ANIMAL);
    public static final EntityType ZOGLIN = define("zoglin", ABSTRACT_MONSTER);
    public static final EntityType ZOMBIE_HORSE = define("zombie_horse", ABSTRACT_HORSE);
    public static final EntityType ZOMBIE_VILLAGER = define("zombie_villager", ZOMBIE);
    public static final EntityType ZOMBIFIED_PIGLIN = define("zombified_piglin", ZOMBIE);
    public static final EntityType PLAYER = define("player", LIVINGENTITY);
    public static final EntityType FISHING_BOBBER = define("fishing_bobber", ENTITY);
    public static final EntityType ENDER_SIGNAL = define("ender_signal", ENTITY);
    public static final EntityType THROWN_EXP_BOTTLE = define("thrown_exp_bottle", PROJECTILE_ABSTRACT);
    public static final EntityType PRIMED_TNT = define("primed_tnt", ENTITY);
    public static final EntityType FIREWORK = define("firework", ENTITY);
    public static final EntityType MINECART_COMMAND = define("minecart_command", MINECART_ABSTRACT);
    public static final EntityType MINECART_RIDEABLE = define("minecart_rideable", MINECART_ABSTRACT);
    public static final EntityType MINECART_CHEST = define("minecart_chest", MINECART_ABSTRACT);
    public static final EntityType MINECART_FURNACE = define("minecart_furnace", MINECART_ABSTRACT);
    public static final EntityType MINECART_TNT = define("minecart_tnt", MINECART_ABSTRACT);
    public static final EntityType MINECART_HOPPER = define("minecart_hopper", MINECART_ABSTRACT);
    public static final EntityType MINECART_MOB_SPAWNER = define("minecart_mob_spawner", MINECART_ABSTRACT);

    // Added in 1.19.4
    public static final EntityType DISPLAY = define("display", ENTITY);
    public static final EntityType BLOCK_DISPLAY = define("block_display", DISPLAY);
    public static final EntityType ITEM_DISPLAY = define("item_display", DISPLAY);
    public static final EntityType TEXT_DISPLAY = define("text_display", DISPLAY);
    public static final EntityType INTERACTION = define("interaction", DISPLAY);
    public static final EntityType SNIFFER = define("sniffer", ABSTRACT_ANIMAL);

    // Added in 1.20.3
    public static final EntityType BREEZE = define("breeze", ABSTRACT_MONSTER);
    public static final EntityType ABSTRACT_WIND_CHARGE = define("abstract_wind_charge", PROJECTILE_ABSTRACT);
    public static final EntityType WIND_CHARGE = define("wind_charge", ABSTRACT_WIND_CHARGE);

    // Added in 1.20.5
    public static final EntityType ARMADILLO = define("armadillo", ABSTRACT_ANIMAL);
    public static final EntityType BOGGED = define("bogged", ABSTRACT_SKELETON);
    public static final EntityType BREEZE_WIND_CHARGE = define("breeze_wind_charge", ABSTRACT_WIND_CHARGE);
    public static final EntityType OMINOUS_ITEM_SPAWNER = define("ominous_item_spawner", ENTITY);

    // added with 1.21.2
    public static final EntityType ACACIA_BOAT = define("acacia_boat", BOAT);
    public static final EntityType ACACIA_CHEST_BOAT = define("acacia_chest_boat", CHEST_BOAT);
    public static final EntityType BAMBOO_CHEST_RAFT = define("bamboo_chest_raft", CHEST_BOAT);
    public static final EntityType BAMBOO_RAFT = define("bamboo_raft", BOAT);
    public static final EntityType BIRCH_BOAT = define("birch_boat", BOAT);
    public static final EntityType BIRCH_CHEST_BOAT = define("birch_chest_boat", CHEST_BOAT);
    public static final EntityType CHERRY_BOAT = define("cherry_boat", BOAT);
    public static final EntityType CHERRY_CHEST_BOAT = define("cherry_chest_boat", CHEST_BOAT);
    public static final EntityType CREAKING = define("creaking", ABSTRACT_MONSTER);
    /**
     * Removed with 1.21.4
     */
    @ApiStatus.Obsolete
    public static final EntityType CREAKING_TRANSIENT = define("creaking_transient", CREAKING);
    public static final EntityType DARK_OAK_BOAT = define("dark_oak_boat", BOAT);
    public static final EntityType DARK_OAK_CHEST_BOAT = define("dark_oak_chest_boat", CHEST_BOAT);
    public static final EntityType JUNGLE_BOAT = define("jungle_boat", BOAT);
    public static final EntityType JUNGLE_CHEST_BOAT = define("jungle_chest_boat", CHEST_BOAT);
    public static final EntityType MANGROVE_BOAT = define("mangrove_boat", BOAT);
    public static final EntityType MANGROVE_CHEST_BOAT = define("mangrove_chest_boat", CHEST_BOAT);
    public static final EntityType OAK_BOAT = define("oak_boat", BOAT);
    public static final EntityType OAK_CHEST_BOAT = define("oak_chest_boat", CHEST_BOAT);
    public static final EntityType PALE_OAK_BOAT = define("pale_oak_boat", BOAT);
    public static final EntityType PALE_OAK_CHEST_BOAT = define("pale_oak_chest_boat", CHEST_BOAT);
    public static final EntityType SPRUCE_BOAT = define("spruce_boat", BOAT);
    public static final EntityType SPRUCE_CHEST_BOAT = define("spruce_chest_boat", CHEST_BOAT);

    /**
     * Returns an immutable view of the entity types.
     *
     * @return Entity Types
     */
    public static Collection<EntityType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
        LEGACY_SPAWN_REGISTRY.unloadMappings();
    }
}
