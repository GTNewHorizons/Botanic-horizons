package net.fuzzycraft.botanichorizons.mod;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public class ServerProxy implements IProxy {

    @Override
    public void onInit() {
        // No-op
    }

    @Override
    public void onPostInit() {
        // No-op
    }
}
