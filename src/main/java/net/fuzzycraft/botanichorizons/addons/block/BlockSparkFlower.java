package net.fuzzycraft.botanichorizons.addons.block;

// Mirrors BlockSpecialFlower

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.fuzzycraft.botanichorizons.addons.item.ItemBlockSparkFlower;
import net.fuzzycraft.botanichorizons.addons.block.subtile.generating.SubTileReiujia;
import net.fuzzycraft.botanichorizons.addons.block.tile.TileSparkFlower;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.lib.LibRenderIDs;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockSpecialFlower;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.integration.coloredlights.LightHelper;
import vazkii.botania.common.item.ModItems;

import cpw.mods.fml.common.registry.GameRegistry;

public class BlockSparkFlower extends BlockFlower implements ITileEntityProvider, ISpecialFlower, IWandable, ILexiconable, IWandHUD {
    
	public static Map<String, IIcon> icons = new HashMap<>();
	public static Map<String, IIcon> iconsAlt = new HashMap<>();
    public static String NAME = "sparkFlower";

	static {
		BotaniaAPI.subtilesForCreativeMenu.addAll(Arrays.asList(new String[] {
				SubTileReiujia.NAME
		}));
	}
    
    public BlockSparkFlower() {
		super(0);
		setBlockName(this.NAME);
		setHardness(0.1F);
		setStepSound(soundTypeGrass);
		setTickRandomly(false);
		setCreativeTab(BotaniaCreativeTab.INSTANCE);
		setBlockBounds(0.3F, 0.0F, 0.3F, 0.8F, 1, 0.8F);
	}
    
    @Override
	public int getLightValue(IBlockAccess world, int x, int y, int z) {
		int currentLight = ((TileSparkFlower) world.getTileEntity(x, y, z)).getLightValue();
		if(currentLight == -1)
			currentLight = 0;
		return LightHelper.getPackedColor(world.getBlockMetadata(x, y, z), currentLight);
	}

	@Override
	public boolean hasComparatorInputOverride() {
		return true;
	}

	@Override
	public int getComparatorInputOverride(World world, int x, int y, int z, int side) {
		return ((TileSparkFlower) world.getTileEntity(x, y, z)).getComparatorInputOverride(side);
	}

	@Override
	public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
		return ((TileSparkFlower) world.getTileEntity(x, y, z)).getPowerLevel(side);
	}

	@Override
	public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
		return isProvidingWeakPower(world, x, y, z, side);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}

	@Override
	public int getRenderType() {
		return LibRenderIDs.idSpecialFlower;
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
	public void registerBlockIcons(IIconRegister register) {
		for(String s : BotaniaAPI.getAllSubTiles())
			if(!s.isEmpty())
				BotaniaAPI.getSignatureForName(s).registerIcons(register);
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
		final TileSparkFlower flower = (TileSparkFlower) world.getTileEntity(x, y, z);
		return flower != null ? flower.getIcon() : BlockModFlower.icons[16];
	}

	@Override
	public IIcon getIcon(int side, int meta) {
		return BlockModFlower.icons[16];
	}

	@Override
	public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
		String name = ((TileSparkFlower) world.getTileEntity(x, y, z)).subTileName;
		return ItemBlockSparkFlower.ofType(name);
	}

	@Override
	protected boolean canPlaceBlockOn(Block block) {
		return super.canPlaceBlockOn(block) || block == ModBlocks.redStringRelay || block == Blocks.mycelium;
	}

	@Override
	public void onBlockHarvested(World world, int x, int y, int z, int meta, EntityPlayer player) {
		if(!player.capabilities.isCreativeMode) {
			dropBlockAsItem(world, x, y, z, meta, 0);
			((TileSparkFlower) world.getTileEntity(x, y, z)).onBlockHarvested(world, x, y, z, meta, player);
		}
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
	public boolean onBlockEventReceived(World world, int x, int y, int z, int eventId, int eventData) {
		super.onBlockEventReceived(world, x, y, z, eventId, eventData);
		TileEntity tileentity = world.getTileEntity(x, y, z);
		return tileentity != null ? tileentity.receiveClientEvent(eventId, eventData) : false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileSparkFlower();
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return ((TileSparkFlower) world.getTileEntity(x, y, z)).getEntry();
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        System.out.println("mana " + ((TileSparkFlower) world.getTileEntity(x, y, z)).getCurrentMana());
		return ((TileSparkFlower) world.getTileEntity(x, y, z)).onWanded(stack, player);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		((TileSparkFlower) world.getTileEntity(x, y, z)).onBlockPlacedBy(world, x, y, z, entity, stack);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		((TileSparkFlower) world.getTileEntity(x, y, z)).onBlockAdded(world, x, y, z);
	}

	@Override
	public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
		float[] rgb = EntitySheep.fleeceColorTable[world.getBlockMetadata(x, y, z)];
		return ((int) (rgb[0] * 255) << 16) + ((int) (rgb[1] * 255) << 8) + (int) (rgb[2] * 255);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getCurrentEquippedItem();
		if(stack != null && stack.getItem() == ModItems.dye) {
			int newMeta = stack.getItemDamage();
			int oldMeta = world.getBlockMetadata(x, y, z);
			if(newMeta != oldMeta)
				world.setBlockMetadataWithNotify(x, y, z, newMeta, 1 | 2);
		}

		return ((TileSparkFlower) world.getTileEntity(x, y, z)).onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileSparkFlower) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}
}