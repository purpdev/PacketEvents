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

package io.github.retrooper.packetevents.mc1211.mixin;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.UserConnectEvent;
import com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.player.UserProfile;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.handler.PacketDecoder;
import io.github.retrooper.packetevents.handler.PacketEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelPipeline;
import net.minecraft.SharedConstants;
import net.minecraft.network.BandwidthDebugMonitor;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(net.minecraft.network.Connection.class)
public class ConnectionMixin {

    // doesn't account for mods like ViaFabric
    @Unique
    private static final ClientVersion CLIENT_VERSION =
        ClientVersion.getById(SharedConstants.getProtocolVersion());

    @Inject(
        method = "configureSerialization",
        at = @At("TAIL")
    )
    private static void configureSerialization(
        ChannelPipeline pipeline, PacketFlow flow, boolean memoryOnly,
        BandwidthDebugMonitor bandwithDebugMonitor, CallbackInfo ci
    ) {
        PacketSide pipelineSide = switch (flow) {
            case CLIENTBOUND -> PacketSide.CLIENT;
            case SERVERBOUND -> PacketSide.SERVER;
        };
        PacketSide apiSide = PacketEvents.getAPI().getInjector().getPacketSide();
        if (pipelineSide != apiSide) {
            // if pipeline side doesn't match api side, don't inject into
            // this pipeline - it probably means this is the pipeline from
            // integrated server to minecraft client, which is currently unsupported
            PacketEvents.getAPI().getLogManager().debug("Skipped pipeline injection on " + pipelineSide);
            return;
        }

        PacketEvents.getAPI().getLogManager().debug("Game connected!");

        Channel channel = pipeline.channel();
        User user = new User(channel, ConnectionState.HANDSHAKING,
            CLIENT_VERSION, new UserProfile(null, null));
        ProtocolManager.USERS.put(channel.pipeline(), user);

        UserConnectEvent connectEvent = new UserConnectEvent(user);
        PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
        if (connectEvent.isCancelled()) {
            channel.unsafe().closeForcibly();
            return;
        }

        channel.pipeline().addAfter("splitter", PacketEvents.DECODER_NAME, new PacketDecoder(apiSide, user));
        channel.pipeline().addAfter("prepender", PacketEvents.ENCODER_NAME, new PacketEncoder(apiSide, user));
        channel.closeFuture().addListener((ChannelFutureListener) future ->
            PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID()));
    }
}