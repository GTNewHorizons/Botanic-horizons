package net.fuzzycraft.botanichorizons.lexicon;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ITwoNamedPage;
import vazkii.botania.api.lexicon.LexiconCategory;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.lexicon.LexiconPage;
import vazkii.botania.common.lexicon.WLexiconEntry;

public class BHLexiconEntry extends LexiconEntry {
    public BHLexiconEntry(String shortName, LexiconCategory category) {
        super(shortName, category);
        BotaniaAPI.addEntry(this, category);
    }

    public LexiconEntry setLexiconPages(LexiconPage... pages) {
        for(LexiconPage page : pages) {
            page.unlocalizedName = "botanichorizons.lexicon.page." + this.getLazyUnlocalizedName() + page.unlocalizedName;
            if (page instanceof ITwoNamedPage) {
                ITwoNamedPage dou = (ITwoNamedPage)page;
                dou.setSecondUnlocalizedName("botanichorizons.lexicon.page." + this.getLazyUnlocalizedName() + dou.getSecondUnlocalizedName());
            }
        }

        return super.setLexiconPages(pages);
    }

    public String getUnlocalizedName() {
        return "botanichorizons.lexicon.entry." + super.getUnlocalizedName();
    }

    public String getTagline() {
        return "botanichorizons.lexicon.tagline." + super.getUnlocalizedName();
    }

    public String getLazyUnlocalizedName() {
        return super.getUnlocalizedName();
    }

    public String getWebLink() {
        // For those interested in writing these pages
        return "https://wiki.gtnewhorizons.com/wiki/BotanicHorizonsLexicon/" + this.unlocalizedName;
    }

    public int compareTo(LexiconEntry o) {
        return o instanceof WLexiconEntry ? 1 : super.compareTo(o);
    }
}