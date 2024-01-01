package net.fuzzycraft.botanichorizons.patches;

import com.cricketcraft.chisel.api.carving.CarvingUtils;
import com.cricketcraft.chisel.api.carving.ICarvingGroup;
import com.cricketcraft.chisel.api.carving.ICarvingRegistry;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.ModFluffBlocks;

import javax.annotation.Nullable;

public class ChiselPatches {

    public static final String GROUP_AZULEJO = "BH_Azulejo";
    public static final String GROUP_LIVINGWOOD = "BH_Livingwood";
    public static final String GROUP_LIVINGROCK = "BH_Livingrock";
    public static final String GROUP_DREAMWOOD = "BH_Dreamwood";

    @Nullable
    private static ICarvingRegistry getChiselIfAvailable() {
        return CarvingUtils.getChiselRegistry();
    }

    public static void applyPatches() {
        ICarvingRegistry registry = getChiselIfAvailable();
        if (registry == null) return;

        // Azulejo cycling into chisel
        ICarvingGroup azulejoGroup = new CarvingUtils.SimpleCarvingGroup(GROUP_AZULEJO);
        registry.addGroup(azulejoGroup);
        for (int i = 0; i < 12; i++) {
            registry.addVariation(GROUP_AZULEJO, ModBlocks.customBrick, 4 + i, 0);
        }

        // note that some LW/LR/DW variations have special properties and can't be chiseled
        registry.addGroup(new CarvingUtils.SimpleCarvingGroup(GROUP_LIVINGWOOD));
        registry.addVariation(GROUP_LIVINGWOOD, ModBlocks.livingwood, 1, 0);
        registry.addVariation(GROUP_LIVINGWOOD, ModBlocks.livingwood, 3, 0);
        registry.addVariation(GROUP_LIVINGWOOD, ModBlocks.livingwood, 4, 0);

        registry.addGroup(new CarvingUtils.SimpleCarvingGroup(GROUP_LIVINGROCK));
        registry.addVariation(GROUP_LIVINGROCK, ModBlocks.livingrock, 1, 0);
        registry.addVariation(GROUP_LIVINGROCK, ModBlocks.livingrock, 3, 0);
        registry.addVariation(GROUP_LIVINGROCK, ModBlocks.livingrock, 4, 0);

        registry.addGroup(new CarvingUtils.SimpleCarvingGroup(GROUP_DREAMWOOD));
        registry.addVariation(GROUP_DREAMWOOD, ModBlocks.dreamwood, 1, 0);
        registry.addVariation(GROUP_DREAMWOOD, ModBlocks.dreamwood, 3, 0);
        registry.addVariation(GROUP_DREAMWOOD, ModBlocks.dreamwood, 4, 0);

        // diorite etc
        for (int i = 0; i < 4; i++) {
            int base = 4 * i;
            registry.addVariation("andesite", ModFluffBlocks.stone, base + 0, 0);
            registry.addVariation("basalts", ModFluffBlocks.stone,  base + 1, 0);
            registry.addVariation("diorite", ModFluffBlocks.stone,  base + 2, 0);
            registry.addVariation("granite", ModFluffBlocks.stone,  base + 3, 0);
        }
    }
}
