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

package io.github.retrooper.packetevents.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.FakeChannelUtil;
import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    /**
     * @reason Associate connection instance with player instance
     */
    @Inject(
            method = "placeNewPlayer",
            at = @At("HEAD")
    )
    private void preNewPlayerPlace(
            Connection connection, ServerPlayer player,
            CommonListenerCookie cookie, CallbackInfo ci
    ) {
        PacketEvents.getAPI().getInjector().setPlayer(connection.channel, player);
    }

    /**
     * @reason Associate connection instance with player instance and handle login event
     */
    @Inject(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastAll(Lnet/minecraft/network/protocol/Packet;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onPlayerLogin(
        Connection connection, ServerPlayer player,
        CommonListenerCookie cookie, CallbackInfo ci
    ) {
        PacketEventsAPI<?> api = PacketEvents.getAPI();

        User user = api.getPlayerManager().getUser(player);
        if (user == null) {
            Object channelObj = api.getPlayerManager().getChannel(player);

            // Check if it's a fake connection
            if (!FakeChannelUtil.isFakeChannel(channelObj) &&
                (!api.isTerminated() || api.getSettings().isKickIfTerminated())) {
                // Kick the player if they're not a fake player
                player.connection.disconnect(Component.literal("PacketEvents 2.0 failed to inject"));
            }
            return;
        }

        api.getEventManager().callEvent(new UserLoginEvent(user, player));
    }

    /**
     * @reason Minecraft creates a new player instance on respawn
     */
    @Inject(
            method = "respawn",
            at = @At("RETURN")
    )
    private void postRespawn(CallbackInfoReturnable<ServerPlayer> cir) {
        ServerPlayer player = cir.getReturnValue();
        Channel channel = player.connection.connection.channel;
        PacketEvents.getAPI().getInjector().setPlayer(channel, player);
    }
}
