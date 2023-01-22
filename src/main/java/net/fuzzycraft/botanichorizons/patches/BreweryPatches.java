package net.fuzzycraft.botanichorizons.patches;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.brew.ModBrews;
import vazkii.botania.common.crafting.ModBrewRecipes;
import vazkii.botania.common.item.ModItems;

public class BreweryPatches {
    public static void applyPatches() {
        ModBrewRecipes.speedBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.speed,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.sugar),
                new ItemStack(Items.redstone));
        ModBrewRecipes.strengthBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.strength,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.blaze_powder),
                new ItemStack(Items.glowstone_dust));
        ModBrewRecipes.hasteBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.haste,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.sugar),
                new ItemStack(Items.gold_nugget));
        ModBrewRecipes.healingBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.healing,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.speckled_melon),
                new ItemStack(Items.potato));
        ModBrewRecipes.jumpBoostBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.jumpBoost,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.feather),
                new ItemStack(Items.carrot));
        ModBrewRecipes.regenerationBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.regen,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.ghast_tear),
                new ItemStack(Items.glowstone_dust));
        ModBrewRecipes.weakRegenerationBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.regenWeak,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.ghast_tear),
                new ItemStack(Items.redstone));
        ModBrewRecipes.resistanceBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.resistance,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.iron_ingot),
                new ItemStack(Items.leather));
        ModBrewRecipes.fireResistanceBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.fireResistance,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.magma_cream),
                new ItemStack(Blocks.netherrack));
        ModBrewRecipes.waterBreathingBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.waterBreathing,
                new ItemStack(Items.nether_wart),
                new ItemStack(ModItems.manaResource, 1, 10),
                new ItemStack(Items.glowstone_dust));
        ModBrewRecipes.invisibilityBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.invisibility,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.snowball),
                new ItemStack(Items.glowstone_dust));
        ModBrewRecipes.nightVisionBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.nightVision,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.spider_eye),
                new ItemStack(Items.golden_carrot));
        ModBrewRecipes.absorptionBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.absorption,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.golden_apple),
                new ItemStack(Items.potato));

        ModBrewRecipes.overloadBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.overload,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.blaze_powder),
                new ItemStack(Items.sugar),
                new ItemStack(Items.glowstone_dust),
                new ItemStack(ModItems.manaResource),
                new ItemStack(Items.spider_eye));
        ModBrewRecipes.soulCrossBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.soulCross,
                new ItemStack(Items.nether_wart),
                new ItemStack(Blocks.soul_sand),
                new ItemStack(Items.paper),
                new ItemStack(Items.apple),
                new ItemStack(Items.bone));
        ModBrewRecipes.featherFeetBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.featherfeet,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.feather),
                new ItemStack(Items.leather),
                new ItemStack(Blocks.wool, 1, -1));
        ModBrewRecipes.emptinessBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.emptiness,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.gunpowder),
                new ItemStack(Items.rotten_flesh),
                new ItemStack(Items.bone),
                new ItemStack(Items.string),
                new ItemStack(Items.ender_pearl));
        ModBrewRecipes.bloodthirstBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.bloodthirst,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.fermented_spider_eye),
                new ItemStack(Items.dye, 1, 4),
                new ItemStack(Items.fire_charge),
                new ItemStack(Items.iron_ingot));
        ModBrewRecipes.allureBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.allure,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.fish),
                new ItemStack(Items.quartz),
                new ItemStack(Items.golden_carrot));
        ModBrewRecipes.clearBrew = BotaniaAPI.registerBrewRecipe(
                ModBrews.clear,
                new ItemStack(Items.nether_wart),
                new ItemStack(Items.quartz),
                new ItemStack(Items.emerald),
                new ItemStack(Items.melon));
    }
}
