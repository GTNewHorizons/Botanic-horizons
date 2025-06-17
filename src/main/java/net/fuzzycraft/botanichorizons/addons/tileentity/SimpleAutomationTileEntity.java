package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class SimpleAutomationTileEntity<R> extends AutomationTileEntity implements IInventory, IInvBasic {

    // Delegates
    @Nullable
    public abstract R findMatchingRecipe(ItemStack stack);
    public abstract int getAvailableParallels(@Nonnull R recipe);
    @Nonnull
    public abstract ItemStack getRecipeOutput(@Nonnull R recipe);
    public abstract int getRecipeInputStackSize(@Nonnull R recipe);

    // definitions
    public final InventoryBasic inventoryHandler;
    static final int INPUT_SIZE = 2;
    static final int OUTPUT_SIZE = 2;

    public SimpleAutomationTileEntity(MultiblockHelper structure) {
        super(structure);

        inventoryHandler = new InventoryBasic("name", false, INPUT_SIZE + OUTPUT_SIZE);

        // Listen to change events so we can mark the chunk dirty on interactions
        inventoryHandler.func_110134_a(this);
    }

    // Business logic

    // process recipes
    public void handleCrafts() {
        int parallel = 64;

        // check if last output slot is empty, i.e. we can safely dump the output
        final ItemStack currentLastSlot = inventoryHandler.getStackInSlot(INPUT_SIZE + OUTPUT_SIZE - 1);
        if (currentLastSlot != null && currentLastSlot.stackSize > 0) return;

        // check available inputs
        final ItemStack craftStack = inventoryHandler.getStackInSlot(0);
        if (craftStack == null || craftStack.getItem() == null) return;
        
        final int max_input_parallel = craftStack.stackSize;
        parallel = Math.min(parallel, max_input_parallel);
        if (parallel <= 0) return;

        // get recipe
        R recipe = findMatchingRecipe(craftStack);
        parallel = Math.min(parallel, getAvailableParallels(recipe));
        if (parallel <= 0) {
            return;
        }

        if (recipe == null) {
            // Recipe is bugged or went missing, dump the input to the output so we don't lose it.
            inventoryHandler.setInventorySlotContents(INPUT_SIZE + OUTPUT_SIZE - 1, craftStack.copy());
            inventoryHandler.setInventorySlotContents(0, null);
            return;
        }

        final int recipe_input_width = getRecipeInputStackSize(recipe);
        if (recipe_input_width > 1) {
            final int max_input_width = craftStack.stackSize / recipe_input_width;
            parallel = Math.min(parallel, max_input_width);
        }
        final ItemStack output_instance = getRecipeOutput(recipe);
        final int max_output_parallel = 64 / output_instance.stackSize;
        parallel = Math.min(parallel, max_output_parallel);

        if (parallel <= 0 || recipe_input_width <= 0) {
            return;
        }

        // perform recipe in batch
        ItemStack output = output_instance.copy();
        output.stackSize = output.stackSize * parallel;

        // commit to inventory
        inventoryHandler.setInventorySlotContents(INPUT_SIZE + OUTPUT_SIZE - 1, output);
        inventoryHandler.decrStackSize(0, parallel * recipe_input_width);
    }

    // Dump output downward
    public void handleOutputs() {
        if (yCoord < 1) return;

        for (int slot = INPUT_SIZE; slot < INPUT_SIZE + OUTPUT_SIZE; slot++) {
            final ItemStack stack = inventoryHandler.getStackInSlot(slot);
            if (stack == null || stack.getItem() == null || stack.stackSize == 0) continue;

            TileEntity outputEntity = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
            if (outputEntity instanceof IInventory) {
                IInventory outputInventory = (IInventory) outputEntity;
                ItemStack remainingItems = InventoryHelper.pushToInventory(outputInventory, stack);
                inventoryHandler.setInventorySlotContents(slot, remainingItems);
            } else if (worldObj.isAirBlock(xCoord, yCoord - 1, zCoord)) {
                // TODO: drop items in world
            }
        }
    }

    // pushes stacks left to try and make free space.
    public void cleanupInventory(int start, int end) {
        for (int checkSlot = start + 1; checkSlot < end; checkSlot++) {
            ItemStack sourceStack = inventoryHandler.getStackInSlot(checkSlot);
            if (sourceStack != null) {
                boolean done = false;
                for (int refSlot = start; refSlot < checkSlot && !done; refSlot++) {
                    ItemStack destinationStack = inventoryHandler.getStackInSlot(refSlot);
                    if (destinationStack == null) {
                        inventoryHandler.setInventorySlotContents(refSlot, sourceStack);
                        inventoryHandler.setInventorySlotContents(checkSlot, null);
                        done = true;
                    } else {
                        final int itemsToMove = InventoryHelper.itemsToMove(destinationStack, sourceStack);
                        if (itemsToMove > 0) {
                            final ItemStack newDestinationStack = destinationStack.copy();
                            final ItemStack newSourceStack = sourceStack.copy();
                            newDestinationStack.stackSize += itemsToMove;
                            newSourceStack.stackSize -= itemsToMove;
                            inventoryHandler.setInventorySlotContents(refSlot, newDestinationStack);
                            if (newSourceStack.stackSize == 0) {
                                inventoryHandler.setInventorySlotContents(checkSlot, null);
                                done = true;
                            } else {
                                inventoryHandler.setInventorySlotContents(checkSlot, newSourceStack);
                                sourceStack = newSourceStack;
                            }
                        }
                    }
                }
            }
        }
    }


    // Persistence
    private static final String KEY_INVENTORY = "inv";

    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setTag(KEY_INVENTORY, InventoryHelper.saveInventoryToNBT(inventoryHandler));
    }

    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        InventoryHelper.readInventoryFromNBT(inventoryHandler, compound.getCompoundTag(KEY_INVENTORY));
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
        if (index < INPUT_SIZE && count > 0) return null;
        return inventoryHandler.decrStackSize(index, count);
    }
    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return inventoryHandler.getStackInSlotOnClosing(index);
    }
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventoryHandler.setInventorySlotContents(index, stack);
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
        if (index >= INPUT_SIZE || index < 0) return false; // do not allow inserts into output slots
        if (stack == null || stack.getItem() == null) return false;

        return findMatchingRecipe(stack) != null; // item not found
    }

    // IInvBasic
    @Override
    public void onInventoryChanged(InventoryBasic p_76316_1_) {
        markDirty();
    }

    // Brock breaking
    public void dropItems(World world, int x, int y, int z) {
        for (int slot = 0; slot < inventoryHandler.getSizeInventory(); slot++) {
            ItemStack drop = inventoryHandler.getStackInSlot(slot);

            if (drop != null && drop.stackSize > 0) {
                ItemStack copy = drop.copy();
                inventoryHandler.setInventorySlotContents(slot, null);
                EntityItem entity = new EntityItem(world, (double)x + 0.5, (double)y + 0.5, (double)z + 0.5, copy);
                InventoryHelper.setRandomDropDirection(entity, world);
                world.spawnEntityInWorld(entity);
            }
        }
    }
}
