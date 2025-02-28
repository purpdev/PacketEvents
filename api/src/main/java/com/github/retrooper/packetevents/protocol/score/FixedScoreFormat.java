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

import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;

public final class FixedScoreFormat implements ScoreFormat {

    private final Component value;

    public FixedScoreFormat(Component value) {
        this.value = value;
    }

    public static FixedScoreFormat read(PacketWrapper<?> wrapper) {
        return new FixedScoreFormat(wrapper.readComponent());
    }

    public static void write(PacketWrapper<?> wrapper, FixedScoreFormat format) {
        wrapper.writeComponent(format.value);
    }

    @Override
    public Component format(int score) {
        return this.value;
    }

    @Override
    public ScoreFormatType<FixedScoreFormat> getType() {
        return ScoreFormatTypes.FIXED;
    }

    public Component getValue() {
        return this.value;
    }
}
