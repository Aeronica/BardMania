package net.aeronica.mods.bardmania.client.render;

import net.aeronica.mods.bardmania.Reference;
import net.aeronica.mods.bardmania.client.model.ModelMelodicToms;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.HoldType;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

public class LayerMelodicToms implements LayerRenderer<EntityLivingBase>
{
    private static final LayerMelodicToms LAYER_MELODIC_TOMS = new LayerMelodicToms();
    private static final ModelMelodicToms modelMelodicToms = new ModelMelodicToms();
    private static final ResourceLocation textureMelodicToms = new ResourceLocation(Reference.MOD_ID, "textures/model/melodic_toms.png");
    private static boolean isAdded = false;

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            Instrument instrument = ((ItemHandHeld) entitylivingbaseIn.getHeldItemMainhand().getItem()).getInstrument();
            if (instrument.general.holdType.equals(HoldType.TWO_HANDED_DRUM))
            {
                Render<Entity> renderEntity = Minecraft.getMinecraft().getRenderManager().getEntityRenderObject(entitylivingbaseIn);
                renderEntity.bindTexture(textureMelodicToms);
                GlStateManager.pushMatrix();
                modelMelodicToms.setRotationAngles(limbSwing, limbSwingAmount, partialTicks, netHeadYaw, headPitch, scale, entitylivingbaseIn);
                modelMelodicToms.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
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
