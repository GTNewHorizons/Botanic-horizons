package net.fuzzycraft.botanichorizons.patches;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ToolDictNames;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.OreDict;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.block.tile.TileCraftCrate;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.item.ItemSignalFlare;
import vazkii.botania.common.item.ItemTwigWand;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

public class CraftingPatches {
    public static void applyPatches() {

        // Lexicon Recipe
        addShapelessOreDictRecipe(new ItemStack(ModItems.lexicon), OreDict.MUSHROOM, Items.book);
        ModCraftingRecipes.recipeLexicon = BotaniaAPI.getLatestAddedRecipe();

        // Petal/Dye Recipes
        // Has 2x yield in extractor
        for (int i = 0; i < 16; i++)
            addShapelessOreDictRecipe(new ItemStack(ModItems.petal, 1, i), LibOreDict.FLOWER[i]);
        ModCraftingRecipes.recipesPetals = BotaniaAPI.getLatestAddedRecipes(16);

        // Wand of the Forest Recipes
        for (int i = 0; i < 16; i++)
            for (int j = 0; j < 16; j++) {
                addOreDictRecipe(
                        ItemTwigWand.forColors(i, j),
                        " AS",
                        " SB",
                        "S  ",
                        'A',
                        OreDict.FLOWER_INGREDIENT[i],
                        'B',
                        OreDict.FLOWER_INGREDIENT[j],
                        'S',
                        LibOreDict.LIVINGWOOD_TWIG);
            }
        ModCraftingRecipes.recipesTwigWand = BotaniaAPI.getLatestAddedRecipes(256);

        // Petal Apothecary Recipes
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(
                    new ItemStack(ModBlocks.altar),
                    "SPS",
                    "XUX",
                    "CTC",
                    'S',
                    "plateSteel",
                    'P',
                    OreDict.FLOWER_INGREDIENT[i],
                    'U',
                    new ItemStack(Items.cauldron),
                    'C',
                    "stone",
                    'T',
                    "blockSilver",
                    'X',
                    "screwSteel");

        ModCraftingRecipes.recipesApothecary = BotaniaAPI.getLatestAddedRecipes(16);

        // Metamorphic Petal Apothecary Recipes
        for (int i = 0; i < 8; i++)
            GameRegistry.addRecipe(
                    new ItemStack(ModBlocks.altar, 1, i + 1),
                    "SSS",
                    "SAS",
                    "SSS",
                    'S',
                    new ItemStack(ModFluffBlocks.biomeStoneA, 1, i + 8),
                    'A',
                    new ItemStack(ModBlocks.altar));
        ModCraftingRecipes.recipesAltarMeta = BotaniaAPI.getLatestAddedRecipes(8);

        // Petal block extraction -- see also GT extractor version
        for (int i = 0; i < 16; i++) {
            addShapelessRecipe(new ItemStack(ModItems.petal, 9, i), new ItemStack(ModBlocks.petalBlock, 1, i));
        }
        ModCraftingRecipes.recipesPetals = BotaniaAPI.getLatestAddedRecipes(16);

        // floral fertiliser using witchery, and a less burdensome version using T2 planet dust
        addShapelessOreDictRecipe(new ItemStack(ModItems.fertilizer), "itemMutandis", "dustBone"); // MV
        addShapelessOreDictRecipe(new ItemStack(ModItems.fertilizer), "dustDraconium", "dustBone"); // EV
        ModCraftingRecipes.recipeFertilizerPowder = BotaniaAPI.getLatestAddedRecipe(); // book can hold only one

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Dilute pool
        addOreDictRecipe(
                new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_DILUTE),
                "P P",
                "RRR",
                'R',
                new ItemStack(ModFluffBlocks.livingrockSlab),
                'P',
                "plateLivingrock");
        ModCraftingRecipes.recipePoolDiluted = BotaniaAPI.getLatestAddedRecipe();

