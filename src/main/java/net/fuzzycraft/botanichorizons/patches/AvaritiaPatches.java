package net.fuzzycraft.botanichorizons.patches;

import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class AvaritiaPatches {
    public static void applyPatches() {
        ItemStack creativeTablet = new ItemStack(ModItems.manaTablet, 1, 500000);
        ItemManaTablet.setMana(creativeTablet, 500000);
        ItemManaTablet.setStackCreative(creativeTablet);
        ExtremeCraftingManager.getInstance()
                .addExtremeShapedOreRecipe(
                        creativeTablet,
                        // "SSSSSSSSS","IIIIIIIII","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii",
                        "GSISaSISG",
                        "SMMM1MMMS",
                        "IMPD5DPMI",
                        "SMDTTTDMS",
                        "c37TiT84c",
                        "SMDTTTDMS",
                        "IMPD6DPMI",
                        "SMMM2MMMS",
                        "GSISbSISG",
                        'M',
                        new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_MANASTEELBLOCK),
                        'D',
                        new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DIAMONDBLOCK),
                        'P',
                        LibOreDict.MANA_PEARL,
                        'G',
                        LibOreDict.GAIA_INGOT,
                        'S',
                        new ItemStack(ModBlocks.shimmerrock),
                        'I',
                        "plateInfinity",
                        'i',
                        new ItemStack(ModItems.dice), // new ItemStack(ModBlocks.conjurationCatalyst),
                        'T',
                        new ItemStack(ModItems.manaTablet),
                        'a',
                        new ItemStack(ModItems.divaCharm),
                        'b',
                        new ItemStack(ModItems.blackHoleTalisman),
                        'c',
                        new ItemStack(ModItems.laputaShard, 1, 0),
                        '1',
                        LibOreDict.RUNE[0],
                        '2',
                        LibOreDict.RUNE[1],
                        '3',
                        LibOreDict.RUNE[2],
                        '4',
                        LibOreDict.RUNE[3],
                        '5',
                        LibOreDict.RUNE[4],
                        '6',
                        LibOreDict.RUNE[5],
                        '7',
                        LibOreDict.RUNE[6],
                        '8',
                        LibOreDict.RUNE[7]);

        ItemStack creativePool = new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_CREATIVE);
        ExtremeCraftingManager.getInstance()
                .addExtremeShapedOreRecipe(
                        creativePool,
                        // "SSSSSSSSS","IIIIIIIII","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii",
                        "GSISaSISG",
                        "SMMM1MMMS",
                        "IMPD5DPMI",
                        "SMDTTTDMS",
                        "c37TiT84c",
                        "SMDTTTDMS",
                        "IMPD6DPMI",
                        "SMMM2MMMS",
                        "GSISbSISG",
                        'M',
                        new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_MANASTEELBLOCK),
                        'D',
                        new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DIAMONDBLOCK),
                        'P',
                        LibOreDict.MANA_PEARL,
                        'G',
                        LibOreDict.GAIA_INGOT,
                        'S',
                        new ItemStack(ModBlocks.shimmerrock),
                        'I',
                        "plateInfinity",
                        'i',
                        new ItemStack(ModItems.dice), // new ItemStack(ModBlocks.conjurationCatalyst),
                        'T',
                        new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_REGULAR_FABULOUS),
                        'a',
                        new ItemStack(ModItems.missileRod),
                        'b',
                        new ItemStack(ModBlocks.manaBomb),
                        'c',
                        new ItemStack(ModItems.laputaShard, 1, 0),
                        '1',
                        LibOreDict.RUNE[0],
                        '2',
                        LibOreDict.RUNE[1],
                        '3',
                        LibOreDict.RUNE[2],
                        '4',
                        LibOreDict.RUNE[3],
                        '5',
                        LibOreDict.RUNE[4],
                        '6',
                        LibOreDict.RUNE[5],
                        '7',
                        LibOreDict.RUNE[6],
                        '8',
                        LibOreDict.RUNE[7]);
    }
}
