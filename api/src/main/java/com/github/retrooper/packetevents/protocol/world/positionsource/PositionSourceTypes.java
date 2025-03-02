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

package com.github.retrooper.packetevents.protocol.world.positionsource;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.BlockPositionSource;
import com.github.retrooper.packetevents.protocol.world.positionsource.builtin.EntityPositionSource;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public final class PositionSourceTypes {

    private static final VersionedRegistry<PositionSourceType<?>> REGISTRY = new VersionedRegistry<>("position_source_type");

    private PositionSourceTypes() {
    }

    public static VersionedRegistry<PositionSourceType<?>> getRegistry() {
        return REGISTRY;
    }

    @ApiStatus.Internal
    public static <T extends PositionSource> PositionSourceType<T> define(
            String name,
            Reader<T> reader, Writer<T> writer,
            Decoder<T> decoder, Encoder<T> encoder
    ) {
        return REGISTRY.define(name, data -> new StaticPositionSourceType<>(
                data, reader, writer, decoder, encoder));
    }

    public static @Nullable PositionSourceType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static PositionSourceType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final PositionSourceType<BlockPositionSource> BLOCK = define("block",
            BlockPositionSource::read, BlockPositionSource::write,
            BlockPositionSource::decodeSource, BlockPositionSource::encodeSource);
    public static final PositionSourceType<EntityPositionSource> ENTITY = define("entity",
            EntityPositionSource::read, EntityPositionSource::write,
            EntityPositionSource::decodeSource, EntityPositionSource::encodeSource);

    static {
        REGISTRY.unloadMappings();
    }

    @FunctionalInterface
    public interface Decoder<T> {

        T decode(NBTCompound compound, ClientVersion version);
    }

    @FunctionalInterface
    public interface Encoder<T> {

        void encode(T value, ClientVersion version, NBTCompound compound);
    }
}
