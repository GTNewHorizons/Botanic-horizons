package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.client.core.handler.HUDHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public class TileAdvancedApothecary extends RecipeAutomationTileEntity<RecipePetals> {

    public static final int MAX_PARALLELS = 8;
    public static final int RECIPE_MANA = 1000;
    public static final int ACTIVATE_MANA = 5000;
    public static int cachedMaxRecipeWidth = 0;

    public TileAdvancedApothecary() {
        super(Multiblocks.apothecary, 2);
    }

    @Override
    public int getManaMaximum() {
        return MAX_PARALLELS * RECIPE_MANA;
    }

    // Recipe wrangling

    @Override
    public Collection<RecipePetals> getAllRecipes() {
        return BotaniaAPI.petalRecipes;
    }

    @Override
    public int maxRecipeWidth() {
        int max = cachedMaxRecipeWidth;
        if (max > 0) {
            return max;
        } else {
            for (RecipePetals recipe: getAllRecipes()) {
                int recipeMax = recipe.getInputs().size();
                if (recipeMax > max) {
                    max = recipeMax;
                }
            }
            cachedMaxRecipeWidth = max;
            return max;
        }
    }

    @Override
    public List<Object> getInputs(@NotNull RecipePetals recipe) {
        return recipe.getInputs();
    }

    @Override
    public List<ItemStack> getOutputs(@NotNull RecipePetals recipe) {
        ArrayList<ItemStack> result = new ArrayList<>();
        result.add(recipe.getOutput());
        return result;
    }

    @Override
    public int getManaRequired(@NotNull RecipePetals recipe, int copies) {
        return copies * RECIPE_MANA;
    }

    @Override
    public int getAvailableParallels(@NotNull RecipePetals recipe) {
        return MAX_PARALLELS;
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(@Nonnull Minecraft mc, @Nonnull ScaledResolution res) {
        ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
        String tooltip = state.getLocalisedHudString(BHBlocks.autoApothecary);
        HUDHandler.drawSimpleManaHUD(state.color, storedMana, getManaMaximum(), tooltip, res);
    }

    // IWrenchable

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoApothecary);
    }


    // IWandable delegate

    public boolean onWanded(EntityPlayer wandUser) {
        this.facing = Facing2D.fromIndex((worldObj.getBlockMetadata(xCoord, yCoord, zCoord) >> 1) & 3);

        if (!isOnline) {
            Exception error = structure.checkEntireStructure(worldObj, xCoord, yCoord, zCoord, this.facing);
            if (error != null) {
                boolean handled = MultiblockHelper.handleFailedStructure(worldObj, wandUser, error);
                return false;
            }

            if (storedMana <= ACTIVATE_MANA) {
                return false;
            }

            storedMana -= ACTIVATE_MANA;
            isOnline = true;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1 + facing.index * 2, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            markDirty();
            return true;
        }
        return false;
    }
}
