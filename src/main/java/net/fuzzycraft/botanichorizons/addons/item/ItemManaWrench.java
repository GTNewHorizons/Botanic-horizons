package net.fuzzycraft.botanichorizons.addons.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.fuzzycraft.botanichorizons.mod.ForgeMod;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.equipment.tool.ToolCommons;

import java.util.HashSet;

import static net.fuzzycraft.botanichorizons.util.Constants.BH_ICON_PREFIX;
import static net.fuzzycraft.botanichorizons.util.Constants.TOOL_CLASS_WRENCH;

public class ItemManaWrench extends ItemTool implements IManaUsingItem {

    private static final int MANA_PER_DAMAGE = 60;

    public ItemManaWrench(ToolMaterial toolMaterial, int toolLevel, String name) {
        super(2.0f /* 1 heart attack */, toolMaterial, new HashSet<>());

        setHarvestLevel(TOOL_CLASS_WRENCH, toolLevel);
        setTextureName(BH_ICON_PREFIX + name);
        setUnlocalizedName(ForgeMod.MOD_ID + "." + name);
        GameRegistry.registerItem(this, name);
    }

    @Override
    public boolean hitEntity(ItemStack par1ItemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {
        damageOrConsumeMana(par1ItemStack, par3EntityLivingBase, 1, MANA_PER_DAMAGE);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
        if(block.getBlockHardness(world, x, y, z) != 0F) {
            damageOrConsumeMana(stack, entity, 1, MANA_PER_DAMAGE);
        }

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

    public void damageOrConsumeMana(ItemStack toolStack, EntityLivingBase toolWielder, int operations, int manaPerOperation) {
        Item item = toolStack.getItem();

        if (item instanceof IManaItem selfSource) {
            int totalConsumed = operations * manaPerOperation;
            int manaAvailable = selfSource.getMana(toolStack);

            if (manaAvailable > totalConsumed) {
                selfSource.addMana(toolStack, -totalConsumed);
                return;
            }
        }
        ToolCommons.damageItem(toolStack, operations, toolWielder, manaPerOperation);
    }
}
