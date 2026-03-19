package net.fuzzycraft.botanichorizons.addons.crafting.recipe;

// Mirrors SpecialFloatingFlowerRecipe

import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.item.ItemBlockSparkFlower;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class SparkFloatingFlowerRecipe implements IRecipe {

	@Override
	public boolean matches(InventoryCrafting var1, World var2) {
		boolean foundFloatingFlower = false;
		boolean foundSparkFlower = false;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null) {
				if(stack.getItem() == Item.getItemFromBlock(BHBlocks.floatingSparkFlower))
					foundFloatingFlower = true;

				else if(stack.getItem() == Item.getItemFromBlock(BHBlocks.sparkFlower))
					foundSparkFlower = true;

				else return false; // Found an invalid item, breaking the recipe
			}
		}

		return foundFloatingFlower && foundSparkFlower;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting var1) {
		ItemStack sparkFlower = null;

		for(int i = 0; i < var1.getSizeInventory(); i++) {
			ItemStack stack = var1.getStackInSlot(i);
			if(stack != null && stack.getItem() == Item.getItemFromBlock(BHBlocks.sparkFlower))
				sparkFlower = stack;
		}

		if(sparkFlower == null)
			return null;

		return ItemBlockSparkFlower.ofType(new ItemStack(BHBlocks.floatingSparkFlower), ItemBlockSparkFlower.getType(sparkFlower));
	}

	@Override
	public int getRecipeSize() {
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput() {
		return null;
	}

}
