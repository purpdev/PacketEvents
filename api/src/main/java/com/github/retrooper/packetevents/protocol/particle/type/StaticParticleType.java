/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class StaticParticleType<T extends ParticleData> extends AbstractMappedEntity implements ParticleType<T> {

    private final PacketWrapper.Reader<T> reader;
    private final PacketWrapper.Writer<T> writer;
    private final ParticleTypes.Decoder<T> decoder;
    private final ParticleTypes.Encoder<T> encoder;

    @ApiStatus.Internal
    public StaticParticleType(
            @Nullable TypesBuilderData data,
            PacketWrapper.Reader<T> reader,
            PacketWrapper.Writer<T> writer,
            ParticleTypes.Decoder<T> decoder,
            ParticleTypes.Encoder<T> encoder
    ) {
        super(data);
        this.reader = reader;
        this.writer = writer;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public T readData(PacketWrapper<?> wrapper) {
        return this.reader.apply(wrapper);
    }

    @Override
    public void writeData(PacketWrapper<?> wrapper, T data) {
        if (this.writer != null) {
            this.writer.accept(wrapper, data);
        } else if (!data.isEmpty()) {
            throw new UnsupportedOperationException("Trying to write non-empty data for " + this.getName());
        }
    }

    @Override
    public T decodeData(NBTCompound compound, ClientVersion version) {
        return this.decoder.decode(compound, version);
    }

    @Override
    public void encodeData(T value, ClientVersion version, NBTCompound compound) {
        if (this.encoder != null) {
            this.encoder.encode(value, version, compound);
        } else if (!value.isEmpty()) {
            throw new UnsupportedOperationException("Trying to encode non-empty data for " + this.getName());
        }
    }
}
