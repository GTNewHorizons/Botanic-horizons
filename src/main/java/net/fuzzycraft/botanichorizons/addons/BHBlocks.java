package net.fuzzycraft.botanichorizons.addons;

import cpw.mods.fml.common.registry.GameRegistry;
import net.fuzzycraft.botanichorizons.addons.block.BlockAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.minecraft.tileentity.TileEntity;

public final class BHBlocks {
    public static BlockAdvancedAlfPortal autoPortal;

    public static void initBlocks() {
        autoPortal = new BlockAdvancedAlfPortal();

        registerTileEntities();
    }

    public static void registerTileEntities() {
        registerTile(TileAdvancedAlfPortal.class, BlockAdvancedAlfPortal.NAME);
    }

    private static void registerTile(Class<? extends TileEntity> clazz, String key) {
        GameRegistry.registerTileEntity(clazz, ForgeMod.MOD_ID + "." + key);
    }
}
