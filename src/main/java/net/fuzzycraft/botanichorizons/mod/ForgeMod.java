package net.fuzzycraft.botanichorizons.mod;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

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

@Mod(modid = ForgeMod.MOD_ID, name = ForgeMod.MOD_NAME, version = ForgeMod.VERSION, dependencies = ForgeMod.DEPENDENCIES)
public class ForgeMod {
    public static final String MOD_ID = "botanichorizons";
    public static final String MOD_NAME = MOD_ID;
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final String DEPENDENCIES = "required-after:Baubles;required-after:Thaumcraft;required-after:Botania;required-after:gregtech;after:witchery;after:BiomesOPlenty;after:dreamcraft;required-after:TConstruct;required-after:Avaritia;after:chisel";

    @Mod.Instance(MOD_ID)
    public static ForgeMod instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("Initializer called");

        OredictPatches.applyPatches();

        CraftingPatches.applyPatches();
        ApothecaryPatches.applyPatches();
        DaisyPatches.applyPatches();
        ManaInfusionPatches.applyPatches();
        RunicAltarPatches.applyPatches();
        AlfheimPatches.applyPatches();
        BreweryPatches.applyPatches();

        GregtechPatches.applyPatches();
        AvaritiaPatches.applyPatches();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ThaumcraftAspects.registerAspects();
        ThaumcraftPatches.applyPatches();
        if (Loader.isModLoaded("chisel")) {
            ChiselPatches.applyPatches();
        }
    }
}
