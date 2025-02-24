/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2025 retrooper and contributors
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

package io.github.retrooper.packetevents.util;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.pose.EntityPose;
import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.player.GameMode;
import com.github.retrooper.packetevents.protocol.player.HumanoidArm;
import com.github.retrooper.packetevents.protocol.potion.PotionType;
import com.github.retrooper.packetevents.protocol.potion.PotionTypes;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionType;
import com.github.retrooper.packetevents.protocol.world.dimension.DimensionTypes;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.google.common.base.Suppliers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

import static net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE;
import static net.minecraft.core.registries.BuiltInRegistries.ITEM;
import static net.minecraft.core.registries.BuiltInRegistries.MOB_EFFECT;
import static net.minecraft.core.registries.BuiltInRegistries.PARTICLE_TYPE;
import static net.minecraft.core.registries.Registries.DIMENSION_TYPE;

public final class MinecraftConversionUtil {

    // lazy-load, the packetevents api instance may not be loaded yet when this class gets initialized
    private static final Supplier<ClientVersion> VERSION =
            Suppliers.memoize(() -> PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());

    private MinecraftConversionUtil() {
    }

    private static ClientVersion version() {
        return VERSION.get();
    }

    public static ResourceLocation fromMinecraftResource(net.minecraft.resources.ResourceLocation resourceLocation) {
        return new ResourceLocation(resourceLocation.getNamespace(), resourceLocation.getPath());
    }

    public static net.minecraft.resources.ResourceLocation toMinecraftResource(ResourceLocation resourceLocation) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(
                resourceLocation.getNamespace(), resourceLocation.getKey());
    }

    public static PotionType fromMobEffect(MobEffect effect) {
        int effectId = MOB_EFFECT.getIdOrThrow(effect);
        return PotionTypes.getById(effectId, version());
    }

    public static PotionType fromMobEffect(Holder<MobEffect> effect) {
        return fromMobEffect(effect.value());
    }

    public static Holder<MobEffect> toMobEffect(PotionType potionType) {
        return MOB_EFFECT.get(potionType.getId(version())).orElseThrow(() ->
                new IllegalArgumentException("Can't lookup potion type: " + potionType));
    }

    public static GameMode fromGameType(GameType gameType) {
        return GameMode.getById(gameType.getId());
    }

    public static GameType toGameType(GameMode gameMode) {
        return GameType.byId(gameMode.getId());
    }

    public static WrappedBlockState fromMinecraftBlockState(BlockState state) {
        int stateId = Block.BLOCK_STATE_REGISTRY.getIdOrThrow(state);
        return WrappedBlockState.getByGlobalId(stateId);
    }

    public static BlockState toMinecraftBlockState(WrappedBlockState state) {
        return Block.BLOCK_STATE_REGISTRY.byIdOrThrow(state.getGlobalId());
    }

    public static EntityType fromMinecraftEntityType(net.minecraft.world.entity.EntityType<?> type) {
        return EntityTypes.getById(version(), ENTITY_TYPE.getIdOrThrow(type));
    }

    public static EntityType fromMinecraftEntityType(Holder<net.minecraft.world.entity.EntityType<?>> type) {
        return fromMinecraftEntityType(type.value());
    }

    public static Holder<net.minecraft.world.entity.EntityType<?>> toMinecraftEntityType(EntityType type) {
        return ENTITY_TYPE.get(type.getId(version())).orElseThrow(() ->
                new IllegalArgumentException("Can't lookup entity type: " + type));
    }

    public static ItemType fromMinecraftItem(Item item) {
        return ItemTypes.getById(version(), ITEM.getIdOrThrow(item));
    }

    public static ItemType fromMinecraftItem(Holder<Item> item) {
        return fromMinecraftItem(item.value());
    }

    public static Holder<Item> toMinecraftItem(ItemType item) {
        return ITEM.get(item.getId(version())).orElseThrow(() ->
                new IllegalArgumentException("Can't lookup item type: " + item));
    }

    public static ItemStack fromMinecraftStack(net.minecraft.world.item.ItemStack stack, RegistryAccess registries) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            net.minecraft.world.item.ItemStack.OPTIONAL_STREAM_CODEC.encode(
                    new RegistryFriendlyByteBuf(buf, registries), stack);
            return PacketWrapper.createUniversalPacketWrapper(buf).readItemStack();
        } finally {
            buf.release();
        }
    }

    public static net.minecraft.world.item.ItemStack toMinecraftStack(ItemStack stack, RegistryAccess registries) {
        ByteBuf buf = PooledByteBufAllocator.DEFAULT.buffer();
        try {
            PacketWrapper.createUniversalPacketWrapper(buf).writeItemStack(stack);
            return net.minecraft.world.item.ItemStack.OPTIONAL_STREAM_CODEC.decode(
                    new RegistryFriendlyByteBuf(buf, registries));
        } finally {
            buf.release();
        }
    }

    public static DimensionType fromMinecraftDimensionType(Level level) {
        return fromMinecraftDimensionType(level.dimensionType(), level.registryAccess());
    }

    public static DimensionType fromMinecraftDimensionType(
            Holder<net.minecraft.world.level.dimension.DimensionType> type,
            RegistryAccess registries
    ) {
        return fromMinecraftDimensionType(type.value(), registries);
    }

    public static DimensionType fromMinecraftDimensionType(
            net.minecraft.world.level.dimension.DimensionType type,
            RegistryAccess registries
    ) {
        Registry<net.minecraft.world.level.dimension.DimensionType> registry =
                registries.lookupOrThrow(DIMENSION_TYPE);
        int dimensionTypeId = registry.getIdOrThrow(type);
        return DimensionTypes.getRegistry().getById(version(), dimensionTypeId);
    }

    public static ParticleType<?> fromMinecraftParticle(net.minecraft.core.particles.ParticleType<?> particle) {
        return ParticleTypes.getById(version(), PARTICLE_TYPE.getIdOrThrow(particle));
    }

    public static ParticleType<?> fromMinecraftParticle(Holder<net.minecraft.core.particles.ParticleType<?>> particle) {
        return fromMinecraftParticle(particle.value());
    }

    public static Holder<net.minecraft.core.particles.ParticleType<?>> toMinecraftParticle(ParticleType<?> particle) {
        return PARTICLE_TYPE.get(particle.getId(version())).orElseThrow(() ->
                new IllegalArgumentException("Can't lookup particle type: " + particle));
    }

    public static EntityPose fromMinecraftPose(Pose pose) {
        return EntityPose.getById(version(), pose.id());
    }

    public static Pose toMinecraftPose(EntityPose pose) {
        return Pose.BY_ID.apply(pose.getId(version()));
    }

    public static HumanoidArm fromMinecraftArm(net.minecraft.world.entity.HumanoidArm arm) {
        return HumanoidArm.getById(arm.getId());
    }

    public static net.minecraft.world.entity.HumanoidArm toMinecraftArm(HumanoidArm arm) {
        return net.minecraft.world.entity.HumanoidArm.BY_ID.apply(arm.getId());
    }
}
