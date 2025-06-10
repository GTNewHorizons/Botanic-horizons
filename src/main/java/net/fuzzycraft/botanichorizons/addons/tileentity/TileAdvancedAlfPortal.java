package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.core.handler.HUDHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public class TileAdvancedAlfPortal extends SimpleAutomationTileEntity<RecipeElvenTrade> implements IManaReceiver, ISparkAttachable, IWrenchable {

    // Balance
    public final int MANA_CAPACITY = 200000;
    public final int CYCLE_TICKS = 20; // time between checks
    public final int CYCLE_UPKEEP = 75;
    public final int RECIPE_MANA = 10; // added cost on top of upkeep
    public final int ACTIVATE_MANA = 95000; // activation cost
    public final int MAX_PARALLELS = 32;

    /*
       Vanilla portal stats for comparison:
       activation cost: 75000 mana
       running cost: 2 mana/t = 40 mana/s
       crafting rate: 4t/recipe = 5 items/s
       crafting cost: no added cost, upkeep is 8 mana per recipe
     */

    // Tile entity state
    protected int cycleRemaining = 0;

    // Definitions

    public TileAdvancedAlfPortal() {
        super(Multiblocks.alfPortal);
    }

    // Business logic

    @Override
    protected void updateEntityCrafting() {
        if (!isOnline) {
            return;
        } else if (cycleRemaining > 0) {
            cycleRemaining--;
        } else if (storedMana < CYCLE_UPKEEP) {
            isOnline = false;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index << 1, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            // TODO: more visual stuff
        } else if (partialStructureValidation()) {
            cycleRemaining = CYCLE_TICKS;
            storedMana -= CYCLE_UPKEEP;

            handleCrafts();
            handleOutputs();
            cleanupInventory(0, INPUT_SIZE);
            cleanupInventory(INPUT_SIZE, INPUT_SIZE + OUTPUT_SIZE);
            markDirty();
        } else {
            isOnline = false;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index << 1, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
        }
    }

    // SimpleAutomationTileEntity delegate

    @Override
    public int getManaMaximum() {
        return isOnline ? MANA_CAPACITY : ACTIVATE_MANA + SPARK_BUFFER_MANA;
    }

    public int getAvailableParallels(@Nonnull RecipeElvenTrade recipe) {
        int parallel = MAX_PARALLELS;

        // check inappropriate recipes
        if (recipe.getInputs().size() != 1) {
            return 0;
        }

        // check energy capacity
        if (RECIPE_MANA > 0) { // just in case some hack turns this off later.
            final int max_mana_parallel = (storedMana - CYCLE_UPKEEP) / RECIPE_MANA;
            parallel = Math.min(parallel, max_mana_parallel);
        }
        return parallel;
    }

    @Override @Nullable
    public RecipeElvenTrade findMatchingRecipe(@Nonnull ItemStack stack) {
        for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
            final List<Object> inputs = recipe.getInputs();
            for (Object input: inputs) {
                if (InventoryHelper.isIngredient(stack, input)) {
                    return recipe;
                }
            }
        }
        return null;
    }

    @Override @Nonnull
    public ItemStack getRecipeOutput(@Nonnull RecipeElvenTrade recipe) {
        return recipe.getOutput();
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
            cycleRemaining = CYCLE_TICKS;
            isOnline = true;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1 + facing.index * 2, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            markDirty();
            return true;
        } else {
            isOnline = false;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index * 2, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            markDirty();
            return true;
        }
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
        String tooltip = state.getLocalisedHudString(BHBlocks.autoPortal);
        HUDHandler.drawSimpleManaHUD(state.color, storedMana, MANA_CAPACITY, tooltip, res);
    }

    // IWrenchable
    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoPortal);
    }
}
