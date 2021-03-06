package me.lyneira.MachinaCore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

/**
 * Class that contains useful information about block types.
 * 
 * @author Lyneira
 */
public final class BlockData {
    private static final int blockIdLimit = 256;
    private static BlockData[] blockId = new BlockData[blockIdLimit];
    private static final Random generator = new Random();
    private static final BreakTime breakTimeInstant = new BreakTime(1);
    private static final BreakTime breakTimeNetherrack = new BreakTime(4);
    private static final BreakTime breakTimeFast = new BreakTime(7);
    private static final BreakTime breakTimeMedium = new BreakTime(15);
    private static final BreakTime breakTimeSlow = new BreakTime(20);
    private static final BreakTime breakTimeTough = new BreakTime(25);
    private static final BreakTime breakTimeObsidian = new BreakTime(220);

    private static class BreakTime {
        int time;

        BreakTime(final int time) {
            this.time = time;
        }
    }

    private BlockData() {
        // Only this class can instantiate itself.
    }

    /**
     * Returns the ItemStack that would result from a player breaking the given
     * Block with an appropriate tool.
     * 
     * @param location
     *            The location where the block is being broken
     * @return The item stack resulting from the block break, or null if there
     *         is no drop
     */
    public static final Collection<ItemStack> breakBlock(final BlockLocation location) {
        Block block = location.getBlock();
        BlockData data;
        int blockType = block.getTypeId();
        try {
            data = blockId[blockType];
        } catch (Exception e) {
            return new ArrayList<ItemStack>(1);
        }
        // Got a BlockMetaData, now determine what to drop

        // Simplest case, ask bukkit
        if (data.drop < 0) {
            return block.getDrops();
        }

        int item;
        short damageValue;
        if (data.drop == 0) {
            return null;
        } else {
            item = data.drop;
        }
        // Determine if we should use the Block's data value or the predefined
        // one
        if (data.data < 0) {
            damageValue = block.getData();
        } else {
            damageValue = (byte) data.data;
        }
        // Determine drop amount
        int amount;
        if (data.dropRandom > 0) {
            amount = data.dropMin + generator.nextInt(data.dropRandom);
        } else {
            amount = data.dropMin;
        }
        if (amount > 0) {
            List<ItemStack> drop = new ArrayList<ItemStack>(1);
            drop.add(new ItemStack(item, amount, damageValue));
            return drop;
        } else {
            return new ArrayList<ItemStack>(1);
        }
    }

    /**
     * Whether this block type is solid, meaning whether a device can rest on
     * it.
     */
    private boolean solid = false;

    /**
     * Whether this blockId can be drilled.
     */
    private boolean drillable = false;

    /**
     * How long (in server ticks) it takes to drill this block.
     */
    private BreakTime drillTime = breakTimeInstant;

    /**
     * What item Id will be dropped when this block is drilled. A value of 0
     * means nothing will be dropped. A negative value means the block will be
     * broken as if a player had dug it.
     */
    private int drop = -1;

    /**
     * Metadata of the item to be dropped. A negative value means the broken
     * block's data will be used. Only used if drop is non-negative.
     */
    private short data = -1;

    /**
     * The minimum amount that will be dropped. If 0 or negative, relies on
     * dropRandom increasing to above 0 for anything to drop.
     */
    private int dropMin = 1;

    /**
     * A random amount from 0 to (dropRandom - 1) that will be dropped in
     * addition to dropMin.
     */
    private int dropRandom = 0;

    /**
     * Whether this block's data must be copied during a move.
     */
    private boolean copyData = false;

    /**
     * Whether this block has an inventory.
     */
    private boolean hasInventory = false;

    /**
     * Whether this block can only exist while attached to another block.
     */
    private boolean attached = false;

