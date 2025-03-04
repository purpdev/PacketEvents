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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;

import java.util.function.Function;

public interface IEntityDataType<T> extends MappedEntity {

    T deserialize(PacketWrapper<?> wrapper);

    void serialize(PacketWrapper<?> wrapper, T value);

    /**
     * Used internally for backwards compatibility
     * for a few datatypes, may be removed at any time.
     */
    @ApiStatus.Internal
    @Contract("_, _ -> new")
    <Z> IEntityDataType<Z> map(Function<T, Z> processor, Function<Z, T> unprocessor);

    @Deprecated
    EntityDataType<T> asLegacy();
}
