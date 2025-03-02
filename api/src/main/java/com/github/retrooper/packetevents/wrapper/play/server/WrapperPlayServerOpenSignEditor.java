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

public class WrapperPlayServerOpenSignEditor extends PacketWrapper<WrapperPlayServerOpenSignEditor> {
    private Vector3i position;
    private boolean isFrontText;

    public WrapperPlayServerOpenSignEditor(PacketSendEvent event) {
        super(event);
    }

    public WrapperPlayServerOpenSignEditor(Vector3i position, boolean isFrontText) {
        super(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        this.position = position;
        this.isFrontText = isFrontText;
    }

    @Override
    public void read() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            this.position = new Vector3i(readLong(), this.serverVersion);
        } else {
            int x = readInt();
            int y = readInt();
            int z = readInt();
            this.position = new Vector3i(x, y, z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
            isFrontText = readBoolean();
        } else {
            isFrontText = true;
        }
    }

    @Override
    public void write() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            long positionVector = position.getSerializedPosition(this.serverVersion);
            writeLong(positionVector);
        } else {
            writeInt(position.x);
            writeInt(position.y);
            writeInt(position.z);
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20)) {
            writeBoolean(isFrontText);
        }
    }

    @Override
    public void copy(WrapperPlayServerOpenSignEditor wrapper) {
        this.position = wrapper.position;
        this.isFrontText = wrapper.isFrontText;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public boolean isFrontText() {
        return isFrontText;
    }

    public void setFrontText(boolean frontText) {
        isFrontText = frontText;
    }
}
