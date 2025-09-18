package net.fuzzycraft.botanichorizons.addons.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.HashSet;

import static net.fuzzycraft.botanichorizons.util.Constants.TOOL_CLASS_WRENCH;

public class ItemManaWrench extends ItemTool implements IManaUsingItem {

    private static final int MANA_PER_DAMAGE = 60;

    public ItemManaWrench(ToolMaterial toolMaterial, int toolLevel, String name) {
        super(2.0f /* 1 heart attack */, toolMaterial, new HashSet<>());

        setHarvestLevel(TOOL_CLASS_WRENCH, toolLevel);
        setUnlocalizedName(name);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        ToolCommons.damageItem(par1ItemStack, 1, par3EntityLivingBase, MANA_PER_DAMAGE);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(block.getBlockHardness(world, x, y, z) != 0F)
            ToolCommons.damageItem(stack, 1, entity, MANA_PER_DAMAGE);

        return true;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity player, int par4, boolean par5) {
        if(!world.isRemote && player instanceof EntityPlayer && stack.getItemDamage() > 0 && ManaItemHandler.requestManaExactForTool(stack, (EntityPlayer) player, MANA_PER_DAMAGE * 2, true))
            stack.setItemDamage(stack.getItemDamage() - 1);
    }

    // IManaUsingItem
    @Override
    public boolean usesMana(ItemStack stack) {
        return true;
    }
}
