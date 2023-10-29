package net.fuzzycraft.botanichorizons.patches;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import vazkii.botania.common.block.ModBlocks;

public class ThaumcraftAspects {

    public static final AspectList FLOWER = new AspectList().add(Aspect.PLANT, 4).add(Aspect.SENSES, 4).add(Aspect.MAGIC, 4);

    public static void registerAspects() {
        for (int i = 0; i < 16; i++) {
            ThaumcraftApi.registerObjectTag(new ItemStack(ModBlocks.flower, 1, i), FLOWER);
        }
    }
}
