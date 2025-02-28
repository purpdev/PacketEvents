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

package io.github.retrooper.packetevents.velocity.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.event.UserLoginEvent;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.network.ProtocolVersion;
import com.velocitypowered.api.plugin.PluginContainer;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.ServerConnection;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;
import io.github.retrooper.packetevents.injector.VelocityPipelineInjector;
import io.github.retrooper.packetevents.manager.PlayerManagerImpl;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Level;

public class VelocityPacketEventsBuilder {
    private static PacketEventsAPI<PluginContainer> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<PluginContainer> build(ProxyServer server, PluginContainer plugin, Logger logger, Path dataDirectory) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(server, plugin, logger, dataDirectory);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<PluginContainer> build(ProxyServer server, PluginContainer plugin, Logger logger, Path dataDirectory,
                                                         PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(server, plugin, logger, dataDirectory, settings);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<PluginContainer> buildNoCache(ProxyServer server, PluginContainer plugin, Logger logger, Path dataDirectory) {
        return buildNoCache(server, plugin, logger, dataDirectory, new PacketEventsSettings());
    }

    public static PacketEventsAPI<PluginContainer> buildNoCache(ProxyServer server, PluginContainer plugin, Logger logger, Path dataDirectory,
                                                                PacketEventsSettings inSettings) {
        return new PacketEventsAPI<PluginContainer>() {
            private final PacketEventsSettings settings = inSettings;
            // TODO Implement platform version
            private final ProtocolManager protocolManager = new ProtocolManagerAbstract() {
                @Override
                public com.github.retrooper.packetevents.protocol.ProtocolVersion getPlatformVersion() {
                    return com.github.retrooper.packetevents.protocol.ProtocolVersion.UNKNOWN;
                }
            };
            private final ServerManager serverManager = new ServerManagerAbstract() {

                private final Field connectionInFlight, registeredServer;
                private ServerVersion version;

                {
                    try {
                        Class<?> playerClass = Class.forName("com.velocitypowered.proxy.connection.client.ConnectedPlayer");
                        this.connectionInFlight = playerClass.getDeclaredField("connectionInFlight");
                        this.connectionInFlight.setAccessible(true);
                        Class<?> serverConnectionClass = Class.forName("com.velocitypowered.proxy.connection.backend.VelocityServerConnection");
                        this.registeredServer = serverConnectionClass.getDeclaredField("registeredServer");
                        this.registeredServer.setAccessible(true);
                    } catch (ReflectiveOperationException exception) {
                        throw new RuntimeException("Error while resolving methods for getting player server connection");
                    }
                }

                @Override
                public ServerVersion getVersion() {
                    if (this.version == null) {
                        ProtocolVersion[] velVers = ProtocolVersion.values();
                        for (int i = velVers.length - 1; i >= 0; i--) {
                            ProtocolVersion velVer = velVers[i];
                            if (velVer.isLegacy() || velVer.isUnknown()) {
                                continue; // skip
                            }
                            String velVerStr = velVer.getVersionIntroducedIn();
                            for (ServerVersion peVer : ServerVersion.values()) {
                                if (peVer.getReleaseName().contains(velVerStr)) {
                                    this.version = peVer;
                                    return peVer;
                                }
                            }
                        }
                        throw new IllegalStateException("Can't find any version compatible with this velocity instance");
                    }
                    return this.version;
                }

                private @Nullable Object getTargetServer(Player player) {
                    ServerConnection server = player.getCurrentServer().orElse(null);
                    if (server != null) {
                        return server;
                    }
                    try {
                        Object connection = this.connectionInFlight.get(player);
                        return connection != null ? this.registeredServer.get(connection) : null;
                    } catch (IllegalAccessException exception) {
                        throw new RuntimeException(exception);
                    }
                }

                @Override
                public Object getRegistryCacheKey(User user, ClientVersion version) {
                    Player player = server.getPlayer(user.getUUID()).orElse(null);
                    return player == null ? null : Objects.hash(this.getTargetServer(player), version);
                }
            };

            private final PlayerManagerAbstract playerManager = new PlayerManagerImpl();

            private final ChannelInjector injector = new VelocityPipelineInjector(server);
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final LogManager logManager = new LogManager() {
                @Override
                protected void log(Level level, @Nullable NamedTextColor color, String message) {
                    System.out.println(message);
                }
            };
            private boolean loaded;
            private boolean initialized;
            private boolean terminated;

            @Override
            public void load() {
                if (!loaded) {
                    String id = plugin.getDescription().getId();
                    // Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;
                    injector.inject();

                    loaded = true;

                    // Register internal packet listener (should be the first listener)
                    // This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalPacketListener());
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void init() {
                // Load if we haven't loaded already
                load();
                if (initialized) return;

                server.getEventManager().register(plugin.getInstance().orElse(null), PostLoginEvent.class,
                        (event) -> {
                            Player player = event.getPlayer();
                            Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
                            // This only happens if a player is a fake player
                            if (channel == null) {
                                return;
                            }
                            PacketEvents.getAPI().getInjector().setPlayer(channel, player);

                            User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                            if (user == null)
                                return;

                            UserLoginEvent loginEvent = new UserLoginEvent(user, player);
                            PacketEvents.getAPI().getEventManager().callEvent(loginEvent);
                        });
                if (settings.shouldCheckForUpdates()) {
                    getUpdateChecker().handleUpdateCheck();
                }

                PacketType.Play.Client.load();
                PacketType.Play.Server.load();
                initialized = true;
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    // Eject the injector if needed(depends on the injector implementation)
                    injector.uninject();
                    // Unregister all our listeners
                    getEventManager().unregisterAllListeners();
                    initialized = false;
                    terminated = true;
                }
            }

            @Override
            public boolean isTerminated() {
                return terminated;
            }

            @Override
            public LogManager getLogManager() {
                return logManager;
            }

            @Override
            public PluginContainer getPlugin() {
                return plugin;
            }

            @Override
            public ProtocolManager getProtocolManager() {
                return protocolManager;
            }

            @Override
            public ServerManager getServerManager() {
                return serverManager;
            }

            @Override
            public PlayerManager getPlayerManager() {
                return playerManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }

            @Override
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }
        };
    }
}
