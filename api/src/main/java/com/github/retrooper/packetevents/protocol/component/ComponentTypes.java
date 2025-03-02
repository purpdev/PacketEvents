/*
 * This file is part of packetevents - https://github.com/retrooper/packetevents
 * Copyright (C) 2024 retrooper and contributors
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

package com.github.retrooper.packetevents.protocol.component;

import com.github.retrooper.packetevents.protocol.color.DyeColor;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ArmorTrim;
import com.github.retrooper.packetevents.protocol.component.builtin.item.BannerLayers;
import com.github.retrooper.packetevents.protocol.component.builtin.item.BundleContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ChargedProjectiles;
import com.github.retrooper.packetevents.protocol.component.builtin.item.CustomData;
import com.github.retrooper.packetevents.protocol.component.builtin.item.DebugStickState;
import com.github.retrooper.packetevents.protocol.component.builtin.item.FireworkExplosion;
import com.github.retrooper.packetevents.protocol.component.builtin.item.FoodProperties;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAdventurePredicate;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemAttributeModifiers;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBees;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemBlockStateProperties;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemConsumable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemContainerLoot;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemCustomModelData;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDamageResistant;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDeathProtection;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemDyeColor;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEquippable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemFireworks;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemJukeboxPlayable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLock;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemLore;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapDecorations;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemMapPostProcessingState;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemModel;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemPotionContents;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemProfile;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRarity;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRecipes;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemRepairable;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTool;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemTooltipStyle;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseCooldown;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemUseRemainder;
import com.github.retrooper.packetevents.protocol.component.builtin.item.LodestoneTracker;
import com.github.retrooper.packetevents.protocol.component.builtin.item.PotDecorations;
import com.github.retrooper.packetevents.protocol.component.builtin.item.SuspiciousStewEffects;
import com.github.retrooper.packetevents.protocol.component.builtin.item.WritableBookContent;
import com.github.retrooper.packetevents.protocol.component.builtin.item.WrittenBookContent;
import com.github.retrooper.packetevents.protocol.item.instrument.Instrument;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Dummy;
import com.github.retrooper.packetevents.util.mappings.VersionedRegistry;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Reader;
import com.github.retrooper.packetevents.wrapper.PacketWrapper.Writer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Contains all item data component types.
 *
 * @see EnchantEffectComponentTypes
 */
public final class ComponentTypes {

    private static final VersionedRegistry<ComponentType<?>> REGISTRY = new VersionedRegistry<>("data_component_type");

    private ComponentTypes() {
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key) {
        return define(key, null, null);
    }

    @ApiStatus.Internal
    public static <T> ComponentType<T> define(String key, @Nullable Reader<T> reader, @Nullable Writer<T> writer) {
        return REGISTRY.define(key, data -> new StaticComponentType<>(data, reader, writer));
    }

    public static VersionedRegistry<ComponentType<?>> getRegistry() {
        return REGISTRY;
    }

    public static ComponentType<?> getByName(String name) {
        return REGISTRY.getByName(name);
    }

    public static ComponentType<?> getById(ClientVersion version, int id) {
        return REGISTRY.getById(version, id);
    }

