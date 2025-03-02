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

package com.github.retrooper.packetevents.protocol.entity.villager.type;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

public final class VillagerTypes {

    private static final VersionedRegistry<VillagerType> REGISTRY = new VersionedRegistry<>("villager_type");

    private VillagerTypes() {
    }

    public static VersionedRegistry<VillagerType> getRegistry() {
        return REGISTRY;
    }

    @Deprecated
    @ApiStatus.Internal
    public static VillagerType define(int id, String name) {
        return define(name);
    }

    @ApiStatus.Internal
    public static VillagerType define(String name) {
        return REGISTRY.define(name, StaticVillagerType::new);
    }

    @Deprecated
    public static VillagerType getById(int id) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        return getById(version.toClientVersion(), id);
    }

    public static VillagerType getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static VillagerType getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static final VillagerType DESERT = define("desert");
    public static final VillagerType JUNGLE = define("jungle");
    public static final VillagerType PLAINS = define("plains");
    public static final VillagerType SAVANNA = define("savanna");
    public static final VillagerType SNOW = define("snow");
    public static final VillagerType SWAMP = define("swamp");
    public static final VillagerType TAIGA = define("taiga");

    /**
     * Returns an immutable view of the villager types.
     *
     * @return Villager Types
     */
    public static Collection<VillagerType> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
