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

package com.github.retrooper.packetevents.protocol.item.type;

import com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes.ItemAttribute;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.states.type.StateType;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class StaticItemType extends AbstractMappedEntity implements ItemType {

    private final int maxAmount;
    private final int maxDurability;
    private final ItemType craftRemainder;
    private final @Nullable StateType placedType;
    private final Set<ItemAttribute> attributes;
    private final Map<ClientVersion, StaticComponentMap> components;

    @ApiStatus.Internal
    public StaticItemType(
            @Nullable TypesBuilderData data,
            int maxAmount,
            int maxDurability,
            ItemType craftRemainder,
            @Nullable StateType placedType,
            Set<ItemAttribute> attributes
    ) {
        super(data);
        this.maxAmount = maxAmount;
        this.maxDurability = maxDurability;
        this.craftRemainder = craftRemainder;
        this.placedType = placedType;
        this.attributes = attributes;
        this.components = new EnumMap<>(ClientVersion.class);
    }

    @Override
    public int getMaxAmount() {
        return this.maxAmount;
    }

    @Override
    public int getMaxDurability() {
        return this.maxDurability;
    }

    @Override
    public ItemType getCraftRemainder() {
        return this.craftRemainder;
    }

    @Override
    public @Nullable StateType getPlacedType() {
        return this.placedType;
    }

    @Override
    public Set<ItemAttribute> getAttributes() {
        return this.attributes;
    }

    @Override
    public StaticComponentMap getComponents(ClientVersion version) {
        if (!version.isRelease()) {
            throw new IllegalArgumentException("Unsupported version for "
                    + "getting components of " + this.getName() + ": " + version);
        }
        return this.components.getOrDefault(version, StaticComponentMap.SHARED_ITEM_COMPONENTS);
    }

    void setComponents(ClientVersion version, StaticComponentMap components) {
        if (this.components.containsKey(version)) {
            throw new IllegalStateException("Components are already defined for "
                    + this.getName() + " in version " + version);
        } else if (!version.isRelease()) {
            throw new IllegalArgumentException("Unsupported version for "
                    + "setting components of " + this.getName() + ": " + version);
        }
        this.components.put(version, components);
    }

    // internal, only used for checking wether
    // component definition is present during startup
    boolean hasComponents(ClientVersion version) {
        return this.components.containsKey(version);
    }

    // fills holes in the base component mappings
    void fillComponents() {
        StaticComponentMap lastComponents = null;
        for (ClientVersion version : ClientVersion.values()) {
            if (!version.isRelease()) {
                continue;
            }
            StaticComponentMap components = this.components.get(version);
            if (components == null) {
                if (lastComponents != null) {
                    this.components.put(version, lastComponents);
                }
                continue;
            }
            if (lastComponents == null) {
                // also fill backwards
                for (ClientVersion beforeVersion : ClientVersion.values()) {
                    if (beforeVersion == version) {
                        break;
                    }
                    this.components.put(beforeVersion, components);
                }
            }
            lastComponents = components;
        }
    }
}
