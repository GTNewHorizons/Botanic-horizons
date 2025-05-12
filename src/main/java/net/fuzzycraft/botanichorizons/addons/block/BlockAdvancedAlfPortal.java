package net.fuzzycraft.botanichorizons.addons.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import net.fuzzycraft.botanichorizons.addons.tileentity.TileAdvancedAlfPortal;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.lexicon.LexiconData;

public class BlockAdvancedAlfPortal extends BlockModContainer<TileAdvancedAlfPortal> implements IWandHUD, IWandable, ILexiconable {

    public static final String NAME = "automatedAlfPortal";

    // META: bit 0 = online, bit 1..2 = 2D facing

    public BlockAdvancedAlfPortal() {
        super(Material.wood);
        setHardness(10.0F);
        setStepSound(soundTypeWood);
        setBlockName(NAME);
    }

    @Override
    public TileAdvancedAlfPortal createNewTileEntity(World world, int meta) {
        return new TileAdvancedAlfPortal();
    }

    IIcon iconOff;
    IIcon iconOn;
    public static IIcon portalTex;

    @SideOnly(Side.CLIENT) @Override
    public void registerBlockIcons(IIconRegister register) {
        // Using Botania's IconHelper to get access to their assets
        this.iconOff = IconHelper.forName(register, "alfheimPortal0");
        this.iconOn = IconHelper.forName(register, "alfheimPortal1");
        portalTex = IconHelper.forName(register, "alfheimPortalInside");
    }

    @SideOnly(Side.CLIENT) @Override
    public IIcon getIcon(int side, int meta) {
        return (meta & 1) == 0 ? this.iconOff : this.iconOn;
    }

    @SideOnly(Side.CLIENT) @Override
    public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
        ((TileAdvancedAlfPortal) world.getTileEntity(x, y, z)).renderHUD(mc, res);
    }

    public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
        return LexiconData.alfhomancyIntro;
    }

    public void onBlockPlacedBy(World worldIn, int x, int y, int z, EntityLivingBase placer, ItemStack itemIn) {
        Facing2D facing = Facing2D.facingPlayer(placer);
        if (placer instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) placer;
            player.addChatMessage(new ChatComponentText("Facing index: " + facing.index + ": " + facing.name()));
        }
        worldIn.setBlockMetadataWithNotify(x, y, z, facing.index << 1, 3);
    }


    public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
        boolean did = ((TileAdvancedAlfPortal)world.getTileEntity(x, y, z)).onWanded(player);
        if (did && player != null) {
            player.addStat(ModAchievements.elfPortalOpen, 1);
        }

        return did;
    }

    public int getLightValue(IBlockAccess world, int x, int y, int z) {
        return (world.getBlockMetadata(x, y, z) & 1) == 0 ? 0 : 15;
    }
}
