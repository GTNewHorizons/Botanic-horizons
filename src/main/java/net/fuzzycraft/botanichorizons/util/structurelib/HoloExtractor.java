package net.fuzzycraft.botanichorizons.util.structurelib;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructableProvider;
import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.util.BlockPos;
import net.fuzzycraft.botanichorizons.util.structurelib.extraction.InMemoryWorld;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HoloExtractor {
    @Nonnull private final World serverWorld;
    @Nonnull private final TileEntity serverTileEntity;
    @Nonnull private final World shadowWorld;
    @Nonnull private final TileEntity shadowTileEntity;
    @Nonnull private final BlockPos coordinates;

    public HoloExtractor(@Nonnull World serverWorld, @Nonnull TileEntity serverTileEntity, @Nonnull World shadowWorld, @Nonnull TileEntity shadowTileEntity, @Nonnull BlockPos coordinates) {
        this.serverWorld = serverWorld;
        this.serverTileEntity = serverTileEntity;
        this.shadowWorld = shadowWorld;
        this.shadowTileEntity = shadowTileEntity;
        this.coordinates = coordinates;
    }

    @Nullable
    public HoloScanner getScanner(int side) {
        IConstructable constructable = getConstructable(shadowTileEntity, side);
        if (constructable == null) {
            return null;
        }
        return new HoloScanner(constructable, shadowWorld);
    }

    @Nullable
    public static HoloExtractor wrapExtractor(@Nonnull TileEntity tileEntity) {
        World serverWorld = tileEntity.getWorldObj();
        BlockPos coordinates = new BlockPos(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);
        Block block = serverWorld.getBlock(coordinates.x, coordinates.y, coordinates.z);
        int blockMeta = serverWorld.getBlockMetadata(coordinates.x, coordinates.y, coordinates.z);

        World shadowWorld = new InMemoryWorld();
        shadowWorld.isRemote = true; // pretend it's a client world to make hints work
        NBTTagCompound copy = new NBTTagCompound();
        tileEntity.writeToNBT(copy);

        TileEntity shadowEntity;
        try {
            // StructureLib is a shitty design, these are all hacks, log if something fails.
            shadowWorld.setBlock(coordinates.x, coordinates.y, coordinates.z, block, blockMeta, 0);
            shadowEntity = block.createTileEntity(shadowWorld, blockMeta);
            shadowWorld.setTileEntity(coordinates.x, coordinates.y, coordinates.z, shadowEntity);
            shadowEntity.readFromNBT(copy);
        } catch (Exception e) {
            FMLLog.log(Level.WARN, e, "Could not extract tile entity to a dummy world: %s at %d, %d, %d",
                    tileEntity.getClass().getName(),
                    tileEntity.xCoord,
                    tileEntity.yCoord,
                    tileEntity.zCoord
            );
            return null;
        }

        return new HoloExtractor(serverWorld, tileEntity, shadowWorld, shadowEntity, coordinates);
    }

    @Nullable
    public static HoloScanner scanTileEntity(@Nonnull TileEntity tileEntity, int side) {
        if (!isProbablyConstructable(tileEntity)) return null;

        HoloExtractor extractor = wrapExtractor(tileEntity);
        if (extractor == null) return null;

        try {
            HoloScanner scanner = extractor.getScanner(side);
            if (scanner == null) return null;
            if (scanner.multiblockLocations.isEmpty()) {
                FMLLog.log(Level.WARN, "Scanning yielded no results: %s at %d, %d, %d",
                        tileEntity.getClass().getName(),
                        tileEntity.xCoord,
                        tileEntity.yCoord,
                        tileEntity.zCoord
                );
                return null;
            }
            return scanner;
        } catch (Exception e) {
            FMLLog.log(Level.WARN, e, "Scanning for hologram failed: %s at %d, %d, %d",
                    tileEntity.getClass().getName(),
                    tileEntity.xCoord,
                    tileEntity.yCoord,
                    tileEntity.zCoord
            );
            return null;
        }
    }

    public static boolean isProbablyConstructable(TileEntity tileEntity) {
        if (tileEntity instanceof IConstructable) return true;
        if (tileEntity instanceof IConstructableProvider) return true;
        if (IMultiblockInfoContainer.get(tileEntity.getClass()) != null) return true;

        return false;
    }

    @Nullable
    public static IConstructable getConstructable(TileEntity tileEntity, int side) {
        if (tileEntity instanceof IConstructable) {
            return (IConstructable) tileEntity;
        } else if (tileEntity instanceof IConstructableProvider) {
            return ((IConstructableProvider) tileEntity).getConstructable();
        } else {
            IMultiblockInfoContainer<Object> container = IMultiblockInfoContainer.get(tileEntity.getClass());
            if (container != null) {
                ExtendedFacing facing = HoloProjectorSupport.getExtendedFacingFromItemUse(tileEntity, side);
                return container.toConstructable(tileEntity, facing);
            }
        }

        return null;
    }
}
