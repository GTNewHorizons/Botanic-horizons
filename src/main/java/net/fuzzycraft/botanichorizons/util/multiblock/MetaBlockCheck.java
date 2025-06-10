package net.fuzzycraft.botanichorizons.util.multiblock;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;

public final class MetaBlockCheck implements MultiblockCheck {
    public final @Nonnull Block referenceBlock;
    public final int referenceMeta;

    public MetaBlockCheck(@Nonnull Block referenceBlock, int referenceMeta) {
        this.referenceBlock = referenceBlock;
        this.referenceMeta = referenceMeta;
    }

    @Override
    public boolean check(@NotNull Block block, @NotNull World world, int x, int y, int z) {
        return block == referenceBlock && referenceMeta == world.getBlockMetadata(x, y, z);
    }

    @Override
    public MultiblockComponent getLexiconRenderer(MultiblockStructure metadata, int xOffset, int yOffset, int zOffset) {
        if (referenceBlock == Blocks.air) return null;
        return new MultiblockComponent(metadata.lexiconPackedCoordinates(xOffset, yOffset, zOffset), referenceBlock, referenceMeta);
    }
}
