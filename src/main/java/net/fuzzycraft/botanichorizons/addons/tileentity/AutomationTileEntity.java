package net.fuzzycraft.botanichorizons.addons.tileentity;

import cpw.mods.fml.common.FMLLog;
import ic2.api.tile.IWrenchable;
import net.fuzzycraft.botanichorizons.util.Facing2D;
import net.fuzzycraft.botanichorizons.util.SparkHelper;
import net.fuzzycraft.botanichorizons.util.multiblock.MultiblockHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.mana.IManaReceiver;
import vazkii.botania.api.mana.spark.ISparkAttachable;
import vazkii.botania.api.mana.spark.ISparkEntity;

import java.util.List;

import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_SEND_TO_CLIENT;
import static net.fuzzycraft.botanichorizons.util.Constants.MC_BLOCK_UPDATE;

abstract public class AutomationTileEntity extends TileEntity implements IManaReceiver, ISparkAttachable, IWrenchable {

    // Common config

    final MultiblockHelper structure;

    // Common state

    protected int storedMana = 0; // can be > MANA_CAPACITY to avoid losses
    protected Facing2D facing = Facing2D.NORTH;
    protected boolean isOnline = false;
    protected int sparkCycleRemaining = 0;
    protected boolean clientSparkTransfer = false;
    protected int structureCycle = 0;

    protected int cachedMana = Integer.MIN_VALUE;
    protected int statusCycle = 0;

    // Delegated state

    public abstract int getManaMaximum();
    protected abstract void updateEntityCrafting();

    public AutomationTileEntity(MultiblockHelper structure) {
        super();
        this.structure = structure;
    }

    // Shared Business logic

    public final int SPARK_CYCLE_TICKS = 100; // time between spark requests for extra mana
    // Startup safety buffer. Should be slightly more than the upkeep needed of one SPARK_CYCLE
    public final int SPARK_BUFFER_MANA = 2000;
    public final int STATUS_CYCLE = 100;
    public final int DEFAULT_MANA_ERROR_RANGE = 500;

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
            shareTEIfNeeded();
        }
    }

    protected void updateEntitySparks() {
        if (sparkCycleRemaining > 0) {
            sparkCycleRemaining--;
        } else {
            sparkCycleRemaining = SPARK_CYCLE_TICKS;
            if (areIncomingTranfersDone()) {
                return;
            }

            if (storedMana < getManaMaximum() + SPARK_BUFFER_MANA) {
                SparkHelper.requestSparkTransfers(worldObj, xCoord, yCoord, zCoord, getAttachedSpark());
                markDirty();
            }
        }
    }

    protected boolean partialStructureValidation() {
        structureCycle++;
        if (structureCycle >= structure.blocks.length) {
            structureCycle = 0;
        }
        return structure.checkStructurePart(worldObj, xCoord, yCoord, zCoord, facing, structureCycle);
    }

    protected void shareTEIfNeeded() {
        statusCycle++;
        if (statusCycle > STATUS_CYCLE) {
            if (shouldShareTE()) {
                VanillaPacketDispatcher.dispatchTEToNearbyPlayers(this);
                cachedMana = storedMana;
            }
            statusCycle = 0;
        }
    }

    protected boolean shouldShareTE() {
        int delta = cachedMana - storedMana;
        return delta <= -DEFAULT_MANA_ERROR_RANGE || delta >= DEFAULT_MANA_ERROR_RANGE;
    }

    // Persistence

    public static final String KEY_MANA = "mana";
    public static final String KEY_ONLINE = "enabled";
    public static final String KEY_SPARK_TRANSFER = "st";
    public static final String KEY_FACING = "face";

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
        compound.setBoolean(KEY_ONLINE, isOnline);
        compound.setInteger(KEY_MANA, storedMana);
        compound.setByte(KEY_FACING, (byte)facing.index);
    }

    public void readCustomNBT(NBTTagCompound compound) {
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
    public ISparkEntity getAttachedSpark() {
        List<ISparkEntity> sparks = worldObj.getEntitiesWithinAABB(ISparkEntity.class, AxisAlignedBB.getBoundingBox(xCoord, yCoord + 1, zCoord, xCoord + 1, yCoord + 2, zCoord + 1));
        if(sparks.size() == 1) {
            Entity e = (Entity) sparks.get(0);
            return (ISparkEntity) e;
        }

        return null;
    }

    @Override
    public int getAvailableSpaceForMana() {
        return Math.max(0, getManaMaximum() - storedMana);
    }

    @Override
    public boolean areIncomingTranfersDone() {
        return storedMana >= getManaMaximum();
    }

    // IManaReceiver

    @Override
    public boolean isFull() {
        return storedMana >= getManaMaximum();
    }

    @Override
    public void recieveMana(int i) {
        storedMana += i;
        markDirty();
    }

    @Override
    public boolean canRecieveManaFromBursts() {
        return true;
    }

    @Override
    public int getCurrentMana() {
        return storedMana;
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

    // abstract: getWrenchDrop returns a stack of the dropped block
}
