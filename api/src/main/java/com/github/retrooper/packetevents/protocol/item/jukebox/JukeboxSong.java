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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class JukeboxSong extends AbstractMappedEntity implements IJukeboxSong {

    private Sound sound;
    private Component description;
    private float lengthInSeconds;
    private int comparatorOutput;

    public JukeboxSong(Sound sound, Component description, float lengthInSeconds, int comparatorOutput) {
        this(null, sound, description, lengthInSeconds, comparatorOutput);
    }

    @ApiStatus.Internal
    public JukeboxSong(
            @Nullable TypesBuilderData data,
            Sound sound,
            Component description,
            float lengthInSeconds,
            int comparatorOutput
    ) {
        super(data);
        this.sound = sound;
        this.description = description;
        this.lengthInSeconds = lengthInSeconds;
        this.comparatorOutput = comparatorOutput;
    }

    @Deprecated
    public static JukeboxSong read(PacketWrapper<?> wrapper) {
        return (JukeboxSong) IJukeboxSong.read(wrapper);
    }

    @Deprecated
    public static void write(PacketWrapper<?> wrapper, JukeboxSong song) {
        IJukeboxSong.write(wrapper, song);
    }

    @Override
    public IJukeboxSong copy(@Nullable TypesBuilderData newData) {
        return new JukeboxSong(newData, sound, description, lengthInSeconds, comparatorOutput);
    }

    @Override
    public Sound getSound() {
        return this.sound;
    }

    @Deprecated
    public void setSound(Sound sound) {
        this.sound = sound;
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Deprecated
    public void setDescription(Component description) {
        this.description = description;
    }

    @Override
    public float getLengthInSeconds() {
        return this.lengthInSeconds;
    }

    @Deprecated
    public void setLengthInSeconds(float lengthInSeconds) {
        this.lengthInSeconds = lengthInSeconds;
    }

    @Override
    public int getComparatorOutput() {
        return this.comparatorOutput;
    }

    @Deprecated
    public void setComparatorOutput(int comparatorOutput) {
        this.comparatorOutput = comparatorOutput;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof JukeboxSong)) return false;
        if (!super.equals(obj)) return false;
        JukeboxSong that = (JukeboxSong) obj;
        if (Float.compare(that.lengthInSeconds, this.lengthInSeconds) != 0) return false;
        if (this.comparatorOutput != that.comparatorOutput) return false;
        if (!this.sound.equals(that.sound)) return false;
        return this.description.equals(that.description);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.sound, this.description, this.lengthInSeconds, this.comparatorOutput);
    }

    @Override
    public String toString() {
        return "JukeboxSong{sound=" + this.sound + ", description=" + this.description + ", lengthInSeconds=" + this.lengthInSeconds + ", comparatorOutput=" + this.comparatorOutput + '}';
    }
}
