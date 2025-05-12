package net.fuzzycraft.botanichorizons.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum Facing2D {
    NORTH(0, 2,  0, -1,  1,  0),
    EAST(1,  5,  1,  0,  0,  1),
    SOUTH(2, 3,  0,  1, -1,  0),
    WEST(3,  4, -1,  0,  0, -1);

    public final int index;
    public final int ic2index;
    public final int dx;
    public final int dz;
    public final int cw_dx;
    public final int cw_dz;

    Facing2D(int index, int ic2index, int dx, int dz, int cw_dx, int cw_dz) {
        this.index = index;
        this.ic2index = ic2index;
        this.dx = dx;
        this.dz = dz;
        this.cw_dx = cw_dx;
        this.cw_dz = cw_dz;
    }

    @Nonnull
    public static Facing2D fromIndex(int index) {
        if (index == NORTH.index) return NORTH;
        if (index == EAST.index) return EAST;
        if (index == SOUTH.index) return SOUTH;
        return WEST;
    }

    @Nullable
    public static Facing2D fromIC2(int index) {
        if (index == NORTH.ic2index) return NORTH;
        if (index == EAST.ic2index) return EAST;
        if (index == SOUTH.ic2index) return SOUTH;
        if (index == WEST.ic2index) return WEST;
        return null;
    }

    @Nonnull
    public static Facing2D facingPlayer(EntityLivingBase entity) {
        int quadrant = MathHelper.floor_double((double)(entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return fromIndex(quadrant);
    }
}
