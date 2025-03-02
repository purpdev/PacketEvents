/*
 * This file is part of ViaVersion - https://github.com/ViaVersion/ViaVersion
 * Copyright (C) 2016-2022 ViaVersion and contributors
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
package io.github.retrooper.packetevents.injector;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import io.github.retrooper.packetevents.handlers.PacketEventsDecoder;
import io.github.retrooper.packetevents.handlers.PacketEventsEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.lang.reflect.Field;
import java.util.Set;

//Thanks to ViaVersion for helping us design this injector.
public class BungeePipelineInjector implements ChannelInjector {
    private static final Field LISTENERS_FIELD;

    static {
        LISTENERS_FIELD = Reflection.getField(ProxyServer.getInstance().getClass(), "listeners");
        LISTENERS_FIELD.setAccessible(true);
    }

    public void injectChannel(Channel channel) {
        Field initializerField = null;
        ChannelHandler bootstrapAcceptor = null;
        for (String channelName : channel.pipeline().names()) {
            if (channelName.contains("QueryHandler")) {
                return; // query handler, abort injection
            }

            ChannelHandler handler = channel.pipeline().get(channelName);
            if (handler == null) continue;
            try {
                Field f = handler.getClass().getDeclaredField("childHandler");
                f.setAccessible(true);
                bootstrapAcceptor = handler;
                initializerField = f;
            } catch (Exception ignore) {
            }
        }

        if (bootstrapAcceptor == null) {
            bootstrapAcceptor = channel.pipeline().first();
            try {
                initializerField = bootstrapAcceptor.getClass().getDeclaredField("childHandler");
                initializerField.setAccessible(true);
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }

        ChannelInitializer<Channel> newInitializer;
        try {
            newInitializer = new BungeeChannelInitializer(initializerField.get(bootstrapAcceptor));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            initializerField.set(bootstrapAcceptor, newInitializer);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void inject() {
        try {
            Set<Channel> listeners = (Set<Channel>) LISTENERS_FIELD.get(ProxyServer.getInstance());

            for (Channel channel : listeners) {
                injectChannel(channel);
            }

            Set<Channel> wrapper = new SetWrapper<>(listeners, this::injectChannel);
            LISTENERS_FIELD.set(ProxyServer.getInstance(), wrapper);


        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uninject() {
        //Uninjection is not easily possible on BungeeCord.
    }

    @Override
    public void setPlayer(Object ch, Object p) {
        Channel channel = (Channel) ch;
        ProxiedPlayer player = (ProxiedPlayer) p;
        PacketEventsDecoder decoder = (PacketEventsDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.player = player;
        decoder.user.getProfile().setUUID(player.getUniqueId());
        decoder.user.getProfile().setName(player.getName());
        PacketEventsEncoder encoder = (PacketEventsEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.player = player;
    }

    @Override
    public void updateUser(Object ch, User user) {
        Channel channel = (Channel) ch;
        PacketEventsDecoder decoder = (PacketEventsDecoder) channel.pipeline().get(PacketEvents.DECODER_NAME);
        decoder.user = user;
        PacketEventsEncoder encoder = (PacketEventsEncoder) channel.pipeline().get(PacketEvents.ENCODER_NAME);
        encoder.user = user;
    }

    @Override
    public boolean isProxy() {
        return true;
    }
}
