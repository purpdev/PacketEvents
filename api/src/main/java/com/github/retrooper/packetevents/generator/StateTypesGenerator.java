package com.github.retrooper.packetevents.generator;

import com.destroystokyo.paper.MaterialTags;
import com.github.retrooper.packetevents.protocol.world.MaterialType;
import com.github.retrooper.packetevents.util.reflection.Reflection;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.Locale;

// How to use the generator
// 1. Uncomment generateStateTypesString() and the jar input files in build.gradle
// 2. Compile packetEvents, calling generateStateTypesString() from somewhere
// 3. Copy sysout from console into editor
// 4. Done!
public final class StateTypesGenerator {

    public static void generateStateTypesString() throws Exception {
        System.out.println("Generating state types...");
        for (Block block : BuiltInRegistries.BLOCK) {
            ResourceLocation blockLocation = BuiltInRegistries.BLOCK.getKey(block);

            // The underlying block's properties:
            BlockBehaviour.Properties props = block.properties();
            float hardness = block.defaultDestroyTime();
            float blastResistance = block.getExplosionResistance();
            boolean requiresCorrectTool = Reflection.getField(BlockBehaviour.Properties.class, "requiresCorrectToolForDrops").getBoolean(props);
            boolean isAir = block instanceof AirBlock;
            boolean isBlocking = block.hasCollision;
            boolean isSolid = block.defaultBlockState().blocksMotion();
            boolean isShapeExceedsCube = block.defaultBlockState().hasLargeCollisionShape();
            String nameUpper = blockLocation.getPath().toUpperCase(Locale.ROOT);

            // Get bukkit material for usage in tags
            Material bukkitMaterial = org.bukkit.Material.valueOf(nameUpper);
            MaterialType material = guessMaterialType(bukkitMaterial);

            StringBuilder stateTypeBuilder = new StringBuilder();
            stateTypeBuilder.append("public static StateType ").append(nameUpper)
                    .append(" = StateTypes.builder()")
                    .append(".name(\"").append(nameUpper).append("\")")
                    .append(".blastResistance(").append(blastResistance).append("f)")
                    .append(".hardness(").append(hardness).append("f)")
                    .append(".isBlocking(").append(isBlocking).append(")")
                    .append(".requiresCorrectTool(").append(requiresCorrectTool).append(")")
                    .append(".isSolid(").append(isSolid).append(")");

            if (isAir) {
                stateTypeBuilder.append(".isAir(true)");
            }

            if (isShapeExceedsCube) {
                stateTypeBuilder.append(".isShapeExceedsCube(true)");
            }

            stateTypeBuilder.append(".setMaterial(MaterialType.")
                    .append(material.name())
                    .append(")");

            stateTypeBuilder.append(".build();");

            System.out.println(stateTypeBuilder);
        }
    }


