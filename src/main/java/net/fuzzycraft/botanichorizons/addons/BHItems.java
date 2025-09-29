package net.fuzzycraft.botanichorizons.addons;

import gregtech.api.enums.Mods;
import net.fuzzycraft.botanichorizons.addons.item.ItemDisassemblyWrench;
import net.fuzzycraft.botanichorizons.addons.item.ItemManaWrench;
import net.fuzzycraft.botanichorizons.addons.item.ItemSelectiveWrench;
import vazkii.botania.api.BotaniaAPI;

public class BHItems {
    public static ItemManaWrench manasteelWrench;
    public static ItemManaWrench terrasteelWrench;
    public static ItemDisassemblyWrench disassemblyWrench;
    public static ItemManaWrench elvenWrench;
    public static ItemSelectiveWrench selectiveWrench;

    public static void initItems() {

        if (Mods.GregTech.isModLoaded()) {
            manasteelWrench = new ItemManaWrench(BotaniaAPI.manasteelToolMaterial, 2, "manasteelWrench");
            terrasteelWrench = new ItemManaWrench(BotaniaAPI.terrasteelToolMaterial, 4, "terrasteelWrench");
            elvenWrench = new ItemManaWrench(BotaniaAPI.elementiumToolMaterial, 3, "elvenWrench");
        }

        if (Mods.StructureLib.isModLoaded()) {
            // Items to disassemble multiblocks
            disassemblyWrench = new ItemDisassemblyWrench(BotaniaAPI.terrasteelToolMaterial);
            selectiveWrench = new ItemSelectiveWrench(BotaniaAPI.elementiumToolMaterial);
        }
    }
}
