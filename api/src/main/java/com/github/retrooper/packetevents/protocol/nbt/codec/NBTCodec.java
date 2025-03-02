/*
 * This file is part of ProtocolSupport - https://github.com/ProtocolSupport/ProtocolSupport
 * Copyright (C) 2021 ProtocolSupport
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.github.retrooper.packetevents.protocol.nbt.codec;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.netty.buffer.ByteBufInputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufOutputStream;
import com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.nbt.serializer.DefaultNBTSerializer;
import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class NBTCodec {

    //PacketEvents start: JSON -> NBT conversion method
    @Deprecated
    public static NBT jsonToNBT(JsonElement element) {
        //Deal with the primitives first
        if (element instanceof JsonPrimitive) {
            if (((JsonPrimitive) element).isBoolean()) {
                return new NBTByte(element.getAsBoolean());
            }
            else if (((JsonPrimitive) element).isString()) {
                return new NBTString(element.getAsString());
            }
            else if (((JsonPrimitive) element).isNumber()) {
                Number num = element.getAsNumber();
                if (num instanceof Float) {
                    return new NBTFloat(num.floatValue());
                }
                else if(num instanceof Double) {
                    return new NBTDouble(num.doubleValue());
                }
                else if (num instanceof Byte) {
                    return new NBTByte(num.byteValue());
                }
                else if (num instanceof Short) {
                    return new NBTShort(num.shortValue());
                }
                else if (num instanceof Integer || num instanceof LazilyParsedNumber) {
                    return new NBTInt(num.intValue());
                }
                else if (num instanceof Long) {
                    return new NBTLong(num.longValue());
                }
            }
        }
        //Then handle arrays
        else if (element instanceof JsonArray) {
            List<NBT> list = new ArrayList<>();
            for (JsonElement var : ((JsonArray)element)) {
                list.add(jsonToNBT(var));
            }
            if (list.isEmpty()) {
                return new NBTList<>(NBTType.COMPOUND);
            }
            NBTList<? extends NBT> l = new NBTList<>(list.get(0).getType());
            for (NBT nbt : list) {
                l.addTagUnsafe(nbt);
            }
            return l;
        }
        //Handle json objects
        else if (element instanceof JsonObject) {
            JsonObject obj = (JsonObject) element;
            NBTCompound compound = new NBTCompound();
            for (Map.Entry<String, JsonElement> jsonEntry : obj.entrySet()) {
                compound.setTag(jsonEntry.getKey(), jsonToNBT(jsonEntry.getValue()));
            }
            return compound;
        }
        else if (element instanceof JsonNull || element == null) {
            return new NBTCompound();
        }
        throw new IllegalStateException("Failed to convert JSON to NBT " + element.toString());
    }
    //PacketEvents end

    //PacketEvents start - NBT to JSON conversion
    @Deprecated
    public static JsonElement nbtToJson(NBT nbt, boolean parseByteAsBool) {
        //TODO once I make my own nbt implementation, make a toJSON method that each nbt class implements to make this  a one liner
        if (nbt instanceof NBTNumber) {
            if (nbt instanceof NBTByte && parseByteAsBool) {
                byte val = ((NBTByte)nbt).getAsByte();
                if (val == 0) {
                    return new JsonPrimitive(false);
                }
                else if (val == 1) {
                    return new JsonPrimitive(true);
                }
            }
            return new JsonPrimitive(((NBTNumber)nbt).getAsNumber());
        }
        else if (nbt instanceof NBTString) {
            return new JsonPrimitive(((NBTString) nbt).getValue());
        }
        else if (nbt instanceof NBTList) {
            NBTList<? extends NBT> list = (NBTList<? extends NBT>) nbt;
            JsonArray jsonArray = new JsonArray();

            list.getTags().forEach(tag -> {
                jsonArray.add(nbtToJson(tag, parseByteAsBool));
            });
            return jsonArray;
        }
        else if (nbt instanceof NBTEnd) {
            throw new IllegalStateException("Encountered the NBTEnd tag during the NBT to JSON conversion: " + nbt.toString());
        }
        else if (nbt instanceof NBTCompound) {
            JsonObject jsonObject = new JsonObject();
            Map<String, NBT> compoundTags = ((NBTCompound)nbt).getTags();
            for (String tagName : compoundTags.keySet()) {
                NBT tag = compoundTags.get(tagName);
                JsonElement jsonValue = nbtToJson(tag, parseByteAsBool);
                jsonObject.add(tagName, jsonValue);
            }
            return jsonObject;
        }
        else {
            throw new IllegalStateException("Failed to convert NBT to JSON.");
        }
    }
    //PacketEvents end

    public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion) {
        NBTLimiter limiter = NBTLimiter.forBuffer(byteBuf);
        return readNBTFromBuffer(byteBuf, serverVersion, limiter);
    }

    public static NBT readNBTFromBuffer(Object byteBuf, ServerVersion serverVersion, NBTLimiter limiter) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try {
                final boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                return DefaultNBTSerializer.INSTANCE.deserializeTag(
                        limiter, new ByteBufInputStream(byteBuf), named);
            } catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
        else {
            try {
                final short length = ByteBufHelper.readShort(byteBuf);
                if (length < 0) {
                    return null;
                }
                Object slicedBuffer = ByteBufHelper.readSlice(byteBuf, length);
                try (DataInputStream stream = new DataInputStream(
                        new GZIPInputStream(new ByteBufInputStream(slicedBuffer)))) {
                    return DefaultNBTSerializer.INSTANCE.deserializeTag(limiter, stream);
                }
            }
            catch (IOException ex) {
                throw new IllegalStateException(ex);
            }
        }
    }

    public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBTCompound tag) {
        writeNBTToBuffer(byteBuf, serverVersion, (NBT) tag);
    }

    public static void writeNBTToBuffer(Object byteBuf, ServerVersion serverVersion, NBT tag) {
        if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_8)) {
            try (ByteBufOutputStream outputStream = new ByteBufOutputStream(byteBuf)) {
                if (tag != null) {
                    boolean named = serverVersion.isOlderThan(ServerVersion.V_1_20_2);
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, tag, named);
                } else {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputStream, NBTEnd.INSTANCE);
                }
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
        else {
            if (tag == null) {
                ByteBufHelper.writeShort(byteBuf, -1);
            } else {
                int lengthWriterIndex = ByteBufHelper.writerIndex(byteBuf);
                ByteBufHelper.writeShort(byteBuf, 0);
                int writerIndexDataStart = ByteBufHelper.writerIndex(byteBuf);
                try (DataOutputStream outputstream = new DataOutputStream(new GZIPOutputStream(new ByteBufOutputStream(byteBuf)))) {
                    DefaultNBTSerializer.INSTANCE.serializeTag(outputstream, tag);
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
                int writerIndexDataEnd = ByteBufHelper.writerIndex(byteBuf);
                ByteBufHelper.writerIndex(byteBuf, lengthWriterIndex);
                ByteBufHelper.writeShort(byteBuf, writerIndexDataEnd - writerIndexDataStart);
                ByteBufHelper.writerIndex(byteBuf, writerIndexDataEnd);
            }
        }
    }
}
