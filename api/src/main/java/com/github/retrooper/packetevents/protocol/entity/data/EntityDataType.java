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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EntityDataType<T> extends AbstractMappedEntity {

    private final PacketWrapper.Reader<? extends T> reader;
    private final PacketWrapper.Writer<T> writer;

    @ApiStatus.Internal
    public EntityDataType(
            @Nullable TypesBuilderData data,
            PacketWrapper.Reader<? extends T> reader,
            PacketWrapper.Writer<T> writer
    ) {
        super(data);
        this.reader = reader;
        this.writer = writer;
    }

    public T read(PacketWrapper<?> wrapper) {
        return this.reader.apply(wrapper);
    }

    public void write(PacketWrapper<?> wrapper, T serializer) {
        this.writer.accept(wrapper, serializer);
    }

    @Deprecated
    public Function<PacketWrapper<?>, T> getDataDeserializer() {
        return this.reader::apply;
    }

    @Deprecated
    public BiConsumer<PacketWrapper<?>, T> getDataSerializer() {
        return this.writer;
    }
}
