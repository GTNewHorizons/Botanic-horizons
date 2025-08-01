package net.fuzzycraft.botanichorizons.addons.block;

import net.fuzzycraft.botanichorizons.addons.item.ItemModBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import vazkii.botania.client.core.helper.IconHelper;
import vazkii.botania.common.core.BotaniaCreativeTab;
import vazkii.botania.common.core.handler.ConfigHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BlockModContainer<T extends TileEntity> extends BlockContainer {

    public int originalLight;

    protected BlockModContainer(Material par2Material) {
        super(par2Material);
        if(registerInCreative())
            setCreativeTab(BotaniaCreativeTab.INSTANCE);
    }

    @Override
    public Block setBlockName(String par1Str) {
        if(shouldRegisterInNameSet())
            GameRegistry.registerBlock(this, ItemModBlock.class, par1Str);
        return super.setBlockName(par1Str);
    }

    protected boolean shouldRegisterInNameSet() {
        return true;
    }

    @Override
    public Block setLightLevel(float p_149715_1_) {
        originalLight = (int) (p_149715_1_ * 15);
        return super.setLightLevel(p_149715_1_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister par1IconRegister) {
        blockIcon = IconHelper.forBlock(par1IconRegister, this);
    }

    public boolean registerInCreative() {
        return true;
    }

    @Override
    public abstract T createNewTileEntity(World world, int meta);

    @Override
    public boolean canCreatureSpawn(EnumCreatureType type, IBlockAccess world, int x, int y, int z) {
        return !ConfigHandler.noMobSpawnOnBlocks;
    }
}