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

package io.github.retrooper.packetevents.factory.spigot;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.bukkit.InternalBukkitListener;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.manager.InternalBukkitPacketListener;
import io.github.retrooper.packetevents.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.manager.protocol.ProtocolManagerImpl;
import io.github.retrooper.packetevents.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.util.BukkitLogManager;
import io.github.retrooper.packetevents.util.SpigotReflectionUtil;
import io.github.retrooper.packetevents.util.folia.FoliaScheduler;
import io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SpigotPacketEventsBuilder {
    private static PacketEventsAPI<Plugin> API_INSTANCE;

    public static void clearBuildCache() {
        API_INSTANCE = null;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> build(Plugin plugin, PacketEventsSettings settings) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin, settings);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin) {
        return buildNoCache(plugin, new PacketEventsSettings());
    }

    public static PacketEventsAPI<Plugin> buildNoCache(Plugin plugin, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<Plugin>() {
            private final PacketEventsSettings settings = inSettings;
            private final ProtocolManager protocolManager = new ProtocolManagerImpl();
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final SpigotChannelInjector injector = new SpigotChannelInjector();
            private final LogManager logManager = new BukkitLogManager();
            private boolean loaded;
            private boolean initialized;
            private boolean lateBind = false;
            private boolean terminated = false;

            @Override
            public void load() {
                if (!loaded) {
                    //Resolve server version and cache
                    String id = plugin.getName().toLowerCase();
                    PacketEvents.IDENTIFIER = "pe-" + id;
                    PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                    PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                    PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                    PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                    PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;
                    try {
                        SpigotReflectionUtil.init();
                        CustomPipelineUtil.init();
                        WrappedBlockState.ensureLoad();
                    } catch (Exception ex) {
                        throw new IllegalStateException(ex);
                    }

                    if (!PacketType.isPrepared()) {
                        PacketType.prepare();
                    }

                    //Server hasn't bound to the port yet.
                    lateBind = !injector.isServerBound();
                    //If late-bind is enabled, we will inject a bit later.
                    if (!lateBind) {
                        injector.inject();
                    }

                    loaded = true;

                    //Register internal packet listener (should be the first listener)
                    //This listener doesn't do any modifications to the packets, just reads data
                    getEventManager().registerListener(new InternalBukkitPacketListener());
                }
            }

            @Override
            public boolean isLoaded() {
                return loaded;
            }

            @Override
            public void init() {
                //Load if we haven't loaded already
                load();
                if (!initialized) {
                    if (settings.shouldCheckForUpdates()) {
                        getUpdateChecker().handleUpdateCheck();
                    }

                    Metrics metrics = new Metrics(plugin, 11327);
                    //Just to have an idea of which versions of packetevents people use
                    metrics.addCustomChart(new SimplePie("packetevents_version", () -> getVersion().toStringWithoutSnapshot()));
                    Bukkit.getPluginManager().registerEvents(new InternalBukkitListener(plugin), plugin);

                    if (lateBind) {
                        //If late-bind is enabled, we still need to inject (after all plugins enabled).
                        Runnable lateBindTask = () -> {
                            if (injector.isServerBound()) {
                                injector.inject();
                            }
                        };
                        FoliaScheduler.runTaskOnInit(plugin, lateBindTask);
                    }

                    // Let people override this, at their own risk
                    if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
                        checkCompatibility();
                    }

                    //Map player instances to the already registered channels (likely a reload)
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
                        SpigotChannelInjector injector = (SpigotChannelInjector) PacketEvents.getAPI().getInjector();
                        injector.updatePlayer(user, player);
                    }

                    initialized = true;
                }
            }

            private void checkCompatibility() {
                // PacketEvents is now enabled, we can now check
                ViaVersionUtil.checkIfViaIsPresent();
                ProtocolSupportUtil.checkIfProtocolSupportIsPresent();
                //If ViaVersion is present, it must be 4.5.0 or higher
                Plugin viaPlugin = Bukkit.getPluginManager().getPlugin("ViaVersion");
                if (viaPlugin != null) {
                    String[] ver = viaPlugin.getDescription().getVersion().split("\\.", 3);
                    int major = Integer.parseInt(ver[0]);
                    int minor = Integer.parseInt(ver[1]);
                    if (major < 4 || major == 4 && minor < 5) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a " +
                                "ViaVersion older than 4.5.0, please update your ViaVersion!");
                        Plugin ourPlugin = getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ViaVersion incompatibility! Update to v4.5.0 or newer!");
                    }
                }
                //If ProtocolLib is present, it needs to be v5.0.0 or newer
                Plugin protocolLibPlugin = Bukkit.getPluginManager().getPlugin("ProtocolLib");
                if (protocolLibPlugin != null) {
                    int majorVersion = Integer.parseInt(protocolLibPlugin.getDescription().getVersion().split("\\.", 2)[0]);
                    if (majorVersion < 5) {
                        PacketEvents.getAPI().getLogManager().severe("You are attempting to combine 2.0 PacketEvents with a " +
                                "ProtocolLib version older than v5.0.0. " +
                                "This is no longer works, please update to their dev builds. " +
                                "https://ci.dmulloy2.net/job/ProtocolLib/lastBuild/");
                        Plugin ourPlugin = getPlugin();
                        Bukkit.getPluginManager().disablePlugin(ourPlugin);
                        throw new IllegalStateException("ProtocolLib incompatibility! Update to v5.0.0 or newer!");
                    }
                }
            }

            @Override
            public boolean isInitialized() {
                return initialized;
            }

            @Override
            public void terminate() {
                if (initialized) {
                    //Uninject the injector if needed(depends on the injector implementation)
                    injector.uninject();
                    for (User user : ProtocolManager.USERS.values()) {
                        ServerConnectionInitializer.destroyHandlers(user.getChannel());
                    }
                    //Unregister all listeners. Because if we attempt to reload, we will end up with duplicate listeners.
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
            public PacketEventsSettings getSettings() {
                return settings;
            }

            @Override
            public NettyManager getNettyManager() {
                return nettyManager;
            }

            @Override
            public ChannelInjector getInjector() {
                return injector;
            }

            @Override
            public LogManager getLogManager() {
                return logManager;
            }
        };
    }
}
