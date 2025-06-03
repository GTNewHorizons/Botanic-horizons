package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.client.core.handler.HUDHandler;

public class TileAdvancedConjurationPool extends TileAdvancedManaPool {

    public TileAdvancedConjurationPool() {
        super(Multiblocks.poolConjuration);
    }

    @Override
    public @Nullable RecipeManaInfusion findMatchingRecipe(ItemStack stack) {
        for(RecipeManaInfusion recipe : BotaniaAPI.manaInfusionRecipes) {
            if (recipe.isConjuration() && InventoryHelper.isIngredient(stack, recipe.getInput())) {
                return recipe;
            }
        }
        return null;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoPoolConjuration);
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
        String tooltip = state.getLocalisedHudString(BHBlocks.autoPoolConjuration);
        HUDHandler.drawSimpleManaHUD(state.color, storedMana, MANA_CAPACITY, tooltip, res);
    }
}
