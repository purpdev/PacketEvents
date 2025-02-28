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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class StaticComponentType<T> extends AbstractMappedEntity implements ComponentType<T> {

    private final @Nullable Reader<T> reader;
    private final @Nullable Writer<T> writer;
    private final @Nullable Decoder<T> decoder;
    private final @Nullable Encoder<T> encoder;

    @ApiStatus.Internal
    public StaticComponentType(
            @Nullable TypesBuilderData data,
            @Nullable Reader<T> reader,
            @Nullable Writer<T> writer
    ) {
        this(data, reader, writer, null, null);
    }

    @ApiStatus.Internal
    public StaticComponentType(
            @Nullable TypesBuilderData data,
            @Nullable Decoder<T> decoder,
            @Nullable Encoder<T> encoder
    ) {
        this(data, null, null, decoder, encoder);
    }

    @ApiStatus.Internal
    public StaticComponentType(
            @Nullable TypesBuilderData data,
            @Nullable Reader<T> reader,
            @Nullable Writer<T> writer,
            @Nullable Decoder<T> decoder,
            @Nullable Encoder<T> encoder
    ) {
        super(data);
        this.reader = reader;
        this.writer = writer;
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public T read(PacketWrapper<?> wrapper) {
        return this.reader != null ? this.reader.apply(wrapper) : null;
    }

    @Override
    public void write(PacketWrapper<?> wrapper, T content) {
        if (this.writer != null) {
            this.writer.accept(wrapper, content);
        }
    }

    @Override
    public T decode(NBT nbt, ClientVersion version) {
        if (this.decoder != null) {
            return this.decoder.decode(nbt, version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public NBT encode(T value, ClientVersion version) {
        if (this.encoder != null) {
            return this.encoder.encode(value, version);
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public <Z> ComponentType<Z> legacyMap(Function<T, Z> mapper, Function<Z, T> unmapper) {
        Reader<Z> reader = this.reader != null ? wrapper -> mapper.apply(this.reader.apply(wrapper)) : null;
        Writer<Z> writer = this.writer != null ? (wrapper, value) -> this.writer.accept(wrapper, unmapper.apply(value)) : null;
        Decoder<Z> decoder = this.decoder != null ? (nbt, version) -> mapper.apply(this.decoder.decode(nbt, version)) : null;
        Encoder<Z> encoder = this.encoder != null ? (value, version) -> this.encoder.encode(unmapper.apply(value), version) : null;
        return new StaticComponentType<>(this.data, reader, writer, decoder, encoder);
    }
}
