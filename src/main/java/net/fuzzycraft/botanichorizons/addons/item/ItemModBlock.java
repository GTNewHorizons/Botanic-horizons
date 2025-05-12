package net.fuzzycraft.botanichorizons.addons.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.item.block.ItemBlockMod;

// Dummy subclass so that we get correctly namespaced to our own mod
public class ItemModBlock extends ItemBlockMod {
    public ItemModBlock(Block block) {
        super(block);
    }

    public String getUnlocalizedNameInefficiently(ItemStack par1ItemStack) {
        return this.getUnlocalizedNameInefficiently_(par1ItemStack).replaceAll("tile.", "tile.botanichorizons.");
    }
}
