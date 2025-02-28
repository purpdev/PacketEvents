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

package io.github.retrooper.packetevents.handlers;

import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import io.github.retrooper.packetevents.injector.ServerConnectionInitializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@ChannelHandler.Sharable
public class PacketEventsDecoder extends MessageToMessageDecoder<ByteBuf> {

    private final PacketSide side = PacketSide.CLIENT;
    public User user;
    public ProxiedPlayer player;

    public PacketEventsDecoder(User user) {
        this.user = user;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        if (!msg.isReadable()) {
            return;
        }
        ProtocolPacketEvent event = PacketEventsImplHelper.handlePacket(ctx.channel(), this.user, this.player,
                msg.retain(), false, this.side);
        ByteBuf buf = event != null ? (ByteBuf) event.getByteBuf() : msg;

        if (buf.isReadable()) {
            out.add(buf);
        } else {
            buf.release();
        }
    }

    @Override
    public void channelInactive(@NotNull ChannelHandlerContext ctx) throws Exception {
        ServerConnectionInitializer.destroyChannel(ctx.channel());
        super.channelInactive(ctx);
    }
}
