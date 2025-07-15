package net.fuzzycraft.botanichorizons.util.multiblock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.multiblock.Multiblock;
import vazkii.botania.api.lexicon.multiblock.MultiblockSet;
import vazkii.botania.api.lexicon.multiblock.component.MultiblockComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class MultiblockHelper {
    public final MultiblockStructure[] blocks;
    private static final Random particleRandomiser = new Random();

    public MultiblockHelper(@Nonnull MultiblockStructure[] blocks) {
        this.blocks = blocks;
    }

    public boolean checkStructurePart(@Nonnull World world, int rootX, int rootY, int rootZ, @Nonnull Facing2D orientation, int index)
    {
        MultiblockStructure part = blocks[index];
        int checkX = transformedX(rootX, orientation, part);
        int checkY = transformedY(rootY, part);
        int checkZ = transformedZ(rootZ, orientation, part);
        if (world.checkChunksExist(checkX, checkY, checkZ, checkX, checkY, checkZ)) {
            Block block = world.getBlock(checkX, checkY, checkZ);
            return part.check.check(block, world, checkX, checkY, checkZ);
        } else {
            // Can't validate structure if it's unloaded
            // assume it's ok for a partially unloaded structure so that it doesn't disable near the loaded edge
            return true;
        }
    }

    public Exception checkEntireStructure(@Nonnull World world, int rootX, int rootY, int rootZ, @Nonnull Facing2D orientation) {
        for(MultiblockStructure part : blocks) {
            int checkX = transformedX(rootX, orientation, part);
            int checkY = transformedY(rootY, part);
            int checkZ = transformedZ(rootZ, orientation, part);
            if (world.checkChunksExist(checkX, checkY, checkZ, checkX, checkY, checkZ)) {
                Block block = world.getBlock(checkX, checkY, checkZ);
                if (!part.check.check(block, world, checkX, checkY, checkZ)) {
                    String message = String.format("x: (%d + %d * %d~%d), z: (%d + %d * %d~%d), b: %s",
                            rootX, part.dx, orientation.dx, orientation.cw_dx,
                            rootZ, part.dz, orientation.dz, orientation.cw_dz,
                            part.toString()
                    );
                    return new WrongBlockException(checkX, checkY, checkZ, message);
                }
            } else {
                // Can't validate structure if it's unloaded
                return new UnloadedBlockException(checkX, checkY, checkZ);
            }
        }
        return null;
    }

    // Pos + dz * dir - dx * dir.cw

    private int transformedX(int rootX, @Nonnull Facing2D orientation, @Nonnull MultiblockStructure item) {
        return rootX + item.dz * orientation.dx - item.dx * orientation.cw_dx;
    }

    private int transformedY(int rootY, @Nonnull MultiblockStructure item) {
        return rootY - item.dy;
    }

    private int transformedZ(int rootZ, @Nonnull Facing2D orientation, @Nonnull MultiblockStructure item) {
        return rootZ + item.dz * orientation.dz - item.dx * orientation.cw_dz;
    }

    @Nonnull
    public MultiblockSet lexiconMultiblock(int xOffset, int yOffset, int zOffset, Block rootBlock, int rootMeta) {
        Multiblock export = new Multiblock();
        for (MultiblockStructure block : blocks) {
            MultiblockComponent component = block.check.getLexiconRenderer(block, xOffset, yOffset, zOffset);
            if (component != null) {
                export.addComponent(component);
            }
        }
        export.addComponent(new MultiblockComponent(
                new ChunkCoordinates(xOffset, yOffset, zOffset),
                rootBlock,
                rootMeta,
                true
        ));

        return export.makeSet();
    }

    /**
     * Tries to do UI for the given error.
     * @param world the world object.
     * @param player the player object triggering the check
     * @param error the Exception generated from the multiblock check
     * @return Returns true if something was done to help the player.
     */
    public static boolean handleFailedStructure(World world, EntityPlayer player, Exception error) {
        if (error instanceof IMultiblockErrorLocation) {
            ChunkCoordinates errorLocation = ((IMultiblockErrorLocation) error).getErrorLocation();
            for (int i = 0; i < 10; i++) {
                world.spawnParticle("fireworksSpark",
                        errorLocation.posX + 0.5, errorLocation.posY + 0.5, errorLocation.posZ + 0.5,
                        MultiblockHelper.genParticleVelocity(), MultiblockHelper.genParticleVelocity(), MultiblockHelper.genParticleVelocity());
            }
            return true;
        } else if (player != null) {
            player.addChatComponentMessage(new ChatComponentText(error.getMessage()));
            return true;
        }

        return false;
    }

    private static float genParticleVelocity() {
        return (particleRandomiser.nextFloat() - 0.5f) / 3;
    }

    @SuppressWarnings("unchecked")
    @SideOnly(Side.CLIENT)
    public void addBuildInfoToTooltip(@SuppressWarnings("rawtypes") List tooltipStrings) {
        int minX = blocks[0].dx;
        int minY = blocks[0].dy;
        int minZ = blocks[0].dz;
        int maxX = minX;
        int maxY = minY;
        int maxZ = minZ;
        for (MultiblockStructure entry: blocks) {
            minX = Math.min(minX, entry.dx);
            minY = Math.min(minY, entry.dy);
            minZ = Math.min(minZ, entry.dz);
            maxX = Math.max(maxX, entry.dx);
            maxY = Math.max(maxY, entry.dy);
            maxZ = Math.max(maxZ, entry.dz);
        }
        int sizeX = maxX - minX + 1;
        int sizeY = maxY - minY + 1;
        int sizeZ = maxZ - minZ + 1;

        String dimensionString = I18n.format("botanichorizons.tooltip.multiblock.1", sizeX, sizeY, sizeZ);
        tooltipStrings.add(dimensionString);
        tooltipStrings.add(I18n.format("botanichorizons.tooltip.multiblock.2"));
        tooltipStrings.add(I18n.format("botanichorizons.tooltip.multiblock.3"));
    }
}
