package net.fuzzycraft.botanichorizons.addons;

import gregtech.api.enums.Mods;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlchemyPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedConjurationPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedCraftingPool;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.multiblock.BasicBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MetaBlockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockBuilder;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockCheck;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.fuzzycraft.botanichorizons.util.structurelib.HoloProjectorSupport;
import net.minecraft.init.Blocks;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;

/**
 * Holds definitions for all multiblock structures
 */
public final class Multiblocks {

    public static MultiblockHelper alfPortal;
    public static MultiblockHelper poolConjuration;
    public static MultiblockHelper poolAlchemy;
    public static MultiblockHelper poolInfusion;

    public static void init() {
        MultiblockCheck air = new BasicBlockCheck(Blocks.air);
        MultiblockCheck livingWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_BLOCK);
        MultiblockCheck livingRock = new MetaBlockCheck(ModBlocks.livingrock, Constants.LIVINGSTONE_META_BLOCK);
        MultiblockCheck glimmerWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_GLIMMERING);
        MultiblockCheck naturaPylon = new MetaBlockCheck(ModBlocks.pylon, Constants.PYLON_META_NATURA);
        MultiblockCheck manaPool = new BasicBlockCheck(ModBlocks.pool);

        // Mana pool

        String[][] poolSharedTemplate = new String[][] {
                new String[] {
                        "     ", "     ", "  p  ", "     ", "     ", "     ", "     "
                },
                new String[] {
                        "sssss", "s   s", "s s s", "s   s", "sssss", " hfh ", "  x  "
                },
                new String[] {
                        "     ", " sss ", " sss ", " sss ", " fff ", " fff ", " f f "
                }
        };

        for (int type = 0; type < 3; type++) {
            MultiblockBuilder poolBuilder = new MultiblockBuilder();
            poolBuilder.setRootCharacter("x");
            poolBuilder.addCheck("s", livingRock);
            poolBuilder.addCheck("p", manaPool);

            if (type == 0) {
                poolBuilder.addCheck("f", new MetaBlockCheck(ModFluffBlocks.manaQuartz, Constants.QUARTZBLOCK_META_CHISELED));
                poolBuilder.addCheck("h", new MetaBlockCheck(ModFluffBlocks.manaQuartzSlab, 0));
                poolInfusion = poolBuilder.buildForMap(poolSharedTemplate);
            } else if (type == 1) {
                poolBuilder.addCheck("f", new MetaBlockCheck(ModFluffBlocks.sunnyQuartz, Constants.QUARTZBLOCK_META_CHISELED));
                poolBuilder.addCheck("h", new MetaBlockCheck(ModFluffBlocks.sunnyQuartzSlab, 0));
                poolAlchemy = poolBuilder.buildForMap(poolSharedTemplate);
            } else if (type == 2) {
                poolBuilder.addCheck("f", new MetaBlockCheck(ModFluffBlocks.elfQuartz, Constants.QUARTZBLOCK_META_CHISELED));
                poolBuilder.addCheck("h", new MetaBlockCheck(ModFluffBlocks.elfQuartzSlab, 0));
                poolConjuration = poolBuilder.buildForMap(poolSharedTemplate);
            }
        }

        // Elven portal

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

    public static void postInit() {
        if (Mods.StructureLib.isModLoaded()) {
            HoloProjectorSupport.registerOrientedWithStructureLib(poolInfusion, BHBlocks.autoPoolInfusion, TileAdvancedCraftingPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(poolAlchemy, BHBlocks.autoPoolAlchemy, TileAdvancedAlchemyPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(poolConjuration, BHBlocks.autoPoolConjuration, TileAdvancedConjurationPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(alfPortal, BHBlocks.autoPortal, TileAdvancedAlfPortal.class);
        }
    }
}
