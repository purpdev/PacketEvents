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

package io.github.retrooper.packetevents.factory.fabric;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.PacketEventsAPI;
import com.github.retrooper.packetevents.injector.ChannelInjector;
import com.github.retrooper.packetevents.manager.InternalPacketListener;
import com.github.retrooper.packetevents.manager.player.PlayerManager;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.manager.server.ServerManager;
import com.github.retrooper.packetevents.netty.NettyManager;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.settings.PacketEventsSettings;
import com.github.retrooper.packetevents.util.LogManager;
import io.github.retrooper.packetevents.impl.netty.NettyManagerImpl;
import io.github.retrooper.packetevents.impl.netty.manager.player.PlayerManagerAbstract;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class FabricPacketEventsAPI extends PacketEventsAPI<FabricLoader> {

    private static final Logger LOGGER = LoggerFactory.getLogger("PacketEvents");

    private final String modId;
    private final EnvType environment;
    private final PacketEventsSettings settings;

    private final ProtocolManager protocolManager;
    private final ServerManager serverManager;
    private final PlayerManagerAbstract playerManager;
    private final ChannelInjector injector;
    private final NettyManager nettyManager = new NettyManagerImpl();
    private final LogManager logManager = new FabricLogger(LOGGER);

    private boolean loaded;
    private boolean initialized;
    private boolean terminated;

    public FabricPacketEventsAPI(String modId, EnvType environment) {
        this(modId, environment, new PacketEventsSettings());
    }

    public FabricPacketEventsAPI(String modId, EnvType environment, PacketEventsSettings settings) {
        this.modId = modId;
        this.environment = environment;
        this.settings = settings;

        this.protocolManager = new FabricProtocolManager(environment);
        this.serverManager = this.constructServerManager();
        this.playerManager = this.constructPlayerManager();
        this.injector = new FabricChannelInjector(environment);
    }

    protected ServerManager constructServerManager() {
        return new FabricServerManager();
    }

    protected PlayerManagerAbstract constructPlayerManager() {
        return new FabricPlayerManager();
    }

    @Override
    public void load() {
        if (this.loaded) {
            return;
        }

        String id = (this.environment.name() + "_" + this.modId).toLowerCase(Locale.ROOT);
        PacketEvents.IDENTIFIER = "pe-" + id;
        PacketEvents.ENCODER_NAME = "pe-encoder-" + id;
        PacketEvents.DECODER_NAME = "pe-decoder-" + id;
        PacketEvents.CONNECTION_HANDLER_NAME = "pe-connection-handler-" + id;
        PacketEvents.SERVER_CHANNEL_HANDLER_NAME = "pe-connection-initializer-" + id;

        WrappedBlockState.ensureLoad();

        // register internal packet listener (should be the first listener)
        // this listener doesn't do any modifications to the packets, just reads data
        this.getEventManager().registerListener(new InternalPacketListener());
        this.loaded = true;
    }

    @Override
    public boolean isLoaded() {
        return this.loaded;
    }

    @Override
    public void init() {
        // load if we haven't loaded already
        this.load();
        if (this.initialized) {
            return;
        }
        if (this.settings.shouldCheckForUpdates()) {
            this.getUpdateChecker().handleUpdateCheck();
        }

        //TODO Cross-platform metrics?

        PacketType.Play.Client.load();
        PacketType.Play.Server.load();
        this.initialized = true;
    }

    @Override
    public boolean isInitialized() {
        return this.initialized;
    }

    @Override
    public void terminate() {
        if (!this.initialized) {
            return;
        }
        this.getEventManager().unregisterAllListeners();
        this.initialized = false;
        this.terminated = true;
    }

    @Override
    public boolean isTerminated() {
        return this.terminated;
    }

    @Override
    public FabricLoader getPlugin() {
        return FabricLoader.getInstance();
    }

    @Override
    public ProtocolManager getProtocolManager() {
        return this.protocolManager;
    }

    @Override
    public ServerManager getServerManager() {
        return this.serverManager;
    }

    @Override
    public LogManager getLogManager() {
        return this.logManager;
    }

    @Override
    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    @Override
    public ChannelInjector getInjector() {
        return this.injector;
    }

    @Override
    public PacketEventsSettings getSettings() {
        return this.settings;
    }

    @Override
    public NettyManager getNettyManager() {
        return this.nettyManager;
    }
}