    public static final ComponentType<NBTCompound> CUSTOM_DATA = define("custom_data",
            // mojang wraps their "persistent" codec as a stream codec just here,
            // so packetevents has to handle nbt strings
            CustomData::read, CustomData::write);
    public static final ComponentType<Integer> MAX_STACK_SIZE = define("max_stack_size",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> MAX_DAMAGE = define("max_damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Integer> DAMAGE = define("damage",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Boolean> UNBREAKABLE = define("unbreakable",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final ComponentType<Component> CUSTOM_NAME = define("custom_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<Component> ITEM_NAME = define("item_name",
            PacketWrapper::readComponent, PacketWrapper::writeComponent);
    public static final ComponentType<ItemLore> LORE = define("lore",
            ItemLore::read, ItemLore::write);
    public static final ComponentType<ItemRarity> RARITY = define("rarity",
            wrapper -> wrapper.readEnum(ItemRarity.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ItemEnchantments> ENCHANTMENTS = define("enchantments",
            ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_PLACE_ON = define("can_place_on",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAdventurePredicate> CAN_BREAK = define("can_break",
            ItemAdventurePredicate::read, ItemAdventurePredicate::write);
    public static final ComponentType<ItemAttributeModifiers> ATTRIBUTE_MODIFIERS = define("attribute_modifiers",
            ItemAttributeModifiers::read, ItemAttributeModifiers::write);
    public static final ComponentType<ItemCustomModelData> CUSTOM_MODEL_DATA_LISTS = define("custom_model_data",
            ItemCustomModelData::read, ItemCustomModelData::write);
    /**
     * Rewritten in 1.21.4
     */
    @Deprecated
    public static final ComponentType<Integer> CUSTOM_MODEL_DATA = CUSTOM_MODEL_DATA_LISTS.legacyMap(
            ItemCustomModelData::getLegacyId, ItemCustomModelData::new);
    public static final ComponentType<Dummy> HIDE_ADDITIONAL_TOOLTIP = define("hide_additional_tooltip",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Dummy> HIDE_TOOLTIP = define("hide_tooltip",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Integer> REPAIR_COST = define("repair_cost",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<Dummy> CREATIVE_SLOT_LOCK = define("creative_slot_lock",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<Boolean> ENCHANTMENT_GLINT_OVERRIDE = define("enchantment_glint_override",
            PacketWrapper::readBoolean, PacketWrapper::writeBoolean);
    public static final ComponentType<Dummy> INTANGIBLE_PROJECTILE = define("intangible_projectile",
            Dummy::dummyReadNbt, Dummy::dummyWriteNbt);
    public static final ComponentType<FoodProperties> FOOD = define("food",
            FoodProperties::read, FoodProperties::write);
    /**
     * Has been migrated to #DAMAGE_RESISTANT with 1.21.2.
     */
    @ApiStatus.Obsolete
    public static final ComponentType<Dummy> FIRE_RESISTANT = define("fire_resistant",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<ItemTool> TOOL = define("tool",
            ItemTool::read, ItemTool::write);
    public static final ComponentType<ItemEnchantments> STORED_ENCHANTMENTS = define("stored_enchantments",
            ItemEnchantments::read, ItemEnchantments::write);
    public static final ComponentType<ItemDyeColor> DYED_COLOR = define("dyed_color",
            ItemDyeColor::read, ItemDyeColor::write);
    public static final ComponentType<Integer> MAP_COLOR = define("map_color",
            PacketWrapper::readInt, PacketWrapper::writeInt);
    public static final ComponentType<Integer> MAP_ID = define("map_id",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<ItemMapDecorations> MAP_DECORATIONS = define("map_decorations",
            ItemMapDecorations::read, ItemMapDecorations::write);
    public static final ComponentType<ItemMapPostProcessingState> MAP_POST_PROCESSING = define("map_post_processing",
            wrapper -> wrapper.readEnum(ItemMapPostProcessingState.values()), PacketWrapper::writeEnum);
    public static final ComponentType<ChargedProjectiles> CHARGED_PROJECTILES = define("charged_projectiles",
            ChargedProjectiles::read, ChargedProjectiles::write);
    public static final ComponentType<BundleContents> BUNDLE_CONTENTS = define("bundle_contents",
            BundleContents::read, BundleContents::write);
    public static final ComponentType<ItemPotionContents> POTION_CONTENTS = define("potion_contents",
            ItemPotionContents::read, ItemPotionContents::write);
    public static final ComponentType<SuspiciousStewEffects> SUSPICIOUS_STEW_EFFECTS = define("suspicious_stew_effects",
            SuspiciousStewEffects::read, SuspiciousStewEffects::write);
    public static final ComponentType<WritableBookContent> WRITABLE_BOOK_CONTENT = define("writable_book_content",
            WritableBookContent::read, WritableBookContent::write);
    public static final ComponentType<WrittenBookContent> WRITTEN_BOOK_CONTENT = define("written_book_content",
            WrittenBookContent::read, WrittenBookContent::write);
    public static final ComponentType<ArmorTrim> TRIM = define("trim",
            ArmorTrim::read, ArmorTrim::write);
    public static final ComponentType<DebugStickState> DEBUG_STICK_STATE = define("debug_stick_state",
            DebugStickState::read, DebugStickState::write);
    public static final ComponentType<NBTCompound> ENTITY_DATA = define("entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<NBTCompound> BUCKET_ENTITY_DATA = define("bucket_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<NBTCompound> BLOCK_ENTITY_DATA = define("block_entity_data",
            PacketWrapper::readNBT, PacketWrapper::writeNBT);
    public static final ComponentType<Instrument> INSTRUMENT = define("instrument",
            Instrument::read, Instrument::write);
    public static final ComponentType<Integer> OMINOUS_BOTTLE_AMPLIFIER = define("ominous_bottle_amplifier",
            PacketWrapper::readVarInt, PacketWrapper::writeVarInt);
    public static final ComponentType<ItemRecipes> RECIPES = define("recipes",
            ItemRecipes::read, ItemRecipes::write);
    public static final ComponentType<LodestoneTracker> LODESTONE_TRACKER = define("lodestone_tracker",
            LodestoneTracker::read, LodestoneTracker::write);
    public static final ComponentType<FireworkExplosion> FIREWORK_EXPLOSION = define("firework_explosion",
            FireworkExplosion::read, FireworkExplosion::write);
    public static final ComponentType<ItemFireworks> FIREWORKS = define("fireworks",
            ItemFireworks::read, ItemFireworks::write);
    public static final ComponentType<ItemProfile> PROFILE = define("profile",
            ItemProfile::read, ItemProfile::write);
    public static final ComponentType<ResourceLocation> NOTE_BLOCK_SOUND = define("note_block_sound",
            PacketWrapper::readIdentifier, PacketWrapper::writeIdentifier);
    public static final ComponentType<BannerLayers> BANNER_PATTERNS = define("banner_patterns",
            BannerLayers::read, BannerLayers::write);
    public static final ComponentType<DyeColor> BASE_COLOR = define("base_color",
            wrapper -> wrapper.readEnum(DyeColor.values()), PacketWrapper::writeEnum);
    public static final ComponentType<PotDecorations> POT_DECORATIONS = define("pot_decorations",
            PotDecorations::read, PotDecorations::write);
    public static final ComponentType<ItemContainerContents> CONTAINER = define("container",
            ItemContainerContents::read, ItemContainerContents::write);
    public static final ComponentType<ItemBlockStateProperties> BLOCK_STATE = define("block_state",
            ItemBlockStateProperties::read, ItemBlockStateProperties::write);
    public static final ComponentType<ItemBees> BEES = define("bees",
            ItemBees::read, ItemBees::write);
    public static final ComponentType<ItemLock> LOCK = define("lock",
            ItemLock::read, ItemLock::write);
    public static final ComponentType<ItemContainerLoot> CONTAINER_LOOT = define("container_loot",
            ItemContainerLoot::read, ItemContainerLoot::write);

    // added in 1.21
    public static final ComponentType<ItemJukeboxPlayable> JUKEBOX_PLAYABLE = define("jukebox_playable",
            ItemJukeboxPlayable::read, ItemJukeboxPlayable::write);

    // added in 1.21.2
    public static final ComponentType<ItemConsumable> CONSUMABLE = define("consumable",
            ItemConsumable::read, ItemConsumable::write);
    public static final ComponentType<ItemUseRemainder> USE_REMAINDER = define("use_remainder",
            ItemUseRemainder::read, ItemUseRemainder::write);
    public static final ComponentType<ItemUseCooldown> USE_COOLDOWN = define("use_cooldown",
            ItemUseCooldown::read, ItemUseCooldown::write);
    public static final ComponentType<ItemEnchantable> ENCHANTABLE = define("enchantable",
            ItemEnchantable::read, ItemEnchantable::write);
    public static final ComponentType<ItemRepairable> REPAIRABLE = define("repairable",
            ItemRepairable::read, ItemRepairable::write);
    public static final ComponentType<ItemModel> ITEM_MODEL = define("item_model",
            ItemModel::read, ItemModel::write);
    public static final ComponentType<ItemDamageResistant> DAMAGE_RESISTANT = define("damage_resistant",
            ItemDamageResistant::read, ItemDamageResistant::write);
    public static final ComponentType<ItemEquippable> EQUIPPABLE = define("equippable",
            ItemEquippable::read, ItemEquippable::write);
    public static final ComponentType<Dummy> GLIDER = define("glider",
            Dummy::dummyRead, Dummy::dummyWrite);
    public static final ComponentType<ItemDeathProtection> DEATH_PROTECTION = define("death_protection",
            ItemDeathProtection::read, ItemDeathProtection::write);
    public static final ComponentType<ItemTooltipStyle> TOOLTIP_STYLE = define("tooltip_style",
            ItemTooltipStyle::read, ItemTooltipStyle::write);

    /**
     * Returns an immutable view of the component types.
     *
     * @return Component Types
     */
    public static Collection<ComponentType<?>> values() {
        return REGISTRY.getEntries();
    }

    static {
        REGISTRY.unloadMappings();
    }
}
