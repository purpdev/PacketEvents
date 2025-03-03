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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import io.github.retrooper.packetevents.PacketEventsClientMod;

/**
 * You should, if possible, never construct a packetevents instance on your own
 * and preferable jar-in-jar the packetevents fabric platform.
 * <p>
 * If you still want to create your own packetevents instance, which is not
 * recommended, use {@link PacketEventsClientMod#constructApi(String)} or
 * {@link io.github.retrooper.packetevents.PacketEventsServerMod#constructApi(String)}.
 *
 * @deprecated flawed concept and doesn't support both client and server
 */
@Deprecated(forRemoval = true)
public final class FabricPacketEventsBuilder {

    private FabricPacketEventsBuilder() {
    }

    public static void clearBuildCache() {
    }

    public static PacketEventsAPI<?> build(String modId) {
        return buildNoCache(modId);
    }

    public static PacketEventsAPI<?> build(String modId, PacketEventsSettings settings) {
        return buildNoCache(modId, settings);
    }

    public static PacketEventsAPI<?> buildNoCache(String modId) {
        return buildNoCache(modId, new PacketEventsSettings());
    }

    public static PacketEventsAPI<?> buildNoCache(String modId, PacketEventsSettings inSettings) {
        return PacketEventsClientMod.constructApi(modId);
    }
}
