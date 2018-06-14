/*
 * Copyright 2018 Paul Boese a.k.a Aeronica
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package net.aeronica.mods.bardmania.client.render;

import net.aeronica.mods.bardmania.BardMania;
import net.aeronica.mods.bardmania.common.ModLogger;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public class LayerWearableInstrument implements LayerRenderer<EntityLivingBase>
{
    private static final LayerWearableInstrument LAYER_WEARABLE_INSTRUMENT = new LayerWearableInstrument();
    private static boolean isAdded = false;

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            Instrument instrument = ((ItemHandHeld) entitylivingbaseIn.getHeldItemMainhand().getItem()).getInstrument();
            ItemStack itemStack = entitylivingbaseIn.getHeldItemMainhand();
            if (instrument.general.wearable)
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
            Minecraft.getMinecraft().getRenderManager().getSkinMap().entrySet().forEach(e -> {
                e.getValue().addLayer(LAYER_WEARABLE_INSTRUMENT);
                isAdded = true;
                ModLogger.info("LayerWearableInstrument added %s", e.toString());
            });
        } catch (Exception e)
        {
            ModLogger.error(e);
        }
    }
}
