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

package io.github.retrooper.packetevents.sponge.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import io.github.retrooper.packetevents.sponge.injector.connection.ServerChannelHandler;
import io.github.retrooper.packetevents.sponge.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.sponge.injector.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.sponge.injector.handlers.PacketEventsEncoder;
import io.github.retrooper.packetevents.sponge.util.InjectedList;
import io.github.retrooper.packetevents.sponge.util.SpongeReflectionUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SpongeChannelInjector implements ChannelInjector {
    //Channels that process connecting clients.
    public final Set<Channel> injectedConnectionChannels = new HashSet<>();
    public List<Object> networkManagers;
    private int connectionChannelsListIndex = -1;

    public void updatePlayer(User user, Object player) {
        PacketEvents.getAPI().getEventManager().callEvent(new UserLoginEvent(user, player));
        Object channel = user.getChannel();
        if (channel == null) {
            channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
        }
        setPlayer(channel, player);
    }

    @Override
    public boolean isServerBound() {
        //We want to check if the server has been bound to the port already.
        Object serverConnection = SpongeReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            //There should only be 2 lists.
            for (int i = 0; i < 2; i++) {
                List<?> list = reflectServerConnection.readList(i);
                for (Object value : list) {
                    if (value instanceof ChannelFuture) {
                        connectionChannelsListIndex = i;
                        //Found the right list.
                        //It has connection channels, so the server has been bound.
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void inject() {
        Object serverConnection = SpongeReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(connectionChannelsListIndex);
            InjectedList<ChannelFuture> wrappedList = new InjectedList<>(connectionChannelFutures, future -> {
                //Each time a channel future is added, we run this.
                //This is automatically also ran for the elements already added before we wrapped the list.
                Channel channel = future.channel();
                //Inject into the server connection channel.
                injectServerChannel(channel);
                //Make sure to store it, so we can uninject later on.
                injectedConnectionChannels.add(channel);
            });

            //Replace the list with our wrapped one.
            reflectServerConnection.writeList(connectionChannelsListIndex, wrappedList);

            //Player channels might have been registered already. Let us add our handlers. We are a little late though.
            if (networkManagers == null) {
                networkManagers = SpongeReflectionUtil.getNetworkManagers();
            }

            synchronized (networkManagers) {
                if (!networkManagers.isEmpty()) {
                    PacketEvents.getAPI().getLogManager().debug("Late bind not enabled, injecting into existing channel");
                }

                for (Object networkManager : networkManagers) {
                    ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                    Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                    if (channel == null) {
                        continue;
                    }
                    try {
                        ServerConnectionInitializer.initChannel(channel, ConnectionState.PLAY);
                    } catch (Exception e) {
                        PacketEvents.getAPI().getLogManager().severe("Sponge injector failed to inject into an existing channel.");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void uninject() {
        //Uninject our connection handler from these connection channels.
        for (Channel connectionChannel : injectedConnectionChannels) {
            uninjectServerChannel(connectionChannel);
        }

        injectedConnectionChannels.clear();
        Object serverConnection = SpongeReflectionUtil.getMinecraftServerConnectionInstance();
        if (serverConnection != null) {
            ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
            List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(connectionChannelsListIndex);
            if (connectionChannelFutures instanceof InjectedList) {
                //Let us unwrap this. We no longer want to listen to connecting channels.
                reflectServerConnection.writeList(connectionChannelsListIndex,
                        ((InjectedList<ChannelFuture>) connectionChannelFutures).originalList());
            }
        }
    }

    private void injectServerChannel(Channel serverChannel) {
        ChannelPipeline pipeline = serverChannel.pipeline();
        ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_HANDLER_NAME);
        if (connectionHandler != null) {
            //Why does it already exist? Remove it.
            pipeline.remove(PacketEvents.CONNECTION_HANDLER_NAME);
        }
        //Some forks add a handler which adds the other necessary vanilla handlers like (decoder, encoder, etc...)
        else if (pipeline.get("MinecraftPipeline#0") != null) {
            pipeline.addAfter("MinecraftPipeline#0", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
        }
        //Otherwise, make sure we are first.
        else {
            pipeline.addFirst(PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
        }

        if (networkManagers == null) {
            networkManagers = SpongeReflectionUtil.getNetworkManagers();
        }

        //Make sure we handled all connected clients.
        synchronized (networkManagers) {
            for (Object networkManager : networkManagers) {
                ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
                Channel channel = networkManagerWrapper.readObject(0, Channel.class);
                if (channel.isOpen()) {
                    if (channel.localAddress().equals(serverChannel.localAddress())) {
                        channel.close();
                    }
                }
            }
        }
    }

    private void uninjectServerChannel(Channel serverChannel) {
        if (serverChannel.pipeline().get(PacketEvents.CONNECTION_HANDLER_NAME) != null) {
            serverChannel.pipeline().remove(PacketEvents.CONNECTION_HANDLER_NAME);
        } else {
            PacketEvents.getAPI().getLogManager().warn("Failed to uninject server channel, handler not found");
        }
    }

    @Override
    public void updateUser(Object channel, User user) {
        PacketEventsEncoder encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.user = user;
        }

        PacketEventsDecoder decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.user = user;
        }
    }

    @Override
    public void setPlayer(Object channel, Object player) {
        final ServerPlayer spongePlayer = (ServerPlayer) player;
        PacketEventsEncoder encoder = getEncoder((Channel) channel);
        if (encoder != null) {
            encoder.player = spongePlayer.uniqueId();
        }

        PacketEventsDecoder decoder = getDecoder((Channel) channel);
        if (decoder != null) {
            decoder.playerId = spongePlayer.uniqueId();
            decoder.user.getProfile().setName(spongePlayer.name());
            decoder.user.getProfile().setUUID(spongePlayer.uniqueId());
        }
    }

    private PacketEventsEncoder getEncoder(Channel channel) {
        return (PacketEventsEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
    }

    private PacketEventsDecoder getDecoder(Channel channel) {
         return (PacketEventsDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
    }

    @Override
    public boolean isProxy() {
        return false;
    }
}
