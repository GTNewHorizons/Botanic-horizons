package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MultiblockCheck {
    boolean check(@Nonnull Block block, @Nonnull World world, int x, int y, int z);

    @Nullable
    MultiblockComponent getLexiconRenderer(MultiblockStructure metadata, int xOffset, int yOffset, int zOffset);
}
