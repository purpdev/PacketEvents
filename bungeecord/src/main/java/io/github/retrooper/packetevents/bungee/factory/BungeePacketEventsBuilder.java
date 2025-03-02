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

package io.github.retrooper.packetevents.bungee.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.ProtocolVersion;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.protocol.ProtocolManagerAbstract;
import io.github.retrooper.packetevents.impl.netty.manager.server.ServerManagerAbstract;
import io.github.retrooper.packetevents.injector.BungeePipelineInjector;
import io.github.retrooper.packetevents.processor.InternalBungeeProcessor;
import net.kyori.adventure.text.format.NamedTextColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.ProtocolConstants;
import org.bstats.bungeecord.Metrics;
import org.bstats.charts.SimplePie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class BungeePacketEventsBuilder {
    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + '\u00A7' + "[0-9A-FK-ORX]");
    private static PacketEventsAPI<Plugin> INSTANCE;

    public static void clearBuildCache() {
        INSTANCE = null;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(plugin);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
        if (INSTANCE == null) {
            INSTANCE = buildNoCache(plugin, settings);
        }
        return INSTANCE;
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
        return buildNoCache(plugin, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<Plugin>() {
            private final PacketEventsSettings settings = inSettings;
            // TODO: Implement platform version
            private final ProtocolManager protocolManager = new ProtocolManagerAbstract() {
                @Override
                public ProtocolVersion getPlatformVersion() {
                    return ProtocolVersion.UNKNOWN;
                }
            };
            private final ServerManager serverManager = new ServerManagerAbstract() {
                private ServerVersion version;

                @Override
                public ServerVersion getVersion() {
                    if (version == null) {
                        version = ServerVersion.getById(ProtocolConstants.SUPPORTED_VERSION_IDS.get(0));
                        if (version == null) {
                            logManager.warn("PacketEvents currently does not support the protocol version " + ProtocolConstants.SUPPORTED_VERSION_IDS.get(0) + " but will act as if the minecraft version were " + ServerVersion.getLatest().getReleaseName() + "!");
                            version = ServerVersion.getLatest();
                        }
                    }
                    return version;
                }

                @Override
                public Object getRegistryCacheKey(User user, ClientVersion version) {
                    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(user.getUUID());
                    if (player == null) {
                        return null;
                    }
                    Server server = player.getServer();
                    if (server == null) {
                        // seems to be null during server switch or on join,
                        // but only for some specific bungee forks?
                        // BungeeCord would be a lot safer if they were to use nullability annotations...
                        return null;
                    }
                    return Objects.hash(server.getInfo(), version);
                }
            };

            private final PlayerManagerAbstract playerManager = new PlayerManagerAbstract() {
                @Override
                public int getPing(@NotNull Object player) {
                    return ((ProxiedPlayer) player).getPing();
                }

                @Override
                public Object getChannel(@NotNull Object player) {
                    return PacketEvents.getAPI().getProtocolManager().getChannel(((ProxiedPlayer) player).getUniqueId());
                }

                @Override
                public User getUser(@NotNull Object player) {
                    ProxiedPlayer p = (ProxiedPlayer) player;
                    Object channel = getChannel(p);
                    User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);

                    // Creating a user that is offline will memory leak
                    if (channel == null) {
                        return null;
                    }

                    if (user == null) {
                        PacketEvents.getAPI().getLogManager().warn("User is null?");
                        user = new User(channel, ConnectionState.PLAY, null, new UserProfile(p.getUniqueId(), p.getName()));

                        synchronized (channel) {
                            if (!ChannelHelper.isOpen(channel)) {
                                return null;
                            }

                            PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
                            PacketEvents.getAPI().getInjector().updateUser(channel, user);
                        }
                    }

                    UserProfile profile = user.getProfile();
                    if (profile.getName() == null) {
                        profile.setName(p.getName());
                    }
                    if (profile.getUUID() == null) {
                        profile.setUUID(p.getUniqueId());
                    }
                    return user;
                }
            };

            private final ChannelInjector injector = new BungeePipelineInjector();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final LogManager logManager = new LogManager() {
                @Override
                protected void log(Level level, @Nullable NamedTextColor color, String message) {
                    // First we must strip away the color codes that might be in this message
                    message = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
                    ProxyServer.getInstance().getLogger().info(message);
                    // TODO: Remove "[com.github.retrooper.packetevents.PacketEventsAPI]:" From logger
                    // PacketEvents.getAPI().getLogger().log(level, color != null ? (color.toString()) : "" + message);
                }
            };
            private boolean loaded;
            private boolean initialized;
            private boolean terminated;

            @Override
            public void load() {
                if (!loaded) {
                    final String id = plugin.getDescription().getName();
                    //Resolve server version and cache
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;

                    WrappedBlockState.ensureLoad();

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
                if (!initialized) {
                    ProxyServer.getInstance().getPluginManager().registerListener(plugin, new InternalBungeeProcessor());
                    if (settings.shouldCheckForUpdates()) {
                        getUpdateChecker().handleUpdateCheck();
                    }

                    Metrics metrics = new Metrics(plugin, 11327);
                    //Just to have an idea of which versions of packetevents people use
                    metrics.addCustomChart(new SimplePie("packetevents_version", () -> getVersion().toStringWithoutSnapshot()));

                    PacketType.Play.Client.load();
                    PacketType.Play.Server.load();
                    initialized = true;
                }
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    // Uninject the injector if needed(depends on the injector implementation)
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
            public Plugin getPlugin() {
                return plugin;
            }

            @Override
            public LogManager getLogManager() {
                return logManager;
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
