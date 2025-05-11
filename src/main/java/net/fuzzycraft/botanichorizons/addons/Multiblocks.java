package net.fuzzycraft.botanichorizons.addons;

import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.multiblock.BasicBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MetaBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockBuilder;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.init.Blocks;
import vazkii.botania.common.block.ModBlocks;

/**
 * Holds definitions for all multiblock structures
 */
public final class Multiblocks {

    public static MultiblockHelper alfPortal;

    public static void init() {
        MultiblockCheck air = new BasicBlockCheck(Blocks.air);
        MultiblockCheck livingWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_BLOCK);
        MultiblockCheck glimmerWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_GLIMMERING);
        MultiblockCheck naturaPylon = new MetaBlockCheck(ModBlocks.pylon, Constants.PYLON_META_NATURA);

        MultiblockBuilder builder = new MultiblockBuilder();
        builder.setRootCharacter("x");
        builder.addCheck(".", air);
        builder.addCheck("l", livingWood);
        builder.addCheck("g", glimmerWood);
        builder.addCheck("n", naturaPylon);

        alfPortal = builder.buildForMap(
                new String[] {
                    "       ", "       ", "  lll  ", "  lnl  ", "  lll  ", "       ", "       "
                },
                new String[] {
                    "       ", "  l l  ", " l   l ", "       ", " l   l ", "  l l  ", "       "
                },
                new String[] {
                    "  l l  ", "       ", "l     l", "       ", "l     l", "       ", "  l l  "
                },
                new String[] {
                    "n l l n", "       ", "l     l", "       ", "l     l", "       ", "n l l n"
                },
                new String[] {
                    "llglgll", "l.....l", "g.....g", "l.....l", "g.....g", "l.....l", "llgxgll"
                },
                new String[] {
                    "       ", " lllll ", " llgll ", " lglgl ", " llgll ", " lllll ", "       "
                }
        );
    }
}
