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

package com.github.retrooper.packetevents.protocol.effect.type;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.TypesBuilder;
import com.github.retrooper.packetevents.util.TypesBuilderData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Effects {
    private static final Map<String, Effect> EFFECT_TYPE_MAPPINGS;
    private static final Map<Byte, Map<Integer, Effect>> EFFECT_TYPE_ID_MAPPINGS;
    private static final TypesBuilder TYPES_BUILDER;

    static {
        EFFECT_TYPE_MAPPINGS = new HashMap<>();
        EFFECT_TYPE_ID_MAPPINGS = new HashMap<>();
        TYPES_BUILDER = new TypesBuilder("effect/effect_type_mappings",
                ClientVersion.V_1_8,
                ClientVersion.V_1_9,
                ClientVersion.V_1_15,
                ClientVersion.V_1_16,
                ClientVersion.V_1_17);
    }

    public static Effect define(@NotNull final String key) {
        final TypesBuilderData data = TYPES_BUILDER.define(key);
        final Effect effect = new Effect() {
            @Override
            public ResourceLocation getName() {
                return data.getName();
            }

            @Override
            public int getId(ClientVersion version) {
                final int index = TYPES_BUILDER.getDataIndex(version);
                return data.getData()[index];
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof Effect) {
                    return getName() == ((Effect) obj).getName();
                }
                return false;
            }
        };

        EFFECT_TYPE_MAPPINGS.put(effect.getName().getKey(), effect);
        for (ClientVersion version : TYPES_BUILDER.getVersions()) {
            final int index = TYPES_BUILDER.getDataIndex(version);
            final Map<Integer, Effect> typeIdMap = EFFECT_TYPE_ID_MAPPINGS.computeIfAbsent((byte) index, k -> new HashMap<>());
            typeIdMap.put(effect.getId(version), effect);
        }
        return effect;
    }

    @Nullable
    public static Effect getByName(@NotNull final String name) {
        return EFFECT_TYPE_MAPPINGS.get(name);
    }

    @Nullable
    public static Effect getById(@NotNull final ClientVersion version, final int id) {
        final int index = TYPES_BUILDER.getDataIndex(version);
        final Map<Integer, Effect> typeIdMap = EFFECT_TYPE_ID_MAPPINGS.get((byte) index);
        return typeIdMap.get(id);
    }

    // 1.8
    public static final Effect CLICK2 = define("click2");
    public static final Effect CLICK1 = define("click1");
    public static final Effect BOW_FIRE = define("bow_fire");
    public static final Effect DOOR_TOGGLE = define("door_toggle");
    public static final Effect EXTINGUISH = define("extinguish");
    public static final Effect RECORD_PLAY = define("record_play");
    public static final Effect GHAST_SHRIEK = define("ghast_shriek");
    public static final Effect GHAST_SHOOT = define("ghast_shoot");
    public static final Effect BALZE_SHOOT = define("blaze_shoot");
    public static final Effect ZOMBIE_CHEW_WOODEN_DOOR = define("zombie_chew_wooden_door");
    public static final Effect ZOMBIE_CHEW_IRON_DOOR = define("zombie_chew_iron_door");
    public static final Effect ZOMBIE_DESTROY_DOOR = define("zombie_destroy_door");
    public static final Effect SMOKE = define("smoke");
    public static final Effect STEP_SOUND = define("step_sound");
    public static final Effect POTION_BREAK = define("potion_break");
    public static final Effect ENDER_SIGNAL = define("ender_signal");
    public static final Effect MOBSPAWNER_FLAMES = define("mobspawner_flames");

    // 1.9
    public static final Effect IRON_DOOR_TOGGLE = define("iron_door_toggle");
    public static final Effect TRAPDOOR_TOGGLE = define("trapdoor_toggle");
    public static final Effect IRON_TRAPDOOR_TOGGLE = define("iron_trapdoor_toggle");
    public static final Effect FENCE_GATE_TOGGLE = define("fence_gate_toggle");
    public static final Effect DOOR_CLOSE = define("door_close");
    public static final Effect IRON_DOOR_CLOSE = define("iron_door_close");
    public static final Effect TRAPDOOR_CLOSE = define("trapdoor_close");
    public static final Effect IRON_TRAPDOOR_CLOSE = define("iron_trapdoor_close");
    public static final Effect FENCE_GATE_CLOSE = define("fence_gate_close");
    public static final Effect BREWING_STAND_BREW = define("brewing_stand_brew");
    public static final Effect CHORUS_FLOWER_GROW = define("chorus_flower_grow");
    public static final Effect CHORUS_FLOWER_DEATH = define("chorus_flower_death");
    public static final Effect PORTAL_TRAVEL = define("portal_travel");
    public static final Effect ENDEREYE_LAUNCH = define("endereye_launch");
    public static final Effect FIREWORK_SHOOT = define("firework_shoot");
    public static final Effect VILLAGER_PLANT_GROW = define("villager_plant_grow");
    public static final Effect DRAGON_BREATH = define("dragon_breath");
    public static final Effect ANVIL_BREAK = define("anvil_break");
    public static final Effect ANVIL_USE = define("anvil_use");
    public static final Effect ANVIL_LAND = define("anvil_land");
    public static final Effect ENDERDRAGON_SHOOT = define("enderdragon_shoot");
    public static final Effect WITHER_BREAK_BLOCK = define("wither_break_block");
    public static final Effect WITHER_SHOOT = define("wither_shoot");
    public static final Effect ZOMBIE_INFECT_ = define("zombie_infect");
    public static final Effect ZOMBIE_CONVERTED_VILLAGER = define("zombie_converted_villager");
    public static final Effect BAT_TAKEOFF = define("bat_takeoff");
    public static final Effect END_GATEWAY_SPAWN = define("end_gateway_spawn");
    public static final Effect ENDERDRAGON_GROWL = define("enderdragon_growl");

    // 1.15
    public static final Effect INSTANT_POTION_BREAK = define("instant_potion_break");

    // 1.16
    public static final Effect WITHER_SPAWNED = define("wither_spawned");
    public static final Effect ENDER_DRAGON_DEATH = define("ender_dragon_death");
    public static final Effect END_PORTAL_CREATED_IN_OVERWORLD = define("end_portal_created_in_overworld");
    public static final Effect PHANTOM_BITES = define("phantom_bites");
    public static final Effect ZOMBIE_CONVERTS_TO_DROWNED = define("zombie_converts_to_drowned");
    public static final Effect HUSK_CONVERTS_TO_ZOMBIE = define("husk_converts_to_zombie");
    public static final Effect GRINDSTONE_USED = define("grindstone_used");
    public static final Effect BOOK_PAGE_TURNED = define("book_page_turned");
    public static final Effect COMPOSTER_COMPOSTS = define("composter_composts");
    public static final Effect LAVA_CONVERTS_BLOCK = define("lava_converts_block");
    public static final Effect REDSTONE_TORCH_BURN = define("redstone_torch_burns_out");
    public static final Effect ENDER_EYE_PLACED = define("ender_eye_placed");
    public static final Effect ENDER_DRAGON_DESTROYS_BLOCK = define("ender_dragon_destroys_block");
    public static final Effect WET_SPONGE_VAPORIZES_IN_NETHER = define("wet_sponge_vaporizes_in_nether");

    // 1.17
    public static final Effect PHANTOM_BITE = define("phantom_bite");
    public static final Effect ZOMBIE_CONVERTED_TO_DROWNED = define("zombie_converted_to_drowned");
    public static final Effect HUSK_CONVERTED_TO_ZOMBIE = define("husk_converted_to_zombie");
    public static final Effect GRINDSTONE_USE = define("grindstone_use");
    public static final Effect BOOK_PAGE_TURN = define("book_page_turn");
    public static final Effect SMITHING_TABLE_USE = define("smithing_table_use");
    public static final Effect POINTED_DRIPSTONE_LAND = define("pointed_dripstone_land");
    public static final Effect POINTED_DRIPSTONE_DRIP_LAVA_INTO_CAULDRON = define("pointed_dripstone_drip_lava_into_cauldron");
    public static final Effect POINTED_DRIPSTONE_DRIP_WATER_INTO_CAULDRON = define("pointed_dripstone_drip_water_into_cauldron");
    public static final Effect SKELETON_CONVERTED_TO_STRAY = define("skeleton_converted_to_stray");
    public static final Effect COMPOSTER_FILL_ATTEMPT = define("composter_fill_attempt");
    public static final Effect LAVA_INTERACT = define("lava_interact");
    public static final Effect REDSTONE_TORCH_BURNOUT = define("redstone_torch_burnout");
    public static final Effect END_PORTAL_FRAME_FILL = define("end_portal_frame_fill");
    public static final Effect DRIPS_DRIPSTONE = define("dripping_dripstone");
    public static final Effect BONE_MEAL_USE = define("bone_meal_use");
    public static final Effect ENDER_DRAGON_DESTROY_BLOCK = define("ender_dragon_destroy_block");
    public static final Effect SPONGE_DRY = define("sponge_dry");
    public static final Effect ELECTRIC_SPARK = define("electric_spark");
    public static final Effect COPPER_WAX_ON = define("copper_wax_on");
    public static final Effect COPPER_WAX_OFF = define("copper_wax_off");
    public static final Effect OXIDISED_COPPER_SCRAPE = define("oxidised_copper_scrape");

    public static Collection<Effect> values = EFFECT_TYPE_MAPPINGS.values();
}
