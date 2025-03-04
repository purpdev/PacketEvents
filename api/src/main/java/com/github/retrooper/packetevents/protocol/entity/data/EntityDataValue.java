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

package com.github.retrooper.packetevents.protocol.entity.data;

import java.util.Objects;

public class EntityDataValue<T> {

    private int index;
    private IEntityDataType<T> type;
    private T value;

    public EntityDataValue(int index, IEntityDataType<T> type, T value) {
        this.index = index;
        this.type = type;
        this.value = value;
    }

    @Deprecated
    public EntityData toLegacy() {
        return new EntityData(this.index, this.type.asLegacy(), this.value);
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public IEntityDataType<T> getType() {
        return this.type;
    }

    public void setType(IEntityDataType<T> type) {
        this.type = type;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof EntityDataValue)) return false;
        EntityDataValue<?> that = (EntityDataValue<?>) obj;
        if (this.index != that.index) return false;
        if (!this.type.equals(that.type)) return false;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.index, this.type, this.value);
    }

    @Override
    public String toString() {
        return "EntityDataValue{index=" + this.index + ", type=" + this.type + ", value=" + this.value + '}';
    }
}
