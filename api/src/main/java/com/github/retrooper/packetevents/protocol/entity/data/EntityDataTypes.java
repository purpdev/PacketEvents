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

package com.github.retrooper.packetevents.protocol.entity.data;

import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.armadillo.ArmadilloState;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class EntityDataTypes {

    private static final VersionedRegistry<EntityDataType<?>> REGISTRY = new VersionedRegistry<>("entity_data_serializer");

    public static final EntityDataType<Byte> BYTE = define("byte", PacketWrapper::readByte, PacketWrapper::writeByte);

    // short was removed in 1.9+
    public static final EntityDataType<Short> SHORT = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);

    public static final EntityDataType<Integer> INT = define("int", wrapper -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readVarInt();
        } else {
            return wrapper.readInt();
        }
    }, (wrapper, value) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(value);
        } else {
            wrapper.writeInt(value);
        }
    });

    public static final EntityDataType<Long> LONG = define("long", PacketWrapper::readVarLong, PacketWrapper::writeVarLong);

    public static final EntityDataType<Float> FLOAT = define("float", PacketWrapper::readFloat, PacketWrapper::writeFloat);

    public static final EntityDataType<String> STRING = define("string", PacketWrapper::readString, PacketWrapper::writeString);

    @Deprecated
    public static final EntityDataType<String> COMPONENT = define("component", PacketWrapper::readComponentJSON, PacketWrapper::writeComponentJSON);
    public static final EntityDataType<Component> ADV_COMPONENT = define("component", PacketWrapper::readComponent, PacketWrapper::writeComponent);

    @Deprecated
    public static final EntityDataType<Optional<String>> OPTIONAL_COMPONENT = define("optional_component", readOptionalComponentJSONDeserializer(), writeOptionalComponentJSONSerializer());
    public static final EntityDataType<Optional<Component>> OPTIONAL_ADV_COMPONENT = define("optional_component", readOptionalComponentDeserializer(), writeOptionalComponentSerializer());

    public static final EntityDataType<ItemStack> ITEMSTACK = define("itemstack", PacketWrapper::readItemStack, PacketWrapper::writeItemStack);

    public static final EntityDataType<Optional<ItemStack>> OPTIONAL_ITEMSTACK = define("optional_itemstack",
            (PacketWrapper<?> wrapper) -> Optional.of(wrapper.readItemStack()),
            (PacketWrapper<?> wrapper, Optional<ItemStack> value) -> wrapper.writeItemStack(value.orElse(null)));

    // boolean was added in 1.9
    public static final EntityDataType<Boolean> BOOLEAN = define("boolean", PacketWrapper::readBoolean, PacketWrapper::writeBoolean);

    public static final EntityDataType<Vector3f> ROTATION = define("rotation",
            (PacketWrapper<?> wrapper) -> new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Vector3f value) -> {
                wrapper.writeFloat(value.x);
                wrapper.writeFloat(value.y);
                wrapper.writeFloat(value.z);
            });

    // 1.7 -> 1.18 block_position is just 3 ints, not serialized with a long
    public static final EntityDataType<Vector3i> BLOCK_POSITION = define("block_position", (PacketWrapper<?> wrapper) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readBlockPosition();
        } else {
            int x = wrapper.readInt();
            int y = wrapper.readInt();
            int z = wrapper.readInt();
            return new Vector3i(x, y, z);
        }
    }, (wrapper, blockPosition) -> {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeBlockPosition(blockPosition);
        } else {
            wrapper.writeInt(blockPosition.getX());
            wrapper.writeInt(blockPosition.getY());
            wrapper.writeInt(blockPosition.getZ());
        }
    });

    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION = define("optional_block_position",
            readOptionalBlockPositionDeserializer(), writeOptionalBlockPositionSerializer());

    public static final EntityDataType<BlockFace> BLOCK_FACE = define("block_face", (PacketWrapper<?> wrapper) -> {
                int id = wrapper.readVarInt();
                return BlockFace.getBlockFaceByValue(id);
            },
            (PacketWrapper<?> wrapper, BlockFace value) -> wrapper.writeVarInt(value.getFaceValue()));

    public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = define("optional_uuid",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(PacketWrapper::readUUID)),
            (PacketWrapper<?> wrapper, Optional<UUID> value) ->
                    wrapper.writeOptional(value.orElse(null), PacketWrapper::writeUUID));

    public static final EntityDataType<Integer> BLOCK_STATE = define("block_state",
            readIntDeserializer(), writeIntSerializer());

    public static final EntityDataType<Integer> OPTIONAL_BLOCK_STATE = define("optional_block_state", readIntDeserializer(), writeIntSerializer());

    // nbt was added in 1.12
    public static final EntityDataType<NBTCompound> NBT = define("nbt", PacketWrapper::readNBT, PacketWrapper::writeNBT);

    public static final EntityDataType<Particle<?>> PARTICLE = define("particle", Particle::read, Particle::write);

    public static final EntityDataType<VillagerData> VILLAGER_DATA = define("villager_data", PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);

    public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = define("optional_int", (PacketWrapper<?> wrapper) -> {
        int i = wrapper.readVarInt();
        return i == 0 ? Optional.empty() : Optional.of(i - 1);
    }, (PacketWrapper<?> wrapper, Optional<Integer> value) -> {
        wrapper.writeVarInt(value.orElse(-1) + 1);
    });

    public static final EntityDataType<EntityPose> ENTITY_POSE = define("entity_pose", (PacketWrapper<?> wrapper) -> {
        int id = wrapper.readVarInt();
        return EntityPose.getById(wrapper.getServerVersion().toClientVersion(), id);
    }, (PacketWrapper<?> wrapper, EntityPose value) -> wrapper.writeVarInt(value.getId(wrapper.getServerVersion().toClientVersion())));

    public static final EntityDataType<Integer> CAT_VARIANT = define("cat_variant_type", readIntDeserializer(), writeIntSerializer());

    public static final EntityDataType<Integer> FROG_VARIANT = define("frog_variant_type", readIntDeserializer(), writeIntSerializer());

    public static final EntityDataType<Optional<WorldBlockPosition>> OPTIONAL_GLOBAL_POSITION = define("optional_global_position",
            (PacketWrapper<?> wrapper) -> Optional.ofNullable(wrapper.readOptional(w -> new WorldBlockPosition(new ResourceLocation(w.readString(32767)), w.readBlockPosition()))),
            (PacketWrapper<?> wrapper, Optional<WorldBlockPosition> value) -> wrapper.writeOptional(value.orElse(null), (w, globalPos) -> {
                w.writeString(globalPos.getWorld().toString());
                w.writeBlockPosition(globalPos.getBlockPosition());
            }));

    public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE = define("painting_variant_type", readIntDeserializer(), writeIntSerializer());

    public static final EntityDataType<SnifferState> SNIFFER_STATE = define("sniffer_state", (PacketWrapper<?> wrapper) -> {
        int id = wrapper.readVarInt();
        return SnifferState.values()[id];
    }, (PacketWrapper<?> wrapper, SnifferState value) -> wrapper.writeVarInt(value.ordinal()));

    public static final EntityDataType<Vector3f> VECTOR3F = define("vector3f",
            (PacketWrapper<?> wrapper) -> new Vector3f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Vector3f value) -> {
                wrapper.writeFloat(value.x);
                wrapper.writeFloat(value.y);
                wrapper.writeFloat(value.z);
            });

    public static final EntityDataType<Quaternion4f> QUATERNION = define("quaternion",
            (PacketWrapper<?> wrapper) -> new Quaternion4f(wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat(), wrapper.readFloat()),
            (PacketWrapper<?> wrapper, Quaternion4f value) -> {
                wrapper.writeFloat(value.getX());
                wrapper.writeFloat(value.getY());
                wrapper.writeFloat(value.getZ());
                wrapper.writeFloat(value.getW());
            });

    // Added in 1.20.5
    public static final EntityDataType<ArmadilloState> ARMADILLO_STATE = define("armadillo_state",
            (PacketWrapper<?> wrapper) -> ArmadilloState.values()[wrapper.readVarInt()],
            (PacketWrapper<?> wrapper, ArmadilloState value) -> wrapper.writeVarInt(value.ordinal())
    );

    public static final EntityDataType<List<Particle<?>>> PARTICLES = define("particles",
            wrapper -> wrapper.readList(Particle::read),
            (wrapper, particles) -> wrapper.writeList(particles, Particle::write)
    );

    public static final EntityDataType<Integer> WOLF_VARIANT =
            define("wolf_variant_type", readIntDeserializer(), writeIntSerializer());

    private EntityDataTypes() {
    }

    public static VersionedRegistry<EntityDataType<?>> getRegistry() {
        return REGISTRY;
    }

    /**
     * Returns an immutable view of the entity-data types.
     *
     * @return Entity-Data Types
     */
    public static Collection<EntityDataType<?>> values() {
        return REGISTRY.getEntries();
    }

    public static EntityDataType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static EntityDataType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    @ApiStatus.Internal
    public static <T, Z extends T> EntityDataType<Z> define(
            String name, PacketWrapper.Reader<Z> reader, PacketWrapper.Writer<T> writer
    ) {
        return REGISTRY.define(name, data -> new EntityDataType<>(data, reader, writer::accept));
    }

    private static PacketWrapper.Reader<Integer> readIntDeserializer() {
        return wrapper -> wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9) ?
                wrapper.readVarInt() : wrapper.readInt();
    }

    private static PacketWrapper.Writer<Number> writeIntSerializer() {
        return (wrapper, value) -> {
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                wrapper.writeVarInt(value.intValue());
            } else {
                wrapper.writeInt(value.intValue());
            }
        };
    }

    @Deprecated
    private static PacketWrapper.Reader<Optional<String>> readOptionalComponentJSONDeserializer() {
        return wrapper -> wrapper.readJavaOptional(PacketWrapper::readComponentJSON);
    }

    @Deprecated
    private static PacketWrapper.Writer<Optional<String>> writeOptionalComponentJSONSerializer() {
        return (wrapper, value) -> wrapper.writeJavaOptional(value, PacketWrapper::writeComponentJSON);
    }

    private static PacketWrapper.Reader<Optional<Component>> readOptionalComponentDeserializer() {
        return wrapper -> wrapper.readJavaOptional(PacketWrapper::readComponent);
    }

    private static PacketWrapper.Writer<Optional<Component>> writeOptionalComponentSerializer() {
        return (wrapper, value) -> wrapper.writeJavaOptional(value, PacketWrapper::writeComponent);
    }

    private static PacketWrapper.Reader<Optional<Vector3i>> readOptionalBlockPositionDeserializer() {
        return wrapper -> {
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                return wrapper.readJavaOptional(PacketWrapper::readBlockPosition);
            }
            return wrapper.readJavaOptional(ew ->
                    new Vector3i(ew.readInt(), ew.readInt(), ew.readInt()));
        };
    }

    private static PacketWrapper.Writer<Optional<Vector3i>> writeOptionalBlockPositionSerializer() {
        return (wrapper, value) -> {
            if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
                wrapper.writeJavaOptional(value, PacketWrapper::writeBlockPosition);
            } else {
                wrapper.writeJavaOptional(value, (ew, valuee) -> {
                    wrapper.writeInt(valuee.getX());
                    wrapper.writeInt(valuee.getY());
                    wrapper.writeInt(valuee.getZ());
                });
            }
        };
    }

    static {
        REGISTRY.unloadMappings();
    }
}
