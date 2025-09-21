package net.fuzzycraft.botanichorizons.addons.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import vazkii.botania.api.mana.IManaGivingItem;
import vazkii.botania.api.mana.IManaItem;
import vazkii.botania.api.mana.IManaTooltipDisplay;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import java.util.List;

public abstract class ItemSuperchargedWrench extends ItemManaWrench implements IManaItem, IManaTooltipDisplay {

    public static final int MAX_MANA = Integer.MAX_VALUE;
    public static final int[] TIERING_LEVELS = new int[] {
            0, 10000, 1000000, 10000000, 100000000, 1000000000, MAX_MANA
    };

    public static final String TAG_MANA = "mana";

    public ItemSuperchargedWrench(ToolMaterial toolMaterial, int toolLevel, String name) {
        super(toolMaterial, toolLevel, name);
    }

    @Override
    public void addInformation(ItemStack heldItem, EntityPlayer player, List<String> tooltips, boolean par4) {
        String rankFormat = StatCollector.translateToLocal("botaniamisc.toolRank");
        String rank = StatCollector.translateToLocal("botania.rank" + getLevel(heldItem));
        tooltips.add(String.format(rankFormat, rank).replaceAll("&", "\u00a7"));
        if (getMana(heldItem) == Integer.MAX_VALUE) {
            tooltips.add(EnumChatFormatting.RED + StatCollector.translateToLocal("botaniamisc.getALife"));
        }
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        for(int baseMana : TIERING_LEVELS) {
            int mana = baseMana - 1;
            if (baseMana > 0) {
                ItemStack stack = new ItemStack(item);
                setMana(stack, mana);
                list.add(stack);
            }
        }
    }

    public void setMana(ItemStack stack, int mana) {
        ItemNBTHelper.setInt(stack, TAG_MANA, mana);
    }

    public int getMana(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, TAG_MANA, 0);
    }

    public int getLevel(ItemStack stack) {
        int mana = getMana(stack);
        for(int i = TIERING_LEVELS.length - 2; i > 0; i--)
            if(mana >= TIERING_LEVELS[i])
                return i;

        return 0;
    }

    // IManaItem

    @Override
    public int getMaxMana(ItemStack stack) {
        return MAX_MANA;
    }

    @Override
    public void addMana(ItemStack stack, int mana) {
        setMana(stack, Math.min(getMana(stack) + mana, MAX_MANA));
    }

    @Override
    public boolean canReceiveManaFromPool(ItemStack stack, TileEntity pool) {
        return true;
    }

    @Override
    public boolean canReceiveManaFromItem(ItemStack stack, ItemStack otherStack) {
        return !(otherStack.getItem() instanceof IManaGivingItem);
    }

    @Override
    public boolean canExportManaToPool(ItemStack stack, TileEntity pool) {
        return false;
    }

    @Override
    public boolean canExportManaToItem(ItemStack stack, ItemStack otherStack) {
        return false;
    }

    @Override
    public boolean isNoExport(ItemStack stack) {
        return true;
    }

    // IManaTooltipDisplay
    // Wanted: The tiering HUD is hardcoded to look for instanceof Terra Shatterer.
    // This needs updates in Botania-Expert to allow reuse. We will use the boring mana HUD for now.

    @Override
    public float getManaFractionForDisplay(ItemStack stack) {
        int level = getLevel(stack);
        int mana = getMana(stack);
        int startMana = TIERING_LEVELS[level];
        int endMana = TIERING_LEVELS[level + 1];
        int barMana = mana - startMana;
        int barTotal = endMana - startMana;
        return (float) barMana / (float) barTotal;
    }
}
