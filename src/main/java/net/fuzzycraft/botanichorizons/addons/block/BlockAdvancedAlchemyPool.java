package net.fuzzycraft.botanichorizons.addons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlchemyPool;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.lexicon.LexiconData;

import static net.fuzzycraft.botanichorizons.util.Constants.BH_ICON_PREFIX;

public class BlockAdvancedAlchemyPool extends BlockModContainer<TileAdvancedAlchemyPool> implements IWandHUD, IWandable, ILexiconable {

    public static final String NAME = "automatedAlchemyPool";

    // META: bit 0 = online, bit 1..2 = 2D facing

    public BlockAdvancedAlchemyPool() {
        super(Material.rock);
        setHardness(10.0F);
        setStepSound(soundTypeStone);
        setBlockName(NAME);
    }

    @Override
    public TileAdvancedAlchemyPool createNewTileEntity(World world, int meta) {
        return new TileAdvancedAlchemyPool();
    }

    IIcon iconPrimary;
    IIcon iconSecondary;

    @SideOnly(Side.CLIENT) @Override
    public void registerBlockIcons(IIconRegister register) {
        this.iconPrimary = register.registerIcon(BH_ICON_PREFIX + "quartzYellowTop");
        this.iconSecondary = register.registerIcon(BH_ICON_PREFIX + "quartzYellowSide");
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 1 || Facing2D.fromIC2(side) == Facing2D.fromIndex(meta >> 1)) ? this.iconPrimary : this.iconSecondary;
    }

    @SideOnly(Side.CLIENT) @Override
    public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
        ((TileAdvancedAlchemyPool) world.getTileEntity(x, y, z)).renderHUD(mc, res);
    }

    public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
        return LexiconData.alfhomancyIntro;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        Facing2D facing = Facing2D.facingPlayer(placer);
        worldIn.setBlockMetadataWithNotify(x, y, z, facing.index << 1, 3);
    }

    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        return ((TileAdvancedAlchemyPool)world.getTileEntity(x, y, z)).onWanded(player);
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 1) == 0 ? 0 : 10;
    }
}
