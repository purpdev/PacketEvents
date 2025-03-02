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

package com.github.retrooper.packetevents.protocol.chat;

import com.github.retrooper.packetevents.protocol.mapper.AbstractMappedEntity;
import com.github.retrooper.packetevents.util.mappings.TypesBuilderData;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Objects;

public class StaticChatType extends AbstractMappedEntity implements ChatType {

    private final @UnknownNullability("only nullable for 1.19") ChatTypeDecoration chatDecoration;
    private final @Nullable ChatTypeDecoration overlayDecoration;
    private final @UnknownNullability("only nullable for 1.19") ChatTypeDecoration narrationDecoration;
    private final @Nullable NarrationPriority narrationPriority;

    public StaticChatType(
            ChatTypeDecoration chatDecoration,
            ChatTypeDecoration narrationDecoration
    ) {
        this(null, chatDecoration, null, narrationDecoration, null);
    }

    @ApiStatus.Internal
    public StaticChatType(
            @Nullable TypesBuilderData data,
            ChatTypeDecoration chatDecoration,
            ChatTypeDecoration narrationDecoration
    ) {
        this(data, chatDecoration, null, narrationDecoration, null);
    }

    public StaticChatType(
            @UnknownNullability("only nullable for 1.19") ChatTypeDecoration chatDecoration,
            @Nullable ChatTypeDecoration overlayDecoration,
            @UnknownNullability("only nullable for 1.19") ChatTypeDecoration narrationDecoration,
            @Nullable NarrationPriority narrationPriority
    ) {
        this(null, chatDecoration, overlayDecoration, narrationDecoration, narrationPriority);
    }

    @ApiStatus.Internal
    public StaticChatType(
            @Nullable TypesBuilderData data,
            @UnknownNullability("only nullable for 1.19") ChatTypeDecoration chatDecoration,
            @Nullable ChatTypeDecoration overlayDecoration,
            @UnknownNullability("only nullable for 1.19") ChatTypeDecoration narrationDecoration,
            @Nullable NarrationPriority narrationPriority
    ) {
        super(data);
        this.chatDecoration = chatDecoration;
        this.overlayDecoration = overlayDecoration;
        this.narrationDecoration = narrationDecoration;
        this.narrationPriority = narrationPriority;
    }

    @Override
    public ChatType copy(@Nullable TypesBuilderData newData) {
        return new StaticChatType(newData, this.chatDecoration,
                this.overlayDecoration, this.narrationDecoration, this.narrationPriority);
    }

    @Override
    public @UnknownNullability("only nullable for 1.19") ChatTypeDecoration getChatDecoration() {
        return this.chatDecoration;
    }

    @Override
    public @Nullable ChatTypeDecoration getOverlayDecoration() {
        return this.overlayDecoration;
    }

    @Override
    public @UnknownNullability("only nullable for 1.19") ChatTypeDecoration getNarrationDecoration() {
        return this.narrationDecoration;
    }

    @Override
    public @Nullable NarrationPriority getNarrationPriority() {
        return this.narrationPriority;
    }

    @Override
    public boolean deepEquals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StaticChatType)) return false;
        if (!super.equals(obj)) return false;
        StaticChatType that = (StaticChatType) obj;
        if (!Objects.equals(this.chatDecoration, that.chatDecoration)) return false;
        if (!Objects.equals(this.overlayDecoration, that.overlayDecoration)) return false;
        if (!Objects.equals(this.narrationDecoration, that.narrationDecoration)) return false;
        return this.narrationPriority == that.narrationPriority;
    }

    @Override
    public int deepHashCode() {
        return Objects.hash(super.hashCode(), this.chatDecoration, this.overlayDecoration, this.narrationDecoration, this.narrationPriority);
    }

    @Override
    public String toString() {
        return "StaticChatType{chatDecoration=" + this.chatDecoration + ", overlayDecoration=" + this.overlayDecoration + ", narrationDecoration=" + this.narrationDecoration + ", narrationPriority=" + this.narrationPriority + '}';
    }
}
