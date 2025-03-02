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

package com.github.retrooper.packetevents.protocol.item;

import com.github.retrooper.packetevents.protocol.component.ComponentType;
import com.github.retrooper.packetevents.protocol.component.ComponentTypes;
import com.github.retrooper.packetevents.protocol.component.PatchableComponentMap;
import com.github.retrooper.packetevents.protocol.component.StaticComponentMap;
import com.github.retrooper.packetevents.protocol.component.builtin.item.ItemEnchantments;
import com.github.retrooper.packetevents.protocol.item.enchantment.Enchantment;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.item.type.ItemType;
import com.github.retrooper.packetevents.protocol.item.type.ItemTypes;
import com.github.retrooper.packetevents.protocol.nbt.NBT;
import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTNumber;
import com.github.retrooper.packetevents.protocol.nbt.NBTShort;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.DAMAGE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.ENCHANTMENTS;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.MAX_DAMAGE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.MAX_STACK_SIZE;
import static com.github.retrooper.packetevents.protocol.component.ComponentTypes.STORED_ENCHANTMENTS;

public class ItemStack {
    public static final ItemStack EMPTY = new ItemStack(ItemTypes.AIR, 0, new NBTCompound(), 0);
    private final ItemType type;
    private int amount;
    @Nullable
    private NBTCompound nbt;
    @Nullable // lazy loaded
    private PatchableComponentMap components; // Added in 1.20.5
    private int legacyData = -1;

    private boolean cachedIsEmpty = false;

    private ItemStack(ItemType type, int amount, @Nullable NBTCompound nbt, int legacyData) {
        this(type, amount, nbt, null, legacyData);
    }

    private ItemStack(
            ItemType type,
            int amount,
            @Nullable NBTCompound nbt,
            @Nullable PatchableComponentMap components,
            int legacyData
    ) {
        this.type = type;
        this.amount = amount;
        this.nbt = nbt;
        this.components = components;
        this.legacyData = legacyData;
        updateCachedEmptyStatus();
    }

    public static ItemStack decode(NBT nbt, ClientVersion version) {
        if (nbt instanceof NBTString) {
            ResourceLocation itemName = new ResourceLocation(((NBTString) nbt).getValue());
            return ItemStack.builder().type(ItemTypes.getByName(itemName.toString())).build();
        }
        NBTCompound compound = (NBTCompound) nbt;
        ItemStack.Builder builder = ItemStack.builder();

        ResourceLocation itemName = Optional.ofNullable(compound.getStringTagValueOrNull("id")).map(Optional::of)
                .orElseGet(() -> Optional.ofNullable(compound.getStringTagValueOrNull("item")))
                .map(ResourceLocation::new).orElseThrow(() -> new IllegalArgumentException(
                        "No item type specified: " + compound.getTags().keySet()));
        builder.type(ItemTypes.getByName(itemName.toString()));
        builder.nbt(compound.getCompoundTagOrNull("tag"));

        Optional.ofNullable(compound.getNumberTagOrNull("Count")).map(Optional::of)
                .orElseGet(() -> Optional.ofNullable(compound.getNumberTagOrNull("count")))
                .map(NBTNumber::getAsInt).ifPresent(builder::amount);

        // TODO components

        return builder.build();
    }

