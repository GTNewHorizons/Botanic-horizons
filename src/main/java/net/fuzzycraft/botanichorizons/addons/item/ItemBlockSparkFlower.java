package net.fuzzycraft.botanichorizons.addons.item;

import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.block.tile.TileSparkFlower;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import vazkii.botania.common.item.block.ItemBlockSpecialFlower;

public class ItemBlockSparkFlower extends ItemBlockSpecialFlower {

	public ItemBlockSparkFlower(Block block1) {
		super(block1);
	}
    
    @Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata) {
		boolean placed = super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata);
		if(placed) {
			String type = getType(stack);
			TileEntity te = world.getTileEntity(x, y, z);
			if(te instanceof TileSparkFlower tile) {
                tile.setSubTile(type);
				tile.onBlockAdded(world, x, y, z);
				tile.onBlockPlacedBy(world, x, y, z, player, stack);
				if(!world.isRemote)
					world.markBlockForUpdate(x, y, z);
			}
		}

		return placed;
	}
    
	public static ItemStack ofType(String type) {
		return ofType(new ItemStack(BHBlocks.sparkFlower), type);
	}
}