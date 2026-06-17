package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.util.ChunkCoordinates;

import javax.annotation.Nonnull;

public class WrongBlockException extends Exception implements IMultiblockErrorLocation {
    public final int xCoord;
    public final int yCoord;
    public final int zCoord;
    public final String blockName;
    public final String foundName;
    public final int foundMeta;

    public WrongBlockException(int xCoord, int yCoord, int zCoord, String blockName, String foundName, int foundMeta) {
        super(String.format("Did not find \"%s\" at (%d, %d, %d), found \"%s\":%d instead", blockName, xCoord, yCoord, zCoord, foundName, foundMeta));
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.zCoord = zCoord;
        this.blockName = blockName;
        this.foundName = foundName;
        this.foundMeta = foundMeta;
    }

    @Override @Nonnull
    public ChunkCoordinates getErrorLocation() {
        return new ChunkCoordinates(xCoord, yCoord, zCoord);
    }
}
