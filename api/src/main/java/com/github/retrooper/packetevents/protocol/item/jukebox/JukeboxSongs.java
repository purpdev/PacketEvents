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

package com.github.retrooper.packetevents.protocol.item.jukebox;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.Sounds;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public final class JukeboxSongs {

    private static final VersionedRegistry<IJukeboxSong> REGISTRY = new VersionedRegistry<>("jukebox_song");

    private JukeboxSongs() {
    }

    private static String makeDescriptionId(String var0, @Nullable ResourceLocation var1) {
        return var1 == null ? var0 + ".unregistered_sadface" :
                var0 + "." + var1.getNamespace() + "." + var1.getKey().replace('/', '.');
    }

    @ApiStatus.Internal
    public static IJukeboxSong define(String key, Sound sound, float lengthInSeconds, int comparatorOutput) {
        return REGISTRY.define(key, data -> new JukeboxSong(data, sound,
                Component.translatable(makeDescriptionId("jukebox_song", data.getName())), lengthInSeconds,
                comparatorOutput));
    }

    public static VersionedRegistry<IJukeboxSong> getRegistry() {
        return REGISTRY;
    }

    public static IJukeboxSong getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static IJukeboxSong getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final IJukeboxSong THIRTEEN = define("13", Sounds.MUSIC_DISC_13, 178, 1);
    public static final IJukeboxSong CAT = define("cat", Sounds.MUSIC_DISC_CAT, 185, 2);
    public static final IJukeboxSong BLOCKS = define("blocks", Sounds.MUSIC_DISC_BLOCKS, 345, 3);
    public static final IJukeboxSong CHIRP = define("chirp", Sounds.MUSIC_DISC_CHIRP, 185, 4);
    public static final IJukeboxSong FAR = define("far", Sounds.MUSIC_DISC_FAR, 174, 5);
    public static final IJukeboxSong MALL = define("mall", Sounds.MUSIC_DISC_MALL, 197, 6);
    public static final IJukeboxSong MELLOHI = define("mellohi", Sounds.MUSIC_DISC_MELLOHI, 96, 7);
    public static final IJukeboxSong STAL = define("stal", Sounds.MUSIC_DISC_STAL, 150, 8);
    public static final IJukeboxSong STRAD = define("strad", Sounds.MUSIC_DISC_STRAD, 188, 9);
    public static final IJukeboxSong WARD = define("ward", Sounds.MUSIC_DISC_WARD, 251, 10);
    public static final IJukeboxSong ELEVEN = define("11", Sounds.MUSIC_DISC_11, 71, 11);
    public static final IJukeboxSong WAIT = define("wait", Sounds.MUSIC_DISC_WAIT, 238, 12);
    public static final IJukeboxSong PIGSTEP = define("pigstep", Sounds.MUSIC_DISC_PIGSTEP, 149, 13);
    public static final IJukeboxSong OTHERSIDE = define("otherside", Sounds.MUSIC_DISC_OTHERSIDE, 195, 14);
    public static final IJukeboxSong FIVE = define("5", Sounds.MUSIC_DISC_5, 178, 15);
    public static final IJukeboxSong RELIC = define("relic", Sounds.MUSIC_DISC_RELIC, 218, 14);
    public static final IJukeboxSong PRECIPICE = define("precipice", Sounds.MUSIC_DISC_PRECIPICE, 299, 13);
    public static final IJukeboxSong CREATOR = define("creator", Sounds.MUSIC_DISC_CREATOR, 176, 12);
    public static final IJukeboxSong CREATOR_MUSIC_BOX = define("creator_music_box", Sounds.MUSIC_DISC_CREATOR_MUSIC_BOX,
            73, 11);

    /**
     * Returns an immutable view of the jukebox songs.
     *
     * @return Jukebox Songs
     */
    public static Collection<IJukeboxSong> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
