package net.fuzzycraft.botanichorizons.addons.item;

public class ItemSelectiveWrench extends ItemSuperchargedWrench {
    public static final String ITEM_ID = "elvenDisassemblyWrench";
    public static final int TOOL_MINING_LEVEL = 3;

    public ItemSelectiveWrench(ToolMaterial toolMaterial) {
        super(toolMaterial, TOOL_MINING_LEVEL, ITEM_ID);
    }
}
