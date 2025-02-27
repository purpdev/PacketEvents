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

package com.github.retrooper.packetevents.protocol.item.consumables;

import com.github.retrooper.packetevents.protocol.item.consumables.builtin.ApplyEffectsConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.builtin.ClearAllEffectsConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.builtin.PlaySoundConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.builtin.RemoveEffectsConsumeEffect;
import com.github.retrooper.packetevents.protocol.item.consumables.builtin.TeleportRandomlyConsumeEffect;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import org.jetbrains.annotations.ApiStatus;

public final class ConsumeEffectTypes {

    private static final VersionedRegistry<ConsumeEffectType<?>> REGISTRY = new VersionedRegistry<>("consume_effect_type");

    private ConsumeEffectTypes() {
    }

    @ApiStatus.Internal
    public static <T extends ConsumeEffect<?>> ConsumeEffectType<T> define(
            String name, Reader<T> reader, Writer<T> writer) {
        return REGISTRY.define(name, data ->
                new StaticConsumeEffectType<>(data, reader, writer));
    }

    public static final ConsumeEffectType<ApplyEffectsConsumeEffect> APPLY_EFFECTS = define(
            "apply_effects", ApplyEffectsConsumeEffect::read, ApplyEffectsConsumeEffect::write);
    public static final ConsumeEffectType<RemoveEffectsConsumeEffect> REMOVE_EFFECTS = define(
            "remove_effects", RemoveEffectsConsumeEffect::read, RemoveEffectsConsumeEffect::write);
    public static final ConsumeEffectType<ClearAllEffectsConsumeEffect> CLEAR_ALL_EFFECTS = define(
            "clear_all_effects", ClearAllEffectsConsumeEffect::read, ClearAllEffectsConsumeEffect::write);
    public static final ConsumeEffectType<TeleportRandomlyConsumeEffect> TELEPORT_RANDOMLY = define(
            "teleport_randomly", TeleportRandomlyConsumeEffect::read, TeleportRandomlyConsumeEffect::write);
    public static final ConsumeEffectType<PlaySoundConsumeEffect> PLAY_SOUND = define(
            "play_sound", PlaySoundConsumeEffect::read, PlaySoundConsumeEffect::write);

    public static VersionedRegistry<ConsumeEffectType<?>> getRegistry() {
        return REGISTRY;
    }

    static {
        REGISTRY.unloadMappings();
    }
}
