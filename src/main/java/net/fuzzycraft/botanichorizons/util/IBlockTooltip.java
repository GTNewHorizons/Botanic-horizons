package net.fuzzycraft.botanichorizons.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IBlockTooltip {
    @SideOnly(Side.CLIENT)
    void addTooltipInformation(ItemStack itemStack, List<String> tooltipStrings);
}
