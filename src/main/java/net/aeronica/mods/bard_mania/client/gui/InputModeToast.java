package net.aeronica.mods.bard_mania.client.gui;

import net.aeronica.mods.bard_mania.client.render.RenderEvents;
import net.aeronica.mods.bard_mania.common.ModConfig;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class InputModeToast implements IToast
{
    private long firstDrawTime;
    private boolean firstShow;
    private ItemStack itemStack = RenderEvents.MALLET;
    private ModConfig.Client.INPUT_MODE inputMode = ModConfig.client.input_mode;

    public InputModeToast() {/*  NOP */}

    public InputModeToast(ItemStack itemStack)
    {
        this.itemStack = itemStack.copy();
    }

    @Override
    public Visibility draw(GuiToast toastGui, long delta)
    {
        if (this.firstShow)
        {
            this.firstDrawTime = delta;
            this.firstShow = false;
        }
        if (!inputMode.equals(ModConfig.client.input_mode))
            return IToast.Visibility.HIDE;
        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 32, 160, 32);
        toastGui.getMinecraft().fontRenderer.drawString(I18n.format("config.bard_mania.input_mode"), 30, 7, -11534256);
        toastGui.getMinecraft().fontRenderer.drawString(I18n.format((ModConfig.client.input_mode).toString()), 30, 18, -11534256);
        RenderHelper.enableGUIStandardItemLighting();
        toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, this.itemStack, 8, 8);
        return delta - this.firstDrawTime >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;
    }
}
