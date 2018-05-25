package net.aeronica.mods.bardmania.client.render;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class LayerDrums implements LayerRenderer<AbstractClientPlayer>
{
    private static final LayerDrums layerDrums = new LayerDrums();

    @Override
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            GlStateManager.pushMatrix();
            GlStateManager.scale(0.5,0.5,0.5);
            GlStateManager.translate(0,0,-1);
            GlStateManager.rotate(180, 1,0,0);
            BardMania.proxy.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.WATER_BUCKET), entitylivingbaseIn, ItemCameraTransforms.TransformType.NONE, false);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    public static void addLayer()
    {
        try
        {
            for(Map.Entry<String, RenderPlayer> e : Minecraft.getMinecraft().getRenderManager().getSkinMap().entrySet())
            {
                e.getValue().addLayer(layerDrums);
                ModLogger.info("LayerDrums added %s", e.toString());
            }
        } catch (Exception e)
        {
            ModLogger.error(e);
        }
    }
}
