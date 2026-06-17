package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public abstract class RecipeAutomationTileEntity<T> extends AutomationTileEntity implements IInventory, IInvBasic {

    public abstract Collection<T> getAllRecipes();
    public abstract int maxRecipeWidth();
    public abstract List<Object> getInputs(@Nonnull T recipe);
    public abstract List<ItemStack> getOutputs(@Nonnull T recipe);
    public abstract int getManaRequired(@Nonnull T recipe, int copies);
    public abstract int getAvailableParallels(@Nonnull T recipe);

    public final int inputSize;
    public final int outputSize;

    public final InventoryBasic inventoryHandler;
    private final HashSet<T> possibleRecipes = new HashSet<>();
    protected T setRecipe = null; // Are we limited to 1 recipe
    protected boolean clientRecipeSet = false;
    protected int lastCheckedMana = 0;

    protected int checkTicks = 0;
    protected final int TICKS_PER_RECIPE_CHECK = 20;

    public RecipeAutomationTileEntity(@Nonnull final MultiblockHelper structure, final int outputSize) {
        super(structure);
        inputSize = maxRecipeWidth();
        this.outputSize = outputSize; // increase later to handle rune returns
        inventoryHandler = new InventoryBasic("name", false, inputSize + outputSize);

        possibleRecipes.addAll(getAllRecipes());
    }

    @Override
    protected void updateEntityCrafting() {
        if (setRecipe == null) {
            checkTicks = 0;
            lastCheckedMana = 0;
        } else if (checkTicks > 0) {
            checkTicks--;
        } else {
            checkTicks = TICKS_PER_RECIPE_CHECK - 1;
            handleOutputs();
            cleanupInventory();

            int craftable = getCopiesCraftable();
            FMLLog.warning("recipe length: %d, craftable items: %d", getInputs(setRecipe).size(), craftable);
            if (craftable > 0) {
                int maxParallel = getAvailableParallels(setRecipe);
                if (craftable > maxParallel) {
                    craftable = maxParallel;
                }

                int mana = getManaRequired(setRecipe, craftable);
                FMLLog.warning("mana: %d/%d/%d, parallels %d/%d", storedMana, mana, lastCheckedMana, craftable, maxParallel);
                if (storedMana >= mana) {
                    if (commitCrafts(craftable)) {
                        storedMana -= mana;
                        lastCheckedMana = 0;

                        recalculateAvailableRecipes();
                        handleOutputs();
                        cleanupInventory();
                        markTEForSharing(true);
                        markDirty();
                    }
                } else if (mana != lastCheckedMana) {
                    lastCheckedMana = mana;
                    sparkCycleRemaining = 0;
                    markTEForSharing(true);
                }
            }
        }
    }

    protected boolean commitCrafts(int numberToCraft) {
        if (setRecipe == null || numberToCraft <= 0) {
            return false; // probably a bug
        }

        List<ItemStack> outputs = getOutputs(setRecipe);
        for (int space = 0; space < outputs.size(); space++) {
            ItemStack destinationSlotStack = inventoryHandler.getStackInSlot(inputSize + outputSize - 1 - space);
            if (destinationSlotStack != null && destinationSlotStack.stackSize > 0) {
                return false; // not enough output space
            }
        }
        boolean didCraft = deductCraft(numberToCraft);
        if (!didCraft) {
            return false; // ingredients were already removed
        }

        // We're past the commit point now, inputs have been removed.
        for (int space = 0; space < outputs.size(); space++) {
            ItemStack resultStack = outputs.get(space).copy();
            resultStack.stackSize = resultStack.stackSize * numberToCraft;
            inventoryHandler.setInventorySlotContents(inputSize + outputSize - 1 - space, resultStack);
        }
        return true;
    }

    // Dump output downward
    public void handleOutputs() {
        InventoryHelper.pushInventoryToWorldDown(this, worldObj, inventoryHandler, inputSize, inputSize + outputSize);
    }

    // pushes stacks left to try and make free space.
    public void cleanupInventory() {
        InventoryHelper.defragInventory(inventoryHandler, inputSize, inputSize + outputSize);
    }

    // get the amount or recipes that can be run with the current ingredients.
    // Limitation: if input 1 applies to ingredient A and B and input 2 applies to ingredient A,
    // input 1 will not get assigned to ingredient B and the recipe won't work.
    // TLDR: Don't do NP-hard recipes.
    protected int getCopiesCraftable() {
        int[] remainingMap = computeItemsAvailable();
        List<Integer>[] applyMap = computeRecipeAssignment();

        // compute upper bound - this may be too high if certain ingredients are used twice.
        int searchMaximum = 64;
        for (List<Integer> application: applyMap) {
            int copiesForThisIngredient = 0;
            for (int reference: application) {
                copiesForThisIngredient += remainingMap[reference];
            }
            if (copiesForThisIngredient < searchMaximum) {
                searchMaximum = copiesForThisIngredient;
            }
            FMLLog.info("computing copies: %d available, %d max", copiesForThisIngredient, searchMaximum);
        }

        // Avoid doing +1 scans, these are notoriously slow even though we already pinned the recipe
        int searchMinimum = 0;
        // attempt the heuristic first. If all ingredients are inserted correctly in one go this is >99% cases the answer
        int attempt = searchMaximum;
        // if the recipe lacks specific items, the loop is not started
        while (searchMinimum != searchMaximum) {
            //FMLLog.info("iterating (%d, %d)", searchMinimum, searchMaximum);
            int[] trialMap = remainingMap.clone();
            boolean success = true;
            for (List<Integer> application: applyMap) {
                int toConsume = attempt;
                for (Integer reference: application) {
                    if (trialMap[reference] >= toConsume) {
                        trialMap[reference] -= toConsume;
                        toConsume = 0;
                        break;
                    } else if (trialMap[reference] > 0) {
                        toConsume -= trialMap[reference];
                        trialMap[reference] = 0;
                    }
                }
                if (toConsume != 0) {
                    success = false;
                    break;
                }
            }

            // Adjust upper/lower bounds
            if (success) {
                searchMinimum = attempt;
            } else {
                searchMaximum = attempt - 1;
            }
            attempt = (searchMaximum + searchMinimum + 1) / 2; // round up
            //FMLLog.info("iteration result (%b, %d, %d)", success, searchMinimum, searchMaximum);
        }

        return searchMinimum;
    }

    protected boolean deductCraft(int copiesRequested) {
        int[] remainingMap = computeItemsAvailable();
        List<Integer>[] applyMap = computeRecipeAssignment();

        for (List<Integer> application: applyMap) {
            int copiesForThisItem = copiesRequested;
            for (Integer reference: application) {
                if (remainingMap[reference] >= copiesForThisItem) {
                    remainingMap[reference] -= copiesForThisItem;
                    copiesForThisItem = 0;
                } else if (remainingMap[reference] > 0) {
                    copiesForThisItem -= remainingMap[reference];
                    remainingMap[reference] = 0;
                }
            }
            if (copiesForThisItem > 0) {
                return false;
            }
        }

        // commit changes
        for (int inputSlot = 0; inputSlot < inputSize; inputSlot++) {
            int newStackSize = remainingMap[inputSlot];
            if (newStackSize == 0) {
                inventoryHandler.setInventorySlotContents(inputSlot, null);
            } else {
                int difference = inventoryHandler.getStackInSlot(inputSlot).stackSize - newStackSize;
                if (difference > 0) {
                    inventoryHandler.decrStackSize(inputSlot, difference);
                }
            }
        }
        return true;
    }

    protected List<Integer>[] computeRecipeAssignment() {
        List<Object> ingredientList = getInputs(setRecipe);
        List<Integer>[] applyMap = new List[ingredientList.size()];
        for (int recipeSlot = 0; recipeSlot < ingredientList.size(); recipeSlot++) {
            List<Integer> mappings = new ArrayList<>();
            for (int inputSlot = 0; inputSlot < inputSize; inputSlot++) {
                ItemStack inputStack = inventoryHandler.getStackInSlot(inputSlot);
                if (inputStack != null && inputStack.stackSize != 0) {
                    if (InventoryHelper.isIngredient(inputStack, ingredientList.get(recipeSlot))) {
                        mappings.add(inputSlot);
                        //FMLLog.info("Assigning slot %d to ingredient %d", inputSlot, recipeSlot);
                    }
                }
            }
            //FMLLog.info("cumulative mapping: %s, %s", mappings.toString(), Arrays.deepToString(applyMap));
            applyMap[recipeSlot] = mappings;
        }
        //FMLLog.info("recipe mapping: %s", Arrays.deepToString(applyMap));
        return applyMap;
    }

    private int[] computeItemsAvailable() {
        int[] remainingMap = new int[inputSize];
        // Map available ingredients
        for (int inputSlot = 0; inputSlot < inputSize; inputSlot++) {
            ItemStack inputStack = inventoryHandler.getStackInSlot(inputSlot);
            if (inputStack == null || inputStack.stackSize == 0) {
                remainingMap[inputSlot] = 0;
            } else {
                remainingMap[inputSlot] = inputStack.stackSize;
                //FMLLog.info("%d items available in slot %d", inputStack.stackSize, inputSlot);
            }
        }
        //FMLLog.info("stockpile mapping: %s", Arrays.toString(remainingMap));
        return remainingMap;
    }

    // Crafting management

    public boolean allowInput(ItemStack stack) {
        for (T recipe: possibleRecipes) {
            if (doesStackFitRecipe(stack, recipe)) {
                return true;
            }
        }
        return false;
    }

    public boolean doesStackFitRecipe(@Nonnull ItemStack stack, @Nonnull T recipe) {
        for (Object input: getInputs(recipe)) {
            if (InventoryHelper.isIngredient(stack, input)) {
                return true;
            }
        }
        return false;
    }


    private void resetPossibleRecipes() {
        possibleRecipes.addAll(getAllRecipes());
    }

    private void truncateRecipes(@Nonnull ItemStack changedStack) {
        int currentCount = possibleRecipes.size();
        Iterator<T> iterator = possibleRecipes.iterator();
        while (iterator.hasNext()) {
            T recipe = iterator.next();
            if (!doesStackFitRecipe(changedStack, recipe)) {
                iterator.remove();
            }
        }
        setRecipe = (possibleRecipes.size() == 1) ? possibleRecipes.iterator().next() : null;
        FMLLog.warning("recipes %d -> %d after %s", currentCount, possibleRecipes.size(), changedStack.toString());
        if (setRecipe != null) {
            FMLLog.warning("selected recipe: %s", setRecipe.toString());
        }
    }

    private void recalculateAvailableRecipes() {
        resetPossibleRecipes();
        for (int slot = 0; slot < inputSize; slot++) {
            ItemStack stack = inventoryHandler.getStackInSlot(slot);
            if (stack != null && stack.stackSize > 0) {
                truncateRecipes(stack);
            }
        }
    }

    // Persistence

    private static final String KEY_INVENTORY = "inv";
    private static final String KEY_RECIPE_SET = "rcp";
    private static final String KEY_RECIPE_MANA = "mana";

    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setTag(KEY_INVENTORY, InventoryHelper.saveInventoryToNBT(inventoryHandler));
        compound.setBoolean(KEY_RECIPE_SET, worldObj.isRemote ? clientRecipeSet : setRecipe != null);
        compound.setInteger(KEY_RECIPE_MANA, lastCheckedMana);
    }

    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        InventoryHelper.readInventoryFromNBT(inventoryHandler, compound.getCompoundTag(KEY_INVENTORY));
        clientRecipeSet = compound.getBoolean(KEY_RECIPE_SET);
        lastCheckedMana = compound.getInteger(KEY_RECIPE_MANA);

        resetPossibleRecipes();
        recalculateAvailableRecipes();
    }

    // IInventory

    @Override
    public int getSizeInventory() {
        return inventoryHandler.getSizeInventory();
    }
    @Override
    public ItemStack getStackInSlot(int slotIn) {
        return inventoryHandler.getStackInSlot(slotIn);
    }
    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index < inputSize && count > 0) return null;
        ItemStack returned = inventoryHandler.decrStackSize(index, count);
        ItemStack remaining = inventoryHandler.getStackInSlot(index);
        if (remaining == null || remaining.stackSize == 0) {
            resetPossibleRecipes();
        }
        return returned;
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return inventoryHandler.getStackInSlotOnClosing(index);
    }
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack previousStack = inventoryHandler.getStackInSlot(index);
        inventoryHandler.setInventorySlotContents(index, stack);
        if (stack == null || stack.stackSize == 0) {
            resetPossibleRecipes();
        } else if (previousStack == null || previousStack.stackSize == 0) {
            truncateRecipes(stack);
        } else {
            resetPossibleRecipes();
            truncateRecipes(stack);
        }
    }
    @Override
    public String getInventoryName() {
        return inventoryHandler.getInventoryName();
    }
    @Override
    public boolean hasCustomInventoryName() {
        return inventoryHandler.hasCustomInventoryName();
    }
    @Override
    public int getInventoryStackLimit() {
        return inventoryHandler.getInventoryStackLimit();
    }
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventoryHandler.isUseableByPlayer(player);
    }
    @Override
    public void openInventory() {
        inventoryHandler.openInventory(); // no-op
    }
    @Override
    public void closeInventory() {
        inventoryHandler.closeInventory(); // no-op
    }
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index >= inputSize || index < 0) return false; // do not allow inserts into output slots
        if (stack == null || stack.getItem() == null) return false;
        return allowInput(stack);
    }

    // IInvBasic
    @Override
    public void onInventoryChanged(InventoryBasic p_76316_1_) {
        markDirty();
    }

    // Brock breaking
    public void dropItems(World world, int x, int y, int z) {
        InventoryHelper.dropAllItems(world, x, y, z, inventoryHandler);
    }

}
