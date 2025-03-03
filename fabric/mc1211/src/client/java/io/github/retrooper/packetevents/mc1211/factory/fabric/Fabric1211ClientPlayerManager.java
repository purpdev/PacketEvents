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

package io.github.retrooper.packetevents.mc1211.factory.fabric;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.NotNull;

public class Fabric1211ClientPlayerManager extends Fabric1211ServerPlayerManager {

    @Override
    public int getPing(@NotNull Object playerObj) {
        if (playerObj instanceof LocalPlayer player) {
            PlayerInfo info = player.connection.getPlayerInfo(player.getUUID());
            if (info != null) {
                return info.getLatency();
            }
            // if the server doesn't show the player info of
            // the player itself, try to fall back to potential
            // latency sampling data - which is often not present
            // TODO fix later for 1.20.1
            return (int) Minecraft.getInstance().getDebugOverlay().getPingLogger().get(0);
        }
        return super.getPing(playerObj);
    }

    @Override
    public Object getChannel(@NotNull Object player) {
        if (player instanceof LocalPlayer) {
            return ((LocalPlayer) player).connection.getConnection().channel;
        }
        return super.getChannel(player);
    }
}
