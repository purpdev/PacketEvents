package com.github.retrooper.packetevents.binary;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;

final class BinaryBufferTypes {

    private BinaryBufferTypes() {}

    static final class Float implements BinaryBufferType<java.lang.Float> {
        @Override
        public java.lang.Float read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readFloat(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Float value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeFloat(buffer, value);
        }
    }
    static final class Double implements BinaryBufferType<java.lang.Double> {
            @Override
            public java.lang.Double read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
                return ByteBufHelper.readDouble(buffer);
            }

            @Override
            public void write(BinaryBuffer buffer, java.lang.Double value, ServerVersion serverVersion, ClientVersion clientVersion) {
                ByteBufHelper.writeDouble(buffer, value);
            }
    }
    static final class Bool implements BinaryBufferType<Boolean> {

        @Override
        public Boolean read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readBoolean(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, Boolean value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeBoolean(buffer, value);
        }
    }
    static final class Byte implements BinaryBufferType<java.lang.Byte> {

        @Override
        public java.lang.Byte read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readByte(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Byte value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeByte(buffer, value);
        }
    }
    static final class Int implements BinaryBufferType<Integer> {

        @Override
        public Integer read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readInt(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, Integer value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeInt(buffer, value);
        }
    }
    static final class Long implements BinaryBufferType<java.lang.Long> {

        @Override
        public java.lang.Long read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readLong(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Long value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeLong(buffer, value);
        }
    }
    static final class Short implements BinaryBufferType<java.lang.Short> {

        @Override
        public java.lang.Short read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            return ByteBufHelper.readShort(buffer);
        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Short value, ServerVersion serverVersion, ClientVersion clientVersion) {
            ByteBufHelper.writeShort(buffer, value);
        }
    }
    static final class VarInt implements BinaryBufferType<Integer> {

        @Override
        public Integer read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            int value = 0;
            int length = 0;
            byte currentByte;
            do {
                currentByte = ByteBufHelper.readByte(buffer);
                value |= (currentByte & 0x7F) << (length * 7);
                length++;
                if (length > 5) {
                    throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
                }
            } while ((currentByte & 0x80) == 0x80);
            return value;
        }

        @Override
        public void write(BinaryBuffer buffer, Integer value, ServerVersion serverVersion, ClientVersion clientVersion) {
            while (true) {
                if ((value & ~0x7F) == 0) {
                    ByteBufHelper.writeByte(buffer, value);
                    break;
                }
                ByteBufHelper.writeByte(buffer, (value & 0x7F) | 0x80);
                value >>>= 7;
            }
        }
    }
    static final class VarLong implements BinaryBufferType<java.lang.Long> {

        @Override
        public java.lang.Long read(BinaryBuffer buffer, ServerVersion serverVersion, ClientVersion clientVersion) {
            long value = 0;
            int size = 0;
            int b;
            while (((b = ByteBufHelper.readByte(buffer)) & 0x80) == 0x80) {
                value |= (long) (b & 0x7F) << (size++ * 7);
            }
            return value | ((long) (b & 0x7F) << (size * 7));        }

        @Override
        public void write(BinaryBuffer buffer, java.lang.Long value, ServerVersion serverVersion, ClientVersion clientVersion) {
            while ((value & ~0x7F) != 0) {
                ByteBufHelper.writeByte(buffer, (int) (value & 0x7F) | 0x80);
                value >>>= 7;
            }

            ByteBufHelper.writeByte(buffer, value.intValue());
        }
    }
}
