package net.fuzzycraft.botanichorizons.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.fuzzycraft.botanichorizons.block.ItemBlockSparkFlower;
import net.fuzzycraft.botanichorizons.block.subtile.generating.SubTileReiujia;
import net.fuzzycraft.botanichorizons.block.tile.TileSparkFlower;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.item.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockSparkFlower extends BlockSpecialFlower {
    
	public static Map<String, IIcon> icons = new HashMap<>();
	public static Map<String, IIcon> iconsAlt = new HashMap<>();

	static {
		BotaniaAPI.subtilesForCreativeMenu.addAll(Arrays.asList(new String[] {
				SubTileReiujia.NAME
		}));
	}

	protected BlockSparkFlower() {
		super();
		setBlockName("sparkFlower");
	}

	@Override
	public Block setBlockName(String name) {
		GameRegistry.registerBlock(this, ItemBlockSparkFlower.class, name);
		return super.setBlockName(name);
	}

	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> list) {
		for(String s : BotaniaAPI.subtilesForCreativeMenu) {
			list.add(ItemBlockSparkFlower.ofType(s));
			if(BotaniaAPI.miniFlowers.containsKey(s))
				list.add(ItemBlockSparkFlower.ofType(BotaniaAPI.miniFlowers.get(s)));
		}
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		String name = ((TileSparkFlower) world.getTileEntity(x, y, z)).subTileName;
		return ItemBlockSparkFlower.ofType(name);
	}

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
		ArrayList<ItemStack> list = new ArrayList<>();
		TileEntity tile = world.getTileEntity(x, y, z);

		if(tile != null) {
			String name = ((TileSparkFlower) tile).subTileName;
			list.add(ItemBlockSparkFlower.ofType(name));
			((TileSparkFlower) tile).getDrops(list);
		}

		return list;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSparkFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return ((TileSparkFlower) world.getTileEntity(x, y, z)).getEntry();
	}

}