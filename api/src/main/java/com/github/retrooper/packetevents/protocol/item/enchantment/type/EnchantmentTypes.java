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

package com.github.retrooper.packetevents.protocol.item.enchantment.type;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.MappingHelper;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public final class EnchantmentTypes {

    private static final Map<String, String> STRING_UPDATER = new HashMap<>();

    static {
        // renamed in 24w03a, during the 1.20.5 update
        STRING_UPDATER.put("minecraft:sweeping", "minecraft:sweeping_edge");
    }

    // load data from file, enchantment types are too complex to define in code here
    private static final Map<ResourceLocation, NBTCompound> ENCHANTMENT_DATA;

    static {
        ENCHANTMENT_DATA = new HashMap<>();
        try (SequentialNBTReader.Compound dataTag = MappingHelper.decompress("mappings/data/enchantment")) {
            dataTag.skipOne(); // skip version
            for (Map.Entry<String, NBT> entry : (SequentialNBTReader.Compound) dataTag.next().getValue()) {
                ResourceLocation enchantKey = new ResourceLocation(entry.getKey());
                ENCHANTMENT_DATA.put(enchantKey, ((SequentialNBTReader.Compound) entry.getValue()).readFully());
            }
        } catch (IOException exception) {
            throw new RuntimeException("Error while reading enchantment type data", exception);
        }
    }

    private static final VersionedRegistry<EnchantmentType> REGISTRY = new VersionedRegistry<>("enchantment");

    private EnchantmentTypes() {
    }

    @ApiStatus.Internal
    public static EnchantmentType define(String key) {
        return REGISTRY.define(key, data -> {
            NBTCompound dataTag = ENCHANTMENT_DATA.get(data.getName());
            if (dataTag == null) {
                throw new IllegalArgumentException("Can't define enchantment " + data.getName() + ", no data found");
            }
            return EnchantmentType.decode(dataTag, ClientVersion.getLatest(), data);
        });
    }

    public static VersionedRegistry<EnchantmentType> getRegistry() {
        return REGISTRY;
    }

    public static @Nullable EnchantmentType getByName(String name) {
        String fixedName = STRING_UPDATER.getOrDefault(name, name);
        return REGISTRY.getByName(fixedName);
    }

    public static @Nullable EnchantmentType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final EnchantmentType ALL_DAMAGE_PROTECTION = define("protection");
    public static final EnchantmentType FIRE_PROTECTION = define("fire_protection");
    public static final EnchantmentType FALL_PROTECTION = define("feather_falling");
    public static final EnchantmentType BLAST_PROTECTION = define("blast_protection");
    public static final EnchantmentType PROJECTILE_PROTECTION = define("projectile_protection");
    public static final EnchantmentType RESPIRATION = define("respiration");
    public static final EnchantmentType AQUA_AFFINITY = define("aqua_affinity");
    public static final EnchantmentType THORNS = define("thorns");
    public static final EnchantmentType DEPTH_STRIDER = define("depth_strider");
    public static final EnchantmentType FROST_WALKER = define("frost_walker");
    public static final EnchantmentType BINDING_CURSE = define("binding_curse");
    public static final EnchantmentType SOUL_SPEED = define("soul_speed");
    public static final EnchantmentType SWIFT_SNEAK = define("swift_sneak");
    public static final EnchantmentType SHARPNESS = define("sharpness");
    public static final EnchantmentType SMITE = define("smite");
    public static final EnchantmentType BANE_OF_ARTHROPODS = define("bane_of_arthropods");
    public static final EnchantmentType KNOCKBACK = define("knockback");
    public static final EnchantmentType FIRE_ASPECT = define("fire_aspect");
    public static final EnchantmentType MOB_LOOTING = define("looting");
    public static final EnchantmentType SWEEPING_EDGE = define("sweeping_edge");
    public static final EnchantmentType BLOCK_EFFICIENCY = define("efficiency");
    public static final EnchantmentType SILK_TOUCH = define("silk_touch");
    public static final EnchantmentType UNBREAKING = define("unbreaking");
    public static final EnchantmentType BLOCK_FORTUNE = define("fortune");
    public static final EnchantmentType POWER_ARROWS = define("power");
    public static final EnchantmentType PUNCH_ARROWS = define("punch");
    public static final EnchantmentType FLAMING_ARROWS = define("flame");
    public static final EnchantmentType INFINITY_ARROWS = define("infinity");
    public static final EnchantmentType FISHING_LUCK = define("luck_of_the_sea");
    public static final EnchantmentType FISHING_SPEED = define("lure");
    public static final EnchantmentType LOYALTY = define("loyalty");
    public static final EnchantmentType IMPALING = define("impaling");
    public static final EnchantmentType RIPTIDE = define("riptide");
    public static final EnchantmentType CHANNELING = define("channeling");
    public static final EnchantmentType MULTISHOT = define("multishot");
    public static final EnchantmentType QUICK_CHARGE = define("quick_charge");
    public static final EnchantmentType PIERCING = define("piercing");
    public static final EnchantmentType MENDING = define("mending");
    public static final EnchantmentType VANISHING_CURSE = define("vanishing_curse");

    // Added in 1.20.5
    public static final EnchantmentType DENSITY = define("density");
    public static final EnchantmentType BREACH = define("breach");
    public static final EnchantmentType WIND_BURST = define("wind_burst");

    static {
        ENCHANTMENT_DATA.clear();
        REGISTRY.unloadMappings();
    }
}
