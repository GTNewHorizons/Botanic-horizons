package net.fuzzycraft.botanichorizons.patches;

import net.fuzzycraft.botanichorizons.util.OreDict;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.item.ModItems;

public class OredictPatches {
    public static void applyPatches() {
        // fold mushrooms and petals together
        for (int i = 0; i < 16; i++) {
            OreDictionary.registerOre(OreDict.FLOWER_INGREDIENT[i], new ItemStack(ModBlocks.mushroom, 1, i));
            OreDictionary.registerOre(OreDict.FLOWER_INGREDIENT[i], new ItemStack(ModItems.petal, 1, i));
            OreDictionary.registerOre(OreDict.MUSHROOM, new ItemStack(ModBlocks.mushroom, 1, i));
        }

        oredictThirdPartyBlocks("minecraft:yellow_flower", "flowerYellow");
        oredictThirdPartyBlocks("minecraft:yellow_flower", "flowerDandelion");

        oredictThirdPartyBlocks(
                "minecraft:red_flower",
                "flowerRed",
                "flowerLightBlue",
                "flowerMagenta",
                "flowerLightGray",
                "flowerRed",
                "flowerOrange",
                "flowerWhite",
                "flowerPink",
                "flowerLightGray");
        oredictThirdPartyBlocks(
                "minecraft:red_flower",
                "flowerPoppy",
                "flowerBlueOrchid",
                "flowerAllium",
                "flowerAzureBluet",
                "flowerTulipRed",
                "flowerTulipOrange",
                "flowerTulipWhite",
                "flowerTulipPink",
                "flowerOxeyeDaisy");

        oredictThirdPartyBlocks(
                "BiomesOPlenty:flowers",
                "flowerClover",
                "flowerSwamp",
                "flowerDeathbloom",
                "flowerGlowflower",
                "flowerHydrangeaBlue",
                "flowerCosmosOrange",
                "flowerDaffodil",
                "flowerWildflower",
                "flowerViolet",
                "flowerAnemoneWhite",
                "flowerWaterlily",
                "flowerEnderlotus",
                "flowerBromeliad",
                "flowerEyebulb",
                null,
                "flowerDandelionPuff");
        oredictThirdPartyBlocks(
                "BiomesOPlenty:flowers",
                "flowerWhite",
                "flowerCyan",
                "flowerGray",
                "flowerWhite",
                "flowerLightBlue",
                "flowerOrange",
                "flowerPink",
                "flowerPurple",
                "flowerPurple",
                "flowerWhite",
                "flowerLightGray",
                "flowerBlack",
                "flowerOrange",
                "flowerBrown",
                null,
                "flowerLightGray");
        oredictThirdPartyBlocks(
                "BiomesOPlenty:flowers2",
                "flowerHibiscusPink",
                "flowerLilyValley",
                "flowerBurningBlossom",
                "flowerLavender",
                "flowerGoldenrod",
                "flowerBluebell",
                "flowerMinersDelight",
                "flowerIcyIris",
                "flowerRose");
        oredictThirdPartyBlocks(
                "BiomesOPlenty:flowers2",
                "flowerPink",
                "flowerWhite",
                "flowerOrange",
                "flowerPurple",
                "flowerYellow",
                "flowerBlue",
                "flowerBrown",
                "flowerLightBlue",
                "flowerRed");

        oredictThirdPartyBlocks(
                "BiomesOPlenty:mushrooms",
                "listInedibleMushroom",
                "listInedibleMushroom",
                "listInedibleMushroom",
                "listInedibleMushroom",
                "listInedibleMushroom",
                "listInedibleMushroom");

        oredictThirdPartyItem("witchery:ingredient", 14, "itemMutandis");
    }

    public static void oredictThirdPartyBlocks(final String id, final String... names) {
        Block root = Block.getBlockFromName(id);
        Item item = Item.getItemFromBlock(root);
        for (int i = 0; i < names.length; i++) {
            OreDictionary.registerOre(names[i], new ItemStack(root, 1, i));
            OreDictionary.registerOre(names[i], new ItemStack(item, 1, i));
        }
    }

    public static void oredictThirdPartyItem(final String id, final int meta, final String name) {
        Item root = (Item) Item.itemRegistry.getObject(id);
        OreDictionary.registerOre(name, new ItemStack(root, 1, meta));
    }
}
