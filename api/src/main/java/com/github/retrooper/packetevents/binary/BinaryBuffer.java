package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;

import java.util.*;

public final class BinaryBuffer {

    public static final BinaryBufferType<Byte> BYTE = new BinaryBufferTypes.Byte();
    public static final BinaryBufferType<Boolean> BOOLEAN = new BinaryBufferTypes.Bool();
    public static final BinaryBufferType<Integer> INT = new BinaryBufferTypes.Int();
    public static final BinaryBufferType<Float> FLOAT = new BinaryBufferTypes.Float();
    public static final BinaryBufferType<Double> DOUBLE = new BinaryBufferTypes.Double();
    public static final BinaryBufferType<Long> LONG = new BinaryBufferTypes.Long();
    public static final BinaryBufferType<Short> SHORT = new BinaryBufferTypes.Short();
    public static final BinaryBufferType<Integer> VAR_INT = new BinaryBufferTypes.VarInt();

    private Object buffer;

    public BinaryBuffer(Object buffer) {
        this.buffer = buffer;
    }

    public BinaryBuffer(int capacity, boolean io) {
        if (io) {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer(capacity);
        }
        else {
            this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer(capacity);
        }
    }

    public BinaryBuffer(int capacity) {
        this(capacity, true);
    }

    public BinaryBuffer(byte[] wrappedData) {
        this.buffer = PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(wrappedData);
    }

    /** Buffer Operations **/
    public void resetReaderIndex() {
        ByteBufHelper.resetReaderIndex(buffer);
    }
    public void resetWriterIndex() {
        ByteBufHelper.resetWriterIndex(buffer);
    }
    public void reset() {
        ByteBufHelper.clear(buffer);
    }


    /** Primitive Read/Write Methods **/
    public <T> T read(BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        return type.read(this, serverVersion, clientVersion);
    }
    public <T> T read(BinaryBufferType<T> type, ClientVersion clientVersion) {
        return type.read(this, clientVersion);
    }
    public <T> T read(BinaryBufferType<T> type) {
        return type.read(this);
    }
    public <T> void write(BinaryBufferType<T> type, T value, ServerVersion serverVersion, ClientVersion clientVersion) {
        type.write(this, value, serverVersion, clientVersion);
    }
    public <T> void write(BinaryBufferType<T> type, T value, ClientVersion clientVersion) {
        type.write(this, value, clientVersion);
    }
    public <T> void write(BinaryBufferType<T> type, T value) {
        type.write(this, value);
    }

    /** Collection Read/Write Methods **/
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        int size = read(VAR_INT, serverVersion, clientVersion);
        if (size > maxSize) {
            throw new IllegalArgumentException("Size of collection is too big: " + size);
        }
        List<T> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(read(type, serverVersion, clientVersion));
        }
        return list;
    }
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type, ClientVersion clientVersion) {
        return readCollection(maxSize, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }
    public <T>Collection<T> readCollection(int maxSize, BinaryBufferType<T> type) {
        return readCollection(maxSize, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }
    public <T>Collection<T> readCollection(BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        return readCollection(Short.MAX_VALUE, type, serverVersion, clientVersion);
    }
    public <T>Collection<T> readCollection(BinaryBufferType<T> type, ClientVersion clientVersion) {
        return readCollection(Short.MAX_VALUE, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }
    public <T>Collection<T> readCollection(BinaryBufferType<T> type) {
        return readCollection(Short.MAX_VALUE, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type, ServerVersion serverVersion, ClientVersion clientVersion) {
        write(VAR_INT, collection.size(), serverVersion, clientVersion);
        for (T t : collection) {
            write(type, t, serverVersion, clientVersion);
        }
    }
    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type, ClientVersion clientVersion) {
        writeCollection(collection, type, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }
    public <T> void writeCollection(Collection<T> collection, BinaryBufferType<T> type) {
        writeCollection(collection, type, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /** Map Read/Write Methods **/
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ServerVersion serverVersion, ClientVersion clientVersion) {
        int size = read(VAR_INT, serverVersion, clientVersion);
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = read(keyType, serverVersion, clientVersion);
            V value = read(valueType, serverVersion, clientVersion);
            map.put(key, value);
        }
        return map;
    }
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ClientVersion clientVersion) {
        return readMap(keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }
    public <K, V> Map<K, V> readMap(BinaryBufferType<K> keyType, BinaryBufferType<V> valueType) {
        return readMap(keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ServerVersion serverVersion, ClientVersion clientVersion) {
        write(VAR_INT, map.size(), serverVersion, clientVersion);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            write(keyType, entry.getKey(), serverVersion, clientVersion);
            write(valueType, entry.getValue(), serverVersion, clientVersion);
        }
    }
    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType, ClientVersion clientVersion) {
        writeMap(map, keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), clientVersion);
    }
    public <K, V> void writeMap(Map<K, V> map, BinaryBufferType<K> keyType, BinaryBufferType<V> valueType) {
        writeMap(map, keyType, valueType, PacketEvents.getAPI().getServerManager().getVersion(), PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
    }

    /** Enums Read/Write Methods **/
    public <T extends Enum<T>> T readEnum(Class<T> enumClass) {
        T[] constants = enumClass.getEnumConstants();
        int ordinal = read(VAR_INT);
        if (ordinal < 0 || ordinal >= constants.length) {
            throw new IllegalArgumentException("Invalid ordinal for enum " + enumClass.getName() + ": " + ordinal);
        }
        return constants[ordinal];
    }
    public <T extends Enum<T>> void writeEnum(T value) {
        write(VAR_INT, value.ordinal());
    }
}
