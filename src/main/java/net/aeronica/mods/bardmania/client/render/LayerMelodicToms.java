package net.aeronica.mods.bardmania.client.render;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.HoldType;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.Map;

public class LayerMelodicToms implements LayerRenderer<EntityLivingBase>
{
    private static final LayerMelodicToms LAYER_MELODIC_TOMS = new LayerMelodicToms();
    private static boolean isAdded = false;

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            Instrument instrument = ((ItemHandHeld) entitylivingbaseIn.getHeldItemMainhand().getItem()).getInstrument();
            ItemStack itemStack = entitylivingbaseIn.getHeldItemMainhand();
            if (instrument.general.holdType.equals(HoldType.TWO_HANDED_DRUM) && instrument.id.equalsIgnoreCase("melodic_toms"))
            {
                GlStateManager.pushMatrix();
                GlStateManager.translate(0, 0.0625 * 9, -0.0625 * 9);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.rotate(180, 1, 0, 0);
                BardMania.proxy.getMinecraft().getRenderItem().renderItem(itemStack, entitylivingbaseIn, ItemCameraTransforms.TransformType.NONE, false);
                GlStateManager.popMatrix();
            }
        }
    }

    @Override
    public boolean shouldCombineTextures()
    {
        return false;
    }

    public static void addLayer()
    {
        if (isAdded) return;
        try
        {
            for (Map.Entry<String, RenderPlayer> e : Minecraft.getMinecraft().getRenderManager().getSkinMap().entrySet())
            {
                e.getValue().addLayer(LAYER_MELODIC_TOMS);
                isAdded = true;
                ModLogger.info("LayerMelodicToms added %s", e.toString());
            }
        } catch (Exception e)
        {
            ModLogger.error(e);
        }
    }
}
