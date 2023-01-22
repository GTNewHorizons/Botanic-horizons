package net.fuzzycraft.botanichorizons.patches;

import net.minecraft.init.Blocks;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;

public class DaisyPatches {
    public static void applyPatches() {
        BotaniaAPI.registerPureDaisyRecipe("stone", ModBlocks.livingrock, 0);
        BotaniaAPI.registerPureDaisyRecipe("logWood", ModBlocks.livingwood, 0);

        BotaniaAPI.registerPureDaisyRecipe("netherrack", Blocks.cobblestone, 0);
        BotaniaAPI.registerPureDaisyRecipe("soulSand", Blocks.sand, 0);
        BotaniaAPI.registerPureDaisyRecipe("ice", Blocks.packed_ice, 0);
        BotaniaAPI.registerPureDaisyRecipe(Blocks.water, Blocks.snow, 0);
    }
}
