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

package com.github.retrooper.packetevents.protocol.recipe.display.slot;

import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

public final class SlotDisplayTypes {

    private static final VersionedRegistry<SlotDisplayType<?>> REGISTRY = new VersionedRegistry<>("slot_display");

    private SlotDisplayTypes() {
    }

    private static <T extends SlotDisplay<?>> SlotDisplayType<T> register(
            String id, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer
    ) {
        return REGISTRY.define(id, data -> new StaticSlotDisplayType<>(data, reader, writer));
    }

    public static VersionedRegistry<SlotDisplayType<?>> getRegistry() {
        return REGISTRY;
    }

    public static final SlotDisplayType<EmptySlotDisplay> EMPTY = register(
            "empty", EmptySlotDisplay::read, EmptySlotDisplay::write);
    public static final SlotDisplayType<AnyFuelSlotDisplay> ANY_FUEL = register(
            "any_fuel", AnyFuelSlotDisplay::read, AnyFuelSlotDisplay::write);
    public static final SlotDisplayType<ItemSlotDisplay> ITEM = register(
            "item", ItemSlotDisplay::read, ItemSlotDisplay::write);
    public static final SlotDisplayType<ItemStackSlotDisplay> ITEM_STACK = register(
            "item_stack", ItemStackSlotDisplay::read, ItemStackSlotDisplay::write);
    public static final SlotDisplayType<TagSlotDisplay> TAG = register(
            "tag", TagSlotDisplay::read, TagSlotDisplay::write);
    public static final SlotDisplayType<SmithingTrimSlotDisplay> SMITHING_TRIM = register(
            "smithing_trim", SmithingTrimSlotDisplay::read, SmithingTrimSlotDisplay::write);
    public static final SlotDisplayType<WithRemainderSlotDisplay> WITH_REMAINDER = register(
            "with_remainder", WithRemainderSlotDisplay::read, WithRemainderSlotDisplay::write);
    public static final SlotDisplayType<CompositeSlotDisplay> COMPOSITE = register(
            "composite", CompositeSlotDisplay::read, CompositeSlotDisplay::write);

    static {
        REGISTRY.unloadMappings();
    }
}
