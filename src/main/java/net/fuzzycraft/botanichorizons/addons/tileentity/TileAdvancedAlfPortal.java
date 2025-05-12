package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.tile.IWrenchable;
import net.fuzzycraft.botanichorizons.addons.BHBlocks;
import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.util.ChargeState;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.InventoryHelper;
import net.fuzzycraft.botanichorizons.util.SparkHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInvBasic;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;
import vazkii.botania.api.recipe.RecipeElvenTrade;
import vazkii.botania.client.core.handler.HUDHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.tile.TileAlfPortal;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

public class TileAdvancedAlfPortal extends TileEntity implements IInventory, IInvBasic, IManaReceiver, ISparkAttachable, IWrenchable {

    // Balance
    public final int MANA_CAPACITY = 200000;
    public final int CYCLE_TICKS = 20; // time between checks
    public final int SPARK_CYCLE_TICKS = 100; // time between spark requests for extra mana
    public final int CYCLE_UPKEEP = 75;
    public final int RECIPE_MANA = 10; // added cost on top of upkeep
    public final int ACTIVATE_MANA = 95000; // activation cost
    public final int MAX_PARALLELS = 32;
    // Startup safety buffer. Should be slightly more than the upkeep needed of one SPARK_CYCLE
    public final int SPARK_BUFFER_MANA = 2000;
    /*
       Vanilla portal stats for comparison:
       activation cost: 75000 mana
       running cost: 2 mana/t = 40 mana/s
       crafting rate: 4t/recipe = 5 items/s
       crafting cost: no added cost, upkeep is 8 mana per recipe
     */

    // Tile entity state
    public final InventoryBasic inventoryHandler;
    protected int storedMana = 0; // can be > MANA_CAPACITY to avoid losses
    protected int cycleRemaining = 0;
    protected int sparkCycleRemaining = 0;
    protected int structureCycle = 0;
    protected Facing2D facing = Facing2D.NORTH;
    protected boolean isOnline = false;
    protected boolean clientSparkTransfer = false;

    // Definitions
    static final int INPUT_SIZE = 2;
    static final int OUTPUT_SIZE = 2;

    // Debug stats
    protected boolean requestedSparkTransfer = false;

    public TileAdvancedAlfPortal() {
        inventoryHandler = new InventoryBasic("name", false, INPUT_SIZE + OUTPUT_SIZE);

        // Listen to change events so we can mark the chunk dirty on interactions
        inventoryHandler.func_110134_a(this);
    }

