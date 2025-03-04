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


/*
 * MIT License

 * Copyright (c) 2021 Andrew Steinborn

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.retrooper.packetevents.wrapper;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.event.ProtocolPacketEvent;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.manager.server.VersionComparison;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.PacketSide;
import com.github.retrooper.packetevents.protocol.chat.ChatType;
import com.github.retrooper.packetevents.protocol.chat.ChatTypes;
import com.github.retrooper.packetevents.protocol.chat.LastSeenMessages;
import com.github.retrooper.packetevents.protocol.chat.MessageSignature;
import com.github.retrooper.packetevents.protocol.chat.Node;
import com.github.retrooper.packetevents.protocol.chat.Parsers;
import com.github.retrooper.packetevents.protocol.chat.Parsers.Parser;
import com.github.retrooper.packetevents.protocol.chat.RemoteChatSession;
import com.github.retrooper.packetevents.protocol.chat.SignedCommandArgument;
import com.github.retrooper.packetevents.protocol.chat.filter.FilterMask;
import com.github.retrooper.packetevents.protocol.chat.filter.FilterMaskType;
import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.entity.data.EntityData;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataType;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.data.EntityMetadataProvider;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntity;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTLimiter;
import com.github.retrooper.packetevents.protocol.nbt.codec.NBTCodec;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.packettype.PacketTypeCommon;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.PublicProfileKey;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.recipe.data.MerchantItemCost;
import com.github.retrooper.packetevents.protocol.recipe.data.MerchantOffer;
import com.github.retrooper.packetevents.protocol.world.Dimension;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.KnownPack;
import com.github.retrooper.packetevents.util.MathUtil;
import com.github.retrooper.packetevents.util.StringUtil;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import com.github.retrooper.packetevents.util.crypto.MinecraftEncryptionUtil;
import com.github.retrooper.packetevents.util.crypto.SaltSignature;
import com.github.retrooper.packetevents.util.crypto.SignatureData;
import com.github.retrooper.packetevents.util.mappings.GlobalRegistryHolder;
import com.github.retrooper.packetevents.util.mappings.IRegistry;
import com.github.retrooper.packetevents.util.mappings.IRegistryHolder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public class PacketWrapper<T extends PacketWrapper<T>> {
    @Nullable
    public Object buffer;

    @ApiStatus.Internal
    public final Object bufferLock = new Object();

    protected ClientVersion clientVersion;
    protected ServerVersion serverVersion;
    private PacketTypeData packetTypeData;
    // For sending chunk data packets, which need this data
    @Nullable
    protected User user;

    private static final int MODERN_MESSAGE_LENGTH = 262144;
    private static final int LEGACY_MESSAGE_LENGTH = 32767;

    public PacketWrapper(ClientVersion clientVersion, ServerVersion serverVersion, int packetID) {
        if (packetID == -1) {
            throw new IllegalArgumentException("Packet does not exist on this protocol version!");
        }
        this.clientVersion = clientVersion;
        this.serverVersion = serverVersion;
        this.buffer = null;
        this.packetTypeData = new PacketTypeData(null, packetID);
    }

    public PacketWrapper(PacketReceiveEvent event) {
        this(event, true);
    }

    public PacketWrapper(PacketReceiveEvent event, boolean readData) {
        this.clientVersion = event.getUser().getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.user = event.getUser();
        this.buffer = event.getByteBuf();
        this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
        if (readData) {
            readEvent(event);
        }
    }

    public PacketWrapper(PacketSendEvent event) {
        this(event, true);
    }

    public PacketWrapper(PacketSendEvent event, boolean readData) {
        this.clientVersion = event.getUser().getClientVersion();
        this.serverVersion = event.getServerVersion();
        this.buffer = event.getByteBuf();
        this.packetTypeData = new PacketTypeData(event.getPacketType(), event.getPacketId());
        this.user = event.getUser();
        if (readData) {
            readEvent(event);
        }
    }

    public PacketWrapper(int packetID, ClientVersion clientVersion) {
        this(clientVersion, PacketEvents.getAPI().getServerManager().getVersion(), packetID);
    }

    public PacketWrapper(int packetID) {
        if (packetID == -1) {
            throw new IllegalArgumentException("Packet does not exist on this protocol version!");
        }
        this.clientVersion = ClientVersion.UNKNOWN;
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        this.buffer = null;
        this.packetTypeData = new PacketTypeData(null, packetID);
    }

    public PacketWrapper(PacketTypeCommon packetType) {
        this.clientVersion = ClientVersion.UNKNOWN;
        this.serverVersion = PacketEvents.getAPI().getServerManager().getVersion();
        this.buffer = null;
        int id = packetType.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
        this.packetTypeData = new PacketTypeData(packetType, id);
    }

    public static PacketWrapper<?> createUniversalPacketWrapper(Object byteBuf) {
        PacketWrapper<?> wrapper = new PacketWrapper<>(ClientVersion.UNKNOWN, PacketEvents.getAPI().getServerManager().getVersion(), -2);
        wrapper.buffer = byteBuf;
        return wrapper;
    }

    public static int getChunkX(long chunkKey) {
        return (int) (chunkKey & 0xFFFFFFFFL);
    }

    public static int getChunkZ(long chunkKey) {
        return (int) ((chunkKey >>> 32) & 0xFFFFFFFFL);
    }

    public static long getChunkKey(int chunkX, int chunkZ) {
        return (long) chunkX & 0xFFFFFFFFL | ((long) chunkZ & 0xFFFFFFFFL) << 32;
    }

    @ApiStatus.Internal
    public final void prepareForSend(Object channel, boolean outgoing, boolean proxy) {
        // Null means the packet was manually created and wasn't sent by the server itself
        // A reference count of 0 means that the packet was freed (it was already sent)
        if (buffer == null || ByteBufHelper.refCnt(buffer) == 0) {
            buffer = ChannelHelper.pooledByteBuf(channel);
        }

        //On proxies, we must rewrite the packet ID in a format compatible for the targeted client version
        if (proxy) {
            User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
            if (packetTypeData.getPacketType() == null) {
                //Get the packet type with the local version packet type mappings.
                packetTypeData.setPacketType(PacketType.getById(outgoing ? PacketSide.SERVER : PacketSide.CLIENT,
                        user.getConnectionState(), serverVersion.toClientVersion(), packetTypeData.getNativePacketId()));
            }
            //Change local version to user version so that the packet can be processed correctly.
            serverVersion = user.getClientVersion().toServerVersion();
            int id = packetTypeData.getPacketType().getId(user.getClientVersion());
            writeVarInt(id);
        } else {
            writeVarInt(packetTypeData.getNativePacketId());
        }
        write();
    }

    @ApiStatus.Internal
    public final void prepareForSend(Object channel, boolean outgoing) {
        prepareForSend(channel, outgoing, PacketEvents.getAPI().getInjector().isProxy());
    }

    public void read() {
    }

    public void write() {

    }

    //TODO Rename to copyFrom, as it copies data from the passed in wrapper.
    public void copy(T wrapper) {

    }

    //TODO public void transform(int protocolVersion) {}
    //Current idea change server version, but still think more

    public final void readEvent(ProtocolPacketEvent event) {
        PacketWrapper<?> last = event.getLastUsedWrapper();
        if (last != null) {
            copy((T) last);
        } else {
            read();
        }
        event.setLastUsedWrapper(this);
    }

    public ClientVersion getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(ClientVersion clientVersion) {
        this.clientVersion = clientVersion;
    }

    public ServerVersion getServerVersion() {
        return serverVersion;
    }

    public void setServerVersion(ServerVersion serverVersion) {
        this.serverVersion = serverVersion;
    }

    public Object getBuffer() {
        return buffer;
    }

    public void setBuffer(Object buffer) {
        this.buffer = buffer;
    }

    /**
     * Gets the Packet ID for the current platform version
     *
     * @return Packet ID
     * @deprecated Use {@link #getNativePacketId()}
     */
    @Deprecated
    public int getPacketId() {
        return getNativePacketId();
    }

    /**
     * Sets the Packet ID for the current platform version
     *
     * @deprecated Use {@link #setNativePacketId(int)}
     */
    @Deprecated
    public void setPacketId(int packetID) {
        setNativePacketId(packetID);
    }

    public int getNativePacketId() {
        return packetTypeData.getNativePacketId();
    }

    public void setNativePacketId(int nativePacketId) {
        this.packetTypeData.setNativePacketId(nativePacketId);
    }

    @ApiStatus.Internal
    public PacketTypeData getPacketTypeData() {
        return packetTypeData;
    }

    public int getMaxMessageLength() {
        return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13) ? MODERN_MESSAGE_LENGTH : LEGACY_MESSAGE_LENGTH;
    }

    @Deprecated
    public void resetByteBuf() {
        ByteBufHelper.clear(buffer);
    }

    public void resetBuffer() {
        ByteBufHelper.clear(buffer);
    }

    public byte readByte() {
        return ByteBufHelper.readByte(buffer);
    }

    public void writeByte(int value) {
        ByteBufHelper.writeByte(buffer, value);
    }

    public short readUnsignedByte() {
        return ByteBufHelper.readUnsignedByte(buffer);
    }

    public boolean readBoolean() {
        return readByte() != 0;
    }

    public void writeBoolean(boolean value) {
        writeByte(value ? 1 : 0);
    }

    public int readInt() {
        return ByteBufHelper.readInt(buffer);
    }

    public void writeInt(int value) {
        ByteBufHelper.writeInt(buffer, value);
    }

    public int readMedium() {
        return ByteBufHelper.readMedium(buffer);
    }

    public void writeMedium(int value) {
        ByteBufHelper.writeMedium(buffer, value);
    }

    public int readVarInt() {
        int value = 0;
        int length = 0;
        byte currentByte;
        do {
            currentByte = readByte();
            value |= (currentByte & 0x7F) << (length * 7);
            length++;
            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public void writeVarInt(int value) {
        /* Got this code/optimization from https://steinborn.me/posts/performance/how-fast-can-you-write-a-varint/
         * Copyright and permission notice above (above the class).
         * Steinborn's post says that the code is under the MIT, last accessed 29.06.2024.
         */
        if ((value & (0xFFFFFFFF << 7)) == 0) {
            writeByte(value);
        } else if ((value & (0xFFFFFFFF << 14)) == 0) {
            int w = (value & 0x7F | 0x80) << 8 | (value >>> 7);
            writeShort(w);
        } else if ((value & (0xFFFFFFFF << 21)) == 0) {
            int w = (value & 0x7F | 0x80) << 16 | ((value >>> 7) & 0x7F | 0x80) << 8 | (value >>> 14);
            writeMedium(w);
        } else if ((value & (0xFFFFFFFF << 28)) == 0) {
            int w = (value & 0x7F | 0x80) << 24 | (((value >>> 7) & 0x7F | 0x80) << 16)
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | (value >>> 21);
            writeInt(w);
        } else {
            int w = (value & 0x7F | 0x80) << 24 | ((value >>> 7) & 0x7F | 0x80) << 16
                    | ((value >>> 14) & 0x7F | 0x80) << 8 | ((value >>> 21) & 0x7F | 0x80);
            writeInt(w);
            writeByte(value >>> 28);
        }
    }

    public <K, V> Map<K, V> readMap(Reader<K> keyFunction, Reader<V> valueFunction) {
        int size = readVarInt();
        Map<K, V> map = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            K key = keyFunction.apply(this);
            V value = valueFunction.apply(this);
            map.put(key, value);
        }
        return map;
    }

    public <K, V> void writeMap(Map<K, V> map, Writer<K> keyConsumer, Writer<V> valueConsumer) {
        writeVarInt(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            keyConsumer.accept(this, key);
            valueConsumer.accept(this, value);
        }
    }

    public VillagerData readVillagerData() {
        int villagerTypeId = readVarInt();
        int villagerProfessionId = readVarInt();
        int level = readVarInt();
        return new VillagerData(villagerTypeId, villagerProfessionId, level);
    }

    public void writeVillagerData(VillagerData data) {
        writeVarInt(data.getType().getId());
        writeVarInt(data.getProfession().getId());
        writeVarInt(data.getLevel());
    }

    // item stack serialization was basically completely rewritten in 1.20.5
    @SuppressWarnings("unchecked")
    public ItemStack readItemStackModern() {
        int count = this.readVarInt();
        if (count <= 0) {
            return ItemStack.EMPTY;
        }
        ItemType itemType = this.readMappedEntity(ItemTypes.getRegistry());

        // read component patch counts
        int presentCount = this.readVarInt();
        int absentCount = this.readVarInt();
        if (presentCount == 0 && absentCount == 0) {
            return ItemStack.builder().type(itemType).amount(count).build();
        }

        PatchableComponentMap components = new PatchableComponentMap(
                itemType.getComponents(), new HashMap<>(4));
        for (int i = 0; i < presentCount; i++) {
            ComponentType<?> type = this.readMappedEntity(ComponentTypes.getRegistry());
            components.set((ComponentType<Object>) type, type.read(this));
        }
        for (int i = 0; i < absentCount; i++) {
            components.unset(this.readMappedEntity(ComponentTypes.getRegistry()));
        }

        return ItemStack.builder().type(itemType).amount(count).components(components).build();
    }

    public ItemStack readPresentItemStack() {
        ItemStack itemStack = this.readItemStack();
        if (itemStack.isEmpty()) {
            throw new RuntimeException("Empty ItemStack not allowed");
        }
        return itemStack;
    }

    public @NotNull ItemStack readItemStack() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            return this.readItemStackModern();
        }

        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (!readBoolean()) {
                return ItemStack.EMPTY;
            }
        }
        int typeID = v1_13_2 ? readVarInt() : readShort();
        if (typeID < 0 && !v1_13_2) { // 1.13.2 doesn't have this logic
            return ItemStack.EMPTY;
        }
        ItemType type = ItemTypes.getById(serverVersion.toClientVersion(), typeID);
        int amount = readByte();
        int legacyData = v1_13_2 ? -1 : readShort();
        NBTCompound nbt = readNBT();
        return ItemStack.builder()
                .type(type)
                .amount(amount)
                .nbt(nbt)
                .legacyData(legacyData)
                .build();
    }

    // item stack serialization was basically completely rewritten in 1.20.5
    @SuppressWarnings("unchecked")
    public void writeItemStackModern(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            this.writeByte(0);
            return;
        }
        this.writeVarInt(itemStack.getAmount());
        this.writeMappedEntity(itemStack.getType());

        if (!itemStack.hasComponentPatches()) {
            this.writeShort(0);
            return; // early return
        }

        // write component patch counts
        Map<ComponentType<?>, Optional<?>> allPatches = itemStack.getComponents().getPatches();
        int presentCount = 0, absentCount = 0;
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                presentCount++;
            } else {
                absentCount++;
            }
        }
        this.writeVarInt(presentCount);
        this.writeVarInt(absentCount);

        // write present patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (patch.getValue().isPresent()) {
                this.writeVarInt(patch.getKey().getId(this.serverVersion.toClientVersion()));
                ((ComponentType<Object>) patch.getKey()).write(this, patch.getValue().get());
            }
        }

        // write absent patches
        for (Map.Entry<ComponentType<?>, Optional<?>> patch : allPatches.entrySet()) {
            if (!patch.getValue().isPresent()) {
                this.writeVarInt(patch.getKey().getId(this.serverVersion.toClientVersion()));
            }
        }
    }

    public void writePresentItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            throw new RuntimeException("Empty ItemStack not allowed");
        }
        this.writeItemStack(itemStack);
    }

    public void writeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            itemStack = ItemStack.EMPTY;
        }

        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            this.writeItemStackModern(itemStack);
            return;
        }

        boolean v1_13_2 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_13_2);
        if (v1_13_2) {
            if (itemStack.isEmpty()) {
                writeBoolean(false);
            } else {
                writeBoolean(true);
                int typeID = itemStack.getType().getId(serverVersion.toClientVersion());
                writeVarInt(typeID);
                writeByte(itemStack.getAmount());
                writeNBT(itemStack.getNBT());
            }
        } else {
            if (itemStack.isEmpty()) {
                writeShort(-1);
            } else {
                int typeID = itemStack.getType().getId(serverVersion.toClientVersion());
                writeShort(typeID);
                writeByte(itemStack.getAmount());
                writeShort(itemStack.getLegacyData());
                writeNBT(itemStack.getNBT());
            }
        }
    }

    public NBTCompound readNBT() {
        return (NBTCompound) this.readNBTRaw();
    }

    public NBT readNBTRaw() {
        return NBTCodec.readNBTFromBuffer(buffer, serverVersion);
    }

    public NBTCompound readUnlimitedNBT() {
        return (NBTCompound) this.readUnlimitedNBTRaw();
    }

    public NBT readUnlimitedNBTRaw() {
        return NBTCodec.readNBTFromBuffer(buffer, serverVersion, NBTLimiter.noop());
    }

    public void writeNBT(NBTCompound nbt) {
        this.writeNBTRaw(nbt);
    }

    public void writeNBTRaw(NBT nbt) {
        NBTCodec.writeNBTToBuffer(buffer, serverVersion, nbt);
    }

    public String readString() {
        return readString(32767);
    }

    public String readString(int maxLen) {
        int j = readVarInt();
        // TODO: Don't throw an exception if the string is too long (but still cut it off and probably kick the player)
        if (j > maxLen * 4) {
            throw new RuntimeException("The received encoded string buffer length is longer than maximum allowed (" + j + " > " + maxLen * 4 + ")");
        } else if (j < 0) {
            throw new RuntimeException("The received encoded string buffer length is less than zero! Weird string!");
        } else {
            String s = ByteBufHelper.toString(buffer, ByteBufHelper.readerIndex(buffer), j, StandardCharsets.UTF_8);
            ByteBufHelper.readerIndex(buffer, ByteBufHelper.readerIndex(buffer) + j);
            if (s.length() > maxLen) {
                throw new RuntimeException("The received string length is longer than maximum allowed (" + j + " > " + maxLen + ")");
            } else {
                return s;
            }
        }
    }

    @Deprecated
    public String readComponentJSON() {
        // needs to be converted to nbt as of 1.20.3
        return AdventureSerializer.asVanilla(this.readComponent());
    }

    public void writeString(String s) {
        writeString(s, 32767);
    }

    public void writeString(String s, int maxLen) {
        writeString(s, maxLen, true);
    }

    public void writeString(String s, int maxLen, boolean substr) {
        if (substr) {
            s = StringUtil.maximizeLength(s, maxLen);
        }
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        if (!substr && bytes.length > maxLen) {
            throw new IllegalStateException("String too big (was " + bytes.length + " bytes encoded, max " + maxLen + ")");
        } else {
            writeVarInt(bytes.length);
            ByteBufHelper.writeBytes(buffer, bytes);
        }
    }

    @Deprecated
    public void writeComponentJSON(String json) {
        // needs to be converted to nbt as of 1.20.3
        this.writeComponent(AdventureSerializer.parseComponent(json));
    }

    public Component readComponent() {
        return this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)
                ? this.readComponentAsNBT() : this.readComponentAsJSON();
    }

    public Component readComponentAsNBT() {
        return AdventureSerializer.fromNbt(readNBTRaw());
    }

    public Component readComponentAsJSON() {
        String jsonString = this.readString(this.getMaxMessageLength());
        return AdventureSerializer.parseComponent(jsonString);
    }

    public void writeComponent(Component component) {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_3)) {
            this.writeComponentAsNBT(component);
        } else {
            this.writeComponentAsJSON(component);
        }
    }

    public void writeComponentAsNBT(Component component) {
        writeNBTRaw(AdventureSerializer.toNbt(component));
    }

    public void writeComponentAsJSON(Component component) {
        String jsonString = AdventureSerializer.toJson(component);
        this.writeString(jsonString, this.getMaxMessageLength());
    }

    public Style readStyle() {
        return AdventureSerializer.getNBTSerializer().deserializeStyle(readNBT());
    }

    public void writeStyle(Style style) {
        writeNBT(AdventureSerializer.getNBTSerializer().serializeStyle(style));
    }

    public ResourceLocation readIdentifier(int maxLen) {
        return new ResourceLocation(readString(maxLen));
    }

    public ResourceLocation readIdentifier() {
        return readIdentifier(32767);
    }

    public void writeIdentifier(ResourceLocation identifier, int maxLen) {
        writeString(identifier.toString(), maxLen);
    }

    public void writeIdentifier(ResourceLocation identifier) {
        writeIdentifier(identifier, 32767);
    }

    public int readUnsignedShort() {
        return ByteBufHelper.readUnsignedShort(buffer);
    }

    public short readShort() {
        return ByteBufHelper.readShort(buffer);
    }

    public void writeShort(int value) {
        ByteBufHelper.writeShort(buffer, value);
    }

    public int readVarShort() {
        int low = readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public void writeVarShort(int value) {
        int low = value & 0x7FFF;
        int high = (value & 0x7F8000) >> 15;
        if (high != 0) {
            low = low | 0x8000;
        }
        writeShort(low);
        if (high != 0) {
            writeByte(high);
        }
    }

    public long readLong() {
        return ByteBufHelper.readLong(buffer);
    }

    public void writeLong(long value) {
        ByteBufHelper.writeLong(buffer, value);
    }

    public long readVarLong() {
        long value = 0;
        int size = 0;
        int b;
        while (((b = readByte()) & 0x80) == 0x80) {
            value |= (long) (b & 0x7F) << (size++ * 7);
        }
        return value | ((long) (b & 0x7F) << (size * 7));
    }

    public void writeVarLong(long l) {
        while ((l & ~0x7F) != 0) {
            this.writeByte((int) (l & 0x7F) | 0x80);
            l >>>= 7;
        }

        this.writeByte((int) l);
    }

    public float readFloat() {
        return ByteBufHelper.readFloat(buffer);
    }

    public void writeFloat(float value) {
        ByteBufHelper.writeFloat(buffer, value);
    }

    public double readDouble() {
        return ByteBufHelper.readDouble(buffer);
    }

    public void writeDouble(double value) {
        ByteBufHelper.writeDouble(buffer, value);
    }

    public byte[] readRemainingBytes() {
        return readBytes(ByteBufHelper.readableBytes(buffer));
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        ByteBufHelper.readBytes(buffer, bytes);
        return bytes;
    }

    public void writeBytes(byte[] array) {
        ByteBufHelper.writeBytes(buffer, array);
    }

    public byte[] readByteArray(int maxLength) {
        int len = readVarInt();
        if (len > maxLength) {
            throw new RuntimeException("The received byte array length is longer than maximum allowed (" + len + " > " + maxLength + ")");
        }
        return readBytes(len);
    }

    public byte[] readByteArray() {
        return readByteArray(ByteBufHelper.readableBytes(buffer));
    }

    public void writeByteArray(byte[] array) {
        writeVarInt(array.length);
        writeBytes(array);
    }

    public int[] readVarIntArray() {
        int readableBytes = ByteBufHelper.readableBytes(buffer);
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("VarIntArray with size " + size + " is bigger than allowed " + readableBytes);
        }

        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArray(int[] array) {
        writeVarInt(array.length);
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray(int size) {
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public byte[] readByteArrayOfSize(int size) {
        byte[] array = new byte[size];
        ByteBufHelper.readBytes(buffer, array);
        return array;
    }

    public void writeByteArrayOfSize(byte[] array) {
        ByteBufHelper.writeBytes(buffer, array);
    }

    public int[] readVarIntArrayOfSize(int size) {
        int[] array = new int[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = readVarInt();
        }
        return array;
    }

    public void writeVarIntArrayOfSize(int[] array) {
        for (int i : array) {
            writeVarInt(i);
        }
    }

    public long[] readLongArray() {
        int readableBytes = ByteBufHelper.readableBytes(buffer) / 8;
        int size = readVarInt();
        if (size > readableBytes) {
            throw new IllegalStateException("LongArray with size " + size + " is bigger than allowed " + readableBytes);
        }
        long[] array = new long[size];

        for (int i = 0; i < array.length; i++) {
            array[i] = readLong();
        }
        return array;
    }

    public void writeLongArray(long[] array) {
        writeVarInt(array.length);
        for (long l : array) {
            writeLong(l);
        }
    }

    public UUID readUUID() {
        long mostSigBits = readLong();
        long leastSigBits = readLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    public Vector3i readBlockPosition() {
        long val = readLong();
        return new Vector3i(val, serverVersion);
    }

    public void writeBlockPosition(Vector3i pos) {
        long val = pos.getSerializedPosition(serverVersion);
        writeLong(val);
    }

    public GameMode readGameMode() {
        return GameMode.getById(readByte());
    }

    public void writeGameMode(@Nullable GameMode mode) {
        int id = mode == null ? -1 : mode.getId();
        writeByte(id);
    }

    public List<EntityData> readEntityMetadata() {
        List<EntityData> list = new ArrayList<>();
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            boolean v1_10 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);
            short index;
            while ((index = readUnsignedByte()) != 255) {
                int typeID = v1_10 ? readVarInt() : readUnsignedByte();
                EntityDataType<?> type = EntityDataTypes.getById(serverVersion.toClientVersion(), typeID);
                if (type == null) {
                    throw new IllegalStateException("Unknown entity metadata type id: " + typeID + " version " + serverVersion.toClientVersion());
                }
                Object value = type.getDataDeserializer().apply(this);
                list.add(new EntityData(index, type, value));
            }
        } else {
            for (byte data = readByte(); data != Byte.MAX_VALUE; data = readByte()) {
                int typeID = (data & 0xE0) >> 5;
                int index = data & 0x1F;
                EntityDataType<?> type = EntityDataTypes.getById(serverVersion.toClientVersion(), typeID);
                Object value = type.getDataDeserializer().apply(this);
                EntityData entityData = new EntityData(index, type, value);
                list.add(entityData);
            }
        }
        return list;
    }

    public void writeEntityMetadata(List<EntityData> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            boolean v1_10 = serverVersion.isNewerThanOrEquals(ServerVersion.V_1_10);
            for (EntityData entityData : list) {
                writeByte(entityData.getIndex());
                if (v1_10) {
                    writeVarInt(entityData.getType().getId(serverVersion.toClientVersion()));
                } else {
                    writeByte(entityData.getType().getId(serverVersion.toClientVersion()));
                }
                entityData.getType().getDataSerializer().accept(this, entityData.getValue());
            }
            writeByte(255); // End of metadata array
        } else {
            for (EntityData entityData : list) {
                int typeID = entityData.getType().getId(serverVersion.toClientVersion());
                int index = entityData.getIndex();
                int data = (typeID << 5 | index & 31) & 255;
                writeByte(data);
                entityData.getType().getDataSerializer().accept(this, entityData.getValue());
            }
            writeByte(127); // End of metadata array
        }
    }

    public void writeEntityMetadata(EntityMetadataProvider metadata) {
        writeEntityMetadata(metadata.entityData(serverVersion.toClientVersion()));
    }

    @Deprecated
    public Dimension readDimension() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            return new Dimension(this.readVarInt());
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                || this.serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
            Dimension dimension = new Dimension(new NBTCompound());
            dimension.setDimensionName(readIdentifier().toString());
            return dimension;
        } else {
            return new Dimension(readNBT());
        }
    }

    @Deprecated
    public void writeDimension(Dimension dimension) {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_20_5)) {
            this.writeVarInt(dimension.getId());
            return;
        }
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                || this.serverVersion.isOlderThan(ServerVersion.V_1_16_2)) {
            writeString(dimension.getDimensionName(), 32767);
        } else {
            writeNBT(dimension.getAttributes());
        }
    }

    public SaltSignature readSaltSignature() {
        long salt = readLong();
        byte[] signature;
        //1.19.3+
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            //Read optional signature
            if (readBoolean()) {
                signature = readBytes(256);
            } else {
                signature = new byte[0];
            }
        } else {
            signature = readByteArray(256);
        }
        return new SaltSignature(salt, signature);
    }

    public void writeSaltSignature(SaltSignature signature) {
        writeLong(signature.getSalt());
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) {
            boolean present = signature.getSignature().length != 0;
            writeBoolean(present);
            if (present) {
                writeBytes(signature.getSignature());
            }

        } else {
            writeByteArray(signature.getSignature());
        }
    }

    public PublicKey readPublicKey() {
        return MinecraftEncryptionUtil.publicKey(readByteArray(512));
    }

    public void writePublicKey(PublicKey publicKey) {
        writeByteArray(publicKey.getEncoded());
    }

    public PublicProfileKey readPublicProfileKey() {
        Instant expiresAt = readTimestamp();
        PublicKey key = readPublicKey();
        byte[] keySignature = readByteArray(4096);
        return new PublicProfileKey(expiresAt, key, keySignature);
    }

    public void writePublicProfileKey(PublicProfileKey key) {
        writeTimestamp(key.getExpiresAt());
        writePublicKey(key.getKey());
        writeByteArray(key.getKeySignature());
    }

    public RemoteChatSession readRemoteChatSession() {
        return new RemoteChatSession(readUUID(), readPublicProfileKey());
    }

    public void writeRemoteChatSession(RemoteChatSession chatSession) {
        writeUUID(chatSession.getSessionId());
        writePublicProfileKey(chatSession.getPublicProfileKey());
    }

    public Instant readTimestamp() {
        return Instant.ofEpochMilli(readLong());
    }

    public void writeTimestamp(Instant timestamp) {
        writeLong(timestamp.toEpochMilli());
    }

    public SignatureData readSignatureData() {
        return new SignatureData(readTimestamp(), readPublicKey(), readByteArray(4096));
    }

    public void writeSignatureData(SignatureData signatureData) {
        writeTimestamp(signatureData.getTimestamp());
        writePublicKey(signatureData.getPublicKey());
        writeByteArray(signatureData.getSignature());
    }

    public static <K> IntFunction<K> limitValue(IntFunction<K> function, int limit) {
        return i -> {
            if (i > limit) {
                throw new RuntimeException("Value " + i + " is larger than limit " + limit);
            }
            return function.apply(i);
        };
    }

    public WorldBlockPosition readWorldBlockPosition() {
        return new WorldBlockPosition(readIdentifier(), readBlockPosition());
    }

    public void writeWorldBlockPosition(WorldBlockPosition pos) {
        writeIdentifier(pos.getWorld());
        writeBlockPosition(pos.getBlockPosition());
    }

    public LastSeenMessages.Entry readLastSeenMessagesEntry() {
        return new LastSeenMessages.Entry(readUUID(), readByteArray());
    }

    public void writeLastMessagesEntry(LastSeenMessages.Entry entry) {
        writeUUID(entry.getUUID());
        writeByteArray(entry.getLastVerifier());
    }

    public LastSeenMessages.Update readLastSeenMessagesUpdate() {
        int signedMessages = readVarInt();
        BitSet seen = BitSet.valueOf(readBytes(3));
        return new LastSeenMessages.Update(signedMessages, seen);
    }

    public void writeLastSeenMessagesUpdate(LastSeenMessages.Update update) {
        writeVarInt(update.getOffset());
        byte[] lastSeen = Arrays.copyOf(update.getAcknowledged().toByteArray(), 3);
        writeBytes(lastSeen);
    }

    public LastSeenMessages.LegacyUpdate readLegacyLastSeenMessagesUpdate() {
        LastSeenMessages lastSeenMessages = readLastSeenMessages();
        LastSeenMessages.Entry lastReceived = readOptional(PacketWrapper::readLastSeenMessagesEntry);
        return new LastSeenMessages.LegacyUpdate(lastSeenMessages, lastReceived);
    }

    public void writeLegacyLastSeenMessagesUpdate(LastSeenMessages.LegacyUpdate legacyUpdate) {
        writeLastSeenMessages(legacyUpdate.getLastSeenMessages());
        writeOptional(legacyUpdate.getLastReceived(), PacketWrapper::writeLastMessagesEntry);
    }

    public MessageSignature readMessageSignature() {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19_3)) return new MessageSignature(readBytes(256));
        else return new MessageSignature(readByteArray());
    }

    public void writeMessageSignature(MessageSignature messageSignature) {
        writeBytes(messageSignature.getBytes());
    }


    public MessageSignature.Packed readMessageSignaturePacked() {
        int id = readVarInt() - 1;
        if (id == -1) {
            return new MessageSignature.Packed(new MessageSignature(readBytes(256)));
        }
        return new MessageSignature.Packed(id);
    }

    public void writeMessageSignaturePacked(MessageSignature.Packed messageSignaturePacked) {
        writeVarInt(messageSignaturePacked.getId() + 1);
        if (messageSignaturePacked.getFullSignature().isPresent()) {
            writeBytes(messageSignaturePacked.getFullSignature().get().getBytes());
        }
    }

    public LastSeenMessages.Packed readLastSeenMessagesPacked() {
        List<MessageSignature.Packed> packedMessageSignatures = readCollection(limitValue(ArrayList::new, 20), PacketWrapper::readMessageSignaturePacked);
        return new LastSeenMessages.Packed(packedMessageSignatures);
    }

    public void writeLastSeenMessagesPacked(LastSeenMessages.Packed lastSeenMessagesPacked) {
        writeCollection(lastSeenMessagesPacked.getPackedMessageSignatures(), PacketWrapper::writeMessageSignaturePacked);
    }

    public LastSeenMessages readLastSeenMessages() {
        List<LastSeenMessages.Entry> entries = readCollection(limitValue(ArrayList::new, 5),
                PacketWrapper::readLastSeenMessagesEntry);
        return new LastSeenMessages(entries);
    }

    public void writeLastSeenMessages(LastSeenMessages lastSeenMessages) {
        writeCollection(lastSeenMessages.getEntries(), PacketWrapper::writeLastMessagesEntry);
    }

    public List<SignedCommandArgument> readSignedCommandArguments() {
        return readCollection(limitValue(ArrayList::new, 8), (_packet) -> new SignedCommandArgument(readString(16), readMessageSignature()));
    }

    public void writeSignedCommandArguments(List<SignedCommandArgument> signedArguments) {
        writeCollection(signedArguments, (_packet, argument) -> {
            writeString(argument.getArgument(), 16);
            writeMessageSignature(argument.getSignature());
        });
    }

    public BitSet readBitSet() {
        return BitSet.valueOf(readLongArray());
    }

    public void writeBitSet(BitSet bitSet) {
        writeLongArray(bitSet.toLongArray());
    }

    public FilterMask readFilterMask() {
        FilterMaskType type = FilterMaskType.getById(readVarInt());
        switch (type) {
            case PARTIALLY_FILTERED:
                return new FilterMask(readBitSet());
            case PASS_THROUGH:
                return FilterMask.PASS_THROUGH;
            case FULLY_FILTERED:
                return FilterMask.FULLY_FILTERED;
            default:
                return null;
        }
    }

    public void writeFilterMask(FilterMask filterMask) {
        writeVarInt(filterMask.getType().getId());
        if (filterMask.getType() == FilterMaskType.PARTIALLY_FILTERED) {
            writeBitSet(filterMask.getMask());
        }
    }

    public MerchantOffer readMerchantOffer() {
        ItemStack buyItemPrimary = MerchantItemCost.readItem(this);
        ItemStack sellItem = this.readItemStack();
        ItemStack buyItemSecondary = this.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)
                || this.getServerVersion().isOlderThan(ServerVersion.V_1_19)
                ? this.readOptional(MerchantItemCost::readItem) : this.readItemStack();
        boolean tradeDisabled = readBoolean();
        int uses = readInt();
        int maxUses = readInt();
        int xp = readInt();
        int specialPrice = readInt();
        float priceMultiplier = readFloat();
        int demand = readInt();
        MerchantOffer data = MerchantOffer.of(buyItemPrimary, buyItemSecondary,
                sellItem, uses, maxUses, xp, specialPrice, priceMultiplier, demand);
        if (tradeDisabled) {
            data.setUses(data.getMaxUses());
        }
        return data;
    }

    public void writeMerchantOffer(MerchantOffer data) {
        MerchantItemCost.writeItem(this, data.getFirstInputItem());
        this.writeItemStack(data.getOutputItem());
        ItemStack buyItemSecondary = data.getSecondInputItem();
        // in this case writing empty itemstacks is just
        // as good as writing nothing according to vanilla server code
        if (buyItemSecondary != null && buyItemSecondary.isEmpty()) {
            buyItemSecondary = null;
        }
        if (this.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_5)
                || this.getServerVersion().isOlderThan(ServerVersion.V_1_19)) {
            this.writeOptional(buyItemSecondary, MerchantItemCost::writeItem);
        } else {
            this.writeItemStack(buyItemSecondary);
        }
        writeBoolean(data.getUses() >= data.getMaxUses());
        writeInt(data.getUses());
        writeInt(data.getMaxUses());
        writeInt(data.getXp());
        writeInt(data.getSpecialPrice());
        writeFloat(data.getPriceMultiplier());
        writeInt(data.getDemand());
    }

    public ChatType.Bound readChatTypeBoundNetwork() {
        ChatType type = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)
                ? this.readMappedEntityOrDirect(ChatTypes.getRegistry(), ChatType::readDirect)
                : this.readMappedEntity(ChatTypes.getRegistry());
        Component name = readComponent();
        Component targetName = readOptional(PacketWrapper::readComponent);
        return new ChatType.Bound(type, name, targetName);
    }

    public void writeChatTypeBoundNetwork(ChatType.Bound chatFormatting) {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21)) {
            this.writeMappedEntityOrDirect(chatFormatting.getType(), ChatType::writeDirect);
        } else {
            this.writeMappedEntity(chatFormatting.getType());
        }
        writeComponent(chatFormatting.getName());
        writeOptional(chatFormatting.getTargetName(), PacketWrapper::writeComponent);
    }

    public Node readNode() {
        byte flags = readByte();
        int nodeType = flags & 0x03; // 0: root, 1: literal, 2: argument

        List<Integer> children = readList(PacketWrapper::readVarInt);

        int redirectNodeIndex = ((flags & 0x08) != 0) ? readVarInt() : 0;
        if (nodeType == 2) {
            String name = this.readString();
            Parser parser = this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)
                    ? this.readMappedEntity(Parsers::getById)
                    : Parsers.getByName(this.readIdentifier().toString());
            List<Object> properties = parser.readProperties(this).orElse(null);
            ResourceLocation suggestionType = ((flags & 0x10) != 0) ? this.readIdentifier() : null;
            return new Node(flags, children, redirectNodeIndex, name, parser, properties, suggestionType);
        } else if (nodeType == 1) {
            String name = this.readString();
            return new Node(flags, children, redirectNodeIndex, name, (Parser) null, null, null);
        } else {
            return new Node(flags, children, redirectNodeIndex, null, (Parser) null, null, null);
        }
    }

    public void writeNode(Node node) {
        writeByte(node.getFlags());
        writeList(node.getChildren(), PacketWrapper::writeVarInt);
        if ((node.getFlags() & 0x08) != 0) {
            writeVarInt(node.getRedirectNodeIndex());
        }
        node.getName().ifPresent(this::writeString);
        if (node.getParser().isPresent()) {
            Parser parser = node.getParser().get();
            if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_19)) {
                this.writeMappedEntity(parser);
            } else {
                this.writeIdentifier(parser.getName());
            }
            if (node.getProperties().isPresent()) {
                parser.writeProperties(this, node.getProperties().get());
            }
        }
        node.getSuggestionsType().ifPresent(this::writeIdentifier);
    }

    public KnownPack readKnownPack() {
        String namespace = this.readString();
        String id = this.readString();
        String version = this.readString();
        return new KnownPack(namespace, id, version);
    }

    public void writeKnownPack(KnownPack knownPack) {
        this.writeString(knownPack.getNamespace());
        this.writeString(knownPack.getId());
        this.writeString(knownPack.getVersion());
    }

    public <T extends Enum<T>> EnumSet<T> readEnumSet(Class<T> enumClazz) {
        T[] values = enumClazz.getEnumConstants();
        byte[] bytes = new byte[-Math.floorDiv(-values.length, 8)];
        ByteBufHelper.readBytes(getBuffer(), bytes);
        BitSet bitSet = BitSet.valueOf(bytes);
        EnumSet<T> set = EnumSet.noneOf(enumClazz);
        for (int i = 0; i < values.length; i++) {
            if (bitSet.get(i)) {
                set.add(values[i]);
            }
        }
        return set;
    }

    public <T extends Enum<T>> void writeEnumSet(EnumSet<T> set, Class<T> enumClazz) {
        T[] values = enumClazz.getEnumConstants();
        BitSet bitSet = new BitSet(values.length);
        for (int i = 0; i < values.length; i++) {
            if (set.contains(values[i])) {
                bitSet.set(i);
            }
        }
        writeBytes(Arrays.copyOf(bitSet.toByteArray(), -Math.floorDiv(-values.length, 8)));
    }

    @Experimental
    public <U, V, R> U readMultiVersional(VersionComparison version, ServerVersion target, Reader<V> first, Reader<R> second) {
        if (serverVersion.is(version, target)) {
            return (U) first.apply(this);
        } else {
            return (U) second.apply(this);
        }
    }

    @Experimental
    public <V> void writeMultiVersional(VersionComparison version, ServerVersion target, V value, Writer<V> first, Writer<V> second) {
        if (serverVersion.is(version, target)) {
            first.accept(this, value);
        } else {
            second.accept(this, value);
        }
    }

    public <R> @Nullable R readOptional(Reader<R> reader) {
        return this.readBoolean() ? reader.apply(this) : null;
    }

    public <V> void writeOptional(@Nullable V value, Writer<V> writer) {
        if (value != null) {
            this.writeBoolean(true);
            writer.accept(this, value);
        } else {
            this.writeBoolean(false);
        }
    }

    public <Z> Optional<Z> readRealOptional(Reader<Z> reader) {
        if (this.readBoolean()) {
            return Optional.of(reader.apply(this));
        }
        return Optional.empty();
    }

    public <Z> void writeRealOptional(Optional<Z> optional, Writer<Z> writer) {
        if (optional.isPresent()) {
            this.writeBoolean(true);
            writer.accept(this, optional.get());
        } else {
            this.writeBoolean(false);
        }
    }

    public <K, C extends Collection<K>> C readCollection(IntFunction<C> function, Reader<K> reader) {
        int size = this.readVarInt();
        Collection<K> collection = function.apply(size);
        for (int i = 0; i < size; ++i) {
            collection.add(reader.apply(this));
        }
        return (C) collection;
    }

    public <K> void writeCollection(Collection<K> collection, Writer<K> writer) {
        this.writeVarInt(collection.size());
        for (K key : collection) {
            writer.accept(this, key);
        }
    }

    public <K> List<K> readList(Reader<K> reader) {
        return this.readCollection(ArrayList::new, reader);
    }

    public <K> void writeList(List<K> list, Writer<K> writer) {
        writeVarInt(list.size());
        for (K key : list) {
            writer.accept(this, key);
        }
    }

    @SuppressWarnings("unchecked") // not unchecked
    public <K> K[] readArray(Reader<K> reader, Class<K> clazz) {
        int length = this.readVarInt();
        K[] array = (K[]) Array.newInstance(clazz, length);
        for (int i = 0; i < length; i++) {
            array[i] = reader.apply(this);
        }
        return array;
    }

    public <K> void writeArray(K[] array, Writer<K> writer) {
        this.writeVarInt(array.length);
        for (K element : array) {
            writer.accept(this, element);
        }
    }

    public <Z extends Enum<?>> Z readEnum(Class<Z> clazz) {
        return this.readEnum(clazz.getEnumConstants());
    }

    public <Z extends Enum<?>> Z readEnum(Z[] values) {
        return values[this.readVarInt()];
    }

    public void writeEnum(Enum<?> value) {
        this.writeVarInt(value.ordinal());
    }

    public <Z extends MappedEntity> Z readMappedEntity(BiFunction<ClientVersion, Integer, Z> getter) {
        int id = this.readVarInt();
        Z entity = getter.apply(this.serverVersion.toClientVersion(), id);
        if (entity == null) {
            throw new IllegalStateException("Can't find mapped entity with id " + id + " using " + getter);
        }
        return entity;
    }

    public IRegistryHolder getRegistryHolder() {
        // workaround to make packet wrappers work without user context on spigot/fabric servers
        // this will not work for bungee or velocity, as we need to have some reference to get
        // the actual cache key
        return this.user != null ? this.user : GlobalRegistryHolder.INSTANCE;
    }

    public <Z extends MappedEntity> Z readMappedEntityOrDirect(
            BiFunction<ClientVersion, Integer, Z> getter, Reader<Z> directReader) {
        int id = this.readVarInt();
        if (id == 0) { // not registered in registry
            return directReader.apply(this);
        }
        Z entity = getter.apply(this.serverVersion.toClientVersion(), id - 1);
        if (entity == null) {
            throw new IllegalStateException("Can't find mapped entity with id " + id + " using " + getter);
        }
        return entity;
    }

    public <Z extends MappedEntity> Z readMappedEntity(IRegistry<Z> registry) {
        IRegistry<Z> replacedRegistry = this.getRegistryHolder().getRegistryOr(registry);
        return this.readMappedEntity((BiFunction<ClientVersion, Integer, Z>) replacedRegistry);
    }

    public <Z extends MappedEntity> Z readMappedEntityOrDirect(IRegistry<Z> registry, Reader<Z> directReader) {
        IRegistry<Z> replacedRegistry = this.getRegistryHolder().getRegistryOr(registry);
        return this.readMappedEntityOrDirect((BiFunction<ClientVersion, Integer, Z>) replacedRegistry, directReader);
    }

    public void writeMappedEntity(MappedEntity entity) {
        if (!entity.isRegistered()) {
            throw new IllegalArgumentException("Can't write id of unregistered entity "
                    + entity.getName() + " (" + entity + ")");
        }
        this.writeVarInt(entity.getId(this.serverVersion.toClientVersion()));
    }

    public <Z extends MappedEntity> void writeMappedEntityOrDirect(Z entity, Writer<Z> writer) {
        if (!entity.isRegistered()) {
            this.writeVarInt(0);
            writer.accept(this, entity);
            return;
        }
        int id = entity.getId(this.serverVersion.toClientVersion());
        this.writeVarInt(id + 1);
    }

    public int readContainerId() {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            return this.readVarInt();
        }
        return this.readUnsignedByte();
    }

    public void writeContainerId(int containerId) {
        if (this.serverVersion.isNewerThanOrEquals(ServerVersion.V_1_21_2)) {
            this.writeVarInt(containerId);
        } else {
            this.writeByte(containerId);
        }
    }

    public void writeRotation(float rotation) {
        this.writeByte((byte) MathUtil.floor(rotation * 256f / 360f));
    }

    public float readRotation() {
        return (float) (this.readByte() * 360) / 256f;
    }

    public @Nullable Integer readNullableVarInt() {
        int i = this.readVarInt();
        return i == 0 ? null : i - 1;
    }

    public void writeNullableVarInt(@Nullable Integer i) {
        this.writeVarInt(i == null ? 0 : i + 1);
    }

    @FunctionalInterface
    public interface Reader<T> extends Function<PacketWrapper<?>, T> {
    }

    @FunctionalInterface
    public interface Writer<T> extends BiConsumer<PacketWrapper<?>, T> {
    }
}
