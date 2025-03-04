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

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.manager.server.ServerVersion;
import com.github.retrooper.packetevents.protocol.entity.armadillo.ArmadilloState;
import com.github.retrooper.packetevents.protocol.entity.cat.CatVariant;
import com.github.retrooper.packetevents.protocol.entity.cat.CatVariants;
import com.github.retrooper.packetevents.protocol.entity.frog.FrogVariant;
import com.github.retrooper.packetevents.protocol.entity.frog.FrogVariants;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.sniffer.SnifferState;
import com.github.retrooper.packetevents.protocol.entity.villager.VillagerData;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariant;
import com.github.retrooper.packetevents.protocol.entity.wolfvariant.WolfVariants;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.BlockFace;
import com.github.retrooper.packetevents.protocol.world.WorldBlockPosition;
import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariant;
import com.github.retrooper.packetevents.protocol.world.painting.PaintingVariants;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.util.Quaternion4f;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public final class EntityDataTypes {

    // there is no proper mojang registry for this yet, the registry name is just a guess
    private static final VersionedRegistry<IEntityDataType<?>> REGISTRY = new VersionedRegistry<>(
            "entity_data_serializer", "entity/entity_data_type_mappings");

    public static final IEntityDataType<Byte> TYPE_BYTE = define("byte",
            PacketWrapper::readByte, PacketWrapper::writeByte);
    public static final IEntityDataType<Integer> INT_TYPE = define("int",
            EntityDataTypes::readVersionedVarInt, EntityDataTypes::writeVersionedVarInt);
    /**
     * Added with 1.19.3
     */
    public static final IEntityDataType<Long> TYPE_LONG = define("long",
            PacketWrapper::readVarLong, PacketWrapper::writeVarLong);
    public static final IEntityDataType<Float> TYPE_FLOAT = define("float",
            PacketWrapper::readFloat, PacketWrapper::writeFloat);
    public static final IEntityDataType<String> TYPE_STRING = define("string",
            PacketWrapper::readString, PacketWrapper::writeString);
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Component> TYPE_COMPONENT = define("component",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    /**
     * Added with 1.13
     */
    public static final IEntityDataType<Optional<Component>> TYPE_OPTIONAL_COMPONENT = define("optional_component",
            wrapper -> wrapper.readRealOptional(PacketWrapper::readComponent),
            (wrapper, component) -> wrapper.writeRealOptional(component, PacketWrapper::writeComponent));
    public static final IEntityDataType<ItemStack> TYPE_ITEM_STACK = define("item_stack",
            PacketWrapper::readItemStack, PacketWrapper::writeItemStack);
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Boolean> TYPE_BOOLEAN = define("boolean",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final IEntityDataType<Vector3f> TYPE_ROTATIONS = define("rotations",
            Vector3f::read, Vector3f::write);
    public static final IEntityDataType<Vector3i> TYPE_BLOCK_POS = define("block_pos",
            EntityDataTypes::readVersionedBlockPosition, EntityDataTypes::writeVersionedBlockPosition);
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Optional<Vector3i>> TYPE_OPTIONAL_BLOCK_POS = define("optional_block_pos",
            wrapper -> wrapper.readRealOptional(EntityDataTypes::readVersionedBlockPosition),
            (wrapper, pos) -> wrapper.writeRealOptional(pos, EntityDataTypes::writeVersionedBlockPosition));
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<BlockFace> TYPE_DIRECTION = define("direction",
            wrapper -> BlockFace.getBlockFaceByValue(wrapper.readVarInt()), PacketWrapper::writeEnum);
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Optional<UUID>> TYPE_OPTIONAL_UUID = define("optional_uuid",
            wrapper -> wrapper.readRealOptional(PacketWrapper::readUUID),
            (wrapper, uuid) -> wrapper.writeRealOptional(uuid, PacketWrapper::writeUUID));
    /**
     * Added with 1.19.4
     */
    public static final IEntityDataType<WrappedBlockState> TYPE_BLOCK_STATE = define("block_state",
            wrapper -> WrappedBlockState.getByGlobalId(wrapper.getServerVersion().toClientVersion(), wrapper.readVarInt()),
            (wrapper, state) -> wrapper.writeVarInt(state.getGlobalId()));
    /**
     * Added with 1.9
     */
    public static final IEntityDataType<Optional<WrappedBlockState>> TYPE_OPTIONAL_BLOCK_STATE = define("optional_block_state", wrapper -> {
        int id = wrapper.readVarInt();
        if (id != 0) {
            ClientVersion version = wrapper.getServerVersion().toClientVersion();
            return Optional.of(WrappedBlockState.getByGlobalId(version, id));
        }
        return Optional.empty();
    }, (wrapper, state) -> {
        if (state.isPresent()) {
            wrapper.writeVarInt(state.get().getGlobalId());
        } else {
            wrapper.writeVarInt(0);
        }
    });
    /**
     * Added with 1.11
     */
    public static final IEntityDataType<NBTCompound> TYPE_COMPOUND_TAG = define("compound_tag",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    /**
     * Added with 1.13
     */
    public static final IEntityDataType<Particle<?>> TYPE_PARTICLE = define("particle",
            Particle::read, Particle::write);
    /**
     * Added with 1.20.5
     */
    public static final IEntityDataType<List<Particle<?>>> TYPE_PARTICLES = define("particles",
            wrapper -> wrapper.readList(Particle::read),
            (wrapper, particles) -> wrapper.writeList(particles, Particle::write));
    /**
     * Added with 1.14
     */
    public static final IEntityDataType<VillagerData> TYPE_VILLAGER_DATA = define("villager_data",
            PacketWrapper::readVillagerData, PacketWrapper::writeVillagerData);
    /**
     * Added with 1.14
     */
    public static final IEntityDataType<Optional<Integer>> TYPE_OPTIONAL_UNSIGNED_INT = define("optional_unsigned_int", wrapper -> {
        int num = wrapper.readVarInt();
        if (num != 0) {
            return Optional.of(num - 1);
        }
        return Optional.empty();
    }, (wrapper, value) ->
            wrapper.writeVarInt(value.orElse(-1) + 1));
    /**
     * Added with 1.14
     */
    public static final IEntityDataType<EntityPose> TYPE_POSE = define("pose",
            wrapper -> EntityPose.getById(wrapper.getServerVersion().toClientVersion(), wrapper.readVarInt()),
            (wrapper, pose) -> wrapper.writeVarInt(pose.getId(wrapper.getServerVersion().toClientVersion())));
    /**
     * Added with 1.19
     */
    public static final IEntityDataType<CatVariant> TYPE_CAT_VARIANT = define("cat_variant", CatVariant::read, CatVariant::write);
    /**
     * Added with 1.20.5
     */
    public static final IEntityDataType<WolfVariant> TYPE_WOLF_VARIANT = define("wolf_variant", WolfVariant::read, WolfVariant::write);
    /**
     * Added with 1.19
     */
    public static final IEntityDataType<FrogVariant> TYPE_FROG_VARIANT = define("frog_variant", FrogVariant::read, FrogVariant::write);
    /**
     * Added with 1.19
     */
    public static final IEntityDataType<Optional<WorldBlockPosition>> TYPE_OPTIONAL_GLOBAL_POS = define("optional_global_pos",
            wrapper -> wrapper.readRealOptional(WorldBlockPosition::read),
            (wrapper, pos) -> wrapper.writeRealOptional(pos, PacketWrapper::writeWorldBlockPosition));
    /**
     * Added with 1.19
     */
    public static final IEntityDataType<PaintingVariant> TYPE_PAINTING_VARIANT = define("painting_variant", PaintingVariant::read, PaintingVariant::write);
    /**
     * Added with 1.19.4
     */
    public static final IEntityDataType<SnifferState> TYPE_SNIFFER_STATE = define("sniffer_state",
            wrapper -> wrapper.readEnum(SnifferState.values()), PacketWrapper::writeEnum);
    /**
     * Added with 1.20.5
     */
    public static final IEntityDataType<ArmadilloState> TYPE_ARMADILLO_STATE = define("armadillo_state",
            wrapper -> wrapper.readEnum(ArmadilloState.values()), PacketWrapper::writeEnum);
    /**
     * Added with 1.19.4
     */
    public static final IEntityDataType<Vector3f> TYPE_VECTOR3 = define("vector3", Vector3f::read, Vector3f::write);
    /**
     * Added with 1.19.4
     */
    public static final IEntityDataType<Quaternion4f> TYPE_QUATERNION = define("quaternion", Quaternion4f::read, Quaternion4f::write);

    /**
     * Removed in 1.9
     */
    @ApiStatus.Obsolete
    public static final IEntityDataType<Short> TYPE_SHORT = define("short", PacketWrapper::readShort, PacketWrapper::writeShort);

    // <editor-fold desc="legacy entity data type definition" defaultstate="collapsed">
    private static final GsonComponentSerializer GSON_SERIALIZER = GsonComponentSerializer.gson();
    @Deprecated
    public static final EntityDataType<Byte> BYTE = TYPE_BYTE.asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> INT = INT_TYPE.asLegacy();
    @Deprecated
    public static final EntityDataType<Short> SHORT = TYPE_SHORT.asLegacy();
    @Deprecated
    public static final EntityDataType<Long> LONG = TYPE_LONG.asLegacy();
    @Deprecated
    public static final EntityDataType<Float> FLOAT = TYPE_FLOAT.asLegacy();
    @Deprecated
    public static final EntityDataType<String> STRING = TYPE_STRING.asLegacy();
    @Deprecated
    public static final EntityDataType<String> COMPONENT = TYPE_COMPONENT.map(
            GSON_SERIALIZER::serialize, GSON_SERIALIZER::deserialize).asLegacy();
    @Deprecated
    public static final EntityDataType<Component> ADV_COMPONENT = TYPE_COMPONENT.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<String>> OPTIONAL_COMPONENT = TYPE_OPTIONAL_COMPONENT.map(
            comp -> comp.map(GSON_SERIALIZER::serialize),
            string -> string.map(GSON_SERIALIZER::deserialize)).asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<Component>> OPTIONAL_ADV_COMPONENT = TYPE_OPTIONAL_COMPONENT.asLegacy();
    @Deprecated
    public static final EntityDataType<ItemStack> ITEMSTACK = TYPE_ITEM_STACK.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<ItemStack>> OPTIONAL_ITEMSTACK = TYPE_ITEM_STACK.map(
            Optional::of, opt -> opt.orElse(ItemStack.EMPTY)).asLegacy();
    @Deprecated
    public static final EntityDataType<Boolean> BOOLEAN = TYPE_BOOLEAN.asLegacy();
    @Deprecated
    public static final EntityDataType<Vector3f> ROTATION = TYPE_ROTATIONS.asLegacy();
    @Deprecated
    public static final EntityDataType<Vector3i> BLOCK_POSITION = TYPE_BLOCK_POS.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<Vector3i>> OPTIONAL_BLOCK_POSITION = TYPE_OPTIONAL_BLOCK_POS.asLegacy();
    @Deprecated
    public static final EntityDataType<BlockFace> BLOCK_FACE = TYPE_DIRECTION.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<UUID>> OPTIONAL_UUID = TYPE_OPTIONAL_UUID.asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> BLOCK_STATE = TYPE_BLOCK_STATE.map(
            WrappedBlockState::getGlobalId, WrappedBlockState::getByGlobalId).asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> OPTIONAL_BLOCK_STATE = TYPE_OPTIONAL_BLOCK_STATE.map(
            state -> state.map(WrappedBlockState::getGlobalId).orElse(0),
            id -> id == 0 ? Optional.empty() : Optional.of(WrappedBlockState.getByGlobalId(id))).asLegacy();
    @Deprecated
    public static final EntityDataType<NBTCompound> NBT = TYPE_COMPOUND_TAG.asLegacy();
    @Deprecated
    public static final EntityDataType<Particle<?>> PARTICLE = TYPE_PARTICLE.asLegacy();
    @Deprecated
    public static final EntityDataType<List<Particle<?>>> PARTICLES = TYPE_PARTICLES.asLegacy();
    @Deprecated
    public static final EntityDataType<VillagerData> VILLAGER_DATA = TYPE_VILLAGER_DATA.asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<Integer>> OPTIONAL_INT = TYPE_OPTIONAL_UNSIGNED_INT.asLegacy();
    @Deprecated
    public static final EntityDataType<EntityPose> ENTITY_POSE = TYPE_POSE.asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> CAT_VARIANT = TYPE_CAT_VARIANT.map(
            variant -> variant.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()),
            id -> CatVariants.getRegistry().getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id)).asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> WOLF_VARIANT = TYPE_WOLF_VARIANT.map(
            variant -> variant.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()),
            id -> WolfVariants.getRegistry().getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id)).asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> FROG_VARIANT = TYPE_FROG_VARIANT.map(
            variant -> variant.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()),
            id -> FrogVariants.getRegistry().getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id)).asLegacy();
    @Deprecated
    public static final EntityDataType<Optional<WorldBlockPosition>> OPTIONAL_GLOBAL_POSITION = TYPE_OPTIONAL_GLOBAL_POS.asLegacy();
    @Deprecated
    public static final EntityDataType<Integer> PAINTING_VARIANT_TYPE = TYPE_PAINTING_VARIANT.map(
            variant -> variant.getId(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion()),
            id -> PaintingVariants.getRegistry().getById(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion(), id)).asLegacy();
    @Deprecated
    public static final EntityDataType<SnifferState> SNIFFER_STATE = TYPE_SNIFFER_STATE.asLegacy();
    @Deprecated
    public static final EntityDataType<ArmadilloState> ARMADILLO_STATE = TYPE_ARMADILLO_STATE.asLegacy();
    @Deprecated
    public static final EntityDataType<Vector3f> VECTOR3F = TYPE_VECTOR3.asLegacy();
    @Deprecated
    public static final EntityDataType<Quaternion4f> QUATERNION = TYPE_QUATERNION.asLegacy();

    @Deprecated
    private static final List<EntityDataType<?>> LEGACY_ENTRIES = Collections.unmodifiableList(REGISTRY.getEntries().stream()
            .map(IEntityDataType::asLegacy).collect(Collectors.toList()));
    // </editor-fold>

    public static VersionedRegistry<IEntityDataType<?>> getRegistry() {
        return REGISTRY;
    }

    /**
     * Returns an immutable view of the entity-data types.
     *
     * @return Entity-Data Types
     */
    @Deprecated
    public static Collection<EntityDataType<?>> values() {
        return LEGACY_ENTRIES;
    }

    @Deprecated
    public static @Nullable EntityDataType<?> getById(ClientVersion version, int id) {
        IEntityDataType<?> type = REGISTRY.getById(version, id);
        return type == null ? null : type.asLegacy();
    }

    @Deprecated
    public static @Nullable EntityDataType<?> getByName(String name) {
        IEntityDataType<?> type = REGISTRY.getByName(name);
        return type == null ? null : type.asLegacy();
    }

    private static <T> IEntityDataType<T> define(
            String name, PacketWrapper.Reader<T> reader, PacketWrapper.Writer<T> writer) {
        return REGISTRY.define(name, data ->
                new EntityDataTypeImpl<>(data, reader, writer));
    }

    private static int readVersionedVarInt(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readVarInt();
        }
        return wrapper.readInt();
    }

    private static void writeVersionedVarInt(PacketWrapper<?> wrapper, Number number) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeVarInt(number.intValue());
        } else {
            wrapper.writeInt(number.intValue());
        }
    }

    private static Vector3i readVersionedBlockPosition(PacketWrapper<?> wrapper) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            return wrapper.readBlockPosition();
        } else {
            return new Vector3i(wrapper.readInt(), wrapper.readInt(), wrapper.readInt());
        }
    }

    private static void writeVersionedBlockPosition(PacketWrapper<?> wrapper, Vector3i vector) {
        if (wrapper.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_9)) {
            wrapper.writeBlockPosition(vector);
        } else {
            wrapper.writeInt(vector.x);
            wrapper.writeInt(vector.y);
            wrapper.writeInt(vector.z);
        }
    }

    static {
        REGISTRY.unloadMappings();
    }
}