    static {
        // The Big Scary Static Init of Everything Block Related...

        set(Material.STONE.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.GRASS.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.DIRT.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.COBBLESTONE.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.WOOD.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.SAPLING.getId()).drillable(true).copyData(true).attached(true);

        set(Material.BEDROCK.getId()).solid(true);

        set(Material.WATER.getId()).copyData(true);

        set(Material.STATIONARY_WATER.getId()).copyData(true);

        set(Material.LAVA.getId()).copyData(true);

        set(Material.STATIONARY_LAVA.getId()).copyData(true);

        set(Material.SAND.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.GRAVEL.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.GOLD_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.IRON_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.COAL_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.LOG.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.LEAVES.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeFast);

        set(Material.SPONGE.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.GLASS.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.LAPIS_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.LAPIS_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.DISPENSER.getId()).solid(true).copyData(true).inventory(true);

        set(Material.SANDSTONE.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeFast);

        set(Material.NOTE_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.BED_BLOCK.getId()).copyData(true);

        set(Material.POWERED_RAIL.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.DETECTOR_RAIL.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.PISTON_STICKY_BASE.getId()).solid(true).copyData(true);

        set(Material.WEB.getId()).drillable(true).drillTime(breakTimeMedium);

        set(Material.LONG_GRASS.getId()).drillable(true).copyData(true).attached(true);

        set(Material.DEAD_BUSH.getId()).drillable(true).attached(true);

        set(Material.PISTON_BASE.getId()).solid(true).copyData(true);

        set(Material.PISTON_EXTENSION.getId()).solid(true).copyData(true);

        set(Material.WOOL.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeFast);

        set(Material.YELLOW_FLOWER.getId()).drillable(true).attached(true);

        set(Material.RED_ROSE.getId()).drillable(true).attached(true);

        set(Material.BROWN_MUSHROOM.getId()).drillable(true).attached(true);

        set(Material.RED_MUSHROOM.getId()).drillable(true).attached(true);

        set(Material.GOLD_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.IRON_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeTough);

        set(Material.DOUBLE_STEP.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.STEP.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.BRICK.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.TNT.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.BOOKSHELF.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.MOSSY_COBBLESTONE.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.OBSIDIAN.getId()).solid(true).drillable(true).drillTime(breakTimeObsidian);

        set(Material.TORCH.getId()).drillable(true).copyData(true).attached(true);

        set(Material.FIRE.getId()).drillable(true).copyData(true);

        set(Material.MOB_SPAWNER.getId()).solid(true);

        set(Material.WOOD_STAIRS.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.CHEST.getId()).copyData(true).inventory(true);

        set(Material.REDSTONE_WIRE.getId()).drillable(true).attached(true);

        set(Material.DIAMOND_ORE.getId()).drillable(true).solid(true).drillTime(breakTimeSlow);

        set(Material.DIAMOND_BLOCK.getId()).drillable(true).solid(true).drillTime(breakTimeTough);

        set(Material.WORKBENCH.getId()).drillable(true).solid(true).drillTime(breakTimeMedium);

        set(Material.CROPS.getId()).drillable(true).copyData(true).attached(true);

        set(Material.SOIL.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeFast);

        set(Material.FURNACE.getId()).solid(true).copyData(true).inventory(true);

        set(Material.BURNING_FURNACE.getId()).solid(true).copyData(true).inventory(true);

        set(Material.SIGN_POST.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.WOODEN_DOOR.getId()).copyData(true).attached(true);

        set(Material.LADDER.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.RAILS.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.COBBLESTONE_STAIRS.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.WALL_SIGN.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.LEVER.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.STONE_PLATE.getId()).drillable(true).attached(true).drillTime(breakTimeMedium);

        set(Material.IRON_DOOR_BLOCK.getId()).copyData(true).attached(true);

        set(Material.WOOD_PLATE.getId()).drillable(true).attached(true).drillTime(breakTimeMedium);

        set(Material.REDSTONE_ORE.getId()).drillable(true).solid(true).drillTime(breakTimeSlow);

        set(Material.GLOWING_REDSTONE_ORE.getId()).drillable(true).solid(true).drillTime(breakTimeSlow);

        set(Material.REDSTONE_TORCH_OFF.getId()).drillable(true).copyData(true).attached(true);

        set(Material.REDSTONE_TORCH_ON.getId()).drillable(true).copyData(true).attached(true);

        set(Material.STONE_BUTTON.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeMedium);

        set(Material.SNOW.getId()).drillable(true).copyData(true).attached(true);

        set(Material.ICE.getId()).drillable(true).solid(true).drillTime(breakTimeMedium);

        set(Material.SNOW_BLOCK.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        // Cactus non-solid for the benefit of builder machina
        set(Material.CACTUS.getId()).drillable(true).attached(true).drillTime(breakTimeFast);

        set(Material.CLAY.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.SUGAR_CANE_BLOCK.getId()).drillable(true).attached(true).drillTime(breakTimeFast);

        set(Material.JUKEBOX.getId()).solid(true).copyData(true);

        set(Material.FENCE.getId()).drillable(true).solid(true).drillTime(breakTimeMedium);

        set(Material.PUMPKIN.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.NETHERRACK.getId()).drillable(true).solid(true).drillTime(breakTimeNetherrack);

        set(Material.SOUL_SAND.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.GLOWSTONE.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.JACK_O_LANTERN.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeFast);

        set(Material.CAKE_BLOCK.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.DIODE_BLOCK_OFF.getId()).drillable(true).copyData(true).attached(true);

        set(Material.DIODE_BLOCK_ON.getId()).drillable(true).copyData(true).attached(true);

     //  set(Material.LOCKED_CHEST.getId()); Locked Chest are no longer in the game

        set(Material.TRAP_DOOR.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.MONSTER_EGGS.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.SMOOTH_BRICK.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeSlow);

        set(Material.HUGE_MUSHROOM_1.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeFast);

        set(Material.HUGE_MUSHROOM_2.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeFast);

        set(Material.IRON_FENCE.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.THIN_GLASS.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.MELON_BLOCK.getId()).drillable(true).solid(true).drillTime(breakTimeFast);

        set(Material.PUMPKIN_STEM.getId()).drillable(true).copyData(true).attached(true);

        set(Material.MELON_STEM.getId()).drillable(true).copyData(true).attached(true);

        set(Material.VINE.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.FENCE_GATE.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.BRICK_STAIRS.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeSlow);

        set(Material.SMOOTH_STAIRS.getId()).drillable(true).solid(true).copyData(true).drillTime(breakTimeSlow);

        set(Material.MYCEL.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.WATER_LILY.getId()).drillable(true).drillTime(breakTimeFast);

        set(Material.NETHER_BRICK.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.NETHER_FENCE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.NETHER_BRICK_STAIRS.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeSlow);

        set(Material.NETHER_WARTS.getId()).drillable(true).copyData(true).drillTime(breakTimeFast);

        set(Material.ENCHANTMENT_TABLE.getId()).solid(true).drillable(true).drillTime(breakTimeObsidian);

        set(Material.BREWING_STAND.getId()).copyData(true).inventory(true);

        set(Material.CAULDRON.getId()).copyData(true);

        set(Material.ENDER_PORTAL.getId()).copyData(true);

        set(Material.ENDER_STONE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.REDSTONE_LAMP_OFF.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.REDSTONE_LAMP_ON.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.WOOD_DOUBLE_STEP.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.WOOD_STEP.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.COCOA.getId()).solid(true).drillable(true).drillTime(breakTimeFast);

        set(Material.SANDSTONE_STAIRS.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.EMERALD_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.EMERALD_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);

        set(Material.SPRUCE_WOOD_STAIRS.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.BIRCH_WOOD_STAIRS.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.JUNGLE_WOOD_STAIRS.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.COBBLE_WALL.getId()).drillable(true).copyData(true).drillTime(breakTimeMedium);

        set(Material.FLOWER_POT.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.CARROT.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.POTATO.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.WOOD_BUTTON.getId()).drillable(true).copyData(true).attached(true).drillTime(breakTimeFast);

        set(Material.ANVIL.getId()).copyData(true);

        set(Material.TRAPPED_CHEST.getId()).copyData(true).inventory(true);

        set(Material.GOLD_PLATE.getId()).drillable(true).attached(true).drillTime(breakTimeMedium);

        set(Material.IRON_PLATE.getId()).drillable(true).attached(true).drillTime(breakTimeMedium);
        
        set(Material.REDSTONE_COMPARATOR_OFF.getId()).drillable(true).copyData(true).attached(true);

        set(Material.REDSTONE_COMPARATOR_ON.getId()).drillable(true).copyData(true).attached(true);
        
        set(Material.DAYLIGHT_DETECTOR.getId()).drillable(true).drillTime(breakTimeFast);
        
        set(Material.REDSTONE_BLOCK.getId()).solid(true).drillable(true).drillTime(breakTimeMedium);
        
        set(Material.QUARTZ_ORE.getId()).solid(true).drillable(true).drillTime(breakTimeSlow);

        set(Material.DROPPER.getId()).solid(true).inventory(true);
        
        set(Material.QUARTZ_BLOCK.getId()).copyData(true).solid(true).drillable(true).drillTime(breakTimeSlow);
        
        set(Material.QUARTZ_STAIRS.getId()).copyData(true).solid(true).drillable(true).drillTime(breakTimeSlow);
        
        set(Material.ACTIVATOR_RAIL.getId()).copyData(true).attached(true).drillable(true).drillTime(breakTimeMedium);
        
        set(Material.DROPPER.getId()).copyData(true).solid(true).inventory(true);
        
        set(Material.LOG.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);
        set(Material.LOG_2.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeMedium);
        set(Material.LEAVES_2.getId()).solid(true).drillable(true).copyData(true).drillTime(breakTimeFast);
        
    }

    // Private setters to make the initialization look better.
    private final BlockData solid(boolean value) {
        this.solid = value;
        return this;
    }

    private final BlockData drillable(boolean value) {
        this.drillable = value;
        return this;
    }

    private final BlockData drillTime(final BreakTime drillTime) {
        this.drillTime = drillTime;
        return this;
    }

    private final BlockData drop(final int drop) {
        this.drop = drop;
        return this;
    }

    private final BlockData data(final short data) {
        this.data = data;
        return this;
    }

    private final BlockData dropMin(final int dropMin) {
        this.dropMin = dropMin;
        return this;
    }

    private final BlockData dropRandom(final int dropRandom) {
        this.dropRandom = dropRandom;
        return this;
    }

    private final BlockData copyData(boolean value) {
        this.copyData = value;
        return this;
    }

    private final BlockData inventory(boolean value) {
        this.hasInventory = value;
        return this;
    }

    private final BlockData attached(boolean value) {
        this.attached = value;
        return this;
    }

    private static final BlockData set(final int typeId) {
        if (typeId >= blockIdLimit)
            return null;

        if (blockId[typeId] == null)
            blockId[typeId] = new BlockData();

        return blockId[typeId];
    }

    static final void loadBlockConfiguration(ConfigurationSection configuration) {
        if (configuration == null)
            return;

        Map<String, Object> blockSections = configuration.getValues(false);
        for (String configItem : blockSections.keySet()) {
            BlockData data;

            if (configuration.isList(configItem)) {
                if (configItem.equals("drill-block")) {
                    for (int i : configuration.getIntegerList(configItem)) {
                        data = set(i);
                        if (data == null)
                            continue;
                        data.drillable(false);
                    }
                }
                continue;
            }

            if (!configuration.isConfigurationSection(configItem))
                continue;

            int typeId;
            try {
                typeId = Integer.valueOf(configItem);
            } catch (Exception e) {
                MachinaCore.log.warning("MachinaCore: Could not parse block data for id: " + configItem);
                continue;
            }

            data = set(typeId);
            if (data == null) {
                MachinaCore.log.warning("MachinaCore: Given block data id invalid: " + configItem);
                continue;
            }

            ConfigurationSection blockSection = configuration.getConfigurationSection(configItem);

            data.solid(blockSection.getBoolean("solid", data.solid));
            data.drillable(blockSection.getBoolean("drillable", data.drillable));
            String breakTime = blockSection.getString("drillTime");
            if (breakTime != null) {
                if (breakTime.equals("instant")) {
                    data.drillTime(breakTimeInstant);
                } else if (breakTime.equals("netherrack")) {
                    data.drillTime(breakTimeNetherrack);
                } else if (breakTime.equals("fast")) {
                    data.drillTime(breakTimeFast);
                } else if (breakTime.equals("medium")) {
                    data.drillTime(breakTimeMedium);
                } else if (breakTime.equals("slow")) {
                    data.drillTime(breakTimeSlow);
                } else if (breakTime.equals("tough")) {
                    data.drillTime(breakTimeTough);
                } else if (breakTime.equals("obsidian")) {
                    data.drillTime(breakTimeObsidian);
                } else {
                    // It might be an int, so try that instead.
                    data.drillTime(new BreakTime(Math.max(blockSection.getInt("drillTime", 1), 1)));
                }
            }
            data.drop(blockSection.getInt("drop", data.drop));
            data.data((short) blockSection.getInt("data", data.data));
            data.dropMin(blockSection.getInt("dropMin", data.dropMin));
            data.dropRandom(blockSection.getInt("dropRandom", data.dropRandom));
            data.copyData(blockSection.getBoolean("copyData", data.copyData));
            data.inventory(blockSection.getBoolean("hasInventory", data.hasInventory));
            data.attached(blockSection.getBoolean("attached", data.attached));
        }
    }

    static final void loadBreakTimeConfiguration(ConfigurationSection configuration) {
        if (configuration == null)
            return;
        breakTimeNetherrack.time = Math.max(configuration.getInt("netherrack", breakTimeNetherrack.time), 1);
        breakTimeFast.time = Math.max(configuration.getInt("fast", breakTimeFast.time), 1);
        breakTimeMedium.time = Math.max(configuration.getInt("medium", breakTimeMedium.time), 1);
        breakTimeSlow.time = Math.max(configuration.getInt("slow", breakTimeSlow.time), 1);
        breakTimeTough.time = Math.max(configuration.getInt("tough", breakTimeTough.time), 1);
        breakTimeObsidian.time = Math.max(configuration.getInt("obsidian", breakTimeObsidian.time), 1);
    }

    /**
     * Returns true if the given block type is solid.
     * 
     * @param typeId
     *            The block id to check
     * @return True if the block type is solid
     */
    public static final boolean isSolid(final int typeId) {
        try {
            return blockId[typeId].solid;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given block type can be drilled.
     * 
     * @param typeId
     *            The block id to check
     * @return True if the block type can be drilled
     */
    public static final boolean isDrillable(final int typeId) {
        try {
            return blockId[typeId].drillable;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the drill time (in server ticks) for the given block type
     * 
     * @param typeId
     *            The block id to check
     * @return The drill time in server ticks
     */
    public static final int getDrillTime(final int typeId) {
        try {
            return blockId[typeId].drillTime.time;
        } catch (Exception e) {
            return breakTimeInstant.time;
        }
    }

    /**
     * Returns true if the given block type has data to be copied during a move.
     * 
     * @param typeId
     *            The block id to check
     * @return True if the block type has data to be copied.
     */
    public static final boolean copyData(final int typeId) {
        try {
            return blockId[typeId].copyData;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given block type has an inventory.
     * 
     * @param typeId
     *            The block id to check
     * @return True if the block type has an inventory.
     */
    public static final boolean hasInventory(final int typeId) {
        try {
            return blockId[typeId].hasInventory;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns true if the given block type can only exist attached to another
     * block.
     * 
     * @param typeId
     *            The block id to check
     * @return True if the block type is attached.
     */
    public static final boolean isAttached(final int typeId) {
        try {
            return blockId[typeId].attached;
        } catch (Exception e) {
            return false;
        }
    }
}
