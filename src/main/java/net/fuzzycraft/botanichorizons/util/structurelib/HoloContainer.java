package net.fuzzycraft.botanichorizons.util.structurelib;

import com.gtnewhorizon.structurelib.alignment.constructable.IMultiblockInfoContainer;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import static net.fuzzycraft.botanichorizons.util.structurelib.HoloProjectorSupport.HOLO_DEFAULT_CHANNEL;

public class HoloContainer<T extends TileEntity> implements IMultiblockInfoContainer<T> {

    private final IStructureDefinition<? super T> structrue;
    private final int offsetA, offsetB, offsetC;

    public HoloContainer(IStructureDefinition<? super T> structrue, int offsetA, int offsetB, int offsetC) {
        this.structrue = structrue;
        this.offsetA = offsetA;
        this.offsetB = offsetB;
        this.offsetC = offsetC;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly, T tileEntity, ExtendedFacing aSide) {
        structrue.buildOrHints(
                tileEntity,
                stackSize,
                HOLO_DEFAULT_CHANNEL,
                tileEntity.getWorldObj(),
                getOrientation(tileEntity, aSide),
                tileEntity.xCoord,
                tileEntity.yCoord,
                tileEntity.zCoord,
                offsetA,
                offsetB,
                offsetC,
                hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudge, ISurvivalBuildEnvironment env, T tileEntity,
                                 ExtendedFacing aSide) {
        return structrue.survivalBuild(
                tileEntity,
                stackSize,
                HOLO_DEFAULT_CHANNEL,
                tileEntity.getWorldObj(),
                getOrientation(tileEntity, aSide),
                tileEntity.xCoord,
                tileEntity.yCoord,
                tileEntity.zCoord,
                offsetA,
                offsetB,
                offsetC,
                elementBudge,
                env,
                false);
    }

    @Override
    public String[] getDescription(ItemStack stackSize) {
        return new String[0];
    }

    public ExtendedFacing getOrientation(T tileEntity, ExtendedFacing aSide) {
        return aSide.getDirection().offsetY != 0 ? ExtendedFacing.DEFAULT : aSide.getOppositeDirection();
    }
}
