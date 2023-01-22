package net.fuzzycraft.botanichorizons.util;

import java.util.ArrayList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.InfusionRecipe;

/**
 * Thaumcraft has an internal check for recipe matching.
 * It however always downconverts ItemStacks into OreDictionary entries,
 * and compares if there's any overlap with the input item.
 * This can lead to unexpected matches or exploits.
 *
 * This class reimplements the method in question and disables the additional check.
 *
 * Should be faster too, though nobody's going to notice that.
 *
 * Covering all use-cases properly would involve changing the recipe from ItemStack[] to Object[]
 */
public class NonFuzzyInfusionRecipe extends InfusionRecipe {
    final boolean fuzzy;

    public NonFuzzyInfusionRecipe(
            String research, Object output, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe) {
        super(research, output, inst, aspects2, input, recipe);
        this.fuzzy = false;
    }

    // Taken from the Thaumcraft API
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        if (this.getRecipeInput() == null) {
            return false;
        } else if (this.research.length() > 0
                && !ThaumcraftApiHelper.isResearchComplete(player.getCommandSenderName(), this.research)) {
            return false;
        } else {
            ItemStack i2 = central.copy();
            if (this.getRecipeInput().getItemDamage() == 32767) {
                i2.setItemDamage(32767);
            }

            if (!areItemStacksEqual(i2, this.getRecipeInput(), fuzzy)) {
                return false;
            } else {
                ArrayList<ItemStack> ii = new ArrayList<>();

                for (ItemStack is : input) {
                    ii.add(is.copy());
                }

                for (ItemStack comp : this.getComponents()) {
                    boolean b = false;

                    for (int a = 0; a < ii.size(); ++a) {
                        i2 = ii.get(a).copy();
                        if (comp.getItemDamage() == 32767) {
                            i2.setItemDamage(32767);
                        }

                        if (areItemStacksEqual(i2, comp, fuzzy)) {
                            ii.remove(a);
                            b = true;
                            break;
                        }
                    }

                    if (!b) {
                        return false;
                    }
                }

                return ii.size() == 0;
            }
        }
    }
}
