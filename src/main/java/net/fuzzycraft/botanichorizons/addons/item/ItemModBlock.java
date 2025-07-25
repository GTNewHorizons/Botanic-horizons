package net.fuzzycraft.botanichorizons.addons.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.util.IBlockTooltip;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockMod;

import java.util.List;

// Dummy subclass so that we get correctly namespaced to our own mod
public class ItemModBlock extends ItemBlockMod {
    public ItemModBlock(Block block) {
        super(block);
    }

    public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
        return this.getUnlocalizedNameInefficiently_(par1ItemStack).replaceAll("tile.", "tile.botanichorizons.");
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltipStrings, boolean p_77624_4_) {
        super.addInformation(itemStack, player, tooltipStrings, p_77624_4_);
        Item item = itemStack.getItem();
        if (item instanceof ItemBlock) {
            Block block = ((ItemBlock)item).field_150939_a;
            if (block instanceof IBlockTooltip) {
                ((IBlockTooltip)block).addTooltipInformation(itemStack, tooltipStrings);
            }
        }
    }
}
