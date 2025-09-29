package net.fuzzycraft.botanichorizons.addons.item;

import net.fuzzycraft.botanichorizons.util.BlockBreakHelper;
import net.fuzzycraft.botanichorizons.util.BlockPos;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloExtractor;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloScanner;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class ItemDisassemblyWrench extends ItemSuperchargedWrench {
    public static final String ITEM_ID = "terrasteelDisassemblyWrench";
    public static final int TOOL_MINING_LEVEL = 4;

    //      D  C  B  A   S    SS
    public static final int[] DISASSEMBLY_PARALLELS = new int[] {
            1, 1, 5, 25, 125, 625
    };
    public static final int DISASSEMBLY_MANA = 150;

    // Speed cache
    public static int resumePos = 0;
    private static final Random dropRandom = new Random();

    public ItemDisassemblyWrench(ToolMaterial toolMaterial) {
        super(toolMaterial, TOOL_MINING_LEVEL, ITEM_ID);
    }

    @Override
    public void addInformation(ItemStack heldItem, EntityPlayer player, List<String> tooltips, boolean par4) {
        super.addInformation(heldItem, player, tooltips, par4);
        tooltips.add(I18n.format("botanichorizons.tooltip.disassembly.rightClick"));
    }

    @Override
    public boolean onItemUseFirst(ItemStack heldItem, EntityPlayer player, World world, int blockX, int blockY, int blockZ, int side, float hitX, float hitY, float hitZ) {
        TileEntity blockTileEntity = world.getTileEntity(blockX, blockY, blockZ);
        if (blockTileEntity == null) {
            player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.missingConstruct"));
            return true;
        }
        return onItemUseTileEntity(blockTileEntity, heldItem, player, world, side);
    }

    private <T extends TileEntity> boolean onItemUseTileEntity(T tileEntity, ItemStack heldItem, EntityPlayer player, World world, int side) {

        int fail_count = 0;

        if (!world.isRemote) {
            HoloScanner scanner = HoloExtractor.scanTileEntity(tileEntity, side);
            if(scanner == null) {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.missingConstruct"));
                return true;
            }

            final int break_capacity = DISASSEMBLY_PARALLELS[getLevel(heldItem)];
            final int startPos = ItemDisassemblyWrench.resumePos % scanner.multiblockLocations.size();
            int slot = startPos;
            int break_capacity_remaining = break_capacity;
            while (break_capacity_remaining > 0) {
                slot = (slot + 1) % scanner.multiblockLocations.size();
                BlockPos pos = scanner.multiblockLocations.get(slot);
                if (slot == startPos) {
                    // No more blocks to break
                    break;
                }

                Block block = world.getBlock(pos.x, pos.y, pos.z);
                int blockMeta = world.getBlockMetadata(pos.x, pos.y, pos.z);

                if (block == Blocks.air) {
                    continue;
                }
                if (BlockBreakHelper.tryBreakWrenchable(world, player, pos.x, pos.y, pos.z, block, blockMeta, player.posX, player.posY, player.posZ)) {
                    break_capacity_remaining--;
                    continue;
                }
                if (BlockBreakHelper.tryBreakHeldTool(world, player, pos.x, pos.y, pos.z, block, blockMeta, player.posX, player.posY, player.posZ, dropRandom)) {
                    break_capacity_remaining--;
                    continue;
                }

                // Cannot break this block
                fail_count++;
            }

            // Item updates
            resumePos = slot;
            int broken_blocks = break_capacity - break_capacity_remaining;
            damageOrConsumeMana(heldItem, player, broken_blocks, DISASSEMBLY_MANA);

            if (broken_blocks == 0 && fail_count == 0) {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.complete"));
            } else if (broken_blocks == 0) { // fail count > 0
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.obstructed"));
            } else { // broken_blocks > 0
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.amount", "" + broken_blocks));
            }

            return true;
        } else {
            if (HoloExtractor.isProbablyConstructable(tileEntity)) {
                // return false to allow intercept on server
                return false;
            } else {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.missingConstruct"));
                return true;
            }
        }
    }
}
