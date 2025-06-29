package net.fuzzycraft.botanichorizons.addons;

import cpw.mods.fml.common.registry.GameRegistry;
import net.fuzzycraft.botanichorizons.addons.block.BlockAdvancedAlchemyPool;
import net.fuzzycraft.botanichorizons.addons.block.BlockAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.addons.block.BlockAdvancedConjurationPool;
import net.fuzzycraft.botanichorizons.addons.block.BlockAdvancedCraftingPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlchemyPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedConjurationPool;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedCraftingPool;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.minecraft.tileentity.TileEntity;

public final class BHBlocks {
    public static BlockAdvancedCraftingPool autoPoolInfusion;
    public static BlockAdvancedAlchemyPool autoPoolAlchemy;
    public static BlockAdvancedConjurationPool autoPoolConjuration;
    public static BlockAdvancedAlfPortal autoPortal;

    public static void initBlocks() {
        autoPoolInfusion = new BlockAdvancedCraftingPool();
        autoPoolAlchemy = new BlockAdvancedAlchemyPool();
        autoPoolConjuration = new BlockAdvancedConjurationPool();
        autoPortal = new BlockAdvancedAlfPortal();

        registerTileEntities();
    }

    public static void registerTileEntities() {
        registerTile(TileAdvancedCraftingPool.class, BlockAdvancedCraftingPool.NAME);
        registerTile(TileAdvancedAlchemyPool.class, BlockAdvancedAlchemyPool.NAME);
        registerTile(TileAdvancedConjurationPool.class, BlockAdvancedConjurationPool.NAME);
        registerTile(TileAdvancedAlfPortal.class, BlockAdvancedAlfPortal.NAME);
    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, ForgeMod.MOD_ID + "." + key);
    }
}
