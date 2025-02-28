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

package com.github.retrooper.packetevents.util.mappings;

import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.nbt.serializer.SequentialNBTReader;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.VersionMapper;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TypesBuilder {
    private final String mapPath;
    private Map<ClientVersion, Map<String, Integer>> entries = new HashMap<>();
    private VersionMapper versionMapper;

    @Nullable
    VersionedRegistry<?> registry;

    public TypesBuilder(String mapPath, boolean lazy) {
        this.mapPath = mapPath;
        if (!lazy) {
            load();
        }
    }

    public TypesBuilder(String mapPath) {
        this(mapPath, false);
    }

    public void load() {
        try (final SequentialNBTReader.Compound rootCompound = MappingHelper.decompress("mappings/" + this.mapPath)) {
            rootCompound.skipOne(); // skip version tag for now
            SequentialNBTReader.Compound compound = (SequentialNBTReader.Compound) rootCompound.next().getValue();

            int length = ((NBTNumber) compound.next().getValue()).getAsInt(); // Second tag is the length
            final SequentialNBTReader.Compound entries = (SequentialNBTReader.Compound) compound.next().getValue(); // Third tag are the entries

            final ClientVersion[] versions = new ClientVersion[length];
            final Map.Entry<String, NBT> first = entries.next();
            if (first.getValue().getType() == NBTType.LIST) {
                loadAsArray(first, entries, versions);
            } else {
                loadAsMap(first, entries, versions);
            }

            versionMapper = new VersionMapper(versions);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load mapping files.", e);
        }
    }

    private void loadAsArray(
            final Map.Entry<String, NBT> first,
            final SequentialNBTReader.Compound entries,
            final ClientVersion[] versions
    ) {
        final ClientVersion start = ClientVersion.valueOf(first.getKey());
        versions[0] = start;
        final List<String> lastEntries = new ArrayList<>();
        for (NBT entry : ((SequentialNBTReader.List) first.getValue())) {
            lastEntries.add(((NBTString) entry).getValue());
        }

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < lastEntries.size(); i++) {
                map.put(lastEntries.get(i), i);
            }
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        int i = 1;
        for (Map.Entry<String, NBT> entry : entries) {
            final ClientVersion version = ClientVersion.valueOf(entry.getKey());
            versions[i++] = version;
            final List<ListDiff<String>> diff = MappingHelper.createListDiff((SequentialNBTReader.Compound) entry.getValue());

            for (int j = diff.size() - 1; j >= 0; j--) {
                diff.get(j).applyTo(lastEntries);
            }
            mapLoader.accept(version);
        }
    }

    private void loadAsMap(
            final Map.Entry<String, NBT> first,
            final SequentialNBTReader.Compound entries,
            final ClientVersion[] versions
    ) {
        final ClientVersion start = ClientVersion.valueOf(first.getKey());
        versions[0] = start;
        final Map<String, Integer> lastEntries = StreamSupport.stream(((SequentialNBTReader.Compound) first.getValue()).spliterator(), false)
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> ((NBTNumber) entry.getValue()).getAsInt()));

        final Consumer<ClientVersion> mapLoader = version -> {
            final Map<String, Integer> map = new HashMap<>(lastEntries);
            this.entries.put(version, map);
        };
        mapLoader.accept(start);

        int i = 1;
        for (Map.Entry<String, NBT> entry : entries) {
            final ClientVersion version = ClientVersion.valueOf(entry.getKey());
            versions[i++] = version;
            final List<MapDiff<String, Integer>> diff = MappingHelper.createDiff((SequentialNBTReader.Compound) entry.getValue());

            for (MapDiff<String, Integer> d : diff) {
                d.applyTo(lastEntries);
            }
            mapLoader.accept(version);
        }
    }

    @ApiStatus.Internal
    public @Nullable VersionedRegistry<?> getRegistry() {
        return this.registry;
    }

    public ClientVersion[] getVersions() {
        return versionMapper.getVersions();
    }

    public ClientVersion[] getReversedVersions() {
        return versionMapper.getReversedVersions();
    }

    public int getDataIndex(ClientVersion rawVersion) {
        return versionMapper.getIndex(rawVersion);
    }

    boolean isMappingDataLoaded() {
        return this.entries != null;
    }

    @ApiStatus.Internal
    public void unloadFileMappings() {
        entries.clear();
        entries = null;
    }

    public TypesBuilderData define(String key) {
        final ResourceLocation name = new ResourceLocation(key);
        final int[] ids = new int[getVersions().length];
        int index = 0;
        for (ClientVersion v : getVersions()) {
            final Map<String, Integer> map = entries.get(v);
            if (map.containsKey(key)) {
                int id = map.get(key);
                ids[index] = id;
            } else {
                ids[index] = -1;
            }
            index++;
        }
        return new TypesBuilderData(this, name, ids);
    }
}
