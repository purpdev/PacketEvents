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

package com.github.retrooper.packetevents.wrapper.play.server;

import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.Direction;
import com.github.retrooper.packetevents.protocol.world.PaintingType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

// Mostly from MCProtocolLib
public class WrapperPlayServerSpawnPainting extends PacketWrapper<WrapperPlayServerSpawnPainting> {
    private int entityId;
    private UUID uuid;
    private @Nullable PaintingType type;
    private Vector3i position;
    private Direction direction;

    public WrapperPlayServerSpawnPainting(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnPainting(int entityId, Vector3i position, Direction direction) {
        this(entityId, new UUID(0L, 0L), null, position, direction);
    }

    public WrapperPlayServerSpawnPainting(int entityId, UUID uuid, Vector3i position, Direction direction) {
        this(entityId, uuid, null, position, direction);
    }

    public WrapperPlayServerSpawnPainting(int entityId, UUID uuid, @Nullable PaintingType type, Vector3i position, Direction direction) {
        super(PacketType.Play.Server.SPAWN_PAINTING);
        this.entityId = entityId;
        this.uuid = uuid;
        this.type = type;
        this.position = position;
        this.direction = direction;
    }

    @Override
    public void read() {
        this.entityId = readVarInt();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            this.uuid = readUUID();
        } else {
            this.uuid = new UUID(0L, 0L);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            this.type = PaintingType.getById(readVarInt());
        } else {
            this.type = PaintingType.getByTitle(readString(13));
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.position = readBlockPosition();
        } else {
            int x = readInt();
            int y = readInt();
            int z = readInt();
            this.position = new Vector3i(x, y, z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.direction = Direction.getByHorizontalIndex(readUnsignedByte());
        } else {
            this.direction = Direction.getByHorizontalIndex(readInt());
        }
    }

    @Override
    public void write() {
        writeVarInt(this.entityId);
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            writeUUID(this.uuid);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
            writeVarInt(this.type.getId());
        } else {
            writeString(this.type.getTitle(), 13);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = this.position.getSerializedPosition(this.serverVersion);
            writeLong(positionVector);
        } else {
            writeInt(this.position.x);
            writeShort(this.position.y);
            writeInt(this.position.z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            writeByte(this.direction.getHorizontalIndex());
        } else {
            writeInt(this.direction.getHorizontalIndex());
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnPainting wrapper) {
        this.entityId = wrapper.entityId;
        this.uuid = wrapper.uuid;
        this.type = wrapper.type;
        this.position = wrapper.position;
        this.direction = wrapper.direction;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public Optional<PaintingType> getType() {
        return Optional.ofNullable(type);
    }

    public void setType(@Nullable PaintingType type) {
        this.type = type;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
