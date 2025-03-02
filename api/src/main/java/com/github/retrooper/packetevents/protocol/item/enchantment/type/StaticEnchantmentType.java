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

package com.github.retrooper.packetevents.protocol.item.enchantment.type;

import com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import com.github.retrooper.packetevents.protocol.item.enchantment.EnchantmentDefinition;
import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class StaticEnchantmentType extends AbstractMappedEntity implements EnchantmentType {

    private final Component description;
    private final EnchantmentDefinition definition;
    private final MappedEntitySet<EnchantmentType> exclusiveSet;
    private final StaticComponentMap effects;

    public StaticEnchantmentType(
            Component description,
            EnchantmentDefinition definition,
            MappedEntitySet<EnchantmentType> exclusiveSet,
            StaticComponentMap effects
    ) {
        this(null, description, definition, exclusiveSet, effects);
    }

    @ApiStatus.Internal
    public StaticEnchantmentType(
            @Nullable TypesBuilderData data,
            Component description,
            EnchantmentDefinition definition,
            MappedEntitySet<EnchantmentType> exclusiveSet,
            StaticComponentMap effects
    ) {
        super(data);
        this.description = description;
        this.definition = definition;
        this.exclusiveSet = exclusiveSet;
        this.effects = effects;
    }

    @Override
    public EnchantmentType copy(@Nullable TypesBuilderData newData) {
        return new StaticEnchantmentType(newData, this.description,
                this.definition, this.exclusiveSet, this.effects);
    }

    @Override
    public Component getDescription() {
        return this.description;
    }

    @Override
    public EnchantmentDefinition getDefinition() {
        return this.definition;
    }

    @Override
    public MappedEntitySet<EnchantmentType> getExclusiveSet() {
        return this.exclusiveSet;
    }

    @Override
    public StaticComponentMap getEffects() {
        return this.effects;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticEnchantmentType)) return false;
        if (!super.equals(obj)) return false;
        StaticEnchantmentType that = (StaticEnchantmentType) obj;
        if (!this.description.equals(that.description)) return false;
        if (!this.definition.equals(that.definition)) return false;
        if (!this.exclusiveSet.equals(that.exclusiveSet)) return false;
        return this.effects.equals(that.effects);
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.description, this.definition, this.exclusiveSet, this.effects);
    }

    @Override
    public String toString() {
        return "StaticEnchantmentType{description=" + this.description + ", definition=" + this.definition + ", exclusiveSet=" + this.exclusiveSet + ", effects=" + this.effects + "}";
    }
}
