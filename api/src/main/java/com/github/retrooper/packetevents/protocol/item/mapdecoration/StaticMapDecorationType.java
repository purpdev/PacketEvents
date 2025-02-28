/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.item.mapdecoration;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public class StaticMapDecorationType extends AbstractMappedEntity implements MapDecorationType {

    private final ResourceLocation assetId;
    private final boolean showOnItemFrame;
    private final int mapColor;
    private final boolean explorationMapElement;
    private final boolean trackCount;

    @ApiStatus.Internal
    public StaticMapDecorationType(
            @Nullable TypesBuilderData data,
            ResourceLocation assetId, boolean showOnItemFrame, int mapColor,
            boolean explorationMapElement, boolean trackCount
    ) {
        super(data);
        this.assetId = assetId;
        this.showOnItemFrame = showOnItemFrame;
        this.mapColor = mapColor;
        this.explorationMapElement = explorationMapElement;
        this.trackCount = trackCount;
    }

    @Override
    public ResourceLocation getAssetId() {
        return this.assetId;
    }

    @Override
    public boolean isShowOnItemFrame() {
        return this.showOnItemFrame;
    }

    @Override
    public int getMapColor() {
        return this.mapColor;
    }

    @Override
    public boolean isExplorationMapElement() {
        return this.explorationMapElement;
    }

    @Override
    public boolean isTrackCount() {
        return this.trackCount;
    }
}
