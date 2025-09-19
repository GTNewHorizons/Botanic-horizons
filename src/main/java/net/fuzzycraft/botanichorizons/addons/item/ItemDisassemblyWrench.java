package net.fuzzycraft.botanichorizons.addons.item;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructableProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import cpw.mods.fml.common.FMLLog;
import ic2.api.tile.IWrenchable;
import net.fuzzycraft.botanichorizons.util.BlockPos;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloExtractor;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloProjectorSupport;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloScanner;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ItemDisassemblyWrench extends ItemManaWrench {
    public static final String ITEM_ID = "DisassemblyWrench";
    public static final int TOOL_MINING_LEVEL = 4;

    private static int resumePos = 0;
    private static final Random dropRandom = new Random();

    public ItemDisassemblyWrench(ToolMaterial toolMaterial) {
        super(toolMaterial, TOOL_MINING_LEVEL, ITEM_ID);
    }

    @Override
    public boolean onItemUseFirst(ItemStack heldItem, EntityPlayer player, World world, int blockX, int blockY, int blockZ, int side, float hitX, float hitY, float hitZ) {
        TileEntity blockTileEntity = world.getTileEntity(blockX, blockY, blockZ);
        if (blockTileEntity == null) {
            FMLLog.warning("[Wrench] Block is not a TE");
            return false;
        }
        return onItemUseTileEntity(blockTileEntity, heldItem, player, world, side);
    }

    private <T extends TileEntity> boolean onItemUseTileEntity(T tileEntity, ItemStack heldItem, EntityPlayer player, World world, int side) {
        
        int break_count = 5;

        if (!world.isRemote) {
            FMLLog.warning("Begin break sequence");

            HoloScanner scanner = HoloExtractor.scanTileEntity(tileEntity, side);
            if(scanner == null) {
                FMLLog.warning("No disassembly available for this multi");
                return true;
            }

            for (int i = 0; i < break_count; i++) {
                ItemDisassemblyWrench.resumePos = (ItemDisassemblyWrench.resumePos + 1) % scanner.multiblockLocations.size();
                BlockPos pos = scanner.multiblockLocations.get(ItemDisassemblyWrench.resumePos);

                FMLLog.warning("Processing multiblock at %d, %d, %d (%d/%d)", pos.x, pos.y, pos.z, resumePos, scanner.multiblockLocations.size());

                Block block = world.getBlock(pos.x, pos.y, pos.z);
                int blockMeta = world.getBlockMetadata(pos.x, pos.y, pos.z);

                if (block instanceof IWrenchable && ((IWrenchable) block).wrenchCanRemove(player)) {
                    FMLLog.warning("Wrench break sequence");
                    ItemStack wrenchedStack = ((IWrenchable) block).getWrenchDrop(player);
                    block.breakBlock(world, pos.x, pos.y, pos.z, block, blockMeta);
                    world.setBlockToAir(pos.x, pos.y, pos.z);
                    EntityItem drop = new EntityItem(world, player.posX, player.posY, player.posZ, wrenchedStack);
                    world.spawnEntityInWorld(drop);
                } else if (block.canHarvestBlock(player, blockMeta)) {
                    FMLLog.warning("Harvestable break sequence");
                    ArrayList<ItemStack> items = block.getDrops(world, pos.x, pos.y, pos.z, blockMeta, 0);
                    float dropChance = ForgeEventFactory.fireBlockHarvesting(items, world, block, pos.x, pos.y, pos.z, blockMeta, 0, 1.0F, false, player);
                    for (ItemStack droppedStack: items) {
                        if (dropRandom.nextFloat() <= dropChance) {
                            EntityItem drop = new EntityItem(world, player.posX, player.posY, player.posZ, droppedStack);
                            world.spawnEntityInWorld(drop);
                        }
                    }
                    block.breakBlock(world, pos.x, pos.y, pos.z, block, blockMeta);
                    world.setBlockToAir(pos.x, pos.y, pos.z);
                } else {
                    String interfaces = Arrays
                            .stream(block.getClass().getInterfaces())
                            .map(Class::getName)
                            .reduce("", (a, b) -> b + " " + a);
                    FMLLog.warning("Cannot harvest block: %s (%s)", block.getClass().getName(), interfaces);
                }
            }
            return true;
        } else {
            // return false to allow intercept on server
            return !HoloExtractor.isProbablyConstructable(tileEntity);
        }
    }
}
