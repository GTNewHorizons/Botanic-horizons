package net.fuzzycraft.botanichorizons.mod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import net.fuzzycraft.botanichorizons.addons.BHItems;
import net.fuzzycraft.botanichorizons.addons.client.render.WrenchItemRenderer;
import net.fuzzycraft.botanichorizons.lexicon.BHLexicon;
import net.minecraftforge.client.MinecraftForgeClient;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void onInit() {
        BHLexicon.clientInit();
    }

    @Override
    public void onPostInit() {
        if (Mods.StructureLib.isModLoaded()) {
            MinecraftForgeClient.registerItemRenderer(BHItems.selectiveWrench, new WrenchItemRenderer());
        }
    }
}
