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

package com.github.retrooper.packetevents.protocol.item.instrument;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

public final class Instruments {

    private static final VersionedRegistry<Instrument> REGISTRY = new VersionedRegistry<>("instrument");

    private Instruments() {
    }

    @ApiStatus.Internal
    public static Instrument define(String key, Sound sound) {
        // vanilla defaults for goat horns
        return define(key, sound, 20 * 7, 256f);
    }

    @ApiStatus.Internal
    public static Instrument define(String key, Sound sound, int useDuration, float range) {
        return REGISTRY.define(key, data -> new StaticInstrument(
                data, sound, useDuration, range,
                Component.translatable("instrument.minecraft." + key)));
    }

    public static VersionedRegistry<Instrument> getRegistry() {
        return REGISTRY;
    }

    public static Instrument getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static Instrument getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final Instrument PONDER_GOAT_HORN = define("ponder_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_0);
    public static final Instrument SING_GOAT_HORN = define("sing_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_1);
    public static final Instrument SEEK_GOAT_HORN = define("seek_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_2);
    public static final Instrument FEEL_GOAT_HORN = define("feel_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_3);
    public static final Instrument ADMIRE_GOAT_HORN = define("admire_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_4);
    public static final Instrument CALL_GOAT_HORN = define("call_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_5);
    public static final Instrument YEARN_GOAT_HORN = define("yearn_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_6);
    public static final Instrument DREAM_GOAT_HORN = define("dream_goat_horn", Sounds.ITEM_GOAT_HORN_SOUND_7);

    static {
        REGISTRY.unloadMappings();
    }
}
