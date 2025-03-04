package com.github.retrooper.packetevents.test;

import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.entity.data.EntityDataTypes;
import com.github.retrooper.packetevents.protocol.entity.data.IEntityDataType;
import com.github.retrooper.packetevents.protocol.item.enchantment.type.EnchantmentTypes;
import com.github.retrooper.packetevents.protocol.player.ClientVersion;
import com.github.retrooper.packetevents.protocol.world.biome.Biomes;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.enums.Axis;
import com.github.retrooper.packetevents.protocol.world.states.enums.East;
import com.github.retrooper.packetevents.protocol.world.states.enums.North;
import com.github.retrooper.packetevents.protocol.world.states.enums.Orientation;
import com.github.retrooper.packetevents.protocol.world.states.enums.South;
import com.github.retrooper.packetevents.protocol.world.states.enums.West;
import com.github.retrooper.packetevents.protocol.world.states.type.StateTypes;
import com.github.retrooper.packetevents.test.base.BaseDummyAPITest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MappingIntegrityTest extends BaseDummyAPITest {

    @Test
    @DisplayName("Test attribute mapping")
    public void testAttributeMapping() {
        assertEquals(11, Attributes.GRAVITY.getId(ClientVersion.V_1_20_5));
        assertEquals(1, Attributes.FOLLOW_RANGE.getId(ClientVersion.V_1_20_2));
    }

    @Test
    @DisplayName("Test enchantment mapping")
    public void testEnchantmentMapping() {
        assertEquals(14, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_20_5));
        assertEquals(13, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_17_1));
        assertEquals(12, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_13_2));
        assertEquals(17, EnchantmentTypes.SMITE.getId(ClientVersion.V_1_12));
    }

    @Test
    @DisplayName("Test biome mapping")
    public void testBiomeMapping() {
        assertEquals(48, Biomes.SOUL_SAND_VALLEY.getId(ClientVersion.V_1_21));
        assertEquals(24, Biomes.GROVE.getId(ClientVersion.V_1_19_4));
        assertEquals(0, Biomes.OCEAN.getId(ClientVersion.V_1_15_1));
    }

    @Test
    @DisplayName("Test state type mapping")
    public void testStateTypeMapping() {
        assertEquals(1, StateTypes.STONE.getMapped().getId(ClientVersion.V_1_8));
        assertEquals(57, StateTypes.DIAMOND_BLOCK.getMapped().getId(ClientVersion.V_1_8));
        assertEquals(1, StateTypes.STONE.getMapped().getId(ClientVersion.V_1_20_5));
        assertEquals(1059, StateTypes.HEAVY_CORE.getMapped().getId(ClientVersion.V_1_20_5));
    }

    @Test
    @DisplayName("Test block state mapping")
    public void testBlockStateMapping() {
        assertEquals(16, StateTypes.STONE.createBlockState(ClientVersion.V_1_8).getGlobalId());
        assertEquals(57 * 16, StateTypes.DIAMOND_BLOCK.createBlockState(ClientVersion.V_1_8).getGlobalId());

        assertEquals(1, StateTypes.STONE.createBlockState(ClientVersion.V_1_20_5).getGlobalId());

        WrappedBlockState crafterState = StateTypes.CRAFTER.createBlockState(ClientVersion.V_1_20_5);
        crafterState.setOrientation(Orientation.DOWN_NORTH);
        assertEquals(26617, crafterState.getGlobalId());

        WrappedBlockState veinState = StateTypes.SCULK_VEIN.createBlockState(ClientVersion.V_1_20_5);
        veinState.setEast(East.TRUE);
        veinState.setNorth(North.TRUE);
        veinState.setWest(West.TRUE);
        veinState.setSouth(South.TRUE);
        assertEquals(22870, veinState.getGlobalId());

        WrappedBlockState redstoneState = StateTypes.REDSTONE_WIRE.createBlockState(ClientVersion.V_1_20_5);
        redstoneState.setEast(East.UP);
        redstoneState.setNorth(North.UP);
        redstoneState.setWest(West.SIDE);
        redstoneState.setSouth(South.UP);
        redstoneState.setPower(5);
        assertEquals(3024, redstoneState.getGlobalId());

        assertEquals(26683, StateTypes.HEAVY_CORE.createBlockState(ClientVersion.V_1_20_5).getGlobalId());
        WrappedBlockState heavyCoreState = StateTypes.HEAVY_CORE.createBlockState(ClientVersion.V_1_20_5);
        heavyCoreState.setWaterlogged(true);
        assertEquals(26682, heavyCoreState.getGlobalId());

        assertEquals(158, StateTypes.PALE_OAK_LOG.createBlockState(ClientVersion.V_1_21_2).getGlobalId());
        WrappedBlockState state = StateTypes.PALE_OAK_LOG.createBlockState(ClientVersion.V_1_21_2);
        state.setAxis(Axis.Z);
        assertEquals(159, state.getGlobalId());
    }

    @Test
    @DisplayName("Test entity data type mapping loading")
    public void testEntityDataTypeMappingLoading() throws IllegalAccessException {
        for (Field field : EntityDataTypes.class.getFields()) {
            if (!Modifier.isStatic(field.getModifiers())
                    || field.getDeclaredAnnotations().length != 0) {
                continue;
            }
            IEntityDataType<?> type = (IEntityDataType<?>) field.get(null);
            if (type == EntityDataTypes.TYPE_SHORT) {
                continue; // ignore legacy types
            }
            assertEquals(type, EntityDataTypes.getRegistry().getByName(type.getName()), type.getName().toString());
            assertEquals(type, EntityDataTypes.getRegistry().getById(ClientVersion.V_1_21_2, type.getId(ClientVersion.V_1_21_2)), type.getName().toString());
        }
    }
}