        // Regular pool
        addOreDictRecipe(
                new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_REGULAR),
                "RIR",
                "RCR",
                "RRR",
                'R',
                new ItemStack(ModFluffBlocks.livingrockSlab),
                'I',
                OreDict.MANA_STEEL_PLATE,
                'C',
                Constants.thaumcraftCrucible());
        ModCraftingRecipes.recipePool = BotaniaAPI.getLatestAddedRecipe();

        // Fancy pool (same stats as regular)
        addOreDictRecipe(
                new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_REGULAR_FABULOUS),
                "RIR",
                "RCR",
                "RRR",
                'R',
                new ItemStack(ModFluffBlocks.shimmerrockSlab),
                'I',
                OreDict.DENSE_MANA_STEEL_PLATE,
                'C',
                Constants.thaumcraftCrucible());
        ModCraftingRecipes.recipePoolFabulous = BotaniaAPI.getLatestAddedRecipe();

        // There is no elven mana pool in this version?

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // regular spreader
        for (int i = 0; i < 16; i++) {
            addOreDictRecipe(
                    new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_REGULAR),
                    "WWW",
                    "WPR",
                    "WWW",
                    'W',
                    new ItemStack(ModFluffBlocks.livingwoodSlab),
                    'P',
                    OreDict.FLOWER_INGREDIENT[i],
                    'R',
                    "ringGold");
        }
        ModCraftingRecipes.recipesSpreader = BotaniaAPI.getLatestAddedRecipes(16);

        // Elven spreader
        for (int i = 0; i < 16; i++) {
            addOreDictRecipe(
                    new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_ALFHEIM),
                    "WWW",
                    "SPR",
                    "WWW",
                    'W',
                    new ItemStack(ModFluffBlocks.dreamwoodSlab),
                    'S',
                    new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_REGULAR),
                    'P',
                    OreDict.FLOWER_INGREDIENT[i],
                    'R',
                    "gemJade");
        }
        ModCraftingRecipes.recipesDreamwoodSpreader = BotaniaAPI.getLatestAddedRecipes(16);

        // gaia spreader
        addOreDictRecipe(
                new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_GAIA),
                "WWW",
                "SPR",
                "WWW",
                'W',
                new ItemStack(ModFluffBlocks.dreamwoodSlab),
                'S',
                new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_ALFHEIM),
                'P',
                LibOreDict.GAIA_INGOT,
                'R',
                "gemOpal");
        ModCraftingRecipes.recipeUltraSpreader = BotaniaAPI.getLatestAddedRecipe();

        // pulse spreader
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_PULSE),
                new ItemStack(ModBlocks.spreader, 1, Constants.SPREADER_META_REGULAR),
                LibOreDict.REDSTONE_ROOT,
                new ItemStack(Items.repeater));
        ModCraftingRecipes.recipeRedstoneSpreader = BotaniaAPI.getLatestAddedRecipe();

        // Mana Distributor
        addOreDictRecipe(
                new ItemStack(ModBlocks.distributor),
                "RGR",
                "GSG",
                "RGR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                "plateSilver",
                'G',
                new ItemStack(ModBlocks.manaGlass));
        ModCraftingRecipes.recipeDistributor = BotaniaAPI.getLatestAddedRecipe();

        // Mana Void Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.manaVoid),
                "SGS",
                "GOG",
                "SGS",
                'S',
                LibOreDict.LIVING_ROCK,
                'O',
                new ItemStack(Blocks.obsidian),
                'G',
                new ItemStack(ModBlocks.manaGlass));
        ModCraftingRecipes.recipeManaVoid = BotaniaAPI.getLatestAddedRecipe();

        // Spreader Turntable Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.turntable),
                "GWG",
                "WPW",
                "GWG",
                'W',
                LibOreDict.LIVING_WOOD,
                'P',
                Blocks.sticky_piston,
                'G',
                "gearWood");
        ModCraftingRecipes.recipeTurntable = BotaniaAPI.getLatestAddedRecipe();

        // Mana Detector Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.manaDetector),
                "RSR",
                "SCS",
                "RSR",
                'R',
                "dustRedstone",
                'C',
                new ItemStack(Items.comparator),
                'S',
                LibOreDict.LIVING_ROCK);
        ModCraftingRecipes.recipeManaDetector = BotaniaAPI.getLatestAddedRecipe();

        // Mana Mirror Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.manaMirror),
                " PR",
                " SI",
                "T  ",
                'P',
                LibOreDict.MANA_PEARL,
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.LIVINGWOOD_TWIG,
                'I',
                OreDict.TERRA_STEEL_PLATE,
                'T',
                new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE));
        ModCraftingRecipes.recipeManaMirror = BotaniaAPI.getLatestAddedRecipe();

        // Mana Tablet Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.manaTablet, 1, Constants.MANA_TABLET_MAGIC_META),
                "SSS",
                "SPS",
                "SSS",
                'S',
                LibOreDict.LIVING_ROCK,
                'P',
                LibOreDict.MANA_PEARL);
        addOreDictRecipe(
                new ItemStack(ModItems.manaTablet, 1, Constants.MANA_TABLET_MAGIC_META),
                "SSS",
                "SDS",
                "SSS",
                'S',
                LibOreDict.LIVING_ROCK,
                'D',
                LibOreDict.MANA_DIAMOND);
        ModCraftingRecipes.recipesManaTablet = BotaniaAPI.getLatestAddedRecipes(2);

        // Mana Pump Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.pump),
                "SSS",
                "IBI",
                "SSS",
                'S',
                LibOreDict.LIVING_ROCK,
                'I',
                OreDict.MANA_STEEL_PLATE,
                'B',
                new ItemStack(Items.bucket));
        ModCraftingRecipes.recipePump = BotaniaAPI.getLatestAddedRecipe();

        // Manastorm Charge Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.manaBomb),
                "LTL",
                "TGT",
                "LTL",
                'L',
                LibOreDict.LIVING_WOOD,
                'T',
                new ItemStack(Blocks.tnt),
                'G',
                LibOreDict.LIFE_ESSENCE);
        ModCraftingRecipes.recipeManaBomb = BotaniaAPI.getLatestAddedRecipe();

        // Manatide Bellows Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.bellows),
                "SSS",
                "RL ",
                "SSS",
                'S',
                new ItemStack(ModFluffBlocks.livingwoodSlab),
                'R',
                LibOreDict.RUNE[3],
                'L',
                new ItemStack(Items.leather));
        ModCraftingRecipes.recipeBellows = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Base blocks

        // Living- and dreamstuff chisel recipes
        addOreDictRecipe(
                new ItemStack(ModBlocks.livingwood, 4, Constants.LIVINGWOOD_META_FRAMED),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_PLANK));
        ModCraftingRecipes.recipeLivingwoodDecor3 = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModBlocks.livingwood, 4, Constants.LIVINGWOOD_META_PATTERNED),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_FRAMED));
        ModCraftingRecipes.recipeLivingwoodDecor4 = BotaniaAPI.getLatestAddedRecipe();
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_MOSSY),
                "cropVine",
                new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_PLANK));
        ModCraftingRecipes.recipeLivingwoodDecor2 = BotaniaAPI.getLatestAddedRecipe();

        addOreDictRecipe(
                new ItemStack(ModBlocks.dreamwood, 4, Constants.LIVINGWOOD_META_FRAMED),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK));
        addOreDictRecipe(
                new ItemStack(ModBlocks.dreamwood, 4, Constants.LIVINGWOOD_META_PATTERNED),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_FRAMED));
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_MOSSY),
                "cropVine",
                new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK));
        // no lexicon for fancy dreamwood

        addOreDictRecipe(
                new ItemStack(ModBlocks.livingrock, 4, Constants.LIVINGSTONE_META_BRICK),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BLOCK));
        ModCraftingRecipes.recipeLivingrockDecor1 = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModBlocks.livingrock, 4, Constants.LIVINGSTONE_META_CHISELED),
                "WW",
                "WW",
                'W',
                new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BRICK));
        ModCraftingRecipes.recipeLivingrockDecor4 = BotaniaAPI.getLatestAddedRecipe();
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_MOSSY),
                "cropVine",
                new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BRICK));
        ModCraftingRecipes.recipeLivingrockDecor2 = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Lenses

        // Mana lens and lenses
        addOreDictRecipe(
                new ItemStack(ModItems.lens),
                "SRS",
                "RGR",
                "SRS",
                'S',
                "screwManasteel",
                'R',
                "ringManasteel",
                'G',
                new ItemStack(ModBlocks.manaGlass));
        ModCraftingRecipes.recipesManaLens = BotaniaAPI.getLatestAddedRecipes(1);

        addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 1), new ItemStack(ModItems.lens), LibOreDict.RUNE[3]);
        ModCraftingRecipes.recipeLensVelocity = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 2), new ItemStack(ModItems.lens), LibOreDict.RUNE[1]);
        ModCraftingRecipes.recipeLensPotency = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 3), new ItemStack(ModItems.lens), LibOreDict.RUNE[2]);
        ModCraftingRecipes.recipeLensResistance = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 4), new ItemStack(ModItems.lens), LibOreDict.RUNE[0]);
        ModCraftingRecipes.recipeLensEfficiency = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 5), new ItemStack(ModItems.lens), "slimeball", "ingotAnyRubber");
        ModCraftingRecipes.recipeLensBounce = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 6),
                new ItemStack(ModItems.lens),
                "blockObsidian",
                "compressedCobblestone1x");
        ModCraftingRecipes.recipeLensGravity = BotaniaAPI.getLatestAddedRecipe();

        if (Loader.isModLoaded("dreamcraft")) {
            addOreDictRecipe(
                    new ItemStack(ModItems.lens, 1, 7),
                    " P ",
                    "ALA",
                    " R ",
                    'P',
                    Item.itemRegistry.getObject("dreamcraft:item.DiamondDrillTip"),
                    'R',
                    "dustRedstone",
                    'A',
                    "gemLapis",
                    'L',
                    new ItemStack(ModItems.lens));
            ModCraftingRecipes.recipeLensBore = BotaniaAPI.getLatestAddedRecipe();
        }

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 8), new ItemStack(ModItems.lens), new ItemStack(Items.diamond_sword));
        ModCraftingRecipes.recipeLensDamaging = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 9), new ItemStack(ModItems.lens), new ItemStack(ModBlocks.platform));
        ModCraftingRecipes.recipeLensPhantom = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 10),
                new ItemStack(ModItems.lens),
                "stickIronMagnetic",
                "stickSteelMagnetic");
        ModCraftingRecipes.recipeLensMagnet = BotaniaAPI.getLatestAddedRecipe();

        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 11), new ItemStack(ModItems.lens), LibOreDict.RUNE[14]);
        ModCraftingRecipes.recipeLensExplosive = BotaniaAPI.getLatestAddedRecipe();

        // Influence Lens Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 12),
                " P ",
                "PLP",
                "PPP",
                'P',
                LibOreDict.PRISMARINE_SHARD,
                'L',
                new ItemStack(ModItems.lens));
        ModCraftingRecipes.recipeLensInfluence = BotaniaAPI.getLatestAddedRecipe();

        // Weight Lens Recipe
        addShapelessOreDictRecipe(new ItemStack(ModItems.lens, 1, 13), new ItemStack(ModItems.lens), "ingotBedrockium");
        ModCraftingRecipes.recipeLensWeight = BotaniaAPI.getLatestAddedRecipe();

        // Paintslinger Lens Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 14),
                " I ",
                "WLW",
                " I ",
                'I',
                OreDict.MANA_STEEL_PLATE,
                'W',
                new ItemStack(Blocks.wool, 1, Short.MAX_VALUE),
                'L',
                new ItemStack(ModItems.lens));
        ModCraftingRecipes.recipeLensPaint = BotaniaAPI.getLatestAddedRecipe();

        // Warp Lens Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 18), new ItemStack(ModItems.lens), LibOreDict.PIXIE_DUST);
        ModCraftingRecipes.recipeLensWarp = BotaniaAPI.getLatestAddedRecipe();

        // Redirective Lens Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 19),
                new ItemStack(ModItems.lens),
                LibOreDict.LIVING_WOOD,
                OreDict.ELEMENTIUM_PLATE);
        ModCraftingRecipes.recipeLensRedirect = BotaniaAPI.getLatestAddedRecipe();

        // Celebratory Lens Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 20),
                new ItemStack(ModItems.lens),
                new ItemStack(Items.fireworks),
                OreDict.ELEMENTIUM_PLATE);
        ModCraftingRecipes.recipeLensFirework = BotaniaAPI.getLatestAddedRecipe();

        // Flare Lens Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 21),
                new ItemStack(ModItems.lens),
                new ItemStack(ModBlocks.elfGlass),
                OreDict.ELEMENTIUM_PLATE);
        ModCraftingRecipes.recipeLensFlare = BotaniaAPI.getLatestAddedRecipe();

        // Mana Lens: Kindle Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 15), new ItemStack(ModItems.lens), new ItemStack(Items.fire_charge));
        ModCraftingRecipes.recipeLensFire = BotaniaAPI.getLatestAddedRecipe();

        // Mana Lens: Piston Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 16),
                new ItemStack(ModItems.lens),
                new ItemStack(ModBlocks.pistonRelay));
        ModCraftingRecipes.recipeLensPiston = BotaniaAPI.getLatestAddedRecipe();

        // Mana Lens: Flash Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 17),
                "GFG",
                "FLF",
                "GFG",
                'G',
                "glowstone",
                'F',
                new ItemStack(Items.fire_charge),
                'L',
                new ItemStack(ModItems.lens));
        addOreDictRecipe(
                new ItemStack(ModItems.lens, 1, 17),
                "FGF",
                "GLG",
                "FGF",
                'G',
                "glowstone",
                'F',
                new ItemStack(Items.fire_charge),
                'L',
                new ItemStack(ModItems.lens));
        ModCraftingRecipes.recipesLensFlash = BotaniaAPI.getLatestAddedRecipes(2);

        // Mana Prism Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.prism),
                "GPG",
                "GSG",
                "GPG",
                'G',
                new ItemStack(ModBlocks.manaGlass),
                'P',
                LibOreDict.PRISMARINE_SHARD,
                'S',
                new ItemStack(ModBlocks.platform, 1, 1));
        ModCraftingRecipes.recipePrism = BotaniaAPI.getLatestAddedRecipe();

        // Lens Clip Recipe
        addOreDictRecipe(new ItemStack(ModItems.clip), " D ", "D D", "DD ", 'D', LibOreDict.DREAM_WOOD);
        ModCraftingRecipes.recipeClip = BotaniaAPI.getLatestAddedRecipe();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Rods and wands

        // Rod of the Lands Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.dirtRod),
                " SD",
                "STS",
                "ES ",
                'D',
                new ItemStack(Blocks.dirt),
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'E',
                LibOreDict.RUNE[2],
                'S',
                "screwTitanium");
        ModCraftingRecipes.recipeDirtRod = BotaniaAPI.getLatestAddedRecipe();

        // Terra Firma Rod Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.terraformRod),
                "sWT",
                "ARS",
                "GMs",
                'T',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                new ItemStack(ModItems.dirtRod),
                'G',
                new ItemStack(ModItems.grassSeeds),
                'W',
                LibOreDict.RUNE[7],
                'S',
                LibOreDict.RUNE[4],
                'M',
                LibOreDict.RUNE[5],
                'A',
                LibOreDict.RUNE[6],
                's',
                "screwNaquadahAlloy");
        ModCraftingRecipes.recipeTerraformRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Seas Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.waterRod),
                " sB",
                "sTs",
                "Rs ",
                'B',
                new ItemStack(Items.potionitem),
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'R',
                LibOreDict.RUNE[0],
                's',
                "screwStainlessSteel");
        ModCraftingRecipes.recipeWaterRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of Bifrost Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.rainbowRod),
                " PD",
                "sEP",
                "Es ",
                'P',
                LibOreDict.PIXIE_DUST,
                'E',
                "stickElvenElementium",
                'D',
                LibOreDict.DRAGONSTONE,
                's',
                "screwTitanium");
        ModCraftingRecipes.recipeRainbowRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Skies Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.tornadoRod),
                " sF",
                "sTs",
                "Rs ",
                'F',
                new ItemStack(Items.feather),
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'R',
                LibOreDict.RUNE[3],
                's',
                "screwStainlessSteel");
        ModCraftingRecipes.recipeTornadoRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Hells Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.fireRod),
                " sF",
                "sTs",
                "Rs ",
                'F',
                new ItemStack(Items.blaze_powder),
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'R',
                LibOreDict.RUNE[1],
                's',
                "screwTitanium");
        ModCraftingRecipes.recipeFireRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Highlands Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.skyDirtRod),
                new ItemStack(ModItems.dirtRod),
                LibOreDict.PIXIE_DUST,
                LibOreDict.RUNE[3]);
        ModCraftingRecipes.recipeSkyDirtRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Unstable Reservoir
        addOreDictRecipe(
                new ItemStack(ModItems.missileRod),
                "GDD",
                "sTD",
                "TsG",
                'G',
                LibOreDict.LIFE_ESSENCE,
                'D',
                LibOreDict.DRAGONSTONE,
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                's',
                "screwNaquadah");
        ModCraftingRecipes.recipeMissileRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Depths Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.cobbleRod),
                " FC",
                "sTW",
                "Ts ",
                'F',
                LibOreDict.RUNE[1],
                'W',
                LibOreDict.RUNE[0],
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'C',
                "cobblestone",
                's',
                "screwStainlessSteel");
        ModCraftingRecipes.recipeCobbleRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Molten Core Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.smeltRod),
                " BF",
                "sTB",
                "Ts ",
                'B',
                new ItemStack(Items.blaze_rod),
                'F',
                LibOreDict.RUNE[1],
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                's',
                "screwStainlessSteel");
        ModCraftingRecipes.recipeSmeltRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Plentiful Mantle
        addOreDictRecipe(
                new ItemStack(ModItems.diviningRod),
                " TD",
                "sTT",
                "Ts ",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'D',
                LibOreDict.MANA_PEARL,
                's',
                "screwAluminium");
        ModCraftingRecipes.recipeDiviningRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Black Mesa Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.gravityRod),
                " TD",
                "sWT",
                "Ts ",
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                'W',
                "cropWheat",
                'D',
                LibOreDict.DRAGONSTONE,
                's',
                "screwTungstenSteel");
        ModCraftingRecipes.recipeGravityRod = BotaniaAPI.getLatestAddedRecipe();

        // Rod of the Shifting Crust Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.exchangeRod),
                " sR",
                "sTs",
                "Ts ",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                LibOreDict.MANA_PEARL,
                'R',
                LibOreDict.RUNE[12],
                's',
                "screwTitanium");
        ModCraftingRecipes.recipeExchangeRod = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Standard armour and tools

        // Mana Armor & Tools Recipes
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelHelm),
                "SSS",
                "ShS",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeManasteelHelm = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelChest),
                "ShS",
                "SSS",
                "SSS",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeManasteelChest = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelLegs),
                "SSS",
                "ShS",
                "S S",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeManasteelLegs = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelBoots),
                "ShS",
                "S S",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeManasteelBoots = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelPick),
                "SSS",
                "fTh",
                " T ",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeManasteelPick = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelShovel),
                " S ",
                "fTh",
                " T ",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeManasteelShovel = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelAxe),
                "SSh",
                "ST ",
                "fT ",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeManasteelAxe = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelSword),
                " S ",
                "fSh",
                " T ",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeManasteelSword = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.manasteelShears),
                "hS",
                "Sf",
                'S',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeManasteelShears = BotaniaAPI.getLatestAddedRecipe();

        // Elementium Armor & Tools Recipes
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumHelm),
                "SSS",
                "ShS",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeElementiumHelm = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumChest),
                "ShS",
                "SSS",
                "SSS",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeElementiumChest = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumLegs),
                "SSS",
                "ShS",
                "S S",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeElementiumLegs = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumBoots),
                "ShS",
                "S S",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeElementiumBoots = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumPick),
                "SSS",
                "fTh",
                " T ",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeElementiumPick = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumShovel),
                " S ",
                "fTh",
                " T ",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeElementiumShovel = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumAxe),
                "SSh",
                "ST ",
                "fT ",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeElementiumAxe = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumSword),
                " S ",
                "fSh",
                " T ",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'T',
                LibOreDict.DREAMWOOD_TWIG,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeElementiumSword = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.elementiumShears),
                "hS",
                "Sf",
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeElementiumShears = BotaniaAPI.getLatestAddedRecipe();

        // Terrasteel Armor Recipes
        addOreDictRecipe(
                new ItemStack(ModItems.terrasteelHelmRevealing),
                "TRT",
                "SAS",
                "fSh",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[4],
                'A',
                new ItemStack(ModItems.manasteelHelmRevealing),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        addOreDictRecipe(
                new ItemStack(ModItems.terrasteelHelm),
                "TRT",
                "SAS",
                "fSh",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[4],
                'A',
                new ItemStack(ModItems.manasteelHelm),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeTerrasteelHelm = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.terrasteelChest),
                "TRT",
                "SAS",
                "fSh",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[5],
                'A',
                new ItemStack(ModItems.manasteelChest),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeTerrasteelChest = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.terrasteelLegs),
                "TRT",
                "SAS",
                "fSh",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[6],
                'A',
                new ItemStack(ModItems.manasteelLegs),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeTerrasteelLegs = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(
                new ItemStack(ModItems.terrasteelBoots),
                "TRT",
                "SAS",
                "fSh",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                OreDict.TERRA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[7],
                'A',
                new ItemStack(ModItems.manasteelBoots),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeTerrasteelBoots = BotaniaAPI.getLatestAddedRecipe();

        // Manaweave Armor Recipes
        addOreDictRecipe(new ItemStack(ModItems.manaweaveHelm), "SSS", "S S", 'S', LibOreDict.MANAWEAVE_CLOTH);
        ModCraftingRecipes.recipeManaweaveHelm = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(new ItemStack(ModItems.manaweaveChest), "S S", "SSS", "SSS", 'S', LibOreDict.MANAWEAVE_CLOTH);
        ModCraftingRecipes.recipeManaweaveChest = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(new ItemStack(ModItems.manaweaveLegs), "SSS", "S S", "S S", 'S', LibOreDict.MANAWEAVE_CLOTH);
        ModCraftingRecipes.recipeManaweaveLegs = BotaniaAPI.getLatestAddedRecipe();
        addOreDictRecipe(new ItemStack(ModItems.manaweaveBoots), "S S", "S S", 'S', LibOreDict.MANAWEAVE_CLOTH);
        ModCraftingRecipes.recipeManaweaveBoots = BotaniaAPI.getLatestAddedRecipe();

        // Helmets of revealing
        Item goggles = (Item) Item.itemRegistry.getObject("Thaumcraft:ItemGoggles");
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.manasteelHelmRevealing), new ItemStack(ModItems.manasteelHelm), goggles);
        ModCraftingRecipes.recipeHelmetOfRevealing =
                BotaniaAPI.getLatestAddedRecipe(); // We want manasteel to show in the Lexicon
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.terrasteelHelmRevealing), new ItemStack(ModItems.terrasteelHelm), goggles);
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.elementiumHelmRevealing), new ItemStack(ModItems.elementiumHelm), goggles);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Misc crafting items

        // Brewery vial
        addOreDictRecipe(
                new ItemStack(ModItems.vial, 1, Constants.VIAL_META_MANAGLASS),
                "GSG",
                "G G",
                "GGG",
                'G',
                new ItemStack(ModBlocks.manaGlass),
                'S',
                "springSmallAnyRubber");
        ModCraftingRecipes.recipeVial = BotaniaAPI.getLatestAddedRecipe();

        // Gaia Spirit Ingot Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.manaResource, 1, 14),
                " S ",
                "SIS",
                " S ",
                'S',
                LibOreDict.LIFE_ESSENCE,
                'I',
                OreDict.TERRA_STEEL_PLATE);
        ModCraftingRecipes.recipeGaiaIngot = BotaniaAPI.getLatestAddedRecipe();

        // Redstone root
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.manaResource, 1, 6),
                "dustRedstone",
                "circuitPrimitive",
                new ItemStack(Blocks.tallgrass, 1, 1));
        ModCraftingRecipes.recipeRedstoneRoot = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Baubles

        // Mana Band Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.manaRing),
                "TI ",
                "I I",
                " I ",
                'T',
                new ItemStack(ModItems.manaTablet, 1, Short.MAX_VALUE),
                'I',
                OreDict.MANA_STEEL_PLATE);
        ModCraftingRecipes.recipeManaRing = BotaniaAPI.getLatestAddedRecipe();

        // Aura Band Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.auraRing),
                "RI ",
                "I I",
                " I ",
                'R',
                LibOreDict.RUNE[8],
                'I',
                OreDict.MANA_STEEL_PLATE);
        ModCraftingRecipes.recipeAuraRing = BotaniaAPI.getLatestAddedRecipe();

        // Greater Mana Band Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.manaRingGreater), OreDict.TERRA_STEEL_PLATE, new ItemStack(ModItems.manaRing));
        ModCraftingRecipes.recipeGreaterManaRing = BotaniaAPI.getLatestAddedRecipe();

        // Greater Aura Band Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.auraRingGreater), OreDict.TERRA_STEEL_PLATE, new ItemStack(ModItems.auraRing));
        ModCraftingRecipes.recipeGreaterAuraRing = BotaniaAPI.getLatestAddedRecipe();

        // Soujourner's Belt Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.travelBelt),
                "ELS",
                "L L",
                "SLA",
                'E',
                LibOreDict.RUNE[2],
                'A',
                LibOreDict.RUNE[3],
                'S',
                "screwManasteel",
                'L',
                new ItemStack(Items.leather));
        ModCraftingRecipes.recipeTravelBelt = BotaniaAPI.getLatestAddedRecipe();

        // Tectonic Girdle Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.knockbackBelt),
                "ALS",
                "L L",
                "SLE",
                'E',
                LibOreDict.RUNE[2],
                'A',
                LibOreDict.RUNE[1],
                'S',
                "screwManasteel",
                'L',
                new ItemStack(Items.leather));
        ModCraftingRecipes.recipeKnocbackBelt = BotaniaAPI.getLatestAddedRecipe();

        // Snowflake Pendant Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.icePendant),
                "WSs",
                "SMS",
                "sSR",
                'S',
                new ItemStack(Items.string),
                's',
                "screwManasteel",
                'M',
                OreDict.MANA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[0],
                'W',
                LibOreDict.RUNE[7]);
        ModCraftingRecipes.recipeIcePendant = BotaniaAPI.getLatestAddedRecipe();

        // Pyroclast Pendant Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.lavaPendant),
                "MSs",
                "SMS",
                "sSF",
                'S',
                new ItemStack(Items.string),
                'D',
                OreDict.MANA_STEEL_PLATE,
                'S',
                "screwManasteel",
                'M',
                LibOreDict.RUNE[5],
                'F',
                LibOreDict.RUNE[1]);
        ModCraftingRecipes.recipeFirePendant = BotaniaAPI.getLatestAddedRecipe();

        // Golden Laurel Crown Recipe (amulet)
        addOreDictRecipe(
                new ItemStack(ModItems.goldLaurel),
                "G G",
                "LEL",
                "LLL",
                'G',
                "ingotGold",
                'L',
                "treeLeaves",
                'E',
                LibOreDict.LIFE_ESSENCE);
        ModCraftingRecipes.recipeGoldenLaurel = BotaniaAPI.getLatestAddedRecipe();

        // Ring of Chordata Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.waterRing),
                "WMP",
                "M M",
                "SM ",
                'W',
                LibOreDict.RUNE[0],
                'M',
                OreDict.MANA_STEEL_PLATE,
                'P',
                new ItemStack(Items.fish, 1, 3),
                'S',
                new ItemStack(Items.fish, 1, 1));
        ModCraftingRecipes.recipeWaterRing = BotaniaAPI.getLatestAddedRecipe();

        // Ring of the Mantle Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.miningRing),
                "EMP",
                "M M",
                " M ",
                'E',
                LibOreDict.RUNE[2],
                'M',
                OreDict.MANA_STEEL_PLATE,
                'P',
                new ItemStack(Items.golden_pickaxe));
        ModCraftingRecipes.recipeMiningRing = BotaniaAPI.getLatestAddedRecipe();

        // Ring of Magnetization Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.magnetRing),
                "LM ",
                "M M",
                " M ",
                'L',
                new ItemStack(ModItems.lens, 1, 10),
                'M',
                OreDict.MANA_STEEL_PLATE);
        ModCraftingRecipes.recipeMagnetRing = BotaniaAPI.getLatestAddedRecipe();

        // Charm of the Diva Recipe (amulet)
        addOreDictRecipe(
                new ItemStack(ModItems.divaCharm),
                "LGP",
                " HG",
                " GL",
                'L',
                LibOreDict.LIFE_ESSENCE,
                'G',
                "ingotGold",
                'H',
                LibOreDict.RUNE[15],
                'P',
                new ItemStack(ModItems.tinyPlanet));
        ModCraftingRecipes.recipeDivaCharm = BotaniaAPI.getLatestAddedRecipe();

        // Flügel Tiara Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.flightTiara),
                "LLL",
                "ILI",
                "FEF",
                'L',
                LibOreDict.LIFE_ESSENCE,
                'I',
                OreDict.ELEMENTIUM_PLATE,
                'F',
                new ItemStack(Items.feather),
                'E',
                LibOreDict.ENDER_AIR_BOTTLE);
        ModCraftingRecipes.recipeFlightTiara = BotaniaAPI.getLatestAddedRecipe();

        // Flügel variants
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                new ItemStack(ModItems.flightTiara, 1, 1),
                new ItemStack(ModItems.flightTiara, 1, Short.MAX_VALUE),
                "gemQuartz"));
        for (int i = 0; i < 7; i++)
            GameRegistry.addRecipe(new ShapelessOreRecipe(
                    new ItemStack(ModItems.flightTiara, 1, 2 + i),
                    new ItemStack(ModItems.flightTiara, 1, Short.MAX_VALUE),
                    LibOreDict.QUARTZ[i]));
        ModCraftingRecipes.recipesWings = BotaniaAPI.getLatestAddedRecipes(8);

        // Great Fairy Ring Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.pixieRing),
                "DE ",
                "EhE",
                " E ",
                'D',
                LibOreDict.PIXIE_DUST,
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipePixieRing = BotaniaAPI.getLatestAddedRecipe();

        // Globetrotter's Sash Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.superTravelBelt),
                "E/s",
                "/S/",
                "L/E",
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'L',
                LibOreDict.LIFE_ESSENCE,
                'S',
                new ItemStack(ModItems.travelBelt),
                '/',
                "screwTitanium",
                's',
                ToolDictNames.craftingToolScrewdriver.name());
        ModCraftingRecipes.recipeSuperTravelBelt = BotaniaAPI.getLatestAddedRecipe();

        // Ring of Far Reach Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.reachRing),
                "RE ",
                "EhE",
                " E ",
                'R',
                LibOreDict.RUNE[15],
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeReachRing = BotaniaAPI.getLatestAddedRecipe();

        // The Spectator Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.itemFinder),
                " I ",
                "IYI",
                "IEI",
                'I',
                "ingotIron",
                'Y',
                new ItemStack(Items.ender_eye),
                'E',
                "gemFlawlessEmerald");
        ModCraftingRecipes.recipeItemFinder = BotaniaAPI.getLatestAddedRecipe();

        // Crimson Pendant Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.superLavaPendant),
                "BBB",
                "BPB",
                "NGN",
                'B',
                new ItemStack(Items.blaze_rod),
                'P',
                new ItemStack(ModItems.lavaPendant),
                'N',
                new ItemStack(Blocks.nether_brick),
                'G',
                LibOreDict.LIFE_ESSENCE);
        ModCraftingRecipes.recipeSuperLavaPendant = BotaniaAPI.getLatestAddedRecipe();

        // Tainted Blood Pendant Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.bloodPendant),
                " Pf",
                "PGP",
                "DPf",
                'P',
                LibOreDict.PRISMARINE_SHARD,
                'G',
                new ItemStack(Items.ghast_tear),
                'D',
                LibOreDict.MANA_DIAMOND,
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeBloodPendant = BotaniaAPI.getLatestAddedRecipe();

        // Cloak of Virtue Recipe (belt)
        addOreDictRecipe(
                new ItemStack(ModItems.holyCloak),
                "WCW",
                "GWG",
                "GSG",
                'C',
                LibOreDict.MANAWEAVE_CLOTH,
                'W',
                new ItemStack(Blocks.wool),
                'G',
                "dustGlowstone",
                'S',
                LibOreDict.LIFE_ESSENCE);
        ModCraftingRecipes.recipeHolyCloak = BotaniaAPI.getLatestAddedRecipe();

        // Cloak of Sin Recipe (belt)
        addOreDictRecipe(
                new ItemStack(ModItems.unholyCloak),
                "WCW",
                "RWR",
                "RSR",
                'C',
                LibOreDict.MANAWEAVE_CLOTH,
                'W',
                new ItemStack(Blocks.wool, 1, 15),
                'R',
                "dustRedstone",
                'S',
                LibOreDict.LIFE_ESSENCE);
        ModCraftingRecipes.recipeUnholyCloak = BotaniaAPI.getLatestAddedRecipe();

        // Manaseer Monocle Recipe (amulet)
        addOreDictRecipe(
                new ItemStack(ModItems.monocle),
                "GI ",
                "ISw",
                "mSR",
                'G',
                new ItemStack(ModBlocks.manaGlass),
                'I',
                OreDict.MANA_STEEL_PLATE,
                'S',
                "springSmallGold",
                'R',
                "ringGold",
                'w',
                ToolDictNames.craftingToolWrench.name(),
                'm',
                ToolDictNames.craftingToolSoftHammer.name());
        ModCraftingRecipes.recipeMonocle = BotaniaAPI.getLatestAddedRecipe();

        // Ring of Correction Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.swapRing),
                "CM ",
                "MhM",
                " M ",
                'C',
                new ItemStack(Blocks.clay),
                'M',
                OreDict.MANA_STEEL_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeSwapRing = BotaniaAPI.getLatestAddedRecipe();

        // Greater Ring of Magnetization Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.magnetRingGreater),
                OreDict.TERRA_STEEL_PLATE,
                new ItemStack(ModItems.magnetRing));
        ModCraftingRecipes.recipeGreaterMagnetRing = BotaniaAPI.getLatestAddedRecipe();

        // Planestrider's Sash Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.speedUpBelt),
                "sMs",
                "PBP",
                "sSs",
                'M',
                new ItemStack(Items.filled_map, 1, Short.MAX_VALUE),
                'P',
                new ItemStack(ModItems.grassSeeds),
                'B',
                new ItemStack(ModItems.travelBelt),
                'S',
                new ItemStack(Items.sugar),
                's',
                "screwStainlessSteel");
        ModCraftingRecipes.recipeSpeedUpBelt = BotaniaAPI.getLatestAddedRecipe();

        // Bauble Case Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.baubleBox),
                " M ",
                "MCG",
                " M ",
                'M',
                OreDict.MANA_STEEL_PLATE,
                'C',
                new ItemStack(Blocks.chest),
                'G',
                "plateGold");
        ModCraftingRecipes.recipeBaubleCase = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Misc utility items

        // Mana Blaster Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.manaGun),
                "SMD",
                " WT",
                "  W",
                'S',
                new ItemStack(ModBlocks.spreader, 1, 1),
                'M',
                LibOreDict.RUNE[8],
                'D',
                LibOreDict.MANA_DIAMOND,
                'T',
                new ItemStack(Blocks.tnt),
                'W',
                LibOreDict.LIVING_WOOD);
        ModCraftingRecipes.recipeManaBlaster = BotaniaAPI.getLatestAddedRecipe();

        // Signal Flare Recipe
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(
                    ItemSignalFlare.forColor(i),
                    "I ",
                    " B",
                    "W ",
                    'B',
                    new ItemStack(ModBlocks.manaBeacon, 1, i),
                    'I',
                    "ingotIron",
                    'W',
                    LibOreDict.LIVING_WOOD);
        ModCraftingRecipes.recipesSignalFlares = BotaniaAPI.getLatestAddedRecipes(16);

        // Horn of the Wild Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.grassHorn),
                " W ",
                "WSW",
                "WW ",
                'W',
                LibOreDict.LIVING_WOOD,
                'S',
                new ItemStack(ModItems.grassSeeds));
        ModCraftingRecipes.recipeGrassHorn = BotaniaAPI.getLatestAddedRecipe();

        // Tiny Planet Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.tinyPlanet),
                "LSL",
                "SPS",
                "LSL",
                'S',
                "stone",
                'L',
                LibOreDict.LIVING_ROCK,
                'P',
                LibOreDict.MANA_PEARL);
        ModCraftingRecipes.recipeTinyPlanet = BotaniaAPI.getLatestAddedRecipe();

        // Soulscribe Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.enderDagger),
                " sP",
                "sSs",
                "Ts ",
                'P',
                LibOreDict.MANA_PEARL,
                'S',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                's',
                "screwDarkSteel");
        ModCraftingRecipes.recipeEnderDagger = BotaniaAPI.getLatestAddedRecipe();

        // Extrapolated Bucket Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.openBucket),
                "EhE",
                " E ",
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeOpenBucket = BotaniaAPI.getLatestAddedRecipe();

        // Spawner mover / Life Aggregator Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.spawnerMover),
                "EIE",
                "ADA",
                "EIE",
                'E',
                LibOreDict.LIFE_ESSENCE,
                'I',
                OreDict.ELEMENTIUM_PLATE,
                'A',
                LibOreDict.ENDER_AIR_BOTTLE,
                'D',
                LibOreDict.DRAGONSTONE);
        ModCraftingRecipes.recipeSpawnerMover = BotaniaAPI.getLatestAddedRecipe();

        // Vine Ball Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.vineBall),
                "VVV",
                "VSV",
                "VVV",
                'V',
                new ItemStack(Blocks.vine),
                'S',
                "slimeball");
        ModCraftingRecipes.recipeVineBall = BotaniaAPI.getLatestAddedRecipe();

        // Livingwood Slingshot Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.slingshot),
                " TA",
                "sTT",
                "Ts ",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'A',
                LibOreDict.RUNE[3],
                's',
                "screwWood");
        ModCraftingRecipes.recipeSlingshot = BotaniaAPI.getLatestAddedRecipe();

        // Hand of Ender
        // none: creative-only due to griefing concerns

        // Vitreous Pickaxe Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.glassPick),
                "GIG",
                "fTh",
                " T ",
                'G',
                "blockGlassColorless",
                'I',
                OreDict.MANA_STEEL_PLATE,
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'f',
                ToolDictNames.craftingToolFile.name(),
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeGlassPick = BotaniaAPI.getLatestAddedRecipe();

        // Horn of the Canopy Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.grassHorn, 1, 1), new ItemStack(ModItems.grassHorn), "treeLeaves");
        ModCraftingRecipes.recipeLeafHorn = BotaniaAPI.getLatestAddedRecipe();

        // Timeless Ivy Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.regenIvy),
                new ItemStack(Blocks.vine),
                LibOreDict.LIFE_ESSENCE,
                "dustElvenElementium");
        ModCraftingRecipes.recipeRegenIvy = BotaniaAPI.getLatestAddedRecipe();

        // Assembly Halo Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.craftingHalo),
                "hPS",
                "ICI",
                "SIs",
                'P',
                LibOreDict.MANA_PEARL,
                'I',
                OreDict.MANA_STEEL_PLATE,
                'C',
                "craftingTableWood",
                'S',
                "screwAluminium",
                's',
                ToolDictNames.craftingToolScrewdriver.name(),
                'h',
                ToolDictNames.craftingToolSoftHammer.name());
        ModCraftingRecipes.recipeCraftingHalo = BotaniaAPI.getLatestAddedRecipe();

        // Manufactory Halo Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.autocraftingHalo),
                new ItemStack(ModItems.craftingHalo),
                LibOreDict.MANA_DIAMOND);
        ModCraftingRecipes.recipeAutocraftingHalo = BotaniaAPI.getLatestAddedRecipe();

        // World Seed Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.worldSeed, 4),
                "G",
                "S",
                "D",
                'G',
                new ItemStack(Blocks.grass),
                'S',
                new ItemStack(Items.wheat_seeds),
                'D',
                LibOreDict.DRAGONSTONE);
        ModCraftingRecipes.recipeWorldSeed = BotaniaAPI.getLatestAddedRecipe();

        // Spellbinding Cloth Recipe (enchantment removal)
        addOreDictRecipe(
                new ItemStack(ModItems.spellCloth),
                " C ",
                "CPC",
                " C ",
                'C',
                LibOreDict.MANAWEAVE_CLOTH,
                'P',
                LibOreDict.MANA_PEARL);
        ModCraftingRecipes.recipeSpellCloth = BotaniaAPI.getLatestAddedRecipe();

        // Thorn Chakram Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.thornChakram, 2),
                "VVV",
                "VTV",
                "VVV",
                'V',
                new ItemStack(Blocks.vine),
                'T',
                "ringTerrasteel");
        ModCraftingRecipes.recipeThornChakram = BotaniaAPI.getLatestAddedRecipe();

        // Horn of the Covering Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.grassHorn, 1, 2),
                new ItemStack(ModItems.grassHorn),
                new ItemStack(Items.snowball));
        ModCraftingRecipes.recipeSnowHorn = BotaniaAPI.getLatestAddedRecipe();

        // Phantom Ink Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.phantomInk, 4),
                LibOreDict.MANA_PEARL,
                "dye",
                new ItemStack(ModBlocks.manaGlass),
                new ItemStack(Items.glass_bottle),
                new ItemStack(Items.glass_bottle),
                new ItemStack(Items.glass_bottle),
                new ItemStack(Items.glass_bottle));
        ModCraftingRecipes.recipePhantomInk = BotaniaAPI.getLatestAddedRecipe();

        // Resolute Ivy Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.keepIvy),
                LibOreDict.PIXIE_DUST,
                new ItemStack(Blocks.vine),
                LibOreDict.ENDER_AIR_BOTTLE);
        ModCraftingRecipes.recipeKeepIvy = BotaniaAPI.getLatestAddedRecipe();

        // Black Hole Talisman Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.blackHoleTalisman),
                "sGs",
                "EAE",
                "sEs",
                'G',
                LibOreDict.LIFE_ESSENCE,
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'A',
                LibOreDict.ENDER_AIR_BOTTLE,
                's',
                "screwDraconium");
        ModCraftingRecipes.recipeBlackHoleTalisman = BotaniaAPI.getLatestAddedRecipe();

        // Stone of Temperance Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.temperanceStone),
                "hSI",
                "SRS",
                "ISf",
                'S',
                "stone",
                'I',
                OreDict.MANA_STEEL_PLATE,
                'R',
                LibOreDict.RUNE[2],
                'f',
                ToolDictNames.craftingToolFile.name(),
                'h',
                ToolDictNames.craftingToolHardHammer.name());
        ModCraftingRecipes.recipeTemperanceStone = BotaniaAPI.getLatestAddedRecipe();

        // Incense Stick Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.incenseStick),
                " DG",
                "DTD",
                "TD ",
                'G',
                new ItemStack(Items.ghast_tear),
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'D',
                "dustCoal");
        ModCraftingRecipes.recipeIncenseStick = BotaniaAPI.getLatestAddedRecipe();

        // Cobweb Recipe
        addOreDictRecipe(
                new ItemStack(Blocks.web),
                "S S",
                " M ",
                "S S",
                'S',
                new ItemStack(Items.string),
                'M',
                LibOreDict.MANA_STRING);
        ModCraftingRecipes.recipeCobweb = BotaniaAPI.getLatestAddedRecipe();

        // Slime in a Bottle Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.slimeBottle),
                "EGE",
                "ESE",
                " E ",
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'G',
                new ItemStack(ModBlocks.elfGlass),
                'S',
                new ItemStack(Items.slime_ball));
        addOreDictRecipe(
                new ItemStack(ModItems.slimeBottle),
                "EGE",
                "ESE",
                " E ",
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'G',
                new ItemStack(ModBlocks.elfGlass),
                'S',
                new ItemStack((Item) Item.itemRegistry.getObject("TConstruct:strangeFood"), 1, 0));
        ModCraftingRecipes.recipeSlimeBottle = BotaniaAPI.getLatestAddedRecipe();

        // Starcaller Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.starSword),
                "  I",
                "AD ",
                "TA ",
                'I',
                OreDict.ELEMENTIUM_PLATE,
                'D',
                LibOreDict.DRAGONSTONE,
                'A',
                LibOreDict.ENDER_AIR_BOTTLE,
                'T',
                new ItemStack(ModItems.terraSword));
        ModCraftingRecipes.recipeStarSword = BotaniaAPI.getLatestAddedRecipe();

        // Flare Chakram Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.thornChakram, 2, 1),
                "BBB",
                "CPC",
                "BBB",
                'B',
                new ItemStack(Items.blaze_powder),
                'P',
                LibOreDict.PIXIE_DUST,
                'C',
                new ItemStack(ModItems.thornChakram));
        ModCraftingRecipes.recipeFireChakram = BotaniaAPI.getLatestAddedRecipe();

        // Thundercaller Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.thunderSword),
                "  I",
                "AD ",
                "TA ",
                'I',
                OreDict.ELEMENTIUM_PLATE,
                'D',
                LibOreDict.MANA_DIAMOND,
                'A',
                LibOreDict.ENDER_AIR_BOTTLE,
                'T',
                new ItemStack(ModItems.terraSword));
        ModCraftingRecipes.recipeThunderSword = BotaniaAPI.getLatestAddedRecipe();

        // Floral Obedience Stick Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.obedienceStick),
                " SM",
                "STS",
                "TS ",
                'M',
                "stickManasteel",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'S',
                "screwWood");
        ModCraftingRecipes.recipeObedienceStick = BotaniaAPI.getLatestAddedRecipe();

        // Cacophonium Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.cacophonium),
                "h1m",
                "424",
                "43f",
                '1',
                "pipeLargeBrass",
                '2',
                "pipeMediumBrass",
                '3',
                "pipeSmallBrass",
                '4',
                "pipeTinyBrass",
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'm',
                ToolDictNames.craftingToolSoftHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeCacophonium = BotaniaAPI.getLatestAddedRecipe();

        // Cellular Block Recipe
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModBlocks.cellBlock, 3),
                new ItemStack(Blocks.cactus),
                new ItemStack(Blocks.cactus),
                new ItemStack(Blocks.cactus),
                new ItemStack(Blocks.cactus),
                new ItemStack(Items.carrot),
                new ItemStack(Items.potato));
        ModCraftingRecipes.recipeCellBlock = BotaniaAPI.getLatestAddedRecipe();

        // Worldshaper's Sextant Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.sextant),
                "hTI",
                "fTT",
                "IMI",
                'T',
                LibOreDict.LIVINGWOOD_TWIG,
                'M',
                OreDict.MANA_STEEL_PLATE,
                'I',
                "stickManasteel",
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'f',
                ToolDictNames.craftingToolFile.name());
        ModCraftingRecipes.recipeSextant = BotaniaAPI.getLatestAddedRecipe();

        // Alternate Pasture Seed Recipes
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 3),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(Blocks.deadbush));
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 4),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(Items.wheat));
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 5),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(Items.dye, 1, 2));
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 6),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(Items.blaze_powder));
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 7),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(ModItems.manaResource, 1, 10));
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModItems.grassSeeds, 1, 8),
                new ItemStack(ModItems.grassSeeds),
                new ItemStack(Items.spider_eye));
        ModCraftingRecipes.recipesAltGrassSeeds = BotaniaAPI.getLatestAddedRecipes(6);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Misc functional blocks

        // Unstable Block Recipes
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(
                    new ItemStack(ModBlocks.unstableBlock, 2, i),
                    "OPO",
                    "PMP",
                    "OPO",
                    'O',
                    new ItemStack(Blocks.obsidian),
                    'P',
                    LibOreDict.PETAL[i],
                    'M',
                    LibOreDict.MANA_PEARL);
        ModCraftingRecipes.recipesUnstableBlocks = BotaniaAPI.getLatestAddedRecipes(16);

        // Unstable Beacon Recipe
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(
                    new ItemStack(ModBlocks.manaBeacon, 1, i),
                    " B ",
                    "BPB",
                    " B ",
                    'B',
                    new ItemStack(ModBlocks.unstableBlock, 1, i),
                    'P',
                    new ItemStack(ModItems.lens, 1, 0));
        ModCraftingRecipes.recipesManaBeacons = BotaniaAPI.getLatestAddedRecipes(16);

        // Tiny Planet Block Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.tinyPlanet), "SSS", "SPS", "SSS", 'S', "stone", 'P', ModItems.tinyPlanet);
        ModCraftingRecipes.recipeTinyPlanetBlock = BotaniaAPI.getLatestAddedRecipe();

        // Alternate non-magic Tiny Planet Block Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.tinyPlanet),
                "SSS",
                "SPS",
                "SSS",
                'S',
                "stone",
                'P',
                new ItemStack((Item) Item.itemRegistry.getObject("gregtech:gt.metaitem.01"), 1, 32674));
        ModCraftingRecipes.recipeTinyPlanetBlock = BotaniaAPI.getLatestAddedRecipe();

        // Drum of the Wild Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.forestDrum),
                "WLW",
                "WHW",
                "WLW",
                'W',
                LibOreDict.LIVING_WOOD,
                'L',
                new ItemStack(Items.leather),
                'H',
                new ItemStack(ModItems.grassHorn));
        ModCraftingRecipes.recipeForestDrum = BotaniaAPI.getLatestAddedRecipe();

        // Open Crate Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.openCrate),
                "WWW",
                "WSW",
                "WsW",
                'W',
                new ItemStack(ModBlocks.livingwood, 1, 1),
                'S',
                "screwWood",
                's',
                ToolDictNames.craftingToolScrewdriver.name());
        ModCraftingRecipes.recipeOpenCrate = BotaniaAPI.getLatestAddedRecipe();

        // Eye of the Ancients Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.forestEye),
                "MSM",
                "SES",
                "MSM",
                'M',
                OreDict.MANA_STEEL_PLATE,
                'S',
                LibOreDict.LIVING_ROCK,
                'E',
                new ItemStack(Items.ender_eye));
        ModCraftingRecipes.recipeForestEye = BotaniaAPI.getLatestAddedRecipe();

        // Spectral Platform Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.platform, 2, 1),
                "343",
                "0D0",
                '0',
                new ItemStack(ModBlocks.dreamwood, 1, 0),
                '3',
                new ItemStack(ModBlocks.dreamwood, 1, 3),
                '4',
                new ItemStack(ModBlocks.dreamwood, 1, 4),
                'D',
                LibOreDict.PIXIE_DUST);
        ModCraftingRecipes.recipeSpectralPlatform = BotaniaAPI.getLatestAddedRecipe();

        // Abstruse Platform Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.platform, 2),
                "343",
                "0P0",
                '0',
                new ItemStack(ModBlocks.livingwood, 1, 0),
                '3',
                new ItemStack(ModBlocks.livingwood, 1, 3),
                '4',
                new ItemStack(ModBlocks.livingwood, 1, 4),
                'P',
                LibOreDict.MANA_PEARL);
        ModCraftingRecipes.recipePlatform = BotaniaAPI.getLatestAddedRecipe();

        // Drum of the Gathering Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.forestDrum, 1, 1),
                "WLW",
                "WEW",
                "WLW",
                'W',
                LibOreDict.DREAM_WOOD,
                'L',
                new ItemStack(Items.leather),
                'E',
                OreDict.ELEMENTIUM_PLATE);
        ModCraftingRecipes.recipeGatherDrum = BotaniaAPI.getLatestAddedRecipe();

        // Life Imbuer Recipe / spawner automation
        addOreDictRecipe(
                new ItemStack(ModBlocks.spawnerClaw),
                "BSB",
                "PMP",
                "PEP",
                'B',
                new ItemStack(Items.blaze_rod),
                'S',
                OreDict.ELEMENTIUM_PLATE,
                'P',
                new ItemStack(ModBlocks.prismarine, 1, 2),
                'M',
                new ItemStack(ModBlocks.storage),
                'E',
                LibOreDict.ENDER_AIR_BOTTLE);
        ModCraftingRecipes.recipeSpawnerClaw = BotaniaAPI.getLatestAddedRecipe();

        // Ender Overseer Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.enderEye),
                "RER",
                "EOE",
                "RER",
                'R',
                "dustRedstone",
                'E',
                new ItemStack(Items.ender_eye),
                'O',
                new ItemStack(Blocks.obsidian));
        ModCraftingRecipes.recipeEnderEyeBlock = BotaniaAPI.getLatestAddedRecipe();

        // Starfield Creator Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.starfield),
                "EPE",
                "EOE",
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'P',
                LibOreDict.PIXIE_DUST,
                'O',
                new ItemStack(Blocks.obsidian));
        ModCraftingRecipes.recipeStarfield = BotaniaAPI.getLatestAddedRecipe();

        // Incense Plate Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.incensePlate),
                "  s",
                "Sw ",
                "WSS",
                'W',
                LibOreDict.LIVING_WOOD,
                'S',
                new ItemStack(ModFluffBlocks.livingwoodSlab),
                'w',
                "screwWood",
                's',
                ToolDictNames.craftingToolScrewdriver.name());
        ModCraftingRecipes.recipeIncensePlate = BotaniaAPI.getLatestAddedRecipe();

        // Hovering Hourglass Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.hourglass),
                "GSG",
                "MrM",
                "GRG",
                'G',
                "plateGold",
                'M',
                new ItemStack(ModBlocks.manaGlass),
                'R',
                "dustRedstone",
                'r',
                "ringGold",
                'S',
                new ItemStack(Blocks.sand));
        ModCraftingRecipes.recipeHourglass = BotaniaAPI.getLatestAddedRecipe();

        // Drum of the Canopy Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.forestDrum, 1, 2),
                "WLW",
                "WHW",
                "WLW",
                'W',
                LibOreDict.LIVING_WOOD,
                'L',
                new ItemStack(Items.leather),
                'H',
                new ItemStack(ModItems.grassHorn, 1, 1));
        ModCraftingRecipes.recipeCanopyDrum = BotaniaAPI.getLatestAddedRecipe();

        // Cocoon of Caprice Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.cocoon),
                "SPS",
                "WEW",
                "SDS",
                'S',
                new ItemStack((Item) Item.itemRegistry.getObject("Thaumcraft:ItemResource"), 1, 14),
                'E',
                "egg",
                'P',
                LibOreDict.PIXIE_DUST,
                'D',
                LibOreDict.DRAGONSTONE,
                'W',
                new ItemStack(ModItems.manaResource, 1, Constants.MANARESOURCE_META_CLOTH));
        ModCraftingRecipes.recipeCocoon = BotaniaAPI.getLatestAddedRecipe();

        // Fel Pumpkin
        addOreDictRecipe(
                new ItemStack(ModBlocks.felPumpkin),
                "sSs",
                "BPF",
                "sGs",
                'S',
                LibOreDict.MANA_STRING,
                'B',
                new ItemStack(Items.bone),
                'P',
                new ItemStack(Blocks.pumpkin),
                'F',
                new ItemStack(Items.rotten_flesh),
                'G',
                new ItemStack(Items.gunpowder),
                's',
                new ItemStack((Item) Item.itemRegistry.getObject("Thaumcraft:ItemResource"), 1, 14));
        ModCraftingRecipes.recipeFelPumpkin = BotaniaAPI.getLatestAddedRecipe();

        // Teru Teru Bozu Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.teruTeruBozu),
                " C ",
                "CRC",
                "CSC",
                'C',
                LibOreDict.MANAWEAVE_CLOTH,
                'R',
                LibOreDict.RUNE[4],
                'S',
                new ItemStack(Blocks.double_plant));
        ModCraftingRecipes.recipeTeruTeruBozu = BotaniaAPI.getLatestAddedRecipe();

        // Livingwood Avatar Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.avatar),
                " W ",
                "WCW",
                "WDW",
                'W',
                LibOreDict.LIVING_WOOD,
                'C',
                new ItemStack((Item) Item.itemRegistry.getObject("Thaumcraft:ItemGolemCore"), 1, 100),
                'D',
                LibOreDict.MANA_DIAMOND);
        ModCraftingRecipes.recipeAvatar = BotaniaAPI.getLatestAddedRecipe();

        // Mana Fluxfield Recipe
        if (ConfigHandler.fluxfieldEnabled) {
            addOreDictRecipe(
                    new ItemStack(ModBlocks.rfGenerator),
                    "SRS",
                    "RIR",
                    "SRS",
                    'S',
                    LibOreDict.LIVING_ROCK,
                    'I',
                    OreDict.MANA_STEEL_PLATE,
                    'R',
                    "plateRedAlloy");
            ModCraftingRecipes.recipeRFGenerator = BotaniaAPI.getLatestAddedRecipe();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Flower extras

        // Glimmering Flowers Recipes
        for (int i = 0; i < 16; i++)
            addShapelessOreDictRecipe(
                    new ItemStack(ModBlocks.shinyFlower, 1, i), "dustGlowstone", "dustGlowstone", LibOreDict.FLOWER[i]);
        ModCraftingRecipes.recipesShinyFlowers = BotaniaAPI.getLatestAddedRecipes(16);

        // Mini Island Recipes
        for (int i = 0; i < 16; i++)
            GameRegistry.addRecipe(
                    new ItemStack(ModBlocks.floatingFlower, 1, i),
                    "F",
                    "S",
                    "D",
                    'F',
                    new ItemStack(ModBlocks.shinyFlower, 1, i),
                    'S',
                    new ItemStack(ModItems.grassSeeds),
                    'D',
                    new ItemStack(Blocks.dirt));
        ModCraftingRecipes.recipesMiniIsland = BotaniaAPI.getLatestAddedRecipes(16);

        // Flower Pouch Recipe
        GameRegistry.addShapedRecipe(
                new ItemStack(ModItems.flowerBag),
                "WPW",
                "W W",
                " W ",
                'P',
                new ItemStack(ModItems.petal, 1, Short.MAX_VALUE),
                'W',
                new ItemStack(Blocks.wool, 1, Short.MAX_VALUE));
        ModCraftingRecipes.recipeFlowerBag = BotaniaAPI.getLatestAddedRecipe();

        // Double Petal Recipes
        for (int i = 0; i < 16; i++)
            addShapelessOreDictRecipe(new ItemStack(ModItems.petal, 2, i), LibOreDict.DOUBLE_FLOWER[i]);
        ModCraftingRecipes.recipesPetalsDouble = BotaniaAPI.getLatestAddedRecipes(16);

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Sparks and Corporea networks

        // Crafty Crate Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModBlocks.openCrate, 1, 1),
                "WCW",
                "WSW",
                "WsW",
                'C',
                "craftingTableWood",
                'W',
                new ItemStack(ModBlocks.dreamwood, 1, 1),
                'S',
                "screwWood",
                's',
                ToolDictNames.craftingToolScrewdriver.name()));
        ModCraftingRecipes.recipeCraftCrate = BotaniaAPI.getLatestAddedRecipe();

        // Crafting Placeholder Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.manaResource, 4, Constants.MANARESOURCE_META_CRAFT),
                "craftingTableWood",
                LibOreDict.LIVING_ROCK);
        ModCraftingRecipes.recipePlaceholder = BotaniaAPI.getLatestAddedRecipe();

        // Pattern Recipes
        {
            int count = TileCraftCrate.PATTERNS.length;
            List<Object> recipeObjects = Arrays.asList(new Object[] {'R', "dustRedstone", 'P', LibOreDict.PLACEHOLDER});

            for (int i = 0; i < count; i++) {
                List<Object> recipe = new ArrayList();
                for (int j = 0; j < 3; j++) {
                    String s = "";
                    for (int k = 0; k < 3; k++) s += TileCraftCrate.PATTERNS[i][j * 3 + k] ? "R" : "P";
                    recipe.add(s);
                }
                recipe.addAll(recipeObjects);

                addOreDictRecipe(new ItemStack(ModItems.craftPattern, 1, i), recipe.toArray(new Object[recipe.size()]));
            }

            ModCraftingRecipes.recipesPatterns = BotaniaAPI.getLatestAddedRecipes(count);
        }

        // Spark Recipe
        for (int i = 0; i < 16; i++)
            addOreDictRecipe(
                    new ItemStack(ModItems.spark),
                    " P ",
                    "BNB",
                    " P ",
                    'B',
                    new ItemStack(Items.blaze_powder),
                    'P',
                    LibOreDict.PETAL[i],
                    'N',
                    new ItemStack(
                            (Item) Item.itemRegistry.getObject("Thaumcraft:ItemWispEssence"), 1, Short.MAX_VALUE));
        ModCraftingRecipes.recipesSpark = BotaniaAPI.getLatestAddedRecipes(16);

        // Spark Augment Recipes
        for (int i = 0; i < 4; i++)
            addShapelessOreDictRecipe(
                    new ItemStack(ModItems.sparkUpgrade, 1, i),
                    LibOreDict.MANA_DIAMOND,
                    "plateTitanium",
                    LibOreDict.RUNE[i]);
        ModCraftingRecipes.recipesSparkUpgrades = BotaniaAPI.getLatestAddedRecipes(4);

        // Corporea Spark Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.corporeaSpark),
                new ItemStack(ModItems.spark),
                LibOreDict.PIXIE_DUST,
                LibOreDict.ENDER_AIR_BOTTLE);
        ModCraftingRecipes.recipeCorporeaSpark = BotaniaAPI.getLatestAddedRecipe();

        // Master Corporea Spark Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.corporeaSpark, 1, 1),
                new ItemStack(ModItems.corporeaSpark),
                LibOreDict.DRAGONSTONE);
        ModCraftingRecipes.recipeMasterCorporeaSpark = BotaniaAPI.getLatestAddedRecipe();

        // Corporea Index Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.corporeaIndex),
                "AOA",
                "OSO",
                "DOD",
                'A',
                LibOreDict.ENDER_AIR_BOTTLE,
                'O',
                new ItemStack(Blocks.obsidian),
                'S',
                new ItemStack(ModItems.corporeaSpark),
                'D',
                LibOreDict.DRAGONSTONE);
        ModCraftingRecipes.recipeCorporeaIndex = BotaniaAPI.getLatestAddedRecipe();

        // Corporea Funnel Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.corporeaFunnel),
                new ItemStack(Blocks.dropper),
                new ItemStack(ModItems.corporeaSpark));
        ModCraftingRecipes.recipeCorporeaFunnel = BotaniaAPI.getLatestAddedRecipe();

        // Corporea Interceptor Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.corporeaInterceptor), "blockRedstone", new ItemStack(ModItems.corporeaSpark));
        ModCraftingRecipes.recipeCorporeaInterceptor = BotaniaAPI.getLatestAddedRecipe();

        // Corporea Crystal Cube Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.corporeaCrystalCube),
                "WGW",
                "GCG",
                "WGW",
                'C',
                new ItemStack(ModItems.corporeaSpark),
                'G',
                new ItemStack(ModBlocks.elfGlass),
                'W',
                LibOreDict.DREAM_WOOD);
        ModCraftingRecipes.recipeCorporeaCrystalCube = BotaniaAPI.getLatestAddedRecipe();

        // Corporea Retainer Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.corporeaRetainer),
                new ItemStack(Blocks.chest),
                new ItemStack(ModItems.corporeaSpark));
        ModCraftingRecipes.recipeCorporeaRetainer = BotaniaAPI.getLatestAddedRecipe();

        // Spark Changer Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.sparkChanger),
                "   ",
                "ESE",
                "SRS",
                'S',
                LibOreDict.LIVING_ROCK,
                'E',
                OreDict.ELEMENTIUM_PLATE,
                'R',
                "dustRedstone");
        ModCraftingRecipes.recipeSparkChanger = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Redstringed collection

        // Red String Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModItems.manaResource, 1, 12),
                new ItemStack(Items.string),
                "blockRedstone",
                LibOreDict.PIXIE_DUST,
                LibOreDict.ENDER_AIR_BOTTLE);
        ModCraftingRecipes.recipeRedString = BotaniaAPI.getLatestAddedRecipe();

        // Red String Container Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringContainer),
                "RRR",
                "RCS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'C',
                "chestWood");
        ModCraftingRecipes.recipeRedStringContainer = BotaniaAPI.getLatestAddedRecipe();

        // Red String Dispenser Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringDispenser),
                "RRR",
                "RDS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'D',
                new ItemStack(Blocks.dispenser));
        ModCraftingRecipes.recipeRedStringDispenser = BotaniaAPI.getLatestAddedRecipe();

        // Red String Fertilizer Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringFertilizer),
                "RRR",
                "RBS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'B',
                new ItemStack(ModItems.fertilizer));
        ModCraftingRecipes.recipeRedStringFertilizer = BotaniaAPI.getLatestAddedRecipe();

        // Red String Comparator Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringComparator),
                "RRR",
                "RCS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'C',
                new ItemStack(Items.comparator));
        ModCraftingRecipes.recipeRedStringComparator = BotaniaAPI.getLatestAddedRecipe();

        // Red String Spoofer Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringRelay),
                "RRR",
                "RMS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'M',
                new ItemStack(ModBlocks.spreader));
        ModCraftingRecipes.recipeRedStringRelay = BotaniaAPI.getLatestAddedRecipe();

        // Red String Interceptor Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.redStringInterceptor),
                "RRR",
                "RMS",
                "RRR",
                'R',
                LibOreDict.LIVING_ROCK,
                'S',
                LibOreDict.RED_STRING,
                'M',
                new ItemStack(Blocks.stone_button));
        ModCraftingRecipes.recipeRedStringInterceptor = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Minecarts and rails

        // Minecart with Mana Pool Recipe
        addOreDictRecipe(
                new ItemStack(ModItems.poolMinecart),
                "hPw",
                " C ",
                " s ",
                'C',
                new ItemStack(Items.minecart),
                'P',
                new ItemStack(ModBlocks.pool),
                'h',
                ToolDictNames.craftingToolHardHammer.name(),
                'w',
                ToolDictNames.craftingToolWrench.name(),
                's',
                ToolDictNames.craftingToolScrewdriver.name());
        ModCraftingRecipes.recipePoolCart = BotaniaAPI.getLatestAddedRecipe();

        // Spectral Rail Recipe
        GameRegistry.addShapelessRecipe(
                new ItemStack(ModBlocks.ghostRail),
                new ItemStack(Blocks.rail),
                new ItemStack(ModBlocks.platform, 1, 1));
        ModCraftingRecipes.recipeGhostRail = BotaniaAPI.getLatestAddedRecipe();

        // Luminizer Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.lightRelay),
                LibOreDict.RED_STRING,
                LibOreDict.DRAGONSTONE,
                "dustGlowstone",
                "dustGlowstone");
        ModCraftingRecipes.recipeLuminizer = BotaniaAPI.getLatestAddedRecipe();

        // Detector Luminizer Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.lightRelay, 1, 1), new ItemStack(ModBlocks.lightRelay), "dustRedstone");
        ModCraftingRecipes.recipeDetectorLuminizer = BotaniaAPI.getLatestAddedRecipe();

        // Luminizer Launcher Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.lightLauncher),
                "DDD",
                "DLD",
                'D',
                LibOreDict.DREAM_WOOD,
                'L',
                new ItemStack(ModBlocks.lightRelay));
        ModCraftingRecipes.recipeLuminizerLauncher = BotaniaAPI.getLatestAddedRecipe();

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Deco blocks

        addWall(ModFluffBlocks.livingwoodWall, 0, new ItemStack(ModBlocks.livingwood, 1, 0));
        addWall(ModFluffBlocks.livingrockWall, 0, new ItemStack(ModBlocks.livingrock, 1, 0));
        addWall(ModFluffBlocks.dreamwoodWall, 0, new ItemStack(ModBlocks.dreamwood, 1, 0));
        addWall(ModFluffBlocks.prismarineWall, 0, new ItemStack(ModBlocks.prismarine, 1, 0));
        GregtechPatches.addStairs(
                ModFluffBlocks.prismarineStairs,
                new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_BLOCK));

        // Glimmering
        addOreDictRecipe(
                new ItemStack(ModBlocks.dreamwood, 4, Constants.LIVINGWOOD_META_GLIMMERING),
                "LWL",
                "WGW",
                "LWL",
                'L',
                "ingotSteeleaf",
                'W',
                LibOreDict.DREAM_WOOD,
                'G',
                "glowstone");

        // Prismarine Brick Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.prismarine, 4, Constants.PRISMARINE_META_BRICK),
                "SS",
                "SS",
                'S',
                LibOreDict.PRISMARINE_BLOCK);
        ModCraftingRecipes.recipePrismarineBrick = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.prismarineBrickStairs,
                new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_BRICK));

        // Dark Prismarine Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.prismarine, 4, Constants.PRISMARINE_META_DARK),
                " S ",
                "SBS",
                " S ",
                'S',
                LibOreDict.PRISMARINE_BLOCK,
                'B',
                new ItemStack(Blocks.nether_brick));
        ModCraftingRecipes.recipeDarkPrismarine = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.darkPrismarineStairs,
                new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_DARK));

        // Sea Lantern Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.seaLamp),
                " S ",
                "SBS",
                " S ",
                'S',
                LibOreDict.PRISMARINE_SHARD,
                'B',
                "glowstone");
        ModCraftingRecipes.recipeSeaLamp = BotaniaAPI.getLatestAddedRecipe();

        // Reed Block Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.reedBlock),
                "rrr",
                "rsr",
                "rrr",
                'r',
                new ItemStack(Items.reeds),
                's',
                "string");
        ModCraftingRecipes.recipeReedBlock = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(ModFluffBlocks.reedStairs, ModBlocks.reedBlock);
        addWall(ModFluffBlocks.reedWall, 0, ModBlocks.reedBlock);

        // Thatch Recipe
        addOreDictRecipe(new ItemStack(ModBlocks.thatch), "w w", "sws", "w w", 'w', "cropWheat", 's', "string");
        ModCraftingRecipes.recipeThatch = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(ModFluffBlocks.thatchStairs, ModBlocks.thatch);

        // Hellish Brick Recipe
        addOreDictRecipe(
                new ItemStack(ModBlocks.customBrick, 4, Constants.BRICK_META_HELL),
                " B ",
                "BSB",
                " B ",
                'B',
                new ItemStack(Blocks.netherrack),
                'S',
                new ItemStack(Blocks.stonebrick));
        ModCraftingRecipes.recipeNetherBrick = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.netherBrickStairs, new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_HELL));

        // Soul Brick Recipe
        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.customBrick, 4, Constants.BRICK_META_SOUL),
                " B ",
                "BSB",
                " B ",
                'B',
                new ItemStack(Blocks.soul_sand),
                'S',
                new ItemStack(Blocks.stonebrick));
        ModCraftingRecipes.recipeSoulBrick = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.soulBrickStairs, new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_SOUL));

        // Snow Brick Recipe
        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.customBrick, 4, Constants.BRICK_META_FROST),
                " B ",
                "BSB",
                " B ",
                'B',
                new ItemStack(Blocks.snow),
                'S',
                new ItemStack(Blocks.stonebrick));
        ModCraftingRecipes.recipeSnowBrick = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.snowBrickStairs, new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_FROST));

        // Roof Tile Recipe
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(ModBlocks.customBrick, 2, Constants.BRICK_META_ROOF),
                "BB",
                "BB",
                "BB",
                'B',
                "ingotBrick"));
        ModCraftingRecipes.recipeRoofTile = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.tileStairs, new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_ROOF));

        // Azulejo Recipe
        addShapelessOreDictRecipe(new ItemStack(ModBlocks.customBrick, 1, 4), "gemLapis", "blockQuartz");
        ModCraftingRecipes.recipeAzulejo = BotaniaAPI.getLatestAddedRecipe();

        // Azulejo Cycling Recipes
        for (int i = 0; i < 12; i++)
            GameRegistry.addShapelessRecipe(
                    new ItemStack(ModBlocks.customBrick, 1, 4 + (i == 11 ? 0 : i + 1)),
                    new ItemStack(ModBlocks.customBrick, 1, 4 + i));
        ModCraftingRecipes.recipesAzulejoCycling = BotaniaAPI.getLatestAddedRecipes(12);

        // Trodden Dirt Recipe
        GameRegistry.addRecipe(new ShapelessOreRecipe(
                new ItemStack(ModBlocks.dirtPath, 4),
                new ItemStack(Blocks.dirt, 1, 1),
                new ItemStack(Blocks.dirt, 1, 1),
                new ItemStack(Blocks.dirt, 1, 1),
                "sand"));
        ModCraftingRecipes.recipeDirtPath = BotaniaAPI.getLatestAddedRecipe();

        // End stone flavours
        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.endStoneBrick, 4), "SS", "SS", 'S', new ItemStack(Blocks.end_stone));
        ModCraftingRecipes.recipeEndStoneBricks = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.endStoneStairs,
                new ItemStack(ModBlocks.endStoneBrick, 1, Constants.ENDSTONE_META_BRICK_YELLOW));

        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.endStoneBrick, 1, 1),
                "S",
                "S",
                'S',
                new ItemStack(ModFluffBlocks.endStoneSlab));
        ModCraftingRecipes.recipeEndStoneChiseledBricks = BotaniaAPI.getLatestAddedRecipe();

        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.endStoneBrick, 4, 2),
                " B ",
                "BPB",
                " B ",
                'B',
                new ItemStack(ModBlocks.endStoneBrick),
                'P',
                new ItemStack(Items.ender_pearl));
        ModCraftingRecipes.recipeEnderBricks = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(
                ModFluffBlocks.enderBrickStairs,
                new ItemStack(ModBlocks.endStoneBrick, 1, Constants.ENDSTONE_META_BRICK_PURPLE));

        GameRegistry.addRecipe(
                new ItemStack(ModBlocks.endStoneBrick, 2, 3),
                "B",
                "B",
                'B',
                new ItemStack(ModBlocks.endStoneBrick, 1, 2));
        ModCraftingRecipes.recipePillarEnderBricks = BotaniaAPI.getLatestAddedRecipe();

        // Bifrost Blocks Recipe
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.bifrostPerm),
                new ItemStack(ModItems.rainbowRod),
                new ItemStack(ModBlocks.elfGlass));
        ModCraftingRecipes.recipeBifrost = BotaniaAPI.getLatestAddedRecipe();
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.shimmerrock), LibOreDict.LIVING_ROCK, new ItemStack(ModBlocks.bifrostPerm));
        ModCraftingRecipes.recipeShimmerrock = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(ModFluffBlocks.shimmerrockStairs, new ItemStack(ModBlocks.shimmerrock));
        addShapelessOreDictRecipe(
                new ItemStack(ModBlocks.shimmerwoodPlanks),
                new ItemStack(ModBlocks.dreamwood, 1, 1),
                new ItemStack(ModBlocks.bifrostPerm));
        ModCraftingRecipes.recipeShimmerwoodPlanks = BotaniaAPI.getLatestAddedRecipe();
        GregtechPatches.addStairs(ModFluffBlocks.shimmerwoodPlankStairs, new ItemStack(ModBlocks.shimmerwoodPlanks));

        // 1.8 Stone Recipes
        ModCraftingRecipes.recipe18StonePolish = new ArrayList<>();
        ModCraftingRecipes.recipe18StoneBrick = new ArrayList<>();
        ModCraftingRecipes.recipe18StoneChisel = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int stairsSelected = 3;
            if (i == 1) {
                // Basalt conflicts with RC stairs, register only one half of the mirror version
                stairsSelected = 1;
            }

            // block
            GregtechPatches.addStairs(ModFluffBlocks.stoneStairs[i], LibOreDict.STONE_18_VARIANTS[i], stairsSelected);
            addOreDictRecipe(
                    new ItemStack(ModFluffBlocks.stone, 1, i),
                    "Q",
                    "Q",
                    'Q',
                    new ItemStack(ModFluffBlocks.stoneSlabs[i]));

            // polished
            addOreDictRecipe(
                    new ItemStack(ModFluffBlocks.stone, 8, i + 4),
                    "SSS",
                    "SfS",
                    "SSS",
                    'S',
                    LibOreDict.STONE_18_VARIANTS[i],
                    'f',
                    ToolDictNames.craftingToolFile.name());
            ModCraftingRecipes.recipe18StonePolish.add(BotaniaAPI.getLatestAddedRecipe());

            // brick
            addOreDictRecipe(
                    new ItemStack(ModFluffBlocks.stone, 4, i + 8), "SS", "SS", 'S', LibOreDict.STONE_18_VARIANTS[i]);
            ModCraftingRecipes.recipe18StoneBrick.add(BotaniaAPI.getLatestAddedRecipe());
            GregtechPatches.addStairs(ModFluffBlocks.stoneStairs[i + 4], LibOreDict.STONE_18_VARIANTS[i + 8]);

            // chiseled 2?
            addOreDictRecipe(
                    new ItemStack(ModFluffBlocks.stone, 1, i + 12),
                    "S",
                    "S",
                    'S',
                    new ItemStack(ModFluffBlocks.stoneSlabs[i + 4], 1, 0));
            ModCraftingRecipes.recipe18StoneChisel.add(BotaniaAPI.getLatestAddedRecipe());

            addWall(ModFluffBlocks.stoneWall, i, LibOreDict.STONE_18_VARIANTS[i]);
        }

        // Metamorphic blocks
        for (int i = 0; i < 8; i++) {
            GameRegistry.addSmelting(
                    new ItemStack(ModFluffBlocks.biomeStoneA, 1, i + 8),
                    new ItemStack(ModFluffBlocks.biomeStoneA, 1, i),
                    0.1F);
            GameRegistry.addRecipe(
                    new ItemStack(ModFluffBlocks.biomeStoneB, 4, i),
                    "SS",
                    "SS",
                    'S',
                    new ItemStack(ModFluffBlocks.biomeStoneA, 1, i));
            GameRegistry.addRecipe(
                    new ItemStack(ModFluffBlocks.biomeStoneB, 1, i + 8),
                    "S",
                    "S",
                    'S',
                    new ItemStack(ModFluffBlocks.biomeStoneSlabs[i + 16]));
            GregtechPatches.addStairs(
                    ModFluffBlocks.biomeStoneStairs[i], new ItemStack(ModFluffBlocks.biomeStoneA, 1, i));
            GregtechPatches.addStairs(
                    ModFluffBlocks.biomeStoneStairs[i + 8], new ItemStack(ModFluffBlocks.biomeStoneA, 1, i + 8));
            GregtechPatches.addStairs(
                    ModFluffBlocks.biomeStoneStairs[i + 16], new ItemStack(ModFluffBlocks.biomeStoneB, 1, i));
            addWall(ModFluffBlocks.biomeStoneWall, i, new ItemStack(ModFluffBlocks.biomeStoneA, 1, i));
        }

        // Portugese Pavement Recipes
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 0), LibOreDict.LIVING_ROCK, "cobblestone", "gravel");
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 1),
                LibOreDict.LIVING_ROCK,
                "cobblestone",
                "gravel",
                new ItemStack(Items.coal));
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 2),
                LibOreDict.LIVING_ROCK,
                "cobblestone",
                "gravel",
                new ItemStack(Items.dye, 1, 4));
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 3),
                LibOreDict.LIVING_ROCK,
                "cobblestone",
                "gravel",
                new ItemStack(Items.redstone));
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 4),
                LibOreDict.LIVING_ROCK,
                "cobblestone",
                "gravel",
                new ItemStack(Items.wheat));
        addShapelessOreDictRecipe(
                new ItemStack(ModFluffBlocks.pavement, 3, 5),
                LibOreDict.LIVING_ROCK,
                "cobblestone",
                "gravel",
                new ItemStack(Items.slime_ball));
        ModCraftingRecipes.recipesPavement = BotaniaAPI.getLatestAddedRecipes(6);

        for (int i = 0; i < ModFluffBlocks.pavementStairs.length; i++)
            GregtechPatches.addStairs(ModFluffBlocks.pavementStairs[i], new ItemStack(ModFluffBlocks.pavement, 1, i));
    }

    public static void addOreDictRecipe(ItemStack output, Object... recipe) {
        CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, recipe));
    }

    public static void addShapelessOreDictRecipe(ItemStack output, Object... recipe) {
        CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, recipe));
    }

    public static void addShapelessRecipe(ItemStack output, ItemStack... recipe) {
        CraftingManager.getInstance().getRecipeList().add(new ShapelessRecipes(output, Arrays.asList(recipe)));
    }

    public static void addWall(Block output, int meta, Object input) {
        addOreDictRecipe(new ItemStack(output, 6, meta), "SSS", "SSS", 'S', input);
    }
}
