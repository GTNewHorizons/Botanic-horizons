package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.Constants;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.common.block.tile.TileTerraPlate;
import vazkii.botania.common.item.ModItems;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public class TileAdvancedTerraPlate extends AutomationTileEntity implements IInventory, IInvBasic {

    // Balance
    public static int MAX_PARALLELS = 64;
    public static int MANA_BUFFER = 10000;
    public static int PUMPING_MANA_BUFFER = 1000000;
    public static int ACTIVATE_MANA = 9000;
    public static int VIRTUAL_SLOTS = 4;
    public static int DRAIN_SLOT = 3;
    public static int CHECK_INTERVAL = 50;
    public static int COST_PERCENT = 90;
    public static long CRAFT_MANA = TileTerraPlate.MAX_MANA / 100 * COST_PERCENT;

    // Definitions
    public final InventoryBasic inventoryHandler;
    public long craftingMana = 0;
    public boolean isPumping = false;
    protected long currentManaThreshold = 0;
    protected int cycleRemaining = 0;

    public TileAdvancedTerraPlate() {
        super(Multiblocks.terraPlate);
        inventoryHandler = new InventoryBasic("name", false, VIRTUAL_SLOTS);
    }

    @Override
    public int getManaMaximum() {
        // prevents burst from completing and then stopping if the unit is active
        return isPumping ? PUMPING_MANA_BUFFER : MANA_BUFFER;
    }

    @Override
    protected void updateEntityCrafting() {
        if (!isOnline) {
            craftingMana = 0;
            return;
        } else if (isPumping) {
            long oldStash = craftingMana;
            craftingMana += storedMana;
            storedMana = 0;
            if (oldStash < currentManaThreshold && craftingMana >= currentManaThreshold) checkCrafts();
            markDirty();
        }

        if (cycleRemaining > 0) {
            cycleRemaining--;
        } else {
            cycleRemaining = CHECK_INTERVAL;
            if (!partialStructureValidation()) {
                craftingMana = 0;
                isOnline = false;
                markDirty();
            } else {
                checkCrafts();
            }
        }
    }

    protected void checkCrafts() {
        int ingredient1size = safeSizeInSlot(0);
        int ingredient2size = safeSizeInSlot(1);
        int ingredient3size = safeSizeInSlot(2);
        int drainSize = safeSizeInSlot(DRAIN_SLOT);

        final int oldStoredMana = storedMana;
        final long oldThreshold = currentManaThreshold;
        final long oldCraftingMana = craftingMana;
        final boolean oldPumping = isPumping;

        int runningCrafts = Math.min(ingredient1size, Math.min(ingredient2size, ingredient3size));
        currentManaThreshold = runningCrafts * CRAFT_MANA;

        // Check if we can complete a craft
        System.out.println(String.format("Plate status: %d/%d/%d/%d %d->%d/%d", ingredient1size, ingredient2size, ingredient3size, drainSize, runningCrafts, craftingMana, currentManaThreshold));
        if (runningCrafts > 0 && craftingMana >= currentManaThreshold && drainSize == 0) {

            // Consume inputs
            inventoryHandler.decrStackSize(0, runningCrafts);
            inventoryHandler.decrStackSize(1, runningCrafts);
            inventoryHandler.decrStackSize(2, runningCrafts);
            ingredient1size -= runningCrafts;
            ingredient2size -= runningCrafts;
            ingredient3size -= runningCrafts;

            // Generate outputs
            ItemStack crafts = new ItemStack(ModItems.manaResource, runningCrafts, Constants.MANARESOURCE_META_TERRASTEEL);
            inventoryHandler.setInventorySlotContents(3, crafts);
            drainSize = runningCrafts;

            // Return up to one tick of excess mana back
            craftingMana -= currentManaThreshold;
            if (storedMana + craftingMana < MANA_BUFFER) {
                storedMana += (int) craftingMana;
                craftingMana = 0;
            }
            currentManaThreshold = 0;
        }

        if (drainSize > 0) {
            ItemStack stack = inventoryHandler.getStackInSlot(DRAIN_SLOT);
            if (stack.getItem() == ModItems.manaResource && stack.getItemDamage() == Constants.MANARESOURCE_META_TERRASTEEL) {

                TileEntity outputEntity = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
                if (outputEntity instanceof IInventory outputInventory) {
                    ItemStack remainingItems = InventoryHelper.pushToInventory(outputInventory, stack);
                    inventoryHandler.setInventorySlotContents(DRAIN_SLOT, remainingItems);
                }

                drainSize = safeSizeInSlot(DRAIN_SLOT);
                markDirty();
            }
        }

        // check consuming state and waste mana
        isPumping = drainSize > 0 || ingredient1size > 0 || ingredient2size > 0 || ingredient3size > 0;
        if (craftingMana > currentManaThreshold) {
            craftingMana = currentManaThreshold;
            markDirty();
        }

        if(oldPumping != isPumping) {
            sparkCycleRemaining = 0;
            updateEntitySparks();
        }

        if (
                oldPumping != isPumping ||
                oldThreshold != currentManaThreshold ||
                oldCraftingMana != craftingMana ||
                oldStoredMana != storedMana
        ) {
            markTEForSharing(false);
        }
    }

    protected int safeSizeInSlot(int slot) {
        ItemStack stack = inventoryHandler.getStackInSlot(slot);
        if (stack == null) return 0;
        return stack.stackSize;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoPlate);
    }

    // Brock breaking
    public void dropItems(World world, int x, int y, int z) {
        InventoryHelper.dropAllItems(world, x, y, z, inventoryHandler);
    }

    public boolean onWanded(EntityPlayer wandUser) {
        this.facing = Facing2D.fromIndex((worldObj.getBlockMetadata(xCoord, yCoord, zCoord) >> 1) & 3);

        markTEForSharing(true);
        if (!isOnline) {
            Exception error = structure.checkEntireStructure(worldObj, xCoord, yCoord, zCoord, this.facing);
            if (error != null) {
                boolean handled = MultiblockHelper.handleFailedStructure(worldObj, wandUser, error);
                return false;
            }

            if (storedMana < ACTIVATE_MANA) {
                return false;
            }

            storedMana -= ACTIVATE_MANA;
            craftingMana = 0;
            isOnline = true;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1 + facing.index * 2, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            markDirty();
            return true;
        } else if (wandUser.isSneaking()) {
            isOnline = false;
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index * 2, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
            markDirty();
            return true;
        } else {
            return true;
        }
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        System.out.println(String.format("Plate HUD: %d/%d/%d/%d %b->%d/%d", safeSizeInSlot(0), safeSizeInSlot(1), safeSizeInSlot(2), safeSizeInSlot(3), isPumping, craftingMana, currentManaThreshold));
        if (!isPumping) {
            ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
            String tooltip = state.getLocalisedHudString(BHBlocks.autoPlate);
            HUDHandler.drawSimpleManaHUD(state.color, storedMana, MANA_BUFFER, tooltip, res);
        } else if (currentManaThreshold == 0) {
            // failing state
            String tooltip = StatCollector.translateToLocal(BHBlocks.autoPlate.getUnlocalizedName() + ".hud.crashing");
            HUDHandler.drawSimpleManaHUD(0xE0A044, MANA_BUFFER, MANA_BUFFER, tooltip, res);
        } else {
            // crafting state
            String tooltip = StatCollector.translateToLocal(BHBlocks.autoPlate.getUnlocalizedName() + ".hud.collecting");
            HUDHandler.drawSimpleManaHUD(0xE0A044, (int)craftingMana, (int)currentManaThreshold, tooltip, res);
        }
    }


    // Persistence
    private static final String KEY_INVENTORY = "inv";
    private static final String KEY_CRAFT_MANA = "mana_c";
    private static final String KEY_PUMPING = "pump";
    private static final String KEY_THRESHOLD = "mana_th";

    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setTag(KEY_INVENTORY, InventoryHelper.saveInventoryToNBT(inventoryHandler));
        compound.setLong(KEY_CRAFT_MANA, craftingMana);
        compound.setLong(KEY_THRESHOLD, currentManaThreshold);
        compound.setBoolean(KEY_PUMPING, isPumping);
        System.out.println("Encode NBT - client=" + worldObj.isRemote);
    }

    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        InventoryHelper.readInventoryFromNBT(inventoryHandler, compound.getCompoundTag(KEY_INVENTORY));
        craftingMana = compound.getLong(KEY_CRAFT_MANA);
        currentManaThreshold = compound.getLong(KEY_THRESHOLD);
        isPumping = compound.getBoolean(KEY_PUMPING);
        System.out.println("Decode NBT - client=" + worldObj.isRemote);
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
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item == null) return false;
        int meta = stack.getItemDamage();

        if (item == ModItems.manaResource && meta == Constants.MANARESOURCE_META_DIAMOND) {
            return index == 0;
        } else if (item == ModItems.manaResource && meta == Constants.MANARESOURCE_META_MANASTEEL) {
            return index == 1;
        } else if (item == ModItems.manaResource && meta == Constants.MANARESOURCE_META_PEARL) {
            return index == 2;
        } else {
            return index == 3; // output slot
        }
    }

    // IInvBasic
    @Override
    public void onInventoryChanged(InventoryBasic p_76316_1_) {
        markDirty();
    }
}
