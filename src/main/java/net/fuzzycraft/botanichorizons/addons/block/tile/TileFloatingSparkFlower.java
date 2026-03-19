package net.fuzzycraft.botanichorizons.addons.block.tile;

import net.fuzzycraft.botanichorizons.addons.item.ItemBlockSparkFlower;

import net.minecraft.item.ItemStack;

import vazkii.botania.common.block.tile.TileFloatingSpecialFlower;

public class TileFloatingSparkFlower extends TileFloatingSpecialFlower {

	@Override
	public ItemStack getDisplayStack() {
		return ItemBlockSparkFlower.ofType(subTileName);
	}
}