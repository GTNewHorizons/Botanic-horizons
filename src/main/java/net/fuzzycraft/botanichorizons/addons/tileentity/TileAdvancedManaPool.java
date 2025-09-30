package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.recipe.RecipeManaInfusion;
import vazkii.botania.common.block.tile.mana.TilePool;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public abstract class TileAdvancedManaPool extends SimpleAutomationTileEntity<RecipeManaInfusion> {

    // Balance
    public final int manaCapacity;
    public static final int CYCLE_TICKS = 20; // time between checks
    public static final int MAX_PARALLELS = 64;
    public static final int ACTIVATE_MANA = 1000;

    protected int cycleRemaining = 0;

    // Constructors

    public TileAdvancedManaPool(MultiblockHelper structure, int capacity) {
        super(structure);
        manaCapacity = capacity;
    }

    // Business logic

    @Override
    protected void updateEntityCrafting() {
        if (!isOnline) {
            return;
        } else if (cycleRemaining > 0) {
            cycleRemaining--;
        } else if (partialStructureValidation()) {
            cycleRemaining = CYCLE_TICKS;

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
    public int getAvailableParallels(@NotNull RecipeManaInfusion recipe) {
        int parallel = MAX_PARALLELS;
        int recipeManaCost = recipe.getManaToConsume();

        if (parallel * recipeManaCost < storedMana) {
            return parallel;
        }

        int externalMana = getExternalMana();
        int totalReservoir = storedMana + externalMana;

        if (recipeManaCost > 0) {
            int manaParallel = totalReservoir / recipeManaCost;
            parallel = Math.min(parallel, manaParallel);
        }

        return parallel;
    }

    @Override
    void consumeNonItemResources(RecipeManaInfusion recipe, int parallel) {
        storedMana -= parallel * recipe.getManaToConsume();

        if (storedMana < 0) {
            int deficit = -storedMana;
            TilePool pool = getExternalPool();
            if (pool != null && pool.getCurrentMana() >= deficit) {
                pool.recieveMana(-deficit);
                storedMana = 0;
            } else {
                FMLLog.bigWarning("Exploit: External mana is no longer available");
                isOnline = false;
                worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index << 1, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            }
        }
    }

    @Nullable
    private TilePool getExternalPool() {
        int poolX = xCoord - 4 * facing.dx;
        int poolZ = zCoord - 4 * facing.dz;
        int poolY = yCoord + 1;
        TileEntity rawPoolTE = worldObj.getTileEntity(poolX, poolY, poolZ);
        if (rawPoolTE instanceof TilePool pool) {
            return pool;
        } else {
            FMLLog.warning("Not a pool at %d %d %d", poolX, poolY, poolZ);
            return null;
        }
    }

    private int getExternalMana() {
        TilePool pool = getExternalPool();
        return (pool != null) ? pool.getCurrentMana() : 0;
    }

    // SimpleAutomationTileEntity

    @Override
    public int getRecipeInputStackSize(@NotNull RecipeManaInfusion recipe) {
        return 1;
    }

    @Override
    public @NotNull ItemStack getRecipeOutput(@NotNull RecipeManaInfusion recipe) {
        return recipe.getOutput();
    }

    @Override
    public int getManaMaximum() {
        return manaCapacity;
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
        }
        return false;
    }
}
