package io.github.retrooper.packetevents.impl.netty.buffer;

import io.netty.buffer.ByteBuf;

public final class FastNettyUtils {

    //Src: https://github.com/PaperMC/Velocity/blob/9cfcfcf2ed5712e792114a3ab824670e25e23526/proxy/src/main/java/com/velocitypowered/proxy/protocol/netty/MinecraftVarintFrameDecoder.java#L82
    public static int readVarInt(final ByteBuf buf) {
        if (buf.readableBytes() < 4)
            return readVarIntSmallBuffer(buf);

        // take the last three bytes and check if any of them have the high bit set
        final int wholeOrMore = buf.getIntLE(buf.readerIndex());
        final int atStop = ~wholeOrMore & 0x808080;
        if (atStop == 0) throw new IllegalArgumentException("VarInt too big");

        final int bitsToKeep = Integer.numberOfTrailingZeros(atStop) + 1;
        buf.skipBytes(bitsToKeep >> 3);

        // https://github.com/netty/netty/pull/14050#issuecomment-2107750734
        int preservedBytes = wholeOrMore & (atStop ^ (atStop - 1));

        // https://github.com/netty/netty/pull/14050#discussion_r1597896639
        preservedBytes = (preservedBytes & 0x007F007F) | ((preservedBytes & 0x00007F00) >> 1);
        preservedBytes = (preservedBytes & 0x00003FFF) | ((preservedBytes & 0x3FFF0000) >> 2);
        return preservedBytes;
    }

    private static int readVarIntSmallBuffer(ByteBuf buf) {
        switch (buf.readableBytes()) {
            case 3:
                return readVarInt3Bytes(buf);
            case 2:
                return readVarInt2Bytes(buf);
            case 1: {
                byte val = buf.readByte();
                //check if it has the continuation bit set
                if ((val & -128) != 0) throw new IllegalArgumentException("VarInt too big for 1 byte");
                return val;
            }
            case 0:
                return 0;//I guess 0? Or an Exception?
            default:
                throw new AssertionError("how");
        }
    }

    private static int readVarInt3Bytes(final ByteBuf buf) {
        // Read 3 bytes in little-endian order
        final int wholeOrMore = buf.getMediumLE(buf.readerIndex()); // Reads 3 bytes as an int
        final int atStop = ~wholeOrMore & 0x808080; // Check for stop bits

        // If no stop bits are found, throw an exception
        if (atStop == 0) throw new IllegalArgumentException("VarInt too big for 3 bytes");

        // Find the position of the first stop bit
        final int bitsToKeep = Integer.numberOfTrailingZeros(atStop) + 1;
        buf.skipBytes(bitsToKeep >> 3); // Skip the processed bytes

        // Extract and preserve the valid bytes
        int preservedBytes = wholeOrMore & (atStop ^ (atStop - 1));

        // Compact the 7-bit chunks
        preservedBytes = (preservedBytes & 0x007F007F) | ((preservedBytes & 0x00007F00) >> 1);
        preservedBytes = (preservedBytes & 0x00003FFF) | ((preservedBytes & 0x3FFF0000) >> 2);

        return preservedBytes;
    }

    private static int readVarInt2Bytes(final ByteBuf buf) {
        // Read 2 bytes in little-endian order
        final int wholeOrMore = buf.getShortLE(buf.readerIndex()); // Reads 2 bytes as an integer
        final int atStop = ~wholeOrMore & 0x8080; // Identify stop bits in the two bytes

        // If no stop bits are found, the VarInt is too large
        if (atStop == 0) throw new IllegalArgumentException("VarInt too big for 2 bytes");

        // Find the first stop bit
        final int bitsToKeep = Integer.numberOfTrailingZeros(atStop) + 1;
        buf.skipBytes(bitsToKeep >> 3); // Skip the number of processed bytes

        // Extract and preserve the relevant 7-bit chunks
        int preservedBytes = wholeOrMore & (atStop ^ (atStop - 1));

        // Compact the 7-bit chunks into a single integer
        preservedBytes = (preservedBytes & 0x007F) | ((preservedBytes & 0x7F00) >> 1);

        return preservedBytes;
    }

}