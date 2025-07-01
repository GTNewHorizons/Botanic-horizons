package net.fuzzycraft.botanichorizons.patches;

import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.crafting.ModElvenTradeRecipes;
import vazkii.botania.common.item.ModItems;

import java.util.ArrayList;

public class AlfheimPatches {
    public static void applyPatches() {
        applyBasicPatches();
        if (ForgeMod.isPackMode()) applyCrossModPatches();
    }

    private static void applyBasicPatches() {
        // These items return themselves instead of getting voided
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.iron_ingot), new ItemStack(Items.iron_ingot));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.iron_block), new ItemStack(Blocks.iron_block));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.ender_pearl), new ItemStack(Items.ender_pearl));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Items.diamond), new ItemStack(Items.diamond));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(Blocks.diamond_block), new ItemStack(Blocks.diamond_block));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource,1, Constants.MANARESOURCE_META_DIAMOND), new ItemStack(ModItems.manaResource,1, Constants.MANARESOURCE_META_DIAMOND));
        BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource,1, Constants.MANARESOURCE_META_MANASTEEL), new ItemStack(ModItems.manaResource,1, Constants.MANARESOURCE_META_MANASTEEL));

        // Basic alfheim recipes - needs to be at least one for subset testing
        ModElvenTradeRecipes.elvenQuartzRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.quartz, 1, 5), new ItemStack(Items.quartz));
    }

    private static void applyCrossModPatches() {
        ModElvenTradeRecipes.dreamwoodRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.dreamwood), Constants.gtTradeCasing());

        ModElvenTradeRecipes.elementiumRecipes = new ArrayList();
        ModElvenTradeRecipes.elementiumRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 7), "gearGtSmallTPVAlloy"));

        ModElvenTradeRecipes.pixieDustRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 8), "craftingSunnariumPart");

        ModElvenTradeRecipes.dragonstoneRecipes = new ArrayList();
        for (ItemStack dict: OreDictionary.getOres("circuitElite")) {
            ModElvenTradeRecipes.dragonstoneRecipes.add(BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModItems.manaResource, 1, 9), dict));
        }

        ModElvenTradeRecipes.alfglassRecipe = BotaniaAPI.registerElvenTradeRecipe(new ItemStack(ModBlocks.elfGlass), "blockGlassEV");
    }
}
