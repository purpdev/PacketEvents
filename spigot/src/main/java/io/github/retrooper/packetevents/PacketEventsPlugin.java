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

package io.github.retrooper.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.event.simple.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.util.TimeStampMode;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSystemChatMessage;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketEventsPlugin extends JavaPlugin {
    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        //Register your listeners
        PacketEvents.getAPI().getSettings().debug(false).checkForUpdates(true).timeStampMode(TimeStampMode.MILLIS).reEncodeByDefault(true);
        PacketEvents.getAPI().init();

        SimplePacketListenerAbstract listener = new SimplePacketListenerAbstract(PacketListenerPriority.HIGH) {
            @Override
            public void onPacketLoginSend(PacketLoginSendEvent event) {
            }

            @Override
            public void onPacketConfigReceive(PacketConfigReceiveEvent event) {
            }

            @Override
            public void onPacketConfigSend(PacketConfigSendEvent event) {
            }

            @Override
            public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
                if (event.getPacketType() == PacketType.Play.Client.INTERACT_ENTITY) {
                    WrapperPlayClientInteractEntity interaction = new WrapperPlayClientInteractEntity(event);
                    if (interaction.getAction() == WrapperPlayClientInteractEntity.InteractAction.ATTACK) {
                        Player player = event.getPlayer();
                        WrapperPlayServerBlockChange blockChange = new WrapperPlayServerBlockChange(SpigotConversionUtil
                                .fromBukkitLocation(player.getLocation()).getPosition().toVector3i().subtract(0, 1, 0),
                                StateTypes.COAL_BLOCK.createBlockState().getGlobalId());

                        event.getUser().sendPacket(blockChange);
                    }
                }
            }   

            @Override
            public void onPacketPlaySend(PacketPlaySendEvent event) {
                if (event.getPacketType() == PacketType.Play.Server.BLOCK_CHANGE) {
                    WrapperPlayServerBlockChange bc = new WrapperPlayServerBlockChange(event);
                    ((Player) event.getPlayer()).sendMessage("Type: " + bc.getBlockState().getType().getName());
                } else if (event.getPacketType() == PacketType.Play.Server.SYSTEM_CHAT_MESSAGE) {
                    WrapperPlayServerSystemChatMessage packet = new WrapperPlayServerSystemChatMessage(event);
                    System.out.println("System chat message: " + AdventureSerializer.asVanilla(packet.getMessage()));
                }
            }

            @Override
            public void onUserConnect(UserConnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " connected...");
            }

            @Override
            public void onUserLogin(UserLoginEvent event) {
                PacketEvents.getAPI().getLogManager().debug("You logged in! User name: " + event.getUser().getProfile().getName());
            }

            @Override
            public void onUserDisconnect(UserDisconnectEvent event) {
                PacketEvents.getAPI().getLogManager().debug("User: (host-name) " + event.getUser().getAddress().getHostString() + " disconnected...");
            }
        };
//        PacketEvents.getAPI().getEventManager().registerListener(listener);
    }

    @Override
    public void onDisable() {
        PacketEvents.getAPI().terminate();
    }
}
