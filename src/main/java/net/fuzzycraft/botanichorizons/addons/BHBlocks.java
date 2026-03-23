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
import net.fuzzycraft.botanichorizons.addons.block.BlockFloatingSparkFlower;
import net.fuzzycraft.botanichorizons.addons.block.BlockSparkFlower;
import net.fuzzycraft.botanichorizons.addons.block.subtile.generating.SubTileReiujia;
import net.fuzzycraft.botanichorizons.addons.block.tile.TileFloatingSparkFlower;
import net.fuzzycraft.botanichorizons.addons.block.tile.TileSparkFlower;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.fuzzycraft.botanichorizons.util.Signature;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

import vazkii.botania.api.BotaniaAPI;

public final class BHBlocks {
    public static BlockAdvancedCraftingPool autoPoolInfusion;
    public static BlockAdvancedAlchemyPool autoPoolAlchemy;
    public static BlockAdvancedConjurationPool autoPoolConjuration;
    public static BlockAdvancedAlfPortal autoPortal;
    public static Block sparkFlower;
    public static Block floatingSparkFlower;

    public static void initBlocks() {
        autoPoolInfusion = new BlockAdvancedCraftingPool();
        autoPoolAlchemy = new BlockAdvancedAlchemyPool();
        autoPoolConjuration = new BlockAdvancedConjurationPool();
        autoPortal = new BlockAdvancedAlfPortal();
        sparkFlower = new BlockSparkFlower();
        floatingSparkFlower = new BlockFloatingSparkFlower();

        registerTileEntities();
    }

    public static void registerTileEntities() {
        registerTile(TileAdvancedCraftingPool.class, BlockAdvancedCraftingPool.NAME);
        registerTile(TileAdvancedAlchemyPool.class, BlockAdvancedAlchemyPool.NAME);
        registerTile(TileAdvancedConjurationPool.class, BlockAdvancedConjurationPool.NAME);
        registerTile(TileAdvancedAlfPortal.class, BlockAdvancedAlfPortal.NAME);
		registerTile(TileSparkFlower.class, BlockSparkFlower.NAME);
		registerTile(TileFloatingSparkFlower.class, BlockFloatingSparkFlower.NAME);
        
        BotaniaAPI.registerSubTile(SubTileReiujia.NAME, SubTileReiujia.class);
        BotaniaAPI.registerSubTileSignature(SubTileReiujia.class, new Signature(SubTileReiujia.NAME));
        BotaniaAPI.addSubTileToCreativeMenu(SubTileReiujia.NAME);
    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, ForgeMod.MOD_ID + "." + key);
    }
}
