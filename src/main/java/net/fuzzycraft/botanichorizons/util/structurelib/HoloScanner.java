package net.fuzzycraft.botanichorizons.util.structurelib;

import com.gtnewhorizon.structurelib.StructureEvent;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.fuzzycraft.botanichorizons.util.BlockPos;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class HoloScanner {
    private static final String TRACKING_IDENTIFIER = "BotanicHorizonsMultiblockScanner";

    public final List<BlockPos> multiblockLocations = new ArrayList<>();

    public HoloScanner(IConstructable constructable, World attachedWorld) {
        MinecraftForge.EVENT_BUS.register(this);
        StructureLibAPI.enableInstrument(TRACKING_IDENTIFIER);

        // This API stinks, send events to OnStructureEvent though a callback located in ThreadLocalStorage
        ItemStack buildStack = new ItemStack(StructureLibAPI.getDefaultHologramItem());
        constructable.construct(buildStack, true);

        StructureLibAPI.disableInstrument();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void OnStructureEvent(StructureEvent.StructureElementVisitedEvent event) {
        BlockPos pos = new BlockPos(event.getX(), event.getY(), event.getZ());
        multiblockLocations.add(pos);
    }
}