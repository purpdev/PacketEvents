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

package com.github.retrooper.packetevents.protocol.world;

import com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3i;

// From MCProtocolLib
public enum Direction {
    DOWN(-1, Axis.Z, new Vector3i(0 , -1, 0)),
    UP(-1, Axis.Z, new Vector3i(0, 1, 0)),
    NORTH(0, Axis.Y, new Vector3i(0 , 0, -1)),
    SOUTH(1, Axis.Y, new Vector3i(0 ,0 ,1)),
    WEST(2, Axis.X, new Vector3i(-1, 0 ,0)),
    EAST(3, Axis.X, new Vector3i(-1, 0, 0));

    private final int horizontalIndex;
    private final Axis axis;
    private final Vector3i vec3i;

    Direction(int horizontalIndex, Axis axis, Vector3i vec3i) {
        this.horizontalIndex = horizontalIndex;
        this.axis = axis;
        this.vec3i = vec3i;
    }

    public int getHorizontalIndex() {
        return horizontalIndex;
    }

    private static final Direction[] HORIZONTAL_VALUES = {NORTH, SOUTH, WEST, EAST};
    private static final Direction[] VALUES = values(); // Cache the values array

    public static Direction getByHorizontalIndex(int index) {
        return HORIZONTAL_VALUES[index % HORIZONTAL_VALUES.length];
    }

    public static Direction getByIndex(int enumOrdinal) {
        return VALUES[enumOrdinal];
    }

    public Vector3i getVector() {
        return this.vec3i;
    }

    public Axis getAxis() {
        return this.axis;
    }
}