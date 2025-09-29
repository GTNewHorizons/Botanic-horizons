package net.fuzzycraft.botanichorizons.addons.item;

import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.util.BlockBreakHelper;
import net.fuzzycraft.botanichorizons.util.BlockPos;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloExtractor;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloScanner;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;
import java.util.Random;

public class ItemSelectiveWrench extends ItemSuperchargedWrench {
    public static final String ITEM_ID = "elvenDisassemblyWrench";
    public static final int TOOL_MINING_LEVEL = 3;

    public static final String TAG_BLOCK = "block";

    private static final Random dropRandom = new Random();

    public ItemSelectiveWrench(ToolMaterial toolMaterial) {
        super(toolMaterial, TOOL_MINING_LEVEL, ITEM_ID);
    }

    @Override
    public void addInformation(ItemStack heldItem, EntityPlayer player, List<String> tooltips, boolean par4) {
        super.addInformation(heldItem, player, tooltips, par4);
        tooltips.add(I18n.format("botanichorizons.tooltip.disassembly.rightClick"));
        tooltips.add(I18n.format("botanichorizons.tooltip.disassembly.shiftRightClick"));
    }

    @Override
    public boolean onItemUseFirst(ItemStack heldItem, EntityPlayer player, World world, int blockX, int blockY, int blockZ, int side, float hitX, float hitY, float hitZ) {
        if (player.isSneaking()) {
            return selectBlock(heldItem, player, world, blockX, blockY, blockZ);
        } else {
            // Check first if we have an item selected
            NBTTagCompound matchItem = ItemNBTHelper.getCompound(heldItem, TAG_BLOCK, true);
            if (matchItem == null) {
                return true;
            }
            ItemStack blockStack = ItemStack.loadItemStackFromNBT(matchItem);
            if (blockStack == null) {
                FMLLog.warning("Invalid ItemStack detected #1: %s", matchItem.toString());
                return true;
            }
            Item wrappedItem = blockStack.getItem();
            if (!(wrappedItem instanceof ItemBlock)) {
                FMLLog.warning("Invalid ItemStack detected #2: %s", matchItem.toString());
                return true;
            }
            Block scanBlock = ((ItemBlock)wrappedItem).field_150939_a;
            int scanMeta = blockStack.getItemDamage();
            if (scanBlock == null) {
                FMLLog.warning("Invalid ItemStack detected #3: %s", matchItem.toString());
                return true;
            }

            // Decoding done, check the tile entity
            TileEntity blockTileEntity = world.getTileEntity(blockX, blockY, blockZ);
            if (blockTileEntity == null) {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.missingConstruct"));
                return true;
            }

            return onItemUseTileEntity(blockTileEntity, heldItem, player, world, side, scanBlock, scanMeta);
        }
    }

    private <T extends TileEntity> boolean onItemUseTileEntity(T tileEntity, ItemStack heldItem, EntityPlayer player, World world, int side, Block scanBlock, int scanMeta) {
        if (!world.isRemote) {
            HoloScanner scanner = HoloExtractor.scanTileEntity(tileEntity, side);
            if(scanner == null) {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.missingConstruct"));
                return true;
            }

            final int break_capacity = ItemDisassemblyWrench.DISASSEMBLY_PARALLELS[getLevel(heldItem)];
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

                // Reject non-selected blocks
                Block block = world.getBlock(pos.x, pos.y, pos.z);
                if (block != scanBlock) {
                    continue;
                }
                int blockMeta = world.getBlockMetadata(pos.x, pos.y, pos.z);
                if (blockMeta != scanMeta) {
                    continue;
                }

                // Attempt to break
                if (BlockBreakHelper.tryBreakWrenchable(world, player, pos.x, pos.y, pos.z, block, blockMeta, player.posX, player.posY, player.posZ)) {
                    break_capacity_remaining--;
                    continue;
                }
                if (BlockBreakHelper.tryBreakHeldTool(world, player, pos.x, pos.y, pos.z, block, blockMeta, player.posX, player.posY, player.posZ, dropRandom)) {
                    break_capacity_remaining--;
                    continue;
                }

                FMLLog.warning("Can't actually disassemble block: %s:%d", block.getUnlocalizedName(), blockMeta);
            }

            // Item updates
            ItemDisassemblyWrench.resumePos = slot;
            int broken_blocks = break_capacity - break_capacity_remaining;
            damageOrConsumeMana(heldItem, player, broken_blocks, ItemDisassemblyWrench.DISASSEMBLY_MANA);

            if (broken_blocks == 0) {
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.complete"));
            } else { // broken_blocks > 0
                player.addChatMessage(new ChatComponentTranslation("botanichorizons.chat.disassembly.amount", "" + broken_blocks));
            }

            return true;
        } else {
            if (HoloExtractor.isProbablyConstructable(tileEntity)) {
                // return false to allow intercept on server
                return false;
            } else {
                player.addChatMessage(new ChatComponentText(I18n.format("botanichorizons.chat.disassembly.missingConstruct")));
                return true;
            }
        }
    }

    private boolean selectBlock(ItemStack heldItem, EntityPlayer player, World world, int blockX, int blockY, int blockZ) {
        Block selectedBlock = world.getBlock(blockX, blockY, blockZ);
        int selectedMeta = world.getBlockMetadata(blockX, blockY, blockZ);

        if (
                !BlockBreakHelper.isWrenchableBlock(selectedBlock, player) &&
                !BlockBreakHelper.isBlockBreakableWithHeldTool(selectedBlock, selectedMeta, player)
        ) {
            player.addChatMessage(new ChatComponentText(I18n.format("botanichorizons.chat.disassembly.notWrenchable")));
            return true;
        }

        ItemStack selection = new ItemStack(selectedBlock, 1, selectedMeta);
        NBTTagCompound savedItem = new NBTTagCompound();
        selection.writeToNBT(savedItem);
        ItemNBTHelper.setCompound(heldItem, TAG_BLOCK, savedItem);

        return false;
    }
}
