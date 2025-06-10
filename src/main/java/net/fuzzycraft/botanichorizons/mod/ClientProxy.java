package net.fuzzycraft.botanichorizons.mod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fuzzycraft.botanichorizons.lexicon.BHLexicon;

@SideOnly(Side.CLIENT)
public class ClientProxy implements IProxy {
    @Override
    public void onInit() {
        BHLexicon.clientInit();
    }

    @Override
    public void onPostInit() {

    }
}
