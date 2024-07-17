package net.fuzzycraft.botanichorizons.patches;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_Utility;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;
import vazkii.botania.common.crafting.ModCraftingRecipes;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibOreDict;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;


public class GregtechPatches {
    public static void applyPatches() {

        // GT mortar for petals into floral powder
        for (int i = 0; i < 16; i++) {
            ItemStack flower = new ItemStack(ModBlocks.flower, 1, i);
            ItemStack petal = new ItemStack(ModItems.petal, 1, i);
            ItemStack powder = new ItemStack(ModItems.dye, 1, i);

            // petals
            GT_ModHandler.addShapelessCraftingRecipe(powder, GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{ToolDictNames.craftingToolMortar, petal});
            addIC2MacerationRecipe(powder, petal);

            // flowers
            GT_ModHandler.addShapelessCraftingRecipe(powder, GT_ModHandler.RecipeBits.NOT_REMOVABLE, new Object[]{ToolDictNames.craftingToolMortar, flower});
            addIC2MacerationRecipe(powder, flower);
        }

        // Petal Block from 9x in compressor + reverse in extractor
        for(int i = 0; i < 16; i++) {
            compressAndExtract(new ItemStack(ModItems.petal, 9, i), new ItemStack(ModBlocks.petalBlock, 1, i));
        }

        // Petals from flowers
        for(int i = 0; i < 16; i++) {
            addIC2ExtractorRecipe(new ItemStack(ModItems.petal, 2, i), new ItemStack(ModBlocks.flower, 1, i));

            for (ItemStack doubleFlowerPart : OreDictionary.getOres(LibOreDict.DOUBLE_FLOWER[i])) {
                addIC2ExtractorRecipe(new ItemStack(ModItems.petal, 4, i), doubleFlowerPart);
            }
        }

        // Better floral fertiliser
        List<ItemStack> mutandis = OreDictionary.getOres("itemMutandis");
        List<ItemStack> dustEV = OreDictionary.getOres("dustDraconium");
        for (ItemStack option: mutandis) {
            addMixerRecipe(new ItemStack(ModItems.fertilizer, 2, 0), 16, 80, option, new ItemStack(Items.dye, 1, 15));
        }
        for (ItemStack option: dustEV) {
            addMixerRecipe(new ItemStack(ModItems.fertilizer, 2, 0), 16, 80, option, new ItemStack(Items.dye, 1, 15));
        }

        // Slabs
        addSlabRecipe(new ItemStack(ModFluffBlocks.dirtPathSlab, 2),          new ItemStack(ModBlocks.dirtPath, 1),             1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.livingrockSlab, 2),        new ItemStack(ModBlocks.livingrock, 1),           1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.prismarineSlab, 2),        new ItemStack(ModBlocks.prismarine, 1),           1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.reedSlab, 2),              new ItemStack(ModBlocks.reedBlock, 1),            1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.shimmerrockSlab, 2),       new ItemStack(ModBlocks.shimmerrock, 1),          1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.shimmerwoodPlankSlab, 2),  new ItemStack(ModBlocks.shimmerwoodPlanks, 1),    1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.thatchSlab, 2),            new ItemStack(ModBlocks.thatch, 1),               1, 16, 80);

        addSlabRecipe(new ItemStack(ModFluffBlocks.livingrockSlab, 2),      new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BLOCK), 1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.livingrockBrickSlab, 2), new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BRICK), 1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.prismarineSlab, 2),      new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_BLOCK),  1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.prismarineBrickSlab, 2), new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_BRICK),  1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.darkPrismarineSlab, 2),  new ItemStack(ModBlocks.prismarine, 1, Constants.PRISMARINE_META_DARK),   1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.endStoneSlab, 2),        new ItemStack(ModBlocks.endStoneBrick, 1, Constants.ENDSTONE_META_BRICK_YELLOW), 1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.enderBrickSlab, 2),      new ItemStack(ModBlocks.endStoneBrick, 1, Constants.ENDSTONE_META_BRICK_PURPLE), 1, 16, 80);

        addSlabRecipe(new ItemStack(ModFluffBlocks.netherBrickSlab, 2), new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_HELL), 1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.soulBrickSlab, 2),   new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_SOUL), 1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.snowBrickSlab, 2),   new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_FROST),1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.tileSlab, 2),        new ItemStack(ModBlocks.customBrick, 1, Constants.BRICK_META_ROOF), 1, 16, 80);

        for (int i = 0; i < 4; i++) {
            addSlabRecipe(new ItemStack(ModFluffBlocks.stoneSlabs[i + 0], 2), new ItemStack(ModFluffBlocks.stone, 1, i + 0), 1, 16, 80);
            addSlabRecipe(new ItemStack(ModFluffBlocks.stoneSlabs[i + 4], 2), new ItemStack(ModFluffBlocks.stone, 1, i + 8), 1, 16, 80);
        }
        for(int i = 0; i < 8; i++) {
            addSlabRecipe(new ItemStack(ModFluffBlocks.biomeStoneSlabs[i],    2), new ItemStack(ModFluffBlocks.biomeStoneA, 1, i), 1, 16, 80);
            addSlabRecipe(new ItemStack(ModFluffBlocks.biomeStoneSlabs[i+8],  2), new ItemStack(ModFluffBlocks.biomeStoneA, 1, i + 8), 1, 16, 80);
            addSlabRecipe(new ItemStack(ModFluffBlocks.biomeStoneSlabs[i+16], 2), new ItemStack(ModFluffBlocks.biomeStoneB, 1, i), 1, 16, 80);
        }
        for (int i = 0; i < ModFluffBlocks.pavementStairs.length; i++)
            addSlabRecipe(new ItemStack(ModFluffBlocks.pavementSlabs[i], 2), new ItemStack(ModFluffBlocks.pavement, 1, i), 1, 16, 80);


        // Blocks to either planks or slabs
        ModCraftingRecipes.recipeLivingwoodDecor1 = addSlabRecipe(new ItemStack(ModBlocks.livingwood, 4, Constants.LIVINGWOOD_META_PLANK), new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_BLOCK), 2, 16, 200);
        addSlabRecipe(new ItemStack(ModBlocks.dreamwood,  4, Constants.LIVINGWOOD_META_PLANK), new ItemStack(ModBlocks.dreamwood, 1,  Constants.LIVINGWOOD_META_BLOCK), 2, 16, 200);
        addSlabRecipe(new ItemStack(ModFluffBlocks.livingwoodSlab, 2),      new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_BLOCK),  1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.livingwoodPlankSlab, 2), new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_PLANK),  1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.dreamwoodSlab, 2),      new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_BLOCK),   1, 16, 80);
        addSlabRecipe(new ItemStack(ModFluffBlocks.dreamwoodPlankSlab, 2), new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK),   1, 16, 80);
        ModCraftingRecipes.recipeLivingwoodTwig = addSlabRecipe(new ItemStack(ModItems.manaResource, 2, Constants.MANARESOURCE_META_TWIG_WOOD), new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_PLANK), 2, 16, 200);
        ModCraftingRecipes.recipeDreamwoodTwig = addSlabRecipe(new ItemStack(ModItems.manaResource, 2, Constants.MANARESOURCE_META_TWIG_DREAM), new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK), 2, 16, 200);
        addStairs(ModFluffBlocks.livingrockStairs, new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BLOCK));
        addStairs(ModFluffBlocks.livingrockBrickStairs, new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BRICK));
        addStairs(ModFluffBlocks.livingwoodStairs, new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_BLOCK));
        addStairs(ModFluffBlocks.livingwoodPlankStairs, new ItemStack(ModBlocks.livingwood, 1, Constants.LIVINGWOOD_META_PLANK));
        addStairs(ModFluffBlocks.dreamwoodStairs, new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_BLOCK));
        addStairs(ModFluffBlocks.dreamwoodPlankStairs, new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK));
        addSlabRecipe(new ItemStack(ModFluffBlocks.dreamwoodPlankSlab, 2), new ItemStack(ModBlocks.dreamwood, 1, Constants.LIVINGWOOD_META_PLANK),   1, 16, 80);

        // Cracked blocks
        ModCraftingRecipes.recipeLivingrockDecor3 = addHammerRecipe(new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_CRACKED), new ItemStack(ModBlocks.livingrock, 1, Constants.LIVINGSTONE_META_BRICK));

        // Quartzes and derivatives
        ModCraftingRecipes.recipeDarkQuartz = addQuartzRecipes(Constants.QUARTZ_META_DARK, new ItemStack(Items.coal, 1, Short.MAX_VALUE), ModFluffBlocks.darkQuartz, ModFluffBlocks.darkQuartzStairs, ModFluffBlocks.darkQuartzSlab);
        addQuartzRecipes(Constants.QUARTZ_META_MANA, null, ModFluffBlocks.manaQuartz, ModFluffBlocks.manaQuartzStairs, ModFluffBlocks.manaQuartzSlab);
        ModCraftingRecipes.recipeBlazeQuartz = addQuartzRecipes(Constants.QUARTZ_META_BLAZE, new ItemStack(Items.blaze_powder), ModFluffBlocks.blazeQuartz, ModFluffBlocks.blazeQuartzStairs, ModFluffBlocks.blazeQuartzSlab);
        ModCraftingRecipes.recipesLavenderQuartz = Collections.singletonList(addQuartzRecipes(Constants.QUARTZ_META_LAVENDER, "flowerLavender", ModFluffBlocks.lavenderQuartz, ModFluffBlocks.lavenderQuartzStairs, ModFluffBlocks.lavenderQuartzSlab));
        ModCraftingRecipes.recipeRedQuartz = addQuartzRecipes(Constants.QUARTZ_META_RED, "dustRedstone", ModFluffBlocks.redQuartz, ModFluffBlocks.redQuartzStairs, ModFluffBlocks.redQuartzSlab);
        addQuartzRecipes(Constants.QUARTZ_META_ELVEN, null, ModFluffBlocks.elfQuartz, ModFluffBlocks.elfQuartzStairs, ModFluffBlocks.elfQuartzSlab);
        ModCraftingRecipes.recipeSunnyQuartz = addQuartzRecipes(Constants.QUARTZ_META_SUNNY, new ItemStack(Blocks.double_plant, 1, 0), ModFluffBlocks.sunnyQuartz, ModFluffBlocks.sunnyQuartzStairs, ModFluffBlocks.sunnyQuartzSlab);


        // Ingots into blocks. Wouldn't be surprised if this becomes unnecessary when GT provides its own ingots
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_MANASTEEL),
                new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_MANASTEELBLOCK)
        );
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_TERRASTEEL),
                new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_TERRASTEELBLOCK)
        );
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_ELEMENTIUM),
                new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_ELEMENTIUMBLOCK)
        );
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_DRAGONSTONE),
                new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DRAGONSTONEBLOCK)
        );
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_DIAMOND),
                new ItemStack(ModBlocks.storage, 1, Constants.STORAGE_META_DIAMONDBLOCK)
        );
        compressAndExtract(
                new ItemStack(ModItems.manaResource, 9, Constants.MANARESOURCE_META_PRISMARINE),
                new ItemStack(ModBlocks.prismarine)
        );

        // Reeds compress to plantballs by default
        addIC2ExtractorRecipe(new ItemStack(Items.reeds, 8), new ItemStack(ModBlocks.reedBlock));
        GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(Items.reeds, 8), GT_Utility.getIntegratedCircuit(16))
                .itemOutputs(new ItemStack(ModBlocks.reedBlock))
                .duration(4*SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

        // Livingwood and Crystal Bows
        GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(ModItems.manaResource, 3, Constants.MANARESOURCE_META_TWIG_WOOD),
                        new ItemStack(ModItems.manaResource, 3, Constants.MANARESOURCE_META_STRING),
                        GT_Utility.getIntegratedCircuit(1))
                .itemOutputs(new ItemStack(ModItems.livingwoodBow))
                .duration(4*SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(ModItems.manaResource, 3, Constants.MANARESOURCE_META_TWIG_DREAM),
                        new ItemStack(ModItems.manaResource, 3, Constants.MANARESOURCE_META_STRING),
                        new ItemStack(ModItems.manaResource, 2, Constants.MANARESOURCE_META_DRAGONSTONE))
                .itemOutputs(new ItemStack(ModItems.crystalBow))
                .duration(4*SECONDS)
                .eut(24)
                .addTo(assemblerRecipes);

        // Decorative baubles
        for(int i = 0; i < 32; i++) {
            Item choice = (i < 16) ? ModItems.petal : ModItems.dye;
            ItemStack fabric = new ItemStack(ModItems.manaResource, 2, Constants.MANARESOURCE_META_CLOTH);
            ItemStack output = new ItemStack(ModItems.cosmetic, 1, i);
            CraftingPatches.addOreDictRecipe(output,
                    "SPS", "PSP", "SPS",
                    'P', new ItemStack(choice, 1, i % 16),
                    'S', LibOreDict.MANAWEAVE_CLOTH
            );
            GT_Values.RA.stdBuilder()
                    .itemInputs(fabric, new ItemStack(choice, 2, i % 16), GT_Utility.getIntegratedCircuit(4))
                    .itemOutputs(output)
                    .duration(6*SECONDS)
                    .eut(80)
                    .addTo(assemblerRecipes);

            addIC2ExtractorRecipe(fabric, new ItemStack(ModItems.cosmetic, 1, i));
        }
        ModCraftingRecipes.recipesCosmeticItems = BotaniaAPI.getLatestAddedRecipes(32);

        // glass panes
        addGlassPane(ModFluffBlocks.managlassPane, ModBlocks.manaGlass);
        addGlassPane(ModFluffBlocks.alfglassPane, ModBlocks.elfGlass);
        addGlassPane(ModFluffBlocks.bifrostPane, ModBlocks.bifrostPerm);

    }

    private static void addMixerRecipe(ItemStack output, int volt, int ticks, ItemStack... input) {
        GT_Values.RA.stdBuilder()
                .itemInputs(input)
                .itemOutputs(output)
                .duration(ticks)
                .eut(volt)
                .addTo(RecipeMaps.mixerRecipes);
    }

    private static void addIC2MacerationRecipe(ItemStack output, ItemStack input) {
        GT_Values.RA.stdBuilder()
                .itemInputs(input)
                .itemOutputs(output)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(RecipeMaps.maceratorRecipes);
    }

    private static void addIC2ExtractorRecipe(ItemStack output, ItemStack input) {
        GT_Values.RA.stdBuilder()
                .itemInputs(input)
                .itemOutputs(output)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(RecipeMaps.extractorRecipes);
    }

    private static void addIC2CompressorRecipe(ItemStack output, ItemStack input) {
        GT_Values.RA.stdBuilder()
                .itemInputs(input)
                .itemOutputs(output)
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(RecipeMaps.compressorRecipes);
    }

    private static void compressAndExtract(ItemStack uncompressed, ItemStack compressed) {
        addIC2ExtractorRecipe(uncompressed, compressed);
        addIC2CompressorRecipe(compressed, uncompressed);
    }

    @Nullable
    private static IRecipe addSlabRecipe(ItemStack output, ItemStack input, int circuit, int volt, int ticks) {
        ItemStack[] inputs;
        if (circuit == 0) {
            inputs = new ItemStack[]{input};
        } else {
            ItemStack circuitStack = GT_Utility.getIntegratedCircuit(circuit);
            inputs = new ItemStack[]{input, circuitStack};
        }
        GT_Values.RA.stdBuilder()
                        .itemInputs(inputs)
                        .itemOutputs(output)
                        .duration(ticks)
                        .eut(volt)
                        .addTo(cutterRecipes);

        if (volt < 32 && (output.stackSize % 2) == 0) {
            ItemStack half_output = new ItemStack(output.getItem(), output.stackSize / 2, output.getItemDamage());
            String r1 = (circuit <= 1) ? "sR" : "s ";
            String r2 = (circuit <= 1) ? "  " : "R ";
            GT_ModHandler.addCraftingRecipe(
                    half_output,
                    new Object[]{
                            r1, r2,
                            'R', input
                    }
            );
            return new ShapedOreRecipe(
                    half_output,
                    r1, r2,
                    's', ToolDictNames.craftingToolSaw.name(),
                    'R', input
            );
        } else return null;
    }

    public static IRecipe addHammerRecipe(ItemStack output, ItemStack input) {
        GT_Values.RA.stdBuilder()
                .itemInputs(input)
                .itemOutputs(output)
                .duration(32 * GT_RecipeBuilder.TICKS)
                .eut(4)
                .addTo(RecipeMaps.hammerRecipes);

        GT_ModHandler.addCraftingRecipe(
                output,
                new Object[]{
                        "h", "R",
                        'R', input
                }
        );
        return new ShapedOreRecipe(
                output, "h", "R",
                'R', input,
                'h', ToolDictNames.craftingToolHardHammer.name()
        );
    }

    public static IRecipe addQuartzRecipes(int quartzMeta, @Nullable Object ingredient, Block block, Block stairs, Block slab) {

        // quartz-to-block and vice versa
        addIC2CompressorRecipe(new ItemStack(block), new ItemStack(ModItems.quartz, 4, quartzMeta));
        for (int i = 0; i < 3; i++) {
            addIC2ExtractorRecipe(new ItemStack(ModItems.quartz, 4, quartzMeta), new ItemStack(block, 1, i));
        }

        addSlabRecipe(new ItemStack(slab, 2, 0), new ItemStack(block, 1, 0), 1, 16, 80);

        // deco flavours
        GameRegistry.addRecipe(new ItemStack(block, 4, 1), "QQ", "QQ", 'Q', block); // chisel
        GameRegistry.addRecipe(new ItemStack(block, 1, 2), "Q", "Q", 'Q', slab); // pillar
        addStairs(stairs, block);

        // un-deco
        for (int i = 1; i < 3; i++) {
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(block, 1, 0),
                    "Qf",
                    'Q', new ItemStack(block, 1, i),
                    'f', ToolDictNames.craftingToolFile.name()
            ));
        }

        if (ingredient != null) {
            if (quartzMeta != Constants.QUARTZ_META_DARK) {
                GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.quartz, 8, quartzMeta),
                        "QQQ", "QCQ", "QQQ",
                        'Q', "gemQuartz",
                        'C', ingredient));
            } else {
                // Smokey Quartz - deduplicate with Thaumic Tinkerer
                GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(Items.quartz, 8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1))
                        .itemOutputs(new ItemStack(ModItems.quartz, 8))
                        .duration(20 * SECONDS)
                        .eut(16)
                        .addTo(RecipeMaps.alloySmelterRecipes);
                GT_Values.RA.stdBuilder()
                        .itemInputs(new ItemStack(Items.quartz, 8), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Charcoal, 1))
                        .itemOutputs(new ItemStack(ModItems.quartz, 8))
                        .duration(20 * SECONDS)
                        .eut(16)
                        .addTo(RecipeMaps.alloySmelterRecipes);
               }
            return BotaniaAPI.getLatestAddedRecipe();
        } else {
            return null;
        }
    }

    public static void addGlassPane(Block paneBlock, Block inputBlock) {
        GT_Values.RA.stdBuilder()
                .itemInputs(new ItemStack(inputBlock, 3))
                .itemOutputs(new ItemStack(paneBlock, 8))
                .duration(2*SECONDS+10*TICKS)
                .eut(7)
                .addTo(cutterRecipes);

        GT_ModHandler.addCraftingRecipe(
                new ItemStack(paneBlock, 2),
                new Object[]{
                        "s", "G",
                        'G', new ItemStack(inputBlock, 1),
                        's', ToolDictNames.craftingToolSaw.name()
                }
        );
    }

    public static void addStairs(Block stairsBlock, Object baseBlock) {
        addStairs(stairsBlock, baseBlock, 3);
    }
    public static void addStairs(Block stairsBlock, Object baseBlock, int flags) {
        if ((flags & 1) > 0) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairsBlock, 4), "  Q", " QQ", "QQQ", 'Q', baseBlock).setMirrored(false));
        if ((flags & 2) > 0) GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(stairsBlock, 4), "Q  ", "QQ ", "QQQ", 'Q', baseBlock).setMirrored(false));
    }
}
