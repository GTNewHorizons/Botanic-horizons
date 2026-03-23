package net.fuzzycraft.botanichorizons.addons.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemBlockFloatingSparkFlower extends ItemBlockSparkFlower {

	public ItemBlockFloatingSparkFlower(Block block1) {
		super(block1);
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String flowerName = getUnlocalizedName(stack) + ".name";
		return String.format(StatCollector.translateToLocal("botaniamisc.floatingPrefix"), StatCollector.translateToLocal(flowerName));
	}

}
