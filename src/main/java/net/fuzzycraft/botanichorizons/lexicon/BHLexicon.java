package net.fuzzycraft.botanichorizons.lexicon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.BHItems;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.lexicon.page.PageCraftingRecipe;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageText;

public final class BHLexicon {
    private BHLexicon() {}

    public static LexiconCategory multiblockCategory;

    public static LexiconEntry multiblockIntro;
    public static LexiconEntry automatedManaPool;
    public static LexiconEntry automatedPortal;
    public static LexiconEntry basicWrenches;
    public static LexiconEntry multiblockWrenches;

    public static void preInit() {
        multiblockCategory = new LexiconCategory("botanichorizons.lexicon.category.multiblock")
                .setPriority(2);
        BotaniaAPI.addCategory(multiblockCategory);
    }

    public static void init() {
        LexiconEntry titlePage = new BHLexiconEntry("multiblockInfo", multiblockCategory);
        titlePage.addPage(new PageText("botanichorizons.lexicon.text.multiblockInfo.1"));
        titlePage.setPriority();
        titlePage.setIcon(new ItemStack(Items.writable_book));
        BHLexicon.multiblockIntro = titlePage;

        LexiconEntry poolEntry = new BHLexiconEntry("automatedManaPool", multiblockCategory);
        poolEntry.addPage(new PageText("botanichorizons.lexicon.text.automatedManaPool.1"));
        poolEntry.addPage(new PageText("botanichorizons.lexicon.text.automatedManaPool.2"));
        poolEntry.addPage(new PageCraftingRecipe("botanichorizons.lexicon.crafting.automatedCraftingPool", BHRecipes.poolInfusionRecipe));
        poolEntry.addPage(new PageMultiblock("botanichorizons.lexicon.preview.automatedCraftingPool",
                Multiblocks.poolInfusion.lexiconMultiblock(
                        0, 1, -2,
                        BHBlocks.autoPoolInfusion, 0
                )));
        poolEntry.addPage(new PageCraftingRecipe("botanichorizons.lexicon.crafting.automatedAlchemyPool", BHRecipes.poolAlchemyRecipe));
        poolEntry.addPage(new PageMultiblock("botanichorizons.lexicon.preview.automatedAlchemyPool",
                Multiblocks.poolAlchemy.lexiconMultiblock(
                        0, 1, -2,
                        BHBlocks.autoPoolAlchemy, 0
                )));
        poolEntry.addPage(new PageCraftingRecipe("botanichorizons.lexicon.crafting.automatedConjurationPool", BHRecipes.poolConjurationRecipe));
        poolEntry.addPage(new PageMultiblock("botanichorizons.lexicon.preview.automatedConjurationPool",
                Multiblocks.poolConjuration.lexiconMultiblock(
                        0, 1, -2,
                        BHBlocks.autoPoolConjuration, 0
                )));
        poolEntry.setIcon(new ItemStack(ModBlocks.pool, 1, Constants.POOL_META_REGULAR_FABULOUS));
        BHLexicon.automatedManaPool = poolEntry;

        LexiconEntry gatewayEntry = new BHLexiconEntry("automatedAlfPortal", multiblockCategory);
        gatewayEntry.setKnowledgeType(BotaniaAPI.elvenKnowledge);
        gatewayEntry.addPage(new PageText("botanichorizons.lexicon.text.automatedAlfPortal.1"));
        gatewayEntry.addPage(new PageText("botanichorizons.lexicon.text.automatedAlfPortal.2"));
        gatewayEntry.addPage(new PageMultiblock("botanichorizons.lexicon.preview.automatedAlfPortal",
                Multiblocks.alfPortal.lexiconMultiblock(
                        0, 1, -2,
                        BHBlocks.autoPortal, 0
                )));
        gatewayEntry.setIcon(new ItemStack(BHBlocks.autoPortal));
        BHLexicon.automatedPortal = gatewayEntry;

        LexiconEntry wrenchEntry = new BHLexiconEntry("basicWrenches", BotaniaAPI.categoryTools);
        wrenchEntry.addPage(new PageText("botanichorizons.lexicon.text.basicWrenches.1"));
        wrenchEntry.setIcon(new ItemStack(BHItems.manasteelWrench));
        BHLexicon.basicWrenches = wrenchEntry;

        LexiconEntry wrenchXLEntry = new BHLexiconEntry("multiblockWrenches", multiblockCategory);
        wrenchXLEntry.addPage(new PageText("botanichorizons.lexicon.text.multiblockWrenches.1"));
        wrenchXLEntry.addPage(new PageText("botanichorizons.lexicon.text.multiblockWrenches.2"));
        wrenchXLEntry.addPage(new PageText("botanichorizons.lexicon.text.multiblockWrenches.3"));
        wrenchXLEntry.setIcon(new ItemStack(BHItems.disassemblyWrench));
        BHLexicon.multiblockWrenches = wrenchEntry;
    }

    @SideOnly(Side.CLIENT)
    public static void clientInit() {
        multiblockCategory.setIcon(new ResourceLocation(ForgeMod.MOD_ID, "textures/gui/mechanisation.png"));
    }
}
