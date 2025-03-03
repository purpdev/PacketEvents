package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

public interface BinaryBufferType<T> {

    T read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion);

    void write(BinaryBuffer buffer, T value, ServerVersion serverVersion, ClientVersion clientVersion);

    default T read(BinaryBuffer buffer, ClientVersion clientVersion) {
        return read(buffer, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    default void write(BinaryBuffer buffer, T value, ClientVersion clientVersion) {
        write(buffer, value, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }

    default T read(BinaryBuffer buffer) {
        ServerVersion v = PacketEvents.getAPI().getServerManager().getVersion();
        return read(buffer, v, v.toClientVersion());
    }

    default void write(BinaryBuffer buffer, T value) {
        write(buffer, value, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

}
