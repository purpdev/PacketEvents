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

package com.github.retrooper.packetevents.wrapper.play.client;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.recipe.RecipeState;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public class WrapperPlayClientRecipeBookData extends PacketWrapper<WrapperPlayClientRecipeBookData> {
    private RecipeState state;
    private @Nullable String recipeIdModern;
    private int recipeIdLegacy;
    private boolean craftingRecipeBookOpen;
    private boolean craftingRecipeBookFilterActive;
    private boolean smeltingRecipeBookOpen;
    private boolean smeltingRecipeBookFilterActive;
    private boolean blastFurnaceRecipeBookOpen;
    private boolean blastFurnaceRecipeBookFilterActive;
    private boolean smokerRecipeBookOpen;
    private boolean smokerRecipeBookFilterActive;

    public WrapperPlayClientRecipeBookData(PacketReceiveEvent event) {
        super(event);
    }

    public WrapperPlayClientRecipeBookData(RecipeState state, int recipeIdLegacy, boolean craftingRecipeBookOpen, boolean craftingRecipeBookFilterActive) {
        super(PacketType.Play.Client.RECIPE_BOOK_DATA);
        this.state = state;
        this.recipeIdLegacy = recipeIdLegacy;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
    }

    public WrapperPlayClientRecipeBookData(RecipeState state, @Nullable String recipeIdModern, boolean craftingRecipeBookOpen,
                                           boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen, boolean smeltingRecipeBookFilterActive) {
        super(PacketType.Play.Client.RECIPE_BOOK_DATA);
        this.state = state;
        this.recipeIdModern = recipeIdModern;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
    }

    public WrapperPlayClientRecipeBookData(RecipeState state, @Nullable String recipeIdModern, boolean craftingRecipeBookOpen,
                                           boolean craftingRecipeBookFilterActive, boolean smeltingRecipeBookOpen, boolean smeltingRecipeBookFilterActive,
                                           boolean blastFurnaceRecipeBookOpen, boolean blastFurnaceRecipeBookFilterActive, boolean smokerRecipeBookOpen,
                                           boolean smokerRecipeBookFilterActive) {
        super(PacketType.Play.Client.RECIPE_BOOK_DATA);
        this.state = state;
        this.recipeIdModern = recipeIdModern;
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
        this.blastFurnaceRecipeBookOpen = blastFurnaceRecipeBookOpen;
        this.blastFurnaceRecipeBookFilterActive = blastFurnaceRecipeBookFilterActive;
        this.smokerRecipeBookOpen = smokerRecipeBookOpen;
        this.smokerRecipeBookFilterActive = smokerRecipeBookFilterActive;
    }

    @Override
    public void read() {
        this.state = RecipeState.VALUES[readVarInt()];
        if (this.state == RecipeState.DISPLAYED_RECIPE) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.recipeIdModern = readString();
            } else {
                this.recipeIdLegacy = readVarInt();
            }
        } else {
            this.craftingRecipeBookOpen = readBoolean();
            this.craftingRecipeBookFilterActive = readBoolean();
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                this.smeltingRecipeBookOpen = readBoolean();
                this.smeltingRecipeBookFilterActive = readBoolean();
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
                this.blastFurnaceRecipeBookOpen = readBoolean();
                this.blastFurnaceRecipeBookFilterActive = readBoolean();
                this.smokerRecipeBookOpen = readBoolean();
                this.smokerRecipeBookFilterActive = readBoolean();
            }
        }
    }

    @Override
    public void write() {
        writeVarInt(this.state.ordinal());
        if (this.state == RecipeState.DISPLAYED_RECIPE) {
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                writeString(this.recipeIdModern);
            } else {
                writeVarInt(this.recipeIdLegacy);
            }
        } else {
            writeBoolean(this.craftingRecipeBookOpen);
            writeBoolean(this.craftingRecipeBookFilterActive);
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13)) {
                writeBoolean(this.smeltingRecipeBookOpen);
                writeBoolean(this.smeltingRecipeBookFilterActive);
            }
            if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
                writeBoolean(this.blastFurnaceRecipeBookOpen);
                writeBoolean(this.blastFurnaceRecipeBookFilterActive);
                writeBoolean(this.smokerRecipeBookOpen);
                writeBoolean(this.smokerRecipeBookFilterActive);
            }
        }
    }

    @Override
    public void copy(WrapperPlayClientRecipeBookData wrapper) {
        this.state = wrapper.state;
        this.recipeIdModern = wrapper.recipeIdModern;
        this.recipeIdLegacy = wrapper.recipeIdLegacy;
        this.craftingRecipeBookOpen = wrapper.craftingRecipeBookOpen;
        this.craftingRecipeBookFilterActive = wrapper.craftingRecipeBookFilterActive;
        this.smeltingRecipeBookOpen = wrapper.smeltingRecipeBookOpen;
        this.smeltingRecipeBookFilterActive = wrapper.smeltingRecipeBookFilterActive;
        this.blastFurnaceRecipeBookOpen = wrapper.blastFurnaceRecipeBookOpen;
        this.blastFurnaceRecipeBookFilterActive = wrapper.blastFurnaceRecipeBookFilterActive;
        this.smokerRecipeBookOpen = wrapper.smokerRecipeBookOpen;
        this.smokerRecipeBookFilterActive = wrapper.smokerRecipeBookFilterActive;
    }

    public RecipeState getState() {
        return state;
    }

    public void setState(RecipeState state) {
        this.state = state;
    }

    public Optional<String> getRecipeIdModern() {
        return Optional.ofNullable(recipeIdModern);
    }

    public void setRecipeIdModern(@Nullable String recipeIdModern) {
        this.recipeIdModern = recipeIdModern;
    }

    public OptionalInt getRecipeIdLegacy() {
        return OptionalInt.of(recipeIdLegacy);
    }

    public void setRecipeIdLegacy(int recipeIdLegacy) {
        this.recipeIdLegacy = recipeIdLegacy;
    }

    public boolean isCraftingRecipeBookOpen() {
        return craftingRecipeBookOpen;
    }

    public void setCraftingRecipeBookOpen(boolean craftingRecipeBookOpen) {
        this.craftingRecipeBookOpen = craftingRecipeBookOpen;
    }

    public boolean isCraftingRecipeBookFilterActive() {
        return craftingRecipeBookFilterActive;
    }

    public void setCraftingRecipeBookFilterActive(boolean craftingRecipeBookFilterActive) {
        this.craftingRecipeBookFilterActive = craftingRecipeBookFilterActive;
    }

    public boolean isSmeltingRecipeBookOpen() {
        return smeltingRecipeBookOpen;
    }

    public void setSmeltingRecipeBookOpen(boolean smeltingRecipeBookOpen) {
        this.smeltingRecipeBookOpen = smeltingRecipeBookOpen;
    }

    public boolean isSmeltingRecipeBookFilterActive() {
        return smeltingRecipeBookFilterActive;
    }

    public void setSmeltingRecipeBookFilterActive(boolean smeltingRecipeBookFilterActive) {
        this.smeltingRecipeBookFilterActive = smeltingRecipeBookFilterActive;
    }

    public boolean isBlastFurnaceRecipeBookOpen() {
        return blastFurnaceRecipeBookOpen;
    }

    public void setBlastFurnaceRecipeBookOpen(boolean blastFurnaceRecipeBookOpen) {
        this.blastFurnaceRecipeBookOpen = blastFurnaceRecipeBookOpen;
    }

    public boolean isBlastFurnaceRecipeBookFilterActive() {
        return blastFurnaceRecipeBookFilterActive;
    }

    public void setBlastFurnaceRecipeBookFilterActive(boolean blastFurnaceRecipeBookFilterActive) {
        this.blastFurnaceRecipeBookFilterActive = blastFurnaceRecipeBookFilterActive;
    }

    public boolean isSmokerRecipeBookOpen() {
        return smokerRecipeBookOpen;
    }

    public void setSmokerRecipeBookOpen(boolean smokerRecipeBookOpen) {
        this.smokerRecipeBookOpen = smokerRecipeBookOpen;
    }

    public boolean isSmokerRecipeBookFilterActive() {
        return smokerRecipeBookFilterActive;
    }

    public void setSmokerRecipeBookFilterActive(boolean smokerRecipeBookFilterActive) {
        this.smokerRecipeBookFilterActive = smokerRecipeBookFilterActive;
    }
}