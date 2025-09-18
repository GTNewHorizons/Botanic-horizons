package net.fuzzycraft.botanichorizons.addons;

import gregtech.api.enums.Mods;
import net.fuzzycraft.botanichorizons.addons.item.ItemDisassemblyWrench;
import net.fuzzycraft.botanichorizons.addons.item.ItemManaWrench;
import vazkii.botania.api.BotaniaAPI;

public class BHItems {
    public static ItemManaWrench manaWrench;
    public static ItemDisassemblyWrench disassemblyWrench;

    public static void initItems() {
        if (Mods.StructureLib.isModLoaded()) {
            // Items to disassemble multiblocks
            manaWrench = new ItemManaWrench(BotaniaAPI.manasteelToolMaterial, 2, "ManaWrench");
            disassemblyWrench = new ItemDisassemblyWrench(BotaniaAPI.elementiumToolMaterial);
        }
    }
}
