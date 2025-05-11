package net.fuzzycraft.botanichorizons.lexicon;

import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.lexicon.page.PageGuide;
import vazkii.botania.common.lexicon.page.PageMultiblock;
import vazkii.botania.common.lexicon.page.PageText;

public final class BHLexicon {
    private BHLexicon() {}

    public static LexiconCategory multiblockCategory;

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

        LexiconEntry gatewayEntry = new BHLexiconEntry("automatedAlfPortal", multiblockCategory);
        gatewayEntry.addPage(new PageText("botanichorizons.lexicon.text.automatedAlfPortal.1"));
        gatewayEntry.addPage(new PageMultiblock("botanichorizons.lexicon.preview.automatedAlfPortal",
                Multiblocks.alfPortal.lexiconMultiblock(
                        0, 1, -2,
                        BHBlocks.autoPortal, 0
                )));
        gatewayEntry.setIcon(new ItemStack(BHBlocks.autoPortal));
    }
}
