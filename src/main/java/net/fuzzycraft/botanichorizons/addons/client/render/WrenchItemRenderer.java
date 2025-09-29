package net.fuzzycraft.botanichorizons.addons.client.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import vazkii.botania.common.core.helper.ItemNBTHelper;

import static net.fuzzycraft.botanichorizons.addons.item.ItemSelectiveWrench.TAG_BLOCK;

@SideOnly(Side.CLIENT)
public class WrenchItemRenderer implements IItemRenderer {

    RenderItem renderItem = new RenderItem();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == IItemRenderer.ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack stack, Object... data) {
        GL11.glPushMatrix();

        // Fixes potential render bug with blocks that draw this item
        renderItem.zLevel = 10f;
        renderItem.renderItemIntoGUI(
                Minecraft.getMinecraft().fontRenderer,
                Minecraft.getMinecraft().renderEngine,
                stack,
                0,
                0
        );

        if (stack.stackTagCompound != null) {
            NBTTagCompound wrenchNBT = ItemNBTHelper.getCompound(stack, TAG_BLOCK, false);
            if (wrenchNBT != null) {
                ItemStack wrenchTarget = ItemStack.loadItemStackFromNBT(wrenchNBT);
                if (wrenchTarget != null) {

                    GL11.glScalef(0.65f, 0.65f, 0.65f);
                    renderItem.zLevel = 11f;

                    renderItem.renderItemIntoGUI(
                            Minecraft.getMinecraft().fontRenderer,
                            Minecraft.getMinecraft().renderEngine,
                            wrenchTarget,
                            8,
                            8
                    );
                }
            }
        }
        GL11.glPopMatrix();
    }

}
