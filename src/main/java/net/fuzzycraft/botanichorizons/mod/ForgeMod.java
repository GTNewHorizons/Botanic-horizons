package net.fuzzycraft.botanichorizons.mod;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.fuzzycraft.botanichorizons.addons.BHBlocks;

import net.fuzzycraft.botanichorizons.addons.Multiblocks;
import net.fuzzycraft.botanichorizons.lexicon.BHLexicon;
import net.fuzzycraft.botanichorizons.patches.AlfheimPatches;
import net.fuzzycraft.botanichorizons.patches.ApothecaryPatches;
import net.fuzzycraft.botanichorizons.patches.AvaritiaPatches;
import net.fuzzycraft.botanichorizons.patches.BreweryPatches;
import net.fuzzycraft.botanichorizons.patches.ChiselPatches;
import net.fuzzycraft.botanichorizons.patches.CraftingPatches;
import net.fuzzycraft.botanichorizons.patches.DaisyPatches;
import net.fuzzycraft.botanichorizons.patches.GregtechPatches;
import net.fuzzycraft.botanichorizons.patches.ManaInfusionPatches;
import net.fuzzycraft.botanichorizons.patches.OredictPatches;
import net.fuzzycraft.botanichorizons.patches.RunicAltarPatches;
import net.fuzzycraft.botanichorizons.patches.ThaumcraftAspects;
import net.fuzzycraft.botanichorizons.patches.ThaumcraftPatches;

import gregtech.api.enums.Mods;

@Mod(modid = ForgeMod.MOD_ID, name = ForgeMod.MOD_NAME, version = ForgeMod.VERSION, dependencies = ForgeMod.DEPENDENCIES)
public class ForgeMod {
    public static final String MOD_ID = "botanichorizons";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final String DEPENDENCIES = "required-after:Baubles;required-after:Thaumcraft;required-after:Botania;required-after:gregtech;after:witchery;after:BiomesOPlenty;after:dreamcraft;required-after:TConstruct;required-after:Avaritia;after:chisel";
    //public static final String DEPENDENCIES = "required-after:Botania"; // developer mode

    @Mod.Instance(MOD_ID)
    public static ForgeMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        BHBlocks.initBlocks();

        OredictPatches.applyPatches();
        AlfheimPatches.applyPatches();
        BHLexicon.preInit();

        if (!isPackMode()) return;
        CraftingPatches.applyPatches();
        ApothecaryPatches.applyPatches();
        DaisyPatches.applyPatches();
        ManaInfusionPatches.applyPatches();
        RunicAltarPatches.applyPatches();
        BreweryPatches.applyPatches();

        GregtechPatches.applyPatches();
        AvaritiaPatches.applyPatches();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        Multiblocks.init();
        BHLexicon.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        if (!isPackMode()) return;

        ThaumcraftAspects.registerAspects();
        ThaumcraftPatches.applyPatches();
        if (Mods.Chisel.isModLoaded()) {
            ChiselPatches.applyPatches();
        }
    }

    public static boolean isPackMode() {
        return Mods.Chisel.isModLoaded() &&
                Mods.Avaritia.isModLoaded() &&
                Mods.Baubles.isModLoaded() &&
                Mods.Thaumcraft.isModLoaded() &&
                Mods.GregTech.isModLoaded() &&
                Mods.TinkerConstruct.isModLoaded();
    }
}
