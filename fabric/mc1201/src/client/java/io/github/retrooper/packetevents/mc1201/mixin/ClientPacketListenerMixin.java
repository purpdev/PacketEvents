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

package io.github.retrooper.packetevents.mc1201.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.TickablePacketListener;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin implements TickablePacketListener,
    ClientGamePacketListener {

    @Final @Shadow public Minecraft minecraft;
    @Shadow public abstract Connection getConnection();
    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "handleLogin",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.PUTFIELD,
                    target = "Lnet/minecraft/client/Minecraft;player:Lnet/minecraft/client/player/LocalPlayer;",
                    shift = At.Shift.AFTER
            )
    )
    private void postLoginPlayerConstruct(CallbackInfo ci) {
        PacketEvents.getAPI().getInjector().setPlayer(this.getConnection().channel, this.minecraft.player);
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "handleRespawn",
            at = @At(
                    // inject immediately after new player instance has been created
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;setId(I)V"
            )
    )
    private void postRespawnPlayerConstruct(CallbackInfo ci, @Local(ordinal = 1) LocalPlayer player) {
        PacketEvents.getAPI().getInjector().setPlayer(this.getConnection().channel, player);
    }
}
