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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

// From MCProtocolLib
public class WrapperPlayClientUpdateCommandBlock extends PacketWrapper<WrapperPlayClientUpdateCommandBlock> {
    private static final int FLAG_TRACK_OUTPUT = 0x01;
    private static final int FLAG_CONDITIONAL = 0x02;
    private static final int FLAG_AUTOMATIC = 0x04;

    private Vector3i position;
    private String command;
    private CommandBlockMode mode;
    private boolean doesTrackOutput;
    private boolean conditional;
    private boolean automatic;
    // PacketEvents
    private short flags;

    public WrapperPlayClientUpdateCommandBlock(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientUpdateCommandBlock(Vector3i position, String command, CommandBlockMode mode, boolean doesTrackOutput, boolean conditional, boolean automatic) {
        super(PacketType.Play.Client.UPDATE_COMMAND_BLOCK);
        this.position = position;
        this.command = command;
        this.mode = mode;
        this.doesTrackOutput = doesTrackOutput;
        this.conditional = conditional;
        this.automatic = automatic;
    }

    @Override
    public void read() {
        this.position = new Vector3i(readLong(), this.serverVersion);
        this.command = readString();
        this.mode = CommandBlockMode.getById(readVarInt());
        this.flags = readUnsignedByte();
        this.doesTrackOutput = (flags & FLAG_TRACK_OUTPUT) != 0;
        this.conditional = (flags & FLAG_CONDITIONAL) != 0;
        this.automatic = (flags & FLAG_AUTOMATIC) != 0;
    }

    @Override
    public void write() {
        writeLong(position.getSerializedPosition(this.serverVersion));
        writeString(command);
        writeVarInt(mode.ordinal());
        if (this.doesTrackOutput) {
            flags |= FLAG_TRACK_OUTPUT;
        }
        if (this.conditional) {
            flags |= FLAG_CONDITIONAL;
        }
        if (this.automatic) {
            flags |= FLAG_AUTOMATIC;
        }
        writeByte(flags);
    }

    @Override
    public void copy(WrapperPlayClientUpdateCommandBlock wrapper) {
        this.position = wrapper.position;
        this.command = wrapper.command;
        this.mode = wrapper.mode;
        this.doesTrackOutput = wrapper.doesTrackOutput;
        this.conditional = wrapper.conditional;
        this.automatic = wrapper.automatic;
        this.flags = wrapper.flags;
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public CommandBlockMode getMode() {
        return mode;
    }

    public void setMode(CommandBlockMode mode) {
        this.mode = mode;
    }

    public boolean isDoesTrackOutput() {
        return doesTrackOutput;
    }

    public void setDoesTrackOutput(boolean doesTrackOutput) {
        this.doesTrackOutput = doesTrackOutput;
    }

    public boolean isConditional() {
        return conditional;
    }

    public void setConditional(boolean conditional) {
        this.conditional = conditional;
    }

    public boolean isAutomatic() {
        return automatic;
    }

    public void setAutomatic(boolean automatic) {
        this.automatic = automatic;
    }

    public short getFlags() {
        return flags;
    }

    public void setFlags(short flags) {
        this.flags = flags;
    }

    public enum CommandBlockMode {
        SEQUENCE, AUTO, REDSTONE;

        private static final CommandBlockMode[] VALUES = values();

        public static CommandBlockMode getById(int id) {
            return VALUES[id];
        }
    }
}
