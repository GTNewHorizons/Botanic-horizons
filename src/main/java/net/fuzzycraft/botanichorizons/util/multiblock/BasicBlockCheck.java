package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;

public final class BasicBlockCheck implements MultiblockCheck {
    @Nonnull
    final Block referenceBlock;

    public BasicBlockCheck(@Nonnull Block referenceBlock) {
        this.referenceBlock = referenceBlock;
    }

    @Override
    public boolean check(@Nonnull Block block, @Nonnull World world, int x, int y, int z) {
        return block == referenceBlock;
    }

    @Override
    public MultiblockComponent getLexiconRenderer(MultiblockStructure metadata, int xOffset, int yOffset, int zOffset) {
        if (referenceBlock == Blocks.air) return null;
        return new MultiblockComponent(metadata.lexiconPackedCoordinates(xOffset, yOffset, zOffset), referenceBlock, 0);
    }

}
