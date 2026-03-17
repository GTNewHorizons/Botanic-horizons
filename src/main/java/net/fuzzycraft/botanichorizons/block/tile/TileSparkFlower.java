package net.fuzzycraft.botanichorizons.block.tile;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;

import vazkii.botania.common.block.tile.TileSpecialFlower;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

public class TileSparkFlower extends TileSpecialFlower implements ISparkAttachable {
    
    public int mana;
    
    @Override
	public boolean canAttachSpark(ItemStack stack) {
		return true;
	}

	@Override
	public void attachSpark(ISparkEntity entity) {
		// NO-OP
	}

	@Override
	public ISparkEntity getAttachedSpark() {
		List<ISparkEntity> sparks = getWorldObj().getEntitiesWithinAABB(
            ISparkEntity.class,
            AxisAlignedBB.getBoundingBox(
                xCoord,
                yCoord + 1,
                zCoord,
                xCoord + 1,
                yCoord + 2,
                zCoord + 1));
		if(sparks.size() == 1) {
			Entity e = (Entity) sparks.get(0);
			return (ISparkEntity) e;
		}
		return null;
	}
    
    // generating flower should never accept mana
    @Override
	public int getAvailableSpaceForMana() { return 0; }
    @Override
    public boolean areIncomingTranfersDone() { return true; }
    
    @Override
    public boolean isFull() { return true; }
    @Override
	public void recieveMana(int mana) {
        // NO-OP
    }
    @Override
	public boolean canRecieveManaFromBursts() { return false; }
    
    @Override
    public int getCurrentMana() { return mana; }
}