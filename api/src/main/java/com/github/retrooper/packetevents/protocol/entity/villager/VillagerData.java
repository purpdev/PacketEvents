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

package com.github.retrooper.packetevents.protocol.entity.villager;

import com.github.retrooper.packetevents.protocol.entity.villager.level.VillagerLevel;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfession;
import com.github.retrooper.packetevents.protocol.entity.villager.profession.VillagerProfessions;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerType;
import com.github.retrooper.packetevents.protocol.entity.villager.type.VillagerTypes;
import org.jetbrains.annotations.Nullable;

public class VillagerData {

    private VillagerType type;
    private VillagerProfession profession;
    private int level;

    public VillagerData(VillagerType type, VillagerProfession profession, VillagerLevel level) {
        this(type, profession, level.getId());
    }

    public VillagerData(VillagerType type, VillagerProfession profession, int level) {
        this.type = type;
        this.profession = profession;
        this.level = level;
    }

    @Deprecated
    public VillagerData(int typeId, int professionId, int level) {
        this(VillagerTypes.getById(typeId), VillagerProfessions.getById(professionId), level);
    }

    public VillagerType getType() {
        return type;
    }

    public void setType(VillagerType type) {
        this.type = type;
    }

    public VillagerProfession getProfession() {
        return profession;
    }

    public void setProfession(VillagerProfession profession) {
        this.profession = profession;
    }

    public int getLevel() {
        return this.level;
    }

    public @Nullable VillagerLevel getVillagerLevel() {
        return VillagerLevel.getById(this.level);
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLevel(VillagerLevel level) {
        this.level = level.getId();
    }
}
