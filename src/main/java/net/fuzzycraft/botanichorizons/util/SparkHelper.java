package net.fuzzycraft.botanichorizons.util;

import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaPool;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class SparkHelper {
    private SparkHelper() {} // Static class

    public static void requestSparkTransfers(@Nonnull World world, double xCoord, double yCoord, double zCoord, @Nullable ISparkEntity spark) {
        if(spark != null) {
            List<ISparkEntity> sparkEntities = vazkii.botania.api.mana.spark.SparkHelper.getSparksAround(world, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5);
            for(ISparkEntity otherSpark : sparkEntities) {
                if(spark == otherSpark)
                    continue;

                if(otherSpark.getAttachedTile() != null && otherSpark.getAttachedTile() instanceof IManaPool)
                    otherSpark.registerTransfer(spark);
            }
        }
    }
}
