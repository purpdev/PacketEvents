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

package com.github.retrooper.packetevents.protocol.entity.villager.profession;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class VillagerProfessions {

    private static final VersionedRegistry<VillagerProfession> REGISTRY = new VersionedRegistry<>("villager_profession");

    private VillagerProfessions() {
    }

    public static VersionedRegistry<VillagerProfession> getRegistry() {
        return REGISTRY;
    }

    @Deprecated
    @ApiStatus.Internal
    public static VillagerProfession define(int id, String name) {
        return define(name);
    }

    @ApiStatus.Internal
    public static VillagerProfession define(String name) {
        return REGISTRY.define(name, StaticVillagerProfession::new);
    }

    @Deprecated
    public static VillagerProfession getById(int id) {
        ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
        return getById(version.toClientVersion(), id);
    }

    public static VillagerProfession getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static VillagerProfession getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static final VillagerProfession NONE = define("none");
    public static final VillagerProfession ARMORER = define("armorer");
    public static final VillagerProfession BUTCHER = define("butcher");
    public static final VillagerProfession CARTOGRAPHER = define("cartographer");
    public static final VillagerProfession CLERIC = define("cleric");
    public static final VillagerProfession FARMER = define("farmer");
    public static final VillagerProfession FISHERMAN = define("fisherman");
    public static final VillagerProfession FLETCHER = define("fletcher");
    public static final VillagerProfession LEATHERWORKER = define("leatherworker");
    public static final VillagerProfession LIBRARIAN = define("librarian");
    public static final VillagerProfession MASON = define("mason");
    public static final VillagerProfession NITWIT = define("nitwit");
    public static final VillagerProfession SHEPHERD = define("shepherd");
    public static final VillagerProfession TOOLSMITH = define("toolsmith");
    public static final VillagerProfession WEAPONSMITH = define("weaponsmith");

    static {
        REGISTRY.unloadMappings();
    }
}
