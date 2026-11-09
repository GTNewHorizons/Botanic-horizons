package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.recipe.RecipePetals;
import vazkii.botania.api.recipe.RecipeRuneAltar;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.ModBlocks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public class TileAdvancedAltar extends RecipeAutomationTileEntity<RecipeRuneAltar> {

    public static final int MAX_PARALLELS = 16;
    public static final int IDLE_MANA = 10000;
    public static final int ACTIVATE_MANA = 5000;
    public static int cachedMaxRecipeWidth = 0;

    public TileAdvancedAltar() {
        super(Multiblocks.altar, 5);
    }

    @Override
    public int getManaMaximum() {
        return setRecipe == null ? IDLE_MANA : IDLE_MANA + lastCheckedMana;
    }

    @Override
    public Collection<RecipeRuneAltar> getAllRecipes() {
        return BotaniaAPI.runeAltarRecipes;
    }

    @Override
    public int maxRecipeWidth() {
        int max = cachedMaxRecipeWidth;
        if (max > 0) {
            return max;
        } else {
            for (RecipeRuneAltar recipe: getAllRecipes()) {
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
    public List<Object> getInputs(@NotNull RecipeRuneAltar recipe) {
        List<Object> baseInputs = new ArrayList<>(recipe.getInputs());
        baseInputs.add(new ItemStack(ModBlocks.livingrock, 1));

        return baseInputs;
    }

    @Override
    public List<ItemStack> getOutputs(@NotNull RecipeRuneAltar recipe) {
        List<ItemStack> outputs = new ArrayList<>(1);
        outputs.add(recipe.getOutput());

        return outputs;
    }

    @Override
    public int getManaRequired(@NotNull RecipeRuneAltar recipe, int copies) {
        return recipe.getManaUsage() * copies;
    }

    @Override
    public int getAvailableParallels(@NotNull RecipeRuneAltar recipe) {
        return MAX_PARALLELS;
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        if (lastCheckedMana == 0 || setRecipe == null) {
            ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
            String tooltip = state.getLocalisedHudString(BHBlocks.autoAltar);
            HUDHandler.drawSimpleManaHUD(state.color, storedMana, IDLE_MANA, tooltip, res);
        } else {
            // crafting state
            String tooltip = StatCollector.translateToLocal(BHBlocks.autoAltar.getUnlocalizedName() + ".hud.collecting");
            HUDHandler.drawSimpleManaHUD(0xE0A044, storedMana, lastCheckedMana + IDLE_MANA, tooltip, res);
        }
    }

    // IWrenchable

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoAltar);
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
