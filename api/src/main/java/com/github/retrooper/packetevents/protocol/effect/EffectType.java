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

package com.github.retrooper.packetevents.protocol.effect;

import com.github.retrooper.packetevents.protocol.effect.type.Effect;
import org.jetbrains.annotations.NotNull;

public class EffectType {
    private Effect effect;

    public EffectType(@NotNull final Effect effect) {
        this.effect = effect;
    }

    public Effect getEffect() {
        return effect;
    }

    public void setEffect(Effect effect) {
        this.effect = effect;
    }

    public Type getType() {
        return Type.getType(effect.getName().getKey());
    }

    public enum Type {
        SOUND,
        VISUAL;

        public static Type getType(@NotNull final String key) {
            if (key.equals("sound")) {
                return SOUND;
            } else if (key.equals("visual")) {
                return VISUAL;
            }
            return null;
        }
    }
}