    public static NBT encodeForParticle(ItemStack itemStack, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_20_5)) {
            boolean simple = itemStack.isEmpty()
                    || itemStack.components == null
                    || itemStack.components.getPatches().isEmpty();
            if (simple) {
                return new NBTString(itemStack.type.getName().toString());
            }
        }

        NBTCompound compound = new NBTCompound();
        compound.setTag("id", new NBTString(itemStack.type.getName().toString()));
        if (version.isOlderThan(ClientVersion.V_1_20_5)) {
            compound.setTag("Count", new NBTInt(itemStack.amount));
            if (itemStack.nbt != null) {
                compound.setTag("tag", itemStack.nbt);
            }
        }

        // TODO components

        return compound;
    }

    public int getMaxStackSize() {
        return this.getComponentOr(MAX_STACK_SIZE,
                // fallback to legacy specified max stack size
                this.getType().getMaxAmount());
    }

    public boolean isStackable() {
        return this.getMaxStackSize() > 1 && (!this.isDamageableItem() || !this.isDamaged());
    }

    public boolean isDamageableItem() {
        return !this.cachedIsEmpty && this.getMaxDamage() > 0
                && (this.nbt == null || !this.nbt.getBoolean("Unbreakable"))
                && !this.getComponentOr(ComponentTypes.UNBREAKABLE, false);
    }

    public boolean isDamaged() {
        return this.isDamageableItem() && this.getDamageValue() > 0;
    }

    public int getDamageValue() {
        if (this.nbt != null) {
            NBTInt damage = this.nbt.getTagOfTypeOrNull("Damage", NBTInt.class);
            if (damage != null) {
                return damage.getAsInt();
            }
        }
        return this.getComponentOr(DAMAGE, 0);
    }

    public void setDamageValue(int damage) {
        int cappedDamage = Math.max(0, damage);
        // set in legacy nbt
        this.getOrCreateTag().setTag("Damage", new NBTInt(cappedDamage));
        // set in components
        this.setComponent(DAMAGE, cappedDamage);
    }

    public int getMaxDamage() {
        return this.getComponentOr(MAX_DAMAGE,
                // fallback to legacy specified max durability
                this.getType().getMaxDurability());
    }

    public NBTCompound getOrCreateTag() {
        if (this.nbt == null) {
            this.nbt = new NBTCompound();
        }

        return this.nbt;
    }

    private void updateCachedEmptyStatus() {
        cachedIsEmpty = isEmpty();
    }

    public ItemType getType() {
        return cachedIsEmpty ? ItemTypes.AIR : type;
    }

    public int getAmount() {
        return cachedIsEmpty ? 0 : amount;
    }

    public void shrink(int amount) {
        this.setAmount(this.getAmount() - amount);
    }

    public void grow(int amount) {
        this.setAmount(this.getAmount() + amount);
    }

    public void setAmount(int amount) {
        this.amount = amount;
        updateCachedEmptyStatus();
    }

    public ItemStack split(int toTake) {
        int i = Math.min(toTake, getAmount());
        ItemStack itemstack = this.copy();
        itemstack.setAmount(i);
        this.shrink(i);
        return itemstack;
    }

    public ItemStack copy() {
        return cachedIsEmpty ? EMPTY : new ItemStack(
                type, amount,
                nbt == null ? null : nbt.copy(),
                components == null ? null : components.copy(),
                legacyData
        );
    }

    @Nullable
    public NBTCompound getNBT() {
        return nbt;
    }

    public void setNBT(NBTCompound nbt) {
        this.nbt = nbt;
    }

    public <T> T getComponentOr(ComponentType<T> type, T otherValue) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOr(type, otherValue);
        }
        return this.getType().getComponents().getOr(type, otherValue);
    }

    public <T> Optional<T> getComponent(ComponentType<T> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().getOptional(type);
        }
        return this.getType().getComponents().getOptional(type);
    }

    public <T> void setComponent(ComponentType<T> type, T value) {
        this.getComponents().set(type, value);
    }

    public <T> void unsetComponent(ComponentType<T> type) {
        this.getComponents().unset(type);
    }

    public <T> void setComponent(ComponentType<T> type, Optional<T> value) {
        this.getComponents().set(type, value);
    }

    public boolean hasComponent(ComponentType<?> type) {
        if (this.hasComponentPatches()) {
            return this.getComponents().has(type);
        }
        return this.getType().getComponents().has(type);
    }

    public boolean hasComponentPatches() {
        return this.components != null && !this.components.getPatches().isEmpty();
    }

    public PatchableComponentMap getComponents() {
        if (this.components == null) { // lazy load on access
            this.components = new PatchableComponentMap(
                    this.type.getComponents(), new HashMap<>(4));
        }
        return this.components;
    }

    /**
     * @param components if set null will reset to components of {@link ItemType}
     */
    public void setComponents(@Nullable PatchableComponentMap components) {
        this.components = components;
    }

    public int getLegacyData() {
        return legacyData;
    }

    public void setLegacyData(int legacyData) {
        this.legacyData = legacyData;
    }

    public boolean isEnchantable(ClientVersion version) {
        if (getType() == ItemTypes.BOOK) return getAmount() == 1;
        if (getType() == ItemTypes.ENCHANTED_BOOK) return false;
        return getMaxStackSize() == 1 && canBeDepleted() && !isEnchanted(version);
    }

    public boolean isEnchanted(ClientVersion version) {
        if (this.isEmpty()) {
            return false;
        }
        // check for components
        if (!this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()
                || !this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).isEmpty()) {
            return true;
        }
        // check for legacy nbt
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> enchantments = this.nbt.getCompoundListTagOrNull(tagName);
            return enchantments != null && !enchantments.getTags().isEmpty();
        }
        return false;
    }

    public List<Enchantment> getEnchantments(ClientVersion version) {
        if (this.isEmpty()) {
            return new ArrayList<>(0);
        }

        // check for components
        ItemEnchantments enchantmentsComp = this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments storedEnchantmentsComp = this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (!enchantmentsComp.isEmpty() || !storedEnchantmentsComp.isEmpty()) {
            List<Enchantment> enchantmentsList = new ArrayList<>(
                    enchantmentsComp.getEnchantmentCount() + storedEnchantmentsComp.getEnchantmentCount());
            for (Map.Entry<EnchantmentType, Integer> enchantment : enchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            for (Map.Entry<EnchantmentType, Integer> enchantment : storedEnchantmentsComp) {
                enchantmentsList.add(new Enchantment(enchantment.getKey(), enchantment.getValue()));
            }
            return enchantmentsList;
        }

        // check for legacy nbt
        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
                List<NBTCompound> compounds = nbtList.getTags();
                List<Enchantment> enchantments = new ArrayList<>(compounds.size());

                for (NBTCompound compound : compounds) {
                    EnchantmentType type = getEnchantmentTypeFromTag(compound, version);

                    if (type != null) {
                        NBTNumber levelTag = compound.getNumberTagOrNull("lvl");
                        if (levelTag != null) {
                            int level = levelTag.getAsInt();
                            Enchantment enchantment = Enchantment.builder().type(type).level(level).build();
                            enchantments.add(enchantment);
                        }
                    }
                }
                return enchantments;
            }
        }

        return new ArrayList<>(0);
    }

    public int getEnchantmentLevel(EnchantmentType enchantment, ClientVersion version) {
        if (this.isEmpty()) {
            return 0;
        }

        // check for components
        ItemEnchantments enchantmentsComp = this.getComponentOr(ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (!enchantmentsComp.isEmpty()) {
            int level = enchantmentsComp.getEnchantmentLevel(enchantment);
            if (level > 0) {
                return level;
            }
        }
        ItemEnchantments storedEnchantmentsComp = this.getComponentOr(STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);
        if (!storedEnchantmentsComp.isEmpty()) {
            int level = storedEnchantmentsComp.getEnchantmentLevel(enchantment);
            if (level > 0) {
                return level;
            }
        }

        if (this.nbt != null) {
            String tagName = this.getEnchantmentsTagName(version);
            NBTList<NBTCompound> nbtList = this.nbt.getCompoundListTagOrNull(tagName);
            if (nbtList != null) {
                for (NBTCompound base : nbtList.getTags()) {
                    EnchantmentType type = getEnchantmentTypeFromTag(base, version);
                    if (Objects.equals(type, enchantment)) {
                        NBTNumber nbtLevel = base.getNumberTagOrNull("lvl");
                        return nbtLevel != null ? nbtLevel.getAsInt() : 0;
                    }
                }
            }
        }

        return 0;
    }

    private static @Nullable EnchantmentType getEnchantmentTypeFromTag(NBTCompound tag, ClientVersion version) {
        if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
            String id = tag.getStringTagValueOrNull("id");
            return EnchantmentTypes.getByName(id);
        } else {
            NBTShort idTag = tag.getTagOfTypeOrNull("id", NBTShort.class);
            return EnchantmentTypes.getById(version, idTag.getAsInt());
        }
    }

    public void setEnchantments(List<Enchantment> enchantments, ClientVersion version) {
        // set in legacy nbt
        nbt = getOrCreateTag(); // Create tag if null
        String tagName = getEnchantmentsTagName(version);
        if (enchantments.isEmpty()) {
            //Let us clear the enchantments
            if (nbt.getTagOrNull(tagName) != null) {
                nbt.removeTag(tagName);
            }
        } else {
            List<NBTCompound> list = new ArrayList<>();
            for (Enchantment enchantment : enchantments) {
                NBTCompound compound = new NBTCompound();
                if (version.isNewerThanOrEquals(ClientVersion.V_1_13)) {
                    compound.setTag("id", new NBTString(enchantment.getType().getName().toString()));
                } else {
                    compound.setTag("id", new NBTShort((short) enchantment.getType().getId(version)));
                }

                compound.setTag("lvl", new NBTShort((short) enchantment.getLevel()));
                list.add(compound);
            }
            assert nbt != null; // NBT was created in getOrCreateTag()
            nbt.setTag(tagName, new NBTList<>(NBTType.COMPOUND, list));
        }

        // set in components
        Map<EnchantmentType, Integer> enchantmentsMap = new HashMap<>(enchantments.size());
        for (Enchantment enchantment : enchantments) {
            enchantmentsMap.put(enchantment.getType(), enchantment.getLevel());
        }
        ComponentType<ItemEnchantments> componentType = this.type == ItemTypes.ENCHANTED_BOOK
                ? STORED_ENCHANTMENTS : ENCHANTMENTS;
        Optional<ItemEnchantments> prevEnchantments = this.getComponent(componentType);
        boolean showInTooltip = prevEnchantments.map(ItemEnchantments::isShowInTooltip).orElse(true);
        this.setComponent(componentType, new ItemEnchantments(enchantmentsMap, showInTooltip));
    }

    @Deprecated
    public String getEnchantmentsTagName(ClientVersion version) {
        String tagName = version.isNewerThanOrEquals(ClientVersion.V_1_13) ? "Enchantments" : "ench";
        if (type == ItemTypes.ENCHANTED_BOOK) {
            tagName = "StoredEnchantments";
        }
        return tagName;
    }

    public boolean canBeDepleted() {
        return this.getType().getMaxDurability() > 0;
    }

    public boolean is(ItemType type) {
        return this.getType() == type;
    }

    public static boolean isSameItemSameTags(ItemStack stack, ItemStack otherStack) {
        return stack.is(otherStack.getType())
                && (stack.isEmpty() && otherStack.isEmpty()
                || (ItemStack.tagMatches(stack, otherStack)
                && Objects.equals(stack.components, otherStack.components)));
    }

    public static boolean tagMatches(ItemStack left, ItemStack right) {
        if (left == right) {
            return true;
        }
        if (left == null) {
            return right.isEmpty();
        }
        if (right == null) {
            return left.isEmpty();
        }
        return Objects.equals(left.nbt, right.nbt)
                && Objects.equals(left.components, right.components);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) obj;
            return getType().equals(itemStack.getType())
                    && amount == itemStack.amount
                    && Objects.equals(nbt, itemStack.nbt)
                    && Objects.equals(components, itemStack.components)
                    && legacyData == itemStack.legacyData;
        }
        return false;
    }

    @Override
    public String toString() {
        if (cachedIsEmpty) {
            return "ItemStack[null]";
        } else {
            String identifier = type == null ? "null" : type.getName().toString();
            int maxAmount = getType().getMaxAmount();
            return "ItemStack[type=" + identifier + ", amount=" + amount + "/" + maxAmount
                    + ", nbt tag names: " + (nbt != null ? nbt.getTagNames() : "[null]")
                    + ", legacyData=" + legacyData + ", components=" + (components != null ? components.getPatches() : null) + "]";
        }
    }

    public boolean isEmpty() {
        return type == null || type == ItemTypes.AIR || amount <= 0;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ItemType type;
        private int amount = 1;
        private NBTCompound nbt = null;
        private PatchableComponentMap components = null;
        private int legacyData = -1;

        public Builder type(ItemType type) {
            this.type = type;
            return this;
        }

        public Builder amount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder nbt(NBTCompound nbt) {
            this.nbt = nbt;
            return this;
        }

        public Builder nbt(String key, NBT tag) {
            if (this.nbt == null) {
                this.nbt = new NBTCompound();
            }
            this.nbt.setTag(key, tag);
            return this;
        }

        public Builder components(@Nullable PatchableComponentMap components) {
            this.components = components;
            return this;
        }

        public <T> Builder component(ComponentType<T> type, @Nullable T value) {
            if (this.components == null) {
                this.components = new PatchableComponentMap(this.type == null
                        ? StaticComponentMap.SHARED_ITEM_COMPONENTS : this.type.getComponents());
            }
            this.components.set(type, value);
            return this;
        }

        public Builder legacyData(int legacyData) {
            this.legacyData = legacyData;
            return this;
        }

        public ItemStack build() {
            return new ItemStack(type, amount, nbt, components, legacyData);
        }

    }
}
