package net.fuzzycraft.botanichorizons.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.item.ItemModBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public enum ChargeState {
    LOW_POWER("empty", 0xFF4444),
    IDLE("idle", 0x44BBBB),
    ACTIVE("active", 0x44FF44);

    public final String suffix;
    public final int color;

    ChargeState(String suffix, int color) {
        this.suffix = suffix;
        this.color = color;
    }

    public static ChargeState genState(boolean active, int mana, int startMana) {
        if (active) {
            return ACTIVE;
        } else if (mana <= startMana) {
            return LOW_POWER;
        } else {
            return IDLE;
        }
    }

    @SideOnly(Side.CLIENT)
    public String getLocalisedHudString(Block baseblock) {
        return getLocalisedHudString(baseblock, 0);
    }

    @SideOnly(Side.CLIENT)
    public String getLocalisedHudString(Block baseblock, int meta) {
        return getLocalisedHudString(new ItemStack(baseblock, 1, meta));
    }

    @SideOnly(Side.CLIENT)
    public String getLocalisedHudString(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof ItemModBlock) {
            return StatCollector.translateToLocal(item.getUnlocalizedNameInefficiently(stack) + ".hud." + suffix);
        } else {
            return StatCollector.translateToLocal(stack.getUnlocalizedName() + ".hud." + suffix);
        }
    }


}