    // This is cooked, just temp, will hand tune to fix diffs if retrooper accepts generator idea
    public static MaterialType guessMaterialType(Material bukkitMaterial) {
        if (bukkitMaterial.isAir()) {
            return MaterialType.AIR;
        }
        if (bukkitMaterial == Material.STRUCTURE_VOID) {
            return MaterialType.STRUCTURAL_AIR;
        }
        if (Tag.WOOL_CARPETS.isTagged(bukkitMaterial)) {
            return MaterialType.CLOTH_DECORATION;
        }
        if (bukkitMaterial == Material.WATER) {
            return MaterialType.WATER;
        }
        if (bukkitMaterial == Material.LAVA) {
            return MaterialType.LAVA;
        }
        if (bukkitMaterial == Material.BUBBLE_COLUMN) {
            return MaterialType.BUBBLE_COLUMN;
        }
        if (bukkitMaterial == Material.CACTUS) {
            return MaterialType.CACTUS;
        }
        if (bukkitMaterial == Material.TNT) {
            return MaterialType.EXPLOSIVE;
        }
        if (bukkitMaterial == Material.BARRIER) {
            return MaterialType.BARRIER;
        }
        if (bukkitMaterial == Material.BEDROCK
                || bukkitMaterial.name().endsWith("ANVIL")) {
            return MaterialType.HEAVY_METAL;
        }
        if (Tag.WOOL.isTagged(bukkitMaterial)
                || Tag.WOOL_CARPETS.isTagged(bukkitMaterial)) {
            return MaterialType.WOOL;
        }
        if (Tag.SHULKER_BOXES.isTagged(bukkitMaterial)) {
            return MaterialType.SHULKER_SHELL;
        }
        if (Tag.PORTALS.isTagged(bukkitMaterial)) {
            return MaterialType.PORTAL;
        }
        if (Tag.FIRE.isTagged(bukkitMaterial)) {
            return MaterialType.FIRE;
        }
        if (bukkitMaterial.name().contains("SKULK")) {
            return MaterialType.SCULK;
        }
        if (MaterialTags.PISTONS.isTagged(bukkitMaterial)) {
            return MaterialType.PISTON;
        }
        if (MaterialTags.SPONGES.isTagged(bukkitMaterial)) {
            return MaterialType.SPONGE;
        }
        if (Tag.LEAVES.isTagged(bukkitMaterial)) {
            return MaterialType.LEAVES;
        }
        if (Tag.BUTTONS.isTagged(bukkitMaterial) || bukkitMaterial.name().startsWith("POTTED_")) { // wtf?
            return MaterialType.DECORATION;
        }
        if (MaterialTags.GLASS.isTagged(bukkitMaterial)
                || MaterialTags.GLASS_PANES.isTagged(bukkitMaterial)) {
            return MaterialType.GLASS;
        }
        if (Tag.ICE.isTagged(bukkitMaterial)) {
            return MaterialType.ICE;
        }
        if (Tag.SNOW.isTagged(bukkitMaterial)) {
            return MaterialType.SNOW;
        }
        if (Tag.SAND.isTagged(bukkitMaterial)) {
            return MaterialType.SAND;
        }
        if (Tag.DIRT.isTagged(bukkitMaterial)) {
            return MaterialType.DIRT;
        }

        if (Tag.LOGS.isTagged(bukkitMaterial)) {
            if (Tag.CRIMSON_STEMS.isTagged(bukkitMaterial)
                    || Tag.WARPED_STEMS.isTagged(bukkitMaterial)) {
                return MaterialType.NETHER_WOOD;
            }
            return MaterialType.WOOD;
        }
        if (Tag.PLANKS.isTagged(bukkitMaterial)) {
            return MaterialType.WOOD;
        }

        if (Tag.WOODEN_FENCES.isTagged(bukkitMaterial)
                || MaterialTags.WOODEN_GATES.isTagged(bukkitMaterial)
                || Tag.WOODEN_DOORS.isTagged(bukkitMaterial)
                || Tag.WOODEN_TRAPDOORS.isTagged(bukkitMaterial)
                || Tag.WOODEN_STAIRS.isTagged(bukkitMaterial)
                || Tag.ALL_HANGING_SIGNS.isTagged(bukkitMaterial)
                || Tag.WOODEN_PRESSURE_PLATES.isTagged(bukkitMaterial)
                || Tag.SIGNS.isTagged(bukkitMaterial)
                || Tag.WOODEN_SLABS.isTagged(bukkitMaterial)
                || Tag.WOODEN_BUTTONS.isTagged(bukkitMaterial)) {
            return MaterialType.WOOD;
        }

        if (Tag.BAMBOO_BLOCKS.isTagged(bukkitMaterial)) {
            return MaterialType.BAMBOO;
        }

        if (Tag.FLOWERS.isTagged(bukkitMaterial)
                || Tag.SMALL_FLOWERS.isTagged(bukkitMaterial)
                || Tag.CROPS.isTagged(bukkitMaterial)
                || bukkitMaterial == Material.PUMPKIN_STEM
                || bukkitMaterial == Material.MELON_STEM
                || Tag.BAMBOO_BLOCKS.isTagged(bukkitMaterial)
                || MaterialTags.MUSHROOMS.isTagged(bukkitMaterial)
                || Tag.SAPLINGS.isTagged(bukkitMaterial)) {
            return MaterialType.PLANT;
        }

        if (Tag.CORAL_BLOCKS.isTagged(bukkitMaterial)
                || Tag.CORALS.isTagged(bukkitMaterial)
                || MaterialTags.CORAL.isTagged(bukkitMaterial)
                || MaterialTags.CORAL_FANS.isTagged(bukkitMaterial)
                || MaterialTags.CORAL_BLOCKS.isTagged(bukkitMaterial)
                || Tag.WALL_CORALS.isTagged(bukkitMaterial)) {
            return MaterialType.WATER_PLANT;
        }

        if (Tag.STONE_BRICKS.isTagged(bukkitMaterial)
                || Tag.STONE_ORE_REPLACEABLES.isTagged(bukkitMaterial)
                || MaterialTags.COBBLESTONES.isTagged(bukkitMaterial)
                || MaterialTags.SANDSTONES.isTagged(bukkitMaterial)) {
            return MaterialType.STONE;
        }

        if (bukkitMaterial == Material.CLAY) {
            return MaterialType.CLAY;
        }

        if (bukkitMaterial.name().contains("AMETHYST")) {
            return MaterialType.AMETHYST;
        }

        if (bukkitMaterial == Material.FROGSPAWN) {
            return MaterialType.FROGSPAWN;
        }
        if (bukkitMaterial.name().contains("FROGLIGHT")) {
            return MaterialType.FROGLIGHT;
        }

        if (bukkitMaterial == Material.DECORATED_POT) {
            return MaterialType.DECORATED_POT;
        }

        if (bukkitMaterial == Material.POWDER_SNOW) {
            return MaterialType.POWDER_SNOW;
        }

        if (bukkitMaterial == Material.MOSS_BLOCK
                || bukkitMaterial == Material.MOSS_CARPET) {
            return MaterialType.MOSS;
        }

        return MaterialType.STONE;
    }
}
