package net.fuzzycraft.botanichorizons.addons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedCraftingPool;
import net.fuzzycraft.botanichorizons.lexicon.BHLexicon;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.IBlockTooltip;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
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

import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.BH_ICON_PREFIX;

public class BlockAdvancedCraftingPool extends BlockModContainer<TileAdvancedCraftingPool> implements IWandHUD, IWandable, ILexiconable, IBlockTooltip {

    public static final String NAME = "automatedCraftingPool";

    // META: bit 0 = online, bit 1..2 = 2D facing

    public BlockAdvancedCraftingPool() {
        super(Material.rock);
        setHardness(10.0F);
        setStepSound(soundTypeStone);
        setBlockName(NAME);
    }

    @Override
    public TileAdvancedCraftingPool createNewTileEntity(World world, int meta) {
        return new TileAdvancedCraftingPool();
    }

    IIcon iconOff;
    IIcon iconOn;

    IIcon iconPrimary;
    IIcon iconSecondary;

    @SideOnly(Side.CLIENT) @Override
    public void registerBlockIcons(IIconRegister register) {
        this.iconPrimary = register.registerIcon(BH_ICON_PREFIX + "quartzBlueTop");
        this.iconSecondary = register.registerIcon(BH_ICON_PREFIX + "quartzBlueSide");
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIcon(int side, int meta) {
        return (side == 1 || Facing2D.fromIC2(side) == Facing2D.fromIndex(meta >> 1)) ? this.iconPrimary : this.iconSecondary;
    }

    @SideOnly(Side.CLIENT) @Override
    public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
        ((TileAdvancedCraftingPool) world.getTileEntity(x, y, z)).renderHUD(mc, res);
    }

    @Override
    public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
        return BHLexicon.automatedManaPool;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        Facing2D facing = Facing2D.facingPlayer(placer);
        worldIn.setBlockMetadataWithNotify(x, y, z, facing.index << 1, 3);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block blockBroken, int meta) {
        TileAdvancedCraftingPool tileEntity = (TileAdvancedCraftingPool)world.getTileEntity(x, y, z);
        if (tileEntity != null) {
            tileEntity.dropItems(world, x, y, z);
        }
        super.breakBlock(world, x, y, z, blockBroken, meta);
    }

    @Override
    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        return ((TileAdvancedCraftingPool)world.getTileEntity(x, y, z)).onWanded(player);
    }

    @Override
    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 1) == 0 ? 0 : 10;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addTooltipInformation(ItemStack itemStack, List<String> tooltipStrings) {
        tooltipStrings.add(I18n.format("botanichorizons.tooltip.parallels", TileAdvancedCraftingPool.MAX_PARALLELS));
        Multiblocks.poolInfusion.addBuildInfoToTooltip(tooltipStrings);
        tooltipStrings.add(I18n.format("botanichorizons.author.combuster"));
    }
}
