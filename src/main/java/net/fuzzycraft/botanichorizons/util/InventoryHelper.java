package net.fuzzycraft.botanichorizons.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class InventoryHelper {

    public static final int TAG_ID_COMPOUND = 10;

    /**
     * Moves items to a selected inventory
     * @param target inventory to push to
     * @param inputStack stack of items to push
     * @return Items that weren't pushed
     */
    public static ItemStack pushToInventory(@Nonnull final IInventory target, @Nonnull ItemStack inputStack) {
        int maxDestSize = target.getInventoryStackLimit();
        int maxItemSize = inputStack.getMaxStackSize();

        for (int slot = 0; slot < target.getSizeInventory(); slot++) {
            if (target.isItemValidForSlot(slot, inputStack)) {

                final ItemStack targetStack = target.getStackInSlot(slot);
                if (targetStack != null) {
                    if (targetStack.stackSize < maxDestSize && canStacksBeMerged(targetStack, inputStack)) {
                        int stackSizeLimit = Math.min(maxDestSize, maxItemSize);
                        int itemsToMove = Math.min(inputStack.stackSize, stackSizeLimit - targetStack.stackSize);

                        ItemStack updatedDestinationStack = targetStack.copy();
                        updatedDestinationStack.stackSize += itemsToMove;
                        target.setInventorySlotContents(slot, updatedDestinationStack);

                        inputStack = inputStack.copy();
                        inputStack.stackSize -= itemsToMove;

                        // check if done
                        if (inputStack.stackSize == 0) return null;
                    }
                } else {
                    int itemsToMove = Math.min(maxDestSize, Math.min(maxItemSize, inputStack.stackSize));
                    if (itemsToMove == inputStack.stackSize) {
                        target.setInventorySlotContents(slot, inputStack);
                        return null;
                    } else {
                        ItemStack splitDestinationStack = inputStack.copy();
                        splitDestinationStack.stackSize = itemsToMove;
                        target.setInventorySlotContents(slot, inputStack);

                        inputStack = inputStack.copy();
                        inputStack.stackSize -= itemsToMove;
                    }
                }
            } // else stack can't go here
        }
        return inputStack;
    }

    public static int itemsToMove(@Nonnull ItemStack target, @Nonnull ItemStack source) {
        if (canStacksBeMerged(target, source)) {
            int maxItemSize = source.getMaxStackSize();
            int maxMove = maxItemSize - target.stackSize;
            return Math.min(maxMove, source.stackSize);
        }
        return 0;
    }

    /**
     * Checks if items from source can be added to destination. Checks stack size for destination but not for source.
     * @param destination targeted ItemStack
     * @param source other ItemStack
     * @return true if source is stackable with destination and destination has space
     */
    public static boolean canStacksBeMerged(@Nonnull final ItemStack destination, @Nonnull final ItemStack source)
    {
        return destination.getItem() == source.getItem() &&
                destination.getItemDamage() == source.getItemDamage() &&
                destination.stackSize <= destination.getMaxStackSize() &&
                ItemStack.areItemStackTagsEqual(destination, source);
    }

    @Nonnull
    public static NBTTagCompound saveInventoryToNBT(@Nonnull IInventory inventory) {
        NBTTagCompound compound = new NBTTagCompound();
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null && stack.stackSize != 0) {
                NBTTagCompound stackData = new NBTTagCompound();
                stack.writeToNBT(stackData);
                compound.setTag("slot_" + slot, stackData);
            }
        }

        return compound;
    }

    public static void readInventoryFromNBT(@Nonnull IInventory inventory, NBTTagCompound compound) {
        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            String tag = "slot_" + slot;
            if (compound != null && compound.hasKey(tag, TAG_ID_COMPOUND)) {
                NBTTagCompound stackData = compound.getCompoundTag(tag);
                ItemStack stack = ItemStack.loadItemStackFromNBT(stackData);
                inventory.setInventorySlotContents(slot, stack);
            } else {
                inventory.setInventorySlotContents(slot, null);
            }
        }
    }
}
