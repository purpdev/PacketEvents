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

public final class BlankScoreFormat implements ScoreFormat {

    public static final BlankScoreFormat INSTANCE = new BlankScoreFormat();

    private BlankScoreFormat() {
    }

    public static BlankScoreFormat read(PacketWrapper<?> wrapper) {
        return INSTANCE;
    }

    public static void write(PacketWrapper<?> wrapper, BlankScoreFormat format) {
        // NO-OP
    }

    @Override
    public Component format(int score) {
        return Component.empty();
    }

    @Override
    public ScoreFormatType<BlankScoreFormat> getType() {
        return ScoreFormatTypes.BLANK;
    }
}
