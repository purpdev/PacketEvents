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

package io.github.retrooper.packetevents.mc1201.factory.fabric;

import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class Fabric1201ServerPlayerManager extends PlayerManagerAbstract {

    @Override
    public int getPing(@NotNull Object player) {
        if (player instanceof ServerPlayer) {
            return ((ServerPlayer) player).latency;
        }
        throw new UnsupportedOperationException("Unsupported player implementation: " + player);
    }

    @Override
    public Object getChannel(@NotNull Object player) {
        if (player instanceof ServerPlayer) {
            return ((ServerPlayer) player).connection.connection.channel;
        }
        throw new UnsupportedOperationException("Unsupported player implementation: " + player);
    }
}
