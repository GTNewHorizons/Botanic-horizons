package net.fuzzycraft.botanichorizons.util.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.item.ItemStack;

public class NEIConfig implements IConfigureNEI {
    @Override
    public String getName() {
        return ForgeMod.MOD_NAME;
    }

    @Override
    public String getVersion() {
        return ForgeMod.VERSION;
    }

    @Override
    public void loadConfig() {
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoPortal), Constants.NEI_RECIPE_CATEGORY_ALFHEIM, -1);
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoPoolInfusion), Constants.NEI_RECIPE_CATEGORY_POOL, -1);
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoPoolAlchemy), Constants.NEI_RECIPE_CATEGORY_POOL, -1);
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoPoolConjuration), Constants.NEI_RECIPE_CATEGORY_POOL, -1);
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoApothecary), Constants.NEI_RECIPE_CATEGORY_APOTHECARY, -1);
        API.addRecipeCatalyst(new ItemStack(BHBlocks.autoAltar), Constants.NEI_RECIPE_CATEGORY_ALTAR, -1);
    }
}