    // Business logic

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            if (clientSparkTransfer) {
                SparkHelper.requestSparkTransfers(worldObj, xCoord, yCoord, zCoord, getAttachedSpark());
                clientSparkTransfer = false;
            }
        } else {
            updateEntityCrafting();
            updateEntitySparks();
        }
    }

    private void updateEntityCrafting() {
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

    private void updateEntitySparks() {
        if (sparkCycleRemaining > 0) {
            sparkCycleRemaining--;
        } else {
            sparkCycleRemaining = SPARK_CYCLE_TICKS;
            if (areIncomingTranfersDone()) {
                return;
            }

            if (
                ((!isOnline) && (storedMana < ACTIVATE_MANA + SPARK_BUFFER_MANA)) ||
                 (isOnline && (storedMana < MANA_CAPACITY - SPARK_BUFFER_MANA))
            ) {
                requestedSparkTransfer = true;
                SparkHelper.requestSparkTransfers(worldObj, xCoord, yCoord, zCoord, getAttachedSpark());
                markDirty();
            } else {
                requestedSparkTransfer = false;
            }
        }
    }

    private boolean partialStructureValidation() {
        structureCycle++;
        if (structureCycle >= Multiblocks.alfPortal.blocks.length) {
            structureCycle = 0;
        }
        return Multiblocks.alfPortal.checkStructurePart(worldObj, xCoord, yCoord, zCoord, facing, structureCycle);
    }

    // process recipes
    private void handleCrafts() {
        int parallel = MAX_PARALLELS;

        // check energy capacity
        if (RECIPE_MANA > 0) { // just in case some hack turns this off later.
            final int max_mana_parallel = (storedMana - CYCLE_UPKEEP) / RECIPE_MANA;
            parallel = Math.min(parallel, max_mana_parallel);
            if (parallel <= 0) return;
        }

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
        RecipeElvenTrade recipe = findMatchingRecipe(craftStack);
        if (recipe == null || recipe.getInputs().size() != 1) {
            // Recipe is bugged or went missing, dump the input to the output so we don't lose it.
            inventoryHandler.setInventorySlotContents(INPUT_SIZE + OUTPUT_SIZE - 1, craftStack.copy());
            inventoryHandler.setInventorySlotContents(0, null);
            return;
        }

        final ItemStack output_instance = recipe.getOutput();
        final int max_output_parallel = 64 / output_instance.stackSize;
        parallel = Math.min(parallel, max_output_parallel); // will be > 0

        // perform recipe in batch
        ItemStack output = output_instance.copy();
        output.stackSize = output.stackSize * parallel;

        // commit to inventory
        inventoryHandler.setInventorySlotContents(INPUT_SIZE + OUTPUT_SIZE - 1, output);
        inventoryHandler.decrStackSize(0, parallel);
    }

    // Dump output downward
    private void handleOutputs() {
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
    private void cleanupInventory(int start, int end) {
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

    @Nullable
    public static RecipeElvenTrade findMatchingRecipe(@Nonnull ItemStack stack) {
        for(RecipeElvenTrade recipe : BotaniaAPI.elvenTradeRecipes) {
            final List<Object> inputs = recipe.getInputs();
            for (Object input: inputs) {
                if (input instanceof String) {
                    int[] oreIds = OreDictionary.getOreIDs(stack);
                    for (int oreId: oreIds) {
                        if (OreDictionary.getOreName(oreId).equals(input)) {
                            return recipe;
                        }
                    }
                } else if (input instanceof ItemStack) {
                    ItemStack compare = (ItemStack) input;
                    if (compare.isItemEqual(stack)) {
                        return recipe;
                    }
                }
            }
        }
        return null;
    }

    // IWandable delegate

    public boolean onWanded(EntityPlayer wandUser) {
        this.facing = Facing2D.fromIndex((worldObj.getBlockMetadata(xCoord, yCoord, zCoord) >> 1) & 3);

        if (!isOnline) {
            Exception error = Multiblocks.alfPortal.checkEntireStructure(worldObj, xCoord, yCoord, zCoord, this.facing);
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

    // Persistence

    private static final String KEY_INVENTORY = "inv";
    private static final String KEY_MANA = "mana";
    private static final String KEY_ONLINE = "enabled";
    private static final String KEY_SPARK_TRANSFER = "st";
    private static final String KEY_FACING = "face";

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        writeCustomNBT(nbttagcompound);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readCustomNBT(packet.func_148857_g());
    }

    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setTag(KEY_INVENTORY, InventoryHelper.saveInventoryToNBT(inventoryHandler));
        compound.setBoolean(KEY_ONLINE, isOnline);
        compound.setInteger(KEY_MANA, storedMana);
        compound.setBoolean(KEY_SPARK_TRANSFER, requestedSparkTransfer);
        compound.setByte(KEY_FACING, (byte)facing.index);
    }

    public void readCustomNBT(NBTTagCompound compound) {
        InventoryHelper.readInventoryFromNBT(inventoryHandler, compound.getCompoundTag(KEY_INVENTORY));
        isOnline = compound.getBoolean(KEY_ONLINE);
        storedMana = compound.getInteger(KEY_MANA);
        clientSparkTransfer = compound.getBoolean(KEY_SPARK_TRANSFER);
        facing = Facing2D.fromIndex(compound.getByte(KEY_FACING));
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        writeCustomNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        readCustomNBT(compound);
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


    // IManaReceiver

    @Override
    public boolean isFull() {
        return storedMana >= MANA_CAPACITY;
    }

    @Override
    public void recieveMana(int i) {
        storedMana += i;
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        return storedMana;
    }

    // Mana HUD

    @SideOnly(Side.CLIENT)
    public void renderHUD(Minecraft mc, ScaledResolution res) {
        ChargeState state = ChargeState.genState(isOnline, storedMana, ACTIVATE_MANA);
        String tooltip = state.getLocalisedHudString(BHBlocks.autoPortal);
        HUDHandler.drawSimpleManaHUD(state.color, storedMana, MANA_CAPACITY, tooltip, res);
    }

    // ISparkAttachable

    @Override
    public boolean canAttachSpark(ItemStack stack) {
        return true;
    }

    @Override
    public void attachSpark(ISparkEntity entity) {
        sparkCycleRemaining = 0;
    }

    @Override
    public int getAvailableSpaceForMana() {
        return Math.max(0, MANA_CAPACITY - storedMana);
    }

    @Override
    public ISparkEntity getAttachedSpark() {
        List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        if(sparks.size() == 1) {
            Entity e = (Entity) sparks.get(0);
            return (ISparkEntity) e;
        }

        return null;
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return storedMana >= (isOnline ? MANA_CAPACITY : ACTIVATE_MANA + SPARK_BUFFER_MANA);
    }

    // IWrenchable

    @Override
    public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int facingIndex) {
        return Facing2D.fromIC2(facingIndex) != null;
    }

    @Override
    public short getFacing() {
        return (short)facing.ic2index;
    }

    @Override
    public void setFacing(short facingIndex) {
        Facing2D newFacing = Facing2D.fromIC2(facingIndex);
        if (newFacing != null && newFacing == facing) return;

        isOnline = false;
        facing = newFacing;

        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, facing.index << 1, MC_BLOCK_UPDATE + MC_BLOCK_SEND_TO_CLIENT);
    }

    @Override
    public boolean wrenchCanRemove(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public float getWrenchDropRate() {
        return 1;
    }

    @Override
    public ItemStack getWrenchDrop(EntityPlayer entityPlayer) {
        return new ItemStack(BHBlocks.autoPortal);
    }
}
