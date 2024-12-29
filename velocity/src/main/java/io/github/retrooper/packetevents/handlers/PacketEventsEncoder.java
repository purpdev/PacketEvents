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
import com.velocitypowered.api.proxy.Player;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

@ChannelHandler.Sharable
public class PacketEventsEncoder extends ChannelOutboundHandlerAdapter {

    private final PacketSide side = PacketSide.SERVER;
    public Player player;
    public User user;

    public PacketEventsEncoder(User user) {
        this.user = user;
    }

    public void handle(ChannelHandlerContext ctx, ByteBuf in, ChannelPromise promise) throws Exception {
        ProtocolPacketEvent event = PacketEventsImplHelper.handlePacket(ctx.channel(),
                this.user, this.player, in.retain(), false, this.side);
        ByteBuf buf = event != null ? (ByteBuf) event.getByteBuf() : in;

        if (buf.isReadable()) {
            ctx.write(buf, promise);
        } else {
            buf.release();
            promise.setSuccess(); // mark as done
        }
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof ByteBuf)) {
            ctx.write(msg, promise);
            return;
        }
        ByteBuf in = (ByteBuf) msg;
        if (!in.isReadable()) {
            in.release();
            promise.setSuccess(); // mark as done
            return;
        }

        try {
            this.handle(ctx, in, promise);
        } finally {
            in.release();
        }
    }
}

