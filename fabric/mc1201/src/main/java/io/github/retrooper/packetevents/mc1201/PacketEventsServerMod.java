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

package io.github.retrooper.packetevents.mc1201;

import io.github.retrooper.packetevents.LazyHolder;
import io.github.retrooper.packetevents.factory.fabric.FabricPacketEventsAPI;
import io.github.retrooper.packetevents.mc1201.factory.fabric.Fabric1201ServerPlayerManager;

public class PacketEventsServerMod extends io.github.retrooper.packetevents.PacketEventsServerMod {

    @Override
    public void onPreLaunch() {
        FabricPacketEventsAPI.staticLazyPlayerManagerHolder = LazyHolder.simple(Fabric1201ServerPlayerManager::new);
        super.preLaunch();
    }
}
