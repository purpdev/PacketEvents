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

package io.github.retrooper.packetevents.sponge.factory;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.sponge.InternalSpongeListener;
import io.github.retrooper.packetevents.sponge.injector.SpongeChannelInjector;
import io.github.retrooper.packetevents.sponge.injector.connection.ServerConnectionInitializer;
import io.github.retrooper.packetevents.sponge.manager.InternalSpongePacketListener;
import io.github.retrooper.packetevents.sponge.manager.player.PlayerManagerImpl;
import io.github.retrooper.packetevents.sponge.manager.protocol.ProtocolManagerImpl;
import io.github.retrooper.packetevents.sponge.manager.server.ServerManagerImpl;
import io.github.retrooper.packetevents.sponge.util.SpongeLogManager;
import io.github.retrooper.packetevents.sponge.util.SpongeReflectionUtil;
import io.github.retrooper.packetevents.sponge.util.viaversion.CustomPipelineUtil;
import io.github.retrooper.packetevents.sponge.util.viaversion.ViaVersionUtil;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.plugin.PluginContainer;

public class SpongePacketEventsBuilder {

    private static PacketEventsAPI<PluginContainer> API_INSTANCE;

    public static void clearBuildCache() {
        API_INSTANCE = null;
    }

    public static PacketEventsAPI<PluginContainer> build(PluginContainer plugin) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<PluginContainer> build(PluginContainer plugin, PacketEventsSettings settings) {
        if (API_INSTANCE == null) {
            API_INSTANCE = buildNoCache(plugin, settings);
        }
        return API_INSTANCE;
    }

    public static PacketEventsAPI<PluginContainer> buildNoCache(PluginContainer plugin) {
        return buildNoCache(plugin, new PacketEventsSettings());
    }

    public static PacketEventsAPI<PluginContainer> buildNoCache(PluginContainer plugin, PacketEventsSettings inSettings) {
        return new PacketEventsAPI<PluginContainer>() {
            private final PacketEventsSettings settings = inSettings;
            private final ProtocolManager protocolManager = new ProtocolManagerImpl();
            private final ServerManager serverManager = new ServerManagerImpl();
            private final PlayerManager playerManager = new PlayerManagerImpl();
            private final NettyManager nettyManager = new NettyManagerImpl();
            private final SpongeChannelInjector injector = new SpongeChannelInjector();
            private final LogManager logManager = new SpongeLogManager();
            private boolean loaded;
            private boolean initialized;
            private boolean terminated;
            private boolean lateBind = false;

            @Override
            public void load() {
                if (loaded) return;

                //Resolve server version and cache
                String id = plugin.metadata().id().toLowerCase();
                PacketEvents.IDENTIFIER = "pe-" + id;
                PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
                PacketEvents.DECODER_NAME = "pe-decoder-" + id;
                PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
                PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;
                PacketEvents.TIMEOUT_HANDLER_NAME = "pe-timeout-handler-" + id;
                try {
                    SpongeReflectionUtil.init();
                    CustomPipelineUtil.init();
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }

                if (!PacketType.isPrepared()) {
                    PacketType.prepare();
                }

                // Server hasn't bound to the port yet.
                lateBind = !injector.isServerBound();
                // If late-bind is enabled, we will inject a bit later.
                if (!lateBind) {
                    injector.inject();
                }

                loaded = true;

                // Register internal packet listener (should be the first listener)
                // This listener doesn't do any modifications to the packets, just reads data
                getEventManager().registerListener(new InternalSpongePacketListener());
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

                Sponge.eventManager().registerListeners(plugin, new InternalSpongeListener());

                if (lateBind) {
                    //If late-bind is enabled, we still need to inject (after all plugins enabled).
                    Runnable lateBindTask = () -> {
                        if (injector.isServerBound()) {
                            injector.inject();
                        }
                    };

                    Sponge.server().scheduler().submit(Task.builder().plugin(plugin).execute(lateBindTask).build());
                }

                // Let people override this, at their own risk
                if (!"true".equalsIgnoreCase(System.getenv("PE_IGNORE_INCOMPATIBILITY"))) {
                    // PacketEvents is now enabled, we can now check
                    ViaVersionUtil.checkIfViaIsPresent();
                }

                initialized = true;
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
                    for (User user : ProtocolManager.USERS.values()) {
                        ServerConnectionInitializer.destroyHandlers(user.getChannel());
                    }

                    // Unregister all listeners. Because if we attempt to reload, we will end up with duplicate listeners.
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
