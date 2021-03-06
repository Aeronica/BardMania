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

package net.aeronica.mods.bard_mania.client.render;

import net.aeronica.mods.bard_mania.BardMania;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.client.actions.base.ModelAccessor;
import net.aeronica.mods.bard_mania.server.ModLogger;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;

public class LayerWearableInstrument implements LayerRenderer<EntityLivingBase>
{
    private static final LayerWearableInstrument LAYER_WEARABLE_INSTRUMENT = new LayerWearableInstrument();
    private static boolean isAdded = false;

    @Override
    public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
        if (!entitylivingbaseIn.isInvisible() && (entitylivingbaseIn.getHeldItemMainhand().getItem() instanceof ItemInstrument))
        {
            Instrument instrument = ((ItemInstrument) entitylivingbaseIn.getHeldItemMainhand().getItem()).getInstrument();
            ItemStack itemStack = ActionManager.getModelDummy((EntityPlayer) entitylivingbaseIn).getInstrumentStack();
            if (instrument.general.wearable && RenderHelper.canRenderEquippedInstrument((EntityPlayer) entitylivingbaseIn))
            {
                GlStateManager.pushMatrix();
                // apply body translation - i.e. Sneaking
                translateToBody();
                // spin and translate the worn item so north faces away from the client player
                GlStateManager.translate(0, 0.0625 * 9, -0.0625 * 9);
                GlStateManager.rotate(180, 0, 1, 0);
                GlStateManager.rotate(180, 1, 0, 0);

                // apply the wearable item translations
                GlStateManager.translate(instrument.display.equipped_third_person.translation[0], instrument.display.equipped_third_person.translation[1], instrument.display.equipped_third_person.translation[2]);
                GlStateManager.rotate(instrument.display.equipped_third_person.rotation[0], 0, 0, 1);
                GlStateManager.rotate(instrument.display.equipped_third_person.rotation[1], 0, 1, 0);
                GlStateManager.rotate(instrument.display.equipped_third_person.rotation[2], 1, 0, 0);
                GlStateManager.scale(instrument.display.equipped_third_person.scale[0], instrument.display.equipped_third_person.scale[1], instrument.display.equipped_third_person.scale[2]);

                // apply animated scaling. allows the worn item to bounce in.
                float actionScale = ActionManager.getModelDummy((EntityPlayer) entitylivingbaseIn).getPartValue(ModelAccessor.WORN_ITEM_SCALE);
                GlStateManager.scale(actionScale,actionScale,actionScale);

                BardMania.proxy.getMinecraft().getRenderItem().renderItem(itemStack, entitylivingbaseIn, ItemCameraTransforms.TransformType.NONE, false);
                GlStateManager.popMatrix();

                // isChild translations
                GlStateManager.pushMatrix();
                if (RenderEvents.getRenderLivingBase().getMainModel().isChild)
                {
                    float f = 0.5F;
                    GlStateManager.translate(0.0F, 0.75F, 0.0F);
                    GlStateManager.scale(0.5F, 0.5F, 0.5F);
                }
                // render the held items per the general wearable options
                ItemStack rightHandItem = instrument.general.rightHand.getHeldAccessory().getItem();
                ItemStack leftHandItem = instrument.general.leftHand.getHeldAccessory().getItem();
                this.renderHeldItem(entitylivingbaseIn, rightHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, EnumHandSide.RIGHT);
                this.renderHeldItem(entitylivingbaseIn, leftHandItem, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, EnumHandSide.LEFT);
                GlStateManager.popMatrix();
            }
        }
    }

    // copied from net.minecraft.client.renderer.entity.layers.LayerHeldItem
    // TODO: cleanup
    private void renderHeldItem(EntityLivingBase entityLivingBase, ItemStack itemStack, ItemCameraTransforms.TransformType transformType, EnumHandSide handSide)
    {
        if (!itemStack.isEmpty())
        {
            GlStateManager.pushMatrix();

            if (entityLivingBase.isSneaking())
            {
                GlStateManager.translate(0.0F, 0.2F, 0.0F);
            }
            // Forge: moved this call down, fixes incorrect offset while sneaking.
            boolean isLeftHand = handSide == EnumHandSide.LEFT;
            this.translateToHand(handSide);

            GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);

            if (isLeftHand)
                RenderEvents.applyLeftHandHeldItemTransforms(ActionManager.getModelDummy((EntityPlayer) entityLivingBase), 0f);
            else
                RenderEvents.applyRightHandHeldItemTransforms(ActionManager.getModelDummy((EntityPlayer) entityLivingBase), 0f);

            GlStateManager.translate((float)(isLeftHand ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(entityLivingBase, itemStack, transformType, isLeftHand);
            GlStateManager.popMatrix();
        }
    }

    // copied from net.minecraft.client.renderer.entity.layers.LayerHeldItem
    protected void translateToHand(EnumHandSide enumHandSide)
    {
        ((ModelBiped)RenderEvents.getRenderLivingBase().getMainModel()).postRenderArm(0.0625F, enumHandSide);
    }

    protected void translateToBody()
    {
        ((ModelBiped)RenderEvents.getRenderLivingBase().getMainModel()).bipedBody.postRender((float) 0.0625);
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
            });
        } catch (Exception e)
        {
            ModLogger.error(e);
        }
    }
}
