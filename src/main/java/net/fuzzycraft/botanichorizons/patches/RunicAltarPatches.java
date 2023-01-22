package net.fuzzycraft.botanichorizons.patches;

import java.util.ArrayList;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.OreDict;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.crafting.ModRuneRecipes;
import vazkii.botania.common.crafting.recipe.HeadRecipe;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class RunicAltarPatches {
    public static final int COST_T1 = Constants.POOL_MAX_MANA_DILUTED;
    public static final int COST_T2 = Constants.POOL_MAX_MANA_DILUTED * 3 / 2;
    public static final int COST_T3 = Constants.POOL_MAX_MANA_DILUTED * 3;

    public static final String ingredient_T1 = "plateStainlessSteel";
    public static final String ingredient_T2 = "plateSterlingSilver";
    public static final String ingredient_T3 = "plateTitanium";

    public static void applyPatches() {

        // Tier 1 runes

        ModRuneRecipes.recipeWaterRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 2, 0),
                COST_T1,
                LibOreDict.MANA_POWDER,
                ingredient_T1,
                new ItemStack(Items.dye, 1, 15),
                new ItemStack(Items.reeds),
                new ItemStack(Items.fishing_rod));
        ModRuneRecipes.recipeFireRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 2, 1),
                COST_T1,
                LibOreDict.MANA_POWDER,
                ingredient_T1,
                new ItemStack(Items.netherbrick),
                new ItemStack(Items.gunpowder),
                new ItemStack(Items.nether_wart));

        ModRuneRecipes.recipesEarthRune = new ArrayList();
        ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 2, 2),
                COST_T1,
                LibOreDict.MANA_POWDER,
                ingredient_T1,
                "stone",
                new ItemStack(Blocks.coal_block),
                new ItemStack(Blocks.brown_mushroom)));
        ModRuneRecipes.recipesEarthRune.add(BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 2, 2),
                COST_T1,
                LibOreDict.MANA_POWDER,
                ingredient_T1,
                "stone",
                new ItemStack(Blocks.coal_block),
                new ItemStack(Blocks.red_mushroom)));

        ModRuneRecipes.recipesAirRune = new ArrayList();
        for (int i = 0; i < 16; i++) {
            ModRuneRecipes.recipesAirRune.add(BotaniaAPI.registerRuneAltarRecipe(
                    new ItemStack(ModItems.rune, 2, 3),
                    COST_T1,
                    LibOreDict.MANA_POWDER,
                    ingredient_T1,
                    new ItemStack(Blocks.carpet, 1, i),
                    new ItemStack(Items.feather),
                    new ItemStack(Items.string)));
        }

        // Tier 2 runes

        ModRuneRecipes.recipeSpringRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 4),
                COST_T2,
                ingredient_T2,
                LibOreDict.RUNE[0],
                LibOreDict.RUNE[1],
                "treeSapling",
                "treeSapling",
                "treeSapling",
                new ItemStack(Items.wheat));
        ModRuneRecipes.recipeSummerRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 5),
                COST_T2,
                ingredient_T2,
                LibOreDict.RUNE[2],
                LibOreDict.RUNE[3],
                new ItemStack(Block.getBlockFromName("sand")),
                new ItemStack(Block.getBlockFromName("sand")),
                new ItemStack(Items.slime_ball),
                new ItemStack(Items.melon));
        ModRuneRecipes.recipeAutumnRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 6),
                COST_T2,
                ingredient_T2,
                LibOreDict.RUNE[1],
                LibOreDict.RUNE[3],
                "treeLeaves",
                "treeLeaves",
                "treeLeaves",
                new ItemStack(Items.spider_eye));

        ModRuneRecipes.recipesWinterRune = new ArrayList();
        for (int i = 0; i < 16; i++) {
            ModRuneRecipes.recipesWinterRune.add(BotaniaAPI.registerRuneAltarRecipe(
                    new ItemStack(ModItems.rune, 1, 7),
                    COST_T2,
                    ingredient_T2,
                    LibOreDict.RUNE[0],
                    LibOreDict.RUNE[2],
                    new ItemStack(Blocks.snow),
                    new ItemStack(Blocks.snow),
                    new ItemStack(Blocks.wool, 1, i),
                    new ItemStack(Items.cake)));
        }

        ModRuneRecipes.recipeManaRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 8),
                COST_T2,
                OreDict.MANA_STEEL_PLATE,
                OreDict.MANA_STEEL_PLATE,
                ingredient_T2,
                ingredient_T2,
                "plateThaumium",
                "plateThaumium",
                LibOreDict.MANA_PEARL);

        // Tier 3 runes

        ModRuneRecipes.recipeLustRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 9),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[5],
                LibOreDict.RUNE[3]);
        ModRuneRecipes.recipeGluttonyRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 10),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[7],
                LibOreDict.RUNE[1]);
        ModRuneRecipes.recipeGreedRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 11),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[4],
                LibOreDict.RUNE[0]);
        ModRuneRecipes.recipeSlothRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 12),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[6],
                LibOreDict.RUNE[3]);
        ModRuneRecipes.recipeWrathRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 13),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[7],
                LibOreDict.RUNE[2]);
        ModRuneRecipes.recipeEnvyRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 14),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[7],
                LibOreDict.RUNE[0]);
        ModRuneRecipes.recipePrideRune = BotaniaAPI.registerRuneAltarRecipe(
                new ItemStack(ModItems.rune, 1, 15),
                COST_T3,
                ingredient_T3,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.MANA_DIAMOND,
                LibOreDict.RUNE[5],
                LibOreDict.RUNE[1]);

        // Special recipes

        ModRuneRecipes.recipeHead = new HeadRecipe(
                new ItemStack(Items.skull, 1, 3),
                22500,
                new ItemStack(Items.skull),
                LibOreDict.PIXIE_DUST,
                LibOreDict.PRISMARINE_SHARD,
                new ItemStack(Items.name_tag),
                new ItemStack(Items.golden_apple));
        BotaniaAPI.runeAltarRecipes.add(ModRuneRecipes.recipeHead);
    }
}
