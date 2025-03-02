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

package com.github.retrooper.packetevents.protocol.score;

import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;

public final class ScoreFormatTypes {

    private static final VersionedRegistry<ScoreFormatType<?>> REGISTRY = new VersionedRegistry<>("number_format_type");

    public static final ScoreFormatType<BlankScoreFormat> BLANK = define("blank",
            BlankScoreFormat::read, BlankScoreFormat::write);
    public static final ScoreFormatType<StyledScoreFormat> STYLED = define("styled",
            StyledScoreFormat::read, StyledScoreFormat::write);
    public static final ScoreFormatType<FixedScoreFormat> FIXED = define("fixed",
            FixedScoreFormat::read, FixedScoreFormat::write);

    static {
        REGISTRY.unloadMappings();
    }

    private ScoreFormatTypes() {
    }

    public static VersionedRegistry<ScoreFormatType<?>> getRegistry() {
        return REGISTRY;
    }

    /**
     * Returns an immutable view of the score format types.
     *
     * @return Score Format Types
     */
    public static Collection<ScoreFormatType<?>> values() {
        return REGISTRY.getEntries();
    }

    @Deprecated
    public static ScoreFormat read(PacketWrapper<?> wrapper) {
        return ScoreFormat.readTyped(wrapper);
    }

    @Deprecated
    public static void write(PacketWrapper<?> wrapper, ScoreFormat format) {
        ScoreFormat.writeTyped(wrapper, format);
    }

    @Deprecated
    @ApiStatus.Internal
    public static <T extends ScoreFormat> ScoreFormatType<T> define(
            int id, String name, Class<T> formatClass,
            Function<PacketWrapper<?>, T> reader,
            BiConsumer<PacketWrapper<?>, T> writer
    ) {
        return define(name, reader::apply, writer::accept);
    }

    @ApiStatus.Internal
    public static <T extends ScoreFormat> ScoreFormatType<T> define(
            String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer
    ) {
        return REGISTRY.define(name, data ->
                new StaticScoreFormatType<>(data, reader, writer));
    }

    public static @Nullable ScoreFormatType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static @Nullable ScoreFormatType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static @Nullable ScoreFormatType<?> getByName(ResourceLocation name) {
        return REGISTRY.getByName(name);
    }
}
