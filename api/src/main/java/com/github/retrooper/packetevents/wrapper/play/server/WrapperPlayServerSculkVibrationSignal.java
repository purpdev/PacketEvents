/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2021 retrooper and contributors
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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WrapperPlayServerSculkVibrationSignal extends PacketWrapper<WrapperPlayServerSculkVibrationSignal> {
    private Vector3i sourcePosition;
    private ResourceLocation destinationIdentifier;
    private @Nullable Vector3i blockPosition;
    private int entityId;
    private int arrivalTicks;

    public WrapperPlayServerSculkVibrationSignal(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSculkVibrationSignal(Vector3i sourcePosition, ResourceLocation destinationIdentifier, Vector3i blockPosition, int entityId, int arrivalTicks) {
        super(PacketType.Play.Server.SCULK_VIBRATION_SIGNAL);
        this.sourcePosition = sourcePosition;
        this.destinationIdentifier = destinationIdentifier;
        this.blockPosition = blockPosition;
        this.entityId = entityId;
        this.arrivalTicks = arrivalTicks;
    }

    @Override
    public void read() {
        this.sourcePosition = new Vector3i(readLong());
        this.destinationIdentifier = readIdentifier();
        if (this.destinationIdentifier.getKey().contains("block")) {
            this.blockPosition = new Vector3i(readLong());
        } else {
            this.entityId = readVarInt();
        }
        this.arrivalTicks = readVarInt();
    }

    @Override
    public void write() {
        writeLong(this.sourcePosition.getSerializedPosition());
        writeIdentifier(this.destinationIdentifier);
        if (this.destinationIdentifier.getKey().contains("block")) {
            writeLong(this.blockPosition.getSerializedPosition());
        } else {
            writeVarInt(this.entityId);
        }
        writeVarInt(this.arrivalTicks);
    }

    @Override
    public void copy(WrapperPlayServerSculkVibrationSignal wrapper) {
        this.sourcePosition = wrapper.sourcePosition;
        this.destinationIdentifier = wrapper.destinationIdentifier;
        this.blockPosition = wrapper.blockPosition;
        this.entityId = wrapper.entityId;
        this.arrivalTicks = wrapper.arrivalTicks;
    }

    public Vector3i getSourcePosition() {
        return sourcePosition;
    }

    public void setSourcePosition(Vector3i sourcePosition) {
        this.sourcePosition = sourcePosition;
    }

    public ResourceLocation getDestinationIdentifier() {
        return destinationIdentifier;
    }

    public void setDestinationIdentifier(ResourceLocation destinationIdentifier) {
        this.destinationIdentifier = destinationIdentifier;
    }

    public Optional<Vector3i> getBlockPosition() {
        return Optional.ofNullable(blockPosition);
    }

    public void setBlockPosition(@Nullable Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public int getArrivalTicks() {
        return arrivalTicks;
    }

    public void setArrivalTicks(int arrivalTicks) {
        this.arrivalTicks = arrivalTicks;
    }
}
