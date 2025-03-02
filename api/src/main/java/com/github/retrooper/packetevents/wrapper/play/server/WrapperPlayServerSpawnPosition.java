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
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.Optional;

public class WrapperPlayServerSpawnPosition extends PacketWrapper<WrapperPlayServerSpawnPosition> {
    private Vector3i position;
    private Float angle;

    public WrapperPlayServerSpawnPosition(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerSpawnPosition(Vector3i position) {
        super(PacketType.Play.Server.SPAWN_POSITION);
        this.position = position;
    }

    public WrapperPlayServerSpawnPosition(Vector3i position, float angle) {
        super(PacketType.Play.Server.SPAWN_POSITION);
        this.position = position;
        this.angle = angle;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.position = new Vector3i(readLong(), this.serverVersion);
        } else {
            int x = readInt();
            int y = readShort();
            int z = readInt();
            this.position = new Vector3i(x, y, z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            this.angle = readFloat();
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = this.position.getSerializedPosition(this.serverVersion);
            writeLong(positionVector);
        } else {
            writeInt(this.position.x);
            writeShort(this.position.y);
            writeInt(this.position.z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
            writeFloat(this.angle);
        }
    }

    @Override
    public void copy(WrapperPlayServerSpawnPosition wrapper) {
        this.position = wrapper.position;
        this.angle = wrapper.angle;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public Optional<Float> getAngle() {
        return Optional.ofNullable(angle);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
