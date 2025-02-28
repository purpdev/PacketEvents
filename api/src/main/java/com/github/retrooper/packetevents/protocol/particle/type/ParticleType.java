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

package com.github.retrooper.packetevents.protocol.particle.type;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public interface ParticleType<T extends ParticleData> extends MappedEntity {

    T readData(PacketWrapper<?> wrapper);

    void writeData(PacketWrapper<?> wrapper, T data);

    T decodeData(NBTCompound compound, ClientVersion version);

    void encodeData(T value, ClientVersion version, NBTCompound compound);

    @Deprecated
    default Function<PacketWrapper<?>, ParticleData> readDataFunction() {
        return this::readData;
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    default BiConsumer<PacketWrapper<?>, ParticleData> writeDataFunction() {
        return (wrapper, data) -> this.writeData(wrapper, (T) data);
    }
}
