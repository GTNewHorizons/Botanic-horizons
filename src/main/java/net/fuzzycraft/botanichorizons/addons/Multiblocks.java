package net.fuzzycraft.botanichorizons.addons;

import gregtech.api.enums.Mods;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlchemyPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAltar;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedApothecary;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedConjurationPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedCraftingPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedTerraPlate;
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
    public static MultiblockHelper altar;
    public static MultiblockHelper apothecary;
    public static MultiblockHelper poolConjuration;
    public static MultiblockHelper poolAlchemy;
    public static MultiblockHelper poolInfusion;
    public static MultiblockHelper terraPlate;

    public static MultiblockHelper placeholder;

    public static void init() {
        MultiblockCheck air = new BasicBlockCheck(Blocks.air);
        MultiblockCheck livingWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_BLOCK);
        MultiblockCheck livingRock = new MetaBlockCheck(ModBlocks.livingrock, Constants.LIVINGSTONE_META_BLOCK);
        MultiblockCheck livingRockSlab = new MetaBlockCheck(ModFluffBlocks.livingrockSlab, 0);
        MultiblockCheck glimmerWood = new MetaBlockCheck(ModBlocks.livingwood, Constants.LIVINGWOOD_META_GLIMMERING);
        MultiblockCheck manaPylon = new MetaBlockCheck(ModBlocks.pylon, Constants.PYLON_META_MANA);
        MultiblockCheck naturaPylon = new MetaBlockCheck(ModBlocks.pylon, Constants.PYLON_META_NATURA);
        MultiblockCheck manaPool = new BasicBlockCheck(ModBlocks.pool);
        MultiblockCheck lapisBlock = new BasicBlockCheck(Blocks.lapis_block);
        MultiblockCheck manaBlock = new MetaBlockCheck(ModBlocks.storage, Constants.STORAGE_META_MANASTEELBLOCK);
        MultiblockCheck terraBlock = new MetaBlockCheck(ModBlocks.storage, Constants.STORAGE_META_ELEMENTIUMBLOCK);
        MultiblockCheck dirt = new MetaBlockCheck(Blocks.dirt, 0);
        MultiblockCheck melon = new BasicBlockCheck(Blocks.melon_block);
        MultiblockCheck pumpkin = new BasicBlockCheck(Blocks.lit_pumpkin);
        MultiblockCheck water = new BasicBlockCheck(Blocks.water);

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

        // Terrestrial Agglomeration plate

        MultiblockBuilder terraBuilder = new MultiblockBuilder();
        terraBuilder.setRootCharacter("x");
        terraBuilder.addCheck(".", air);
        terraBuilder.addCheck("b", lapisBlock);
        terraBuilder.addCheck("w", livingRock);
        terraBuilder.addCheck("g", terraBlock);
        terraBuilder.addCheck("p", manaPylon);

        terraPlate = terraBuilder.buildForMap(
                new String[]{
                    "         ", "         ", "         ", "   ...   ", "   ...   ", "   ...   ", "         ", "         ", "         "
                },
                new String[]{
                    "         ", " p     p ", "         ", "   bbb   ", "   bgb   ", "   bbb   ", "         ", " p     p ", "         "
                },
                new String[]{
                    "bbbwwwbbb", "bbbwwwbbb", "bbbwwwbbb", "wwwbbbwww", "wwwbbbwww", "wwwbbbwww", "bbbwwwbbb", "bbbwwwbbb", "bbbwxwbbb"
                }
        );

        // Petal apothecary

        MultiblockBuilder apothecaryBuilder = new MultiblockBuilder();
        apothecaryBuilder.setRootCharacter("x");
        apothecaryBuilder.addCheck(".", air);
        apothecaryBuilder.addCheck("w", livingWood);
        apothecaryBuilder.addCheck("r", livingRock);
        apothecaryBuilder.addCheck("W", water);
        apothecaryBuilder.addCheck("d", dirt);
        apothecaryBuilder.addCheck("P", pumpkin);
        apothecaryBuilder.addCheck("M", melon);

        apothecary = apothecaryBuilder.buildForMap(
                new String[]{
                    "wrrrwrrrw", "         ", "         ", "         ", "         "
                },
                new String[]{
                    "wrrrwrrrw", "         ", " P M ... ", "     ... ", "         "
                },
                new String[]{
                    "wrrrwrrrw", "rdddrWWWr", "rdddrWWWr", "rdddrWWWr", "rrrrxrrrr"
                },
                new String[]{
                    "wrrrwrrrw", " rrr rrr ", " rrr rrr ", " rrr rrr ", "         "
                }
        );

        // Altar

        MultiblockBuilder altarBuilder = new MultiblockBuilder();
        altarBuilder.setRootCharacter("x");
        altarBuilder.addCheck("r", livingRock);
        altarBuilder.addCheck("s", livingRockSlab);
        altarBuilder.addCheck("m", manaBlock);

        altar = altarBuilder.buildForMap(
                new String[]{
                        "       ", "       ", "       ", "   x   ", "       ", "       ", "       "
                },
                new String[]{
                        "       ", " sssss ", " smrms ", " sr rs ", " smrms ", " sssss ", "       "
                },
                new String[]{
                        "mrrrrrm", "r     r", "r     r", "r     r", "r     r", "r     r", "mrrrrrm"
                }
        );


        // For new multiblocks

        MultiblockBuilder tempBuilder = new MultiblockBuilder();
        tempBuilder.setRootCharacter("x");
        tempBuilder.addCheck("w", livingWood);
        tempBuilder.addCheck("r", livingRock);
        placeholder = tempBuilder.buildForMap(
                new String[] {
                        "rwr", "w w", "rxr"
                }
        );

    }

    public static void postInit() {
        if (Mods.StructureLib.isModLoaded()) {
            HoloProjectorSupport.registerOrientedWithStructureLib(poolInfusion, BHBlocks.autoPoolInfusion, TileAdvancedCraftingPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(poolAlchemy, BHBlocks.autoPoolAlchemy, TileAdvancedAlchemyPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(poolConjuration, BHBlocks.autoPoolConjuration, TileAdvancedConjurationPool.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(alfPortal, BHBlocks.autoPortal, TileAdvancedAlfPortal.class);

            HoloProjectorSupport.registerOrientedWithStructureLib(apothecary, BHBlocks.autoApothecary, TileAdvancedApothecary.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(altar, BHBlocks.autoAltar, TileAdvancedAltar.class);
            HoloProjectorSupport.registerOrientedWithStructureLib(terraPlate, BHBlocks.autoPlate, TileAdvancedTerraPlate.class);
        }
    }
}
