package net.fuzzycraft.botanichorizons.patches;

import fox.spiteful.avaritia.blocks.LudicrousBlocks;
import fox.spiteful.avaritia.crafting.ExtremeCraftingManager;
import fox.spiteful.avaritia.items.LudicrousItems;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.item.ItemStack;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ItemManaTablet;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.item.block.ItemBlockSpecialFlower;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibOreDict;

public class AvaritiaPatches {
    public static void applyPatches() {
        ItemStack creativeTablet = new ItemStack(ModItems.manaTablet, 1, 500000);
        ItemManaTablet.setMana(creativeTablet, 500000);
        ItemManaTablet.setStackCreative(creativeTablet);
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                creativeTablet,
                //"SSSSSSSSS","IIIIIIIII","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii",
                "GSISaSISG",
                "SMMM1MMMS",
                "IMPD5DPMI",
                "SMDTTTDMS",
                "c37TiT84c",
                "SMDTTTDMS",
                "IMPD6DPMI",
                "SMMM2MMMS",
                "GSISbSISG",
                'M', new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_MANASTEELBLOCK),
                'D', new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DIAMONDBLOCK),
                'P', LibOreDict.MANA_PEARL,
                'G', LibOreDict.GAIA_INGOT,
                'S', new ItemStack(ModBlocks.shimmerrock),
                'I', "plateInfinity",
                'i', new ItemStack(ModItems.dice), //new ItemStack(ModBlocks.conjurationCatalyst),
                'T', new ItemStack(ModItems.manaTablet),
                'a', new ItemStack(ModItems.divaCharm),
                'b', new ItemStack(ModItems.blackHoleTalisman),
                'c', new ItemStack(ModItems.laputaShard, 1, 0),
                '1', LibOreDict.RUNE[0],
                '2', LibOreDict.RUNE[1],
                '3', LibOreDict.RUNE[2],
                '4', LibOreDict.RUNE[3],
                '5', LibOreDict.RUNE[4],
                '6', LibOreDict.RUNE[5],
                '7', LibOreDict.RUNE[6],
                '8', LibOreDict.RUNE[7]
        );

        ItemStack creativePool = new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_CREATIVE);
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                creativePool,
                //"SSSSSSSSS","IIIIIIIII","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii",
                "GSISaSISG",
                "SMMM1MMMS",
                "IMPD5DPMI",
                "SMDTTTDMS",
                "c37TiT84c",
                "SMDTTTDMS",
                "IMPD6DPMI",
                "SMMM2MMMS",
                "GSISbSISG",
                'M', new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_MANASTEELBLOCK),
                'D', new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DIAMONDBLOCK),
                'P', LibOreDict.MANA_PEARL,
                'G', LibOreDict.GAIA_INGOT,
                'S', new ItemStack(ModBlocks.shimmerrock),
                'I', "plateInfinity",
                'i', new ItemStack(ModItems.dice), //new ItemStack(ModBlocks.conjurationCatalyst),
                'T', new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_REGULAR_FABULOUS),
                'a', new ItemStack(ModItems.missileRod),
                'b', new ItemStack(ModBlocks.manaBomb),
                'c', new ItemStack(ModItems.laputaShard, 1, 0),
                '1', LibOreDict.RUNE[0],
                '2', LibOreDict.RUNE[1],
                '3', LibOreDict.RUNE[2],
                '4', LibOreDict.RUNE[3],
                '5', LibOreDict.RUNE[4],
                '6', LibOreDict.RUNE[5],
                '7', LibOreDict.RUNE[6],
                '8', LibOreDict.RUNE[7]
        );

        ItemStack creativeFlower = ItemBlockSpecialFlower.ofType("Asgardandelion");
        ExtremeCraftingManager.getInstance().addExtremeShapedOreRecipe(
                creativeFlower,
                //"SSSSSSSSS","IIIIIIIII","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii","iiiiiiiii",
                "R0D0B0E0M",
                "000iii000",
                "d0iIIIi0n",
                "00iIFIi00",
                "T0iIIIi0r",
                "010iii010",
                "t1102011H",
                "001121100",
                "e0K131S0m",
                'R', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ARCANE_ROSE),
                'D', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DANDELIFEON),
                'B', ItemBlockSpecialFlower.ofType("Beegonia"),
                'E', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENTROPINNYUM),
                'M', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_MUNCHDEW),
                'd', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_DAYBLOOM),
                'n', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NIGHTSHADE),
                'T', ItemBlockSpecialFlower.ofType("Tainthistle"), 
                'r', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_RAFFLOWSIA),
                't', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_THERMALILY),
                'H', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_HYDROANGEAS),
                'e', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_ENDOFLAME),
                'K', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_KEKIMURUS),
                '3', ItemBlockSpecialFlower.ofType("Soarleander"), 
                'S', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_SPECTROLUS),
                'm', ItemBlockSpecialFlower.ofType(LibBlockNames.SUBTILE_NARSLIMMUS),
                'F', new ItemStack(LudicrousBlocks.infinitato),
                '1', new ItemStack(LudicrousItems.resource, 1, 3), //Neutronium nugget
                '2', "blockCosmicNeutronium",
                'i', "plateInfinity",
                'I', "plateDenseInfinity" 
        );
    }
}
