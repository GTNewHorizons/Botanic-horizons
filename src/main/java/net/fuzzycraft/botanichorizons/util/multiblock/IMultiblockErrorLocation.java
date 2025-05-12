package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.util.ChunkCoordinates;

import javax.annotation.Nonnull;

public interface IMultiblockErrorLocation {
    @Nonnull ChunkCoordinates getErrorLocation();
}
