package net.fuzzycraft.botanichorizons.util.structurelib;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import ic2.api.tile.IWrenchable;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class OrientedHoloContainer<T extends TileEntity & IWrenchable> extends HoloContainer<T>{
    public OrientedHoloContainer(IStructureDefinition<? super T> structure, int offsetA, int offsetB, int offsetC) {
        super(structure, offsetA, offsetB, offsetC);
    }

    @Override
    public ExtendedFacing getOrientation(T tileEntity, ExtendedFacing aSide) {
        short ic2Facing = tileEntity.getFacing();
        return ExtendedFacing.of(ForgeDirection.getOrientation(ic2Facing).getOpposite());
    }
}
