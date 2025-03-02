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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public final class VersionedRegistry<T extends MappedEntity> implements IRegistry<T> {

    private final ResourceLocation registryKey;
    private final TypesBuilder typesBuilder;

    private final Map<String, T> typeMap = new HashMap<>();
    private final Map<Byte, Map<Integer, T>> typeIdMap = new HashMap<>();

    public VersionedRegistry(String registry) {
        this(registry, "registries/" + registry);
    }

    public VersionedRegistry(String registry, String mappingsPath) {
        this(new ResourceLocation(registry), mappingsPath);
    }

    public VersionedRegistry(ResourceLocation registryKey, String mappingsPath) {
        this.registryKey = registryKey;
        this.typesBuilder = new TypesBuilder(mappingsPath);
        this.typesBuilder.registry = this;
    }

    @ApiStatus.Internal
    public <Z extends T> Z define(String name, Function<TypesBuilderData, Z> builder) {
        Z instance = builder.apply(this.typesBuilder.define(name));
        MappingHelper.registerMapping(this.typesBuilder, this.typeMap, this.typeIdMap, instance);
        return instance;
    }

    @VisibleForTesting
    @ApiStatus.Internal
    public boolean isMappingDataLoaded() {
        return this.typesBuilder.isMappingDataLoaded();
    }

    @ApiStatus.Internal
    public void unloadMappings() {
        this.typesBuilder.unloadFileMappings();
    }

    @Override
    public @Nullable T getByName(ResourceLocation name) {
        return this.typeMap.get(name.toString()); // skip norming call
    }

    @Override
    public @Nullable T getByName(String name) {
        // prepend "minecraft:" prefix if no other namespace has been specified
        return this.typeMap.get(ResourceLocation.normString(name));
    }

    @Override
    public @Nullable T getById(ClientVersion version, int id) {
        int index = this.typesBuilder.getDataIndex(version);
        Map<Integer, T> idMap = this.typeIdMap.get((byte) index);
        return idMap.get(id);
    }

    @Override
    public int getId(MappedEntity entity, ClientVersion version) {
        return entity.getId(version);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<T> getEntries() {
        return Collections.unmodifiableCollection(this.typeMap.values());
    }

    @Override
    public int size() {
        return this.typeMap.size();
    }

    @Override
    public ResourceLocation getRegistryKey() {
        return this.registryKey;
    }

    @Override
    public String toString() {
        return "VersionedRegistry[" + this.registryKey + ']';
    }
}
