package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.util.ChunkCoordinates;

public final class MultiblockStructure {
    public final int dx;
    public final int dy;
    public final int dz;
    public final MultiblockCheck check;

    public MultiblockStructure(int dx, int dy, int dz, MultiblockCheck check) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.check = check;
    }

    public ChunkCoordinates lexiconPackedCoordinates(int xOffset, int yOffset, int zOffset) {
        return new ChunkCoordinates(-dx + xOffset, -dy + yOffset, -dz + zOffset);
    }
}
