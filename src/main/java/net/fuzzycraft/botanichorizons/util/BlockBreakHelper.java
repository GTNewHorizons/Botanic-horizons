package net.fuzzycraft.botanichorizons.util;

import cpw.mods.fml.common.FMLLog;
import ic2.api.tile.IWrenchable;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Random;

public class BlockBreakHelper {
    private BlockBreakHelper() {} // static class

    public static boolean isWrenchableBlock(Block block, EntityPlayer player) {
        return (block instanceof IWrenchable && ((IWrenchable) block).wrenchCanRemove(player));
    }

    public static boolean tryBreakWrenchable(World world, EntityPlayer player, int x, int y, int z, Block block, int blockMeta, double dropX, double dropY, double dropZ) {
        if (block instanceof IWrenchable && ((IWrenchable) block).wrenchCanRemove(player)) {
            ItemStack wrenchedStack = ((IWrenchable) block).getWrenchDrop(player);
            block.breakBlock(world, x, y, z, block, blockMeta);
            world.setBlockToAir(x, y, z);
            EntityItem drop = new EntityItem(world, dropX, dropY, dropZ, wrenchedStack);
            world.spawnEntityInWorld(drop);
            return true;
        } else {
            return false;
        }
    }

    public static boolean isBlockBreakableWithHeldTool(Block block, int meta, EntityPlayer player) {
        return block.canHarvestBlock(player, meta);
    }

    public static boolean tryBreakHeldTool(World world, EntityPlayer player, int blockX, int blockY, int blockZ, Block block, int blockMeta, double dropX, double dropY, double dropZ, Random random) {
        if (block.canHarvestBlock(player, blockMeta)) {
            ArrayList<ItemStack> items = block.getDrops(world, blockX, blockY, blockZ, blockMeta, 0);
            float dropChance = ForgeEventFactory.fireBlockHarvesting(items, world, block, blockX, blockY, blockZ, blockMeta, 0, 1.0F, false, player);
            for (ItemStack droppedStack: items) {
                if (random.nextFloat() <= dropChance) {
                    EntityItem drop = new EntityItem(world, dropX, dropY, dropZ, droppedStack);
                    world.spawnEntityInWorld(drop);
                }
            }
            block.breakBlock(world, blockX, blockY, blockZ, block, blockMeta);
            world.setBlockToAir(blockX, blockY, blockZ);

            return true;
        } else {
            return false;
        }
    }
}
