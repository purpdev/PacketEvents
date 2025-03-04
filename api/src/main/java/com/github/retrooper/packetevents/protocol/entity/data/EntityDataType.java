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

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

@Deprecated
public final class EntityDataType<T> {

    private final IEntityDataType<T> delegate;

    EntityDataType(IEntityDataType<T> delegate) {
        this.delegate = delegate;
    }

    public String getName() {
        return this.delegate.getName().toString();
    }

    public int getId(ClientVersion version) {
        return this.delegate.getId(version);
    }

    public Function<PacketWrapper<?>, T> getDataDeserializer() {
        return this.delegate::deserialize;
    }

    @SuppressWarnings("unchecked")
    public BiConsumer<PacketWrapper<?>, Object> getDataSerializer() {
        return (wrapper, value) -> this.delegate.serialize(wrapper, (T) value);
    }
}
