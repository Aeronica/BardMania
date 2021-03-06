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

import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import net.aeronica.mods.bard_mania.Reference;
import net.aeronica.mods.bard_mania.client.actions.base.ActionManager;
import net.aeronica.mods.bard_mania.client.actions.base.ModelDummy;
import net.aeronica.mods.bard_mania.client.audio.SoundHelper;
import net.aeronica.mods.bard_mania.server.IPlaceableBounding;
import net.aeronica.mods.bard_mania.server.LocationArea;
import net.aeronica.mods.bard_mania.server.ModConfig;
import net.aeronica.mods.bard_mania.server.caps.BardActionHelper;
import net.aeronica.mods.bard_mania.server.init.ModInstruments;
import net.aeronica.mods.bard_mania.server.item.ItemInstrument;
import net.aeronica.mods.bard_mania.server.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.util.Iterator;

import static net.aeronica.mods.bard_mania.client.actions.base.ModelAccessor.*;
import static net.aeronica.mods.bard_mania.client.render.RenderHelper.decrementPlayTimers;
import static net.aeronica.mods.bard_mania.client.render.RenderHelper.renderPartyingWhilePlaying;
import static net.aeronica.mods.bard_mania.server.ModConfig.Client.INPUT_MODE.MIDI;
import static net.minecraft.client.gui.inventory.GuiInventory.drawEntityOnScreen;

/*
 * box Rendering code mechanics by thebrightspark from the mod StructuralRelocation
 * https://github.com/thebrightspark/StructuralRelocation
 *
 *                  GNU GENERAL PUBLIC LICENSE
 *                     Version 2, June 1991
 *
 * Copyright (C) 1989, 1991 Free Software Foundation, Inc., <http://fsf.org/>
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 */
@SuppressWarnings("unused")
@Mod.EventBusSubscriber(Side.CLIENT)
public class RenderEvents
{
    public static final ResourceLocation GUI_BACKGROUND = new ResourceLocation(Reference.MOD_ID, "textures/gui/gui_player_background.png");
    public static final int HOT_BAR_CLEARANCE = 40;
    private static Minecraft mc = Minecraft.getMinecraft();
    private static float motionIncDec = 0.05F;
    private static float motionSimple = 0F;

    public static final ItemStack DRUM_STICK = new ItemStack(ModInstruments.DRUM_STICK);
    public static final ItemStack MALLET = new ItemStack(ModInstruments.MALLET);

    public static Field inPortal = ReflectionHelper.findField(Entity.class, "inPortal", "field_71087_bX");

    private static RenderLivingBase<?> renderLivingBase;

    public static RenderLivingBase<?> getRenderLivingBase()
    {
        return renderLivingBase;
    }

    private static ItemStack getHeldSelector()
    {
        Iterator<ItemStack> heldItems = mc.player.getHeldEquipment().iterator();
        while (heldItems.hasNext())
        {
            ItemStack held = heldItems.next();
            if (!held.isEmpty() && held.getItem() instanceof IPlaceableBounding)
                return held;
        }
        return ItemStack.EMPTY;
    }

    private static void renderBox(BlockPos pos, double partialTicks)
    {
        renderBox(new AxisAlignedBB(pos).grow(-0.00625d), partialTicks);
    }

    private static void renderBox(BlockPos pos1, BlockPos pos2, double partialTicks)
    {
        renderBox(new AxisAlignedBB(pos1, pos2).grow(-0.00625d), partialTicks);
    }

    private static void renderBox(AxisAlignedBB box, double partialTicks)
    {
        //Get player's actual position
        EntityPlayerSP player = mc.player;
        double x = player.prevPosX + (player.posX - player.prevPosX) * partialTicks;
        double y = player.prevPosY + (player.posY - player.prevPosY) * partialTicks;
        double z = player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks;
        //Render the box
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(4f);
        GlStateManager.disableTexture2D();
        GlStateManager.translate(-x, -y, -z);
        RenderGlobal.drawSelectionBoundingBox(box, 0f, 1f, 1f, 0.4f);
        RenderGlobal.renderFilledBox(box, 0f, 1f, 1f, 0.2f);
        RenderGlobal.drawSelectionBoundingBox(box, 0f, 1f, 1f, 0.4f);
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    @SubscribeEvent
    public static void renderSelection(RenderWorldLastEvent event)
    {
        //Get held Selector item
        ItemStack heldItem = getHeldSelector();
        if (heldItem.isEmpty()) return;

        IPlaceableBounding blockPlacer = (IPlaceableBounding) heldItem.getItem();
        EntityPlayerSP player = mc.player;
        World world = player.getEntityWorld();

        RayTraceResult rayTraceResult = ForgeHooks.rayTraceEyes(player, 5);

        if (rayTraceResult != null && rayTraceResult.typeOfHit.equals(RayTraceResult.Type.BLOCK) && rayTraceResult.sideHit.equals(EnumFacing.UP))
        {
            BlockPos pos = rayTraceResult.getBlockPos();
            if (blockPlacer.canPlaceHere(player, world, heldItem, pos, rayTraceResult.sideHit))
            {
                LocationArea boundingBox = blockPlacer.getBoundingBox(player, world, pos);
                renderBox(boundingBox.getStartingPoint(), boundingBox.getStartPointPlusSize(), event.getPartialTicks());
            }
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Post event)
    {
        // display a mini-me when in first-person view, in MIDI mode and an instrument is equipped
        if (mc.gameSettings.showDebugInfo || mc.gameSettings.thirdPersonView > 0 || !mc.inGameHasFocus || ModConfig.client.input_mode != MIDI) return;
        if (!event.isCancelable() && event.getType() == ElementType.EXPERIENCE && mc.player.getHeldItemMainhand().getItem() instanceof ItemInstrument && RenderHelper.canRenderEquippedInstrument(mc.player))
        {
            int width = event.getResolution().getScaledWidth();
            int height = event.getResolution().getScaledHeight() - HOT_BAR_CLEARANCE;
            drawGuiPlayerBackgroundLayer(mc.getRenderPartialTicks(), (width), height);
        }
//        int yy = 20;
//        for (EntityPlayer player : mc.world.playerEntities)
//        {boolean isInPortal = false;
//            try
//            {
//                isInPortal = inPortal.getBoolean(player);
//            } catch (IllegalAccessException e)
//            {
//                e.printStackTrace();
//            }
//            drawCenteredString(String.format("%s %s isInPortal: %s %5.2f", player.getDisplayName().getUnformattedText(), BardActionHelper.isInstrumentEquipped(player), isInPortal, mc.player.prevTimeInPortal),
//                               event.getResolution().getScaledWidth()/2, yy += 10, 0xFFFFFF);
//        }
    }

    /**
     * Renders the specified text to the screen, center-aligned. Args : renderer, string, x, y, color
     */
    public static void drawCenteredString(String text, int x, int y, int color)
    {
        mc.fontRenderer.drawStringWithShadow(text, (float)(x - mc.fontRenderer.getStringWidth(text) / 2), (float)y, color);
    }

    /**
     * Renders the specified text to the screen. Args : renderer, string, x, y, color
     */
    public static void drawString(String text, int x, int y, int color)
    {
        mc.fontRenderer.drawStringWithShadow(text, (float)x, (float)y, color);
    }

    // TODO: add position and area size - incomplete
    private static void drawGuiPlayerBackgroundLayer(float partialTicks, int scaledWidth, int scaledHeight)
    {
        mc.getTextureManager().bindTexture(GUI_BACKGROUND);
        int xPos = (scaledWidth - 50) / scaledWidth ;
        int yPos = (scaledHeight - 60) / scaledHeight;
        int entityXPos = (xPos + 50/2) + 2;
        int entityYPos = (yPos + 60 - 5);

        Gui.drawModalRectWithCustomSizedTexture(xPos, yPos, 0f,0f,50, 60, 128, 128);
        drawEntityOnScreen(entityXPos, entityYPos , 25, (float) 10, (float) -10, mc.player);
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderSpecificHandEvent event)
    {
        ItemStack heldItem = event.getItemStack();
        if (event.getHand().equals(EnumHand.OFF_HAND) && BardActionHelper.isInstrumentEquipped(mc.player)) event.setCanceled(true);

        if (!(heldItem.getItem() instanceof ItemInstrument)) return;
        Instrument instrument = ((ItemInstrument) heldItem.getItem()).getInstrument();

        if (event.getHand() == EnumHand.MAIN_HAND && BardActionHelper.isInstrumentEquipped(mc.player) && instrument.general.wearable)
        {
            event.setCanceled(true);
            GlStateManager.pushMatrix();
            {
                // translate the worn item to be in front of the players mid body
               GlStateManager.translate(0f, -0.0625 * 9, -0.0625 * 9);

                // apply the equipped first person wearable item translations
                GlStateManager.translate(instrument.display.equipped_first_person.translation[0], instrument.display.equipped_first_person.translation[1], instrument.display.equipped_first_person.translation[2]);
                GlStateManager.rotate(instrument.display.equipped_first_person.rotation[0], 0, 0, 1);
                GlStateManager.rotate(instrument.display.equipped_first_person.rotation[1], 0, 1, 0);
                GlStateManager.rotate(instrument.display.equipped_first_person.rotation[2], 1, 0, 0);
                GlStateManager.scale(instrument.display.equipped_first_person.scale[0], instrument.display.equipped_first_person.scale[1], instrument.display.equipped_first_person.scale[2]);
                Minecraft.getMinecraft().getRenderItem().renderItem(event.getItemStack(), ItemCameraTransforms.TransformType.NONE);
            }
            GlStateManager.popMatrix();
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase.equals(TickEvent.Phase.START))
        {
            motionSimple += motionIncDec;
            if (motionSimple > 1F) motionIncDec = -0.0250F;
            if (motionSimple < -1F) motionIncDec = 0.0250F;
            decrementPlayTimers();
            SoundHelper.updateBackgroundMusicPausing();
        }
    }

    @SubscribeEvent
    public static void onRenderLivingEvent(RenderLivingEvent.Pre event)
    {
        renderLivingBase = event.getRenderer();
        renderPartyingWhilePlaying();
    }

    @SubscribeEvent
    public static void onRenderHeldItem(RenderItemEvent.Held.Pre event)
    {
        if(!((event.getEntity() instanceof EntityPlayer) && (event.getEntity().getHeldItemMainhand().getItem() instanceof ItemInstrument)) && RenderHelper.canRenderEquippedInstrument((EntityPlayer) event.getEntity()))
        {
            event.setCanceled(false);
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntity();
        ItemStack itemMain = player.getHeldItemMainhand();
        boolean isMainHandHeld = !itemMain.isEmpty() && itemMain.getItem() instanceof ItemInstrument;
        boolean renderLeft = event.getHandSide().equals(EnumHandSide.LEFT);
        boolean canRenderEquippedInstrument = RenderHelper.canRenderEquippedInstrument(player);

        if (player.getPrimaryHand() != event.getHandSide())
            if (isMainHandHeld && canRenderEquippedInstrument)
            {
                event.setCanceled(true);
                return;
            }

        ItemStack heldItem = event.getItem();
        if (heldItem.getItem() instanceof ItemInstrument && event.getHandSide().equals(player.getPrimaryHand()) && canRenderEquippedInstrument)
        {
            event.setCanceled(true);
            Instrument instrument = ((ItemInstrument) heldItem.getItem()).getInstrument();
            if (instrument.general.wearable) return;

            applyRightHandHeldItemTransforms(ActionManager.getModelDummy(player), motionSimple);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(player, heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, renderLeft);
        }

        if (heldItem.getItem() instanceof ItemInstrument && !event.getHandSide().equals(player.getPrimaryHand()) && canRenderEquippedInstrument)
        {
            event.setCanceled(true);
            Instrument instrument = ((ItemInstrument) heldItem.getItem()).getInstrument();
            if (instrument.general.wearable) return;

            applyLeftHandHeldItemTransforms(ActionManager.getModelDummy(player), motionSimple);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(player, heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, renderLeft);
        }
    }

    @SubscribeEvent
    public static void onSetupAngles(ModelPlayerEvent.SetupAngles.Post event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemInstrument) && RenderHelper.canRenderEquippedInstrument(player))
        {
            ModelPlayer model = event.getModelPlayer();
            applyPlayerModelRotation(model, ActionManager.getModelDummy(player), motionSimple, player.getPrimaryHand().equals(EnumHandSide.LEFT));
            copyModelAngles(model.bipedRightArm, model.bipedRightArmwear);
            copyModelAngles(model.bipedLeftArm, model.bipedLeftArmwear);
            copyModelAngles(model.bipedRightLeg, model.bipedRightLegwear);
            copyModelAngles(model.bipedLeftLeg, model.bipedLeftLegwear);
        }
    }

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemInstrument)
        {
            applyPlayerPreRender(player, ActionManager.getModelDummy(player), motionSimple, player.getPrimaryHand().equals(EnumHandSide.LEFT));
        }

    }

    @SubscribeEvent
    public static void onRenderEntityItem(RenderItemEvent.Entity.Pre event)
    {
        // not really needed since this mod is not using any attachments, but might be fun to play with
        event.setCanceled(renderInstrument(event.getItem(), event.getTransformType()));
    }

    @SubscribeEvent
    public static void onRenderEntityItem(RenderItemEvent.Gui.Pre event)
    {
        // not really needed since this mod is not using any attachments, but might be fun to play with
        event.setCanceled(renderInstrument(event.getItem(), event.getTransformType()));
    }

    private static boolean renderInstrument(ItemStack stack, ItemCameraTransforms.TransformType transformType)
    {
        if (stack.getItem() instanceof ItemInstrument)
        {
            GlStateManager.pushMatrix();
            RenderUtil.applyTransformType(stack, transformType);
            RenderUtil.renderModel(stack);
            GlStateManager.popMatrix();
            return true;
        }
        return false;
    }

    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }

    private static void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float motion, boolean leftHand)
    {
        model.bipedHead.rotateAngleX = actions.getPartValue(HEAD_POSE_ROT_X) - actions.getPartValue(HEAD_ACTION_ROT_X);
        model.bipedHead.rotateAngleY += actions.getPartValue(HEAD_POSE_ROT_Y) - actions.getPartValue(HEAD_ACTION_ROT_Y);
        model.bipedHead.rotateAngleZ += actions.getPartValue(HEAD_POSE_ROT_Z) - actions.getPartValue(HEAD_ACTION_ROT_Z);

        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_X) - actions.getPartValue(BODY_ACTION_ROT_X);
        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_Y) - actions.getPartValue(BODY_ACTION_ROT_Y);
        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_Z) - actions.getPartValue(BODY_ACTION_ROT_Z);

        model.bipedRightArm.rotateAngleX = actions.getPartValue(RIGHT_ARM_POSE_ROT_X) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_X) + motion / 30f;
        model.bipedRightArm.rotateAngleY = actions.getPartValue(RIGHT_ARM_POSE_ROT_Y) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_Y) - motion / 30f;
        model.bipedRightArm.rotateAngleZ = actions.getPartValue(RIGHT_ARM_POSE_ROT_Z) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_Z);

        model.bipedLeftArm.rotateAngleX = actions.getPartValue(LEFT_ARM_POSE_ROT_X) - actions.getPartValue(LEFT_ARM_ACTION_ROT_X) - motion / 30f;
        model.bipedLeftArm.rotateAngleY = actions.getPartValue(LEFT_ARM_POSE_ROT_Y) - actions.getPartValue(LEFT_ARM_ACTION_ROT_Y) + motion / 30f;
        model.bipedLeftArm.rotateAngleZ = actions.getPartValue(LEFT_ARM_POSE_ROT_Z) - actions.getPartValue(LEFT_ARM_ACTION_ROT_Z);

        model.bipedRightLeg.rotateAngleX += actions.getPartValue(RIGHT_LEG_POSE_ROT_X) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_X);
        model.bipedRightLeg.rotateAngleY += actions.getPartValue(RIGHT_LEG_POSE_ROT_Y) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_Y);
        model.bipedRightLeg.rotateAngleZ += actions.getPartValue(RIGHT_LEG_POSE_ROT_Z) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_Z);

        model.bipedLeftLeg.rotateAngleX += actions.getPartValue(LEFT_LEG_POSE_ROT_X) - actions.getPartValue(LEFT_LEG_ACTION_ROT_X);
        model.bipedLeftLeg.rotateAngleY += actions.getPartValue(LEFT_LEG_POSE_ROT_Y) - actions.getPartValue(LEFT_LEG_ACTION_ROT_Y);
        model.bipedLeftLeg.rotateAngleZ += actions.getPartValue(LEFT_LEG_POSE_ROT_Z) - actions.getPartValue(LEFT_LEG_ACTION_ROT_Z);
    }

    private static void applyPlayerPreRender(EntityPlayer player, ModelDummy actions, float motion, boolean leftHand)
    {
        player.prevRenderYawOffset = player.prevRotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
        player.renderYawOffset = player.rotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
    }

    public static void applyRightHandHeldItemTransforms(ModelDummy actions, float motion)
    {
        GlStateManager.translate(actions.getPartValue(RIGHT_HAND_ITEM_TRANS_X), actions.getPartValue(RIGHT_HAND_ITEM_TRANS_Y), actions.getPartValue(RIGHT_HAND_ITEM_TRANS_Z));
        GlStateManager.rotate(actions.getPartValue(RIGHT_HAND_ITEM_ROT_Z), 0, 0, 1);
        GlStateManager.rotate(actions.getPartValue(RIGHT_HAND_ITEM_ROT_Y), 0, 1, 0);
        GlStateManager.rotate(actions.getPartValue(RIGHT_HAND_ITEM_ROT_X), 1, 0, 0);
    }

    public static void applyLeftHandHeldItemTransforms(ModelDummy actions, float motion)
    {
        GlStateManager.translate(actions.getPartValue(LEFT_HAND_ITEM_TRANS_X), actions.getPartValue(LEFT_HAND_ITEM_TRANS_Y), actions.getPartValue(LEFT_HAND_ITEM_TRANS_Z));
        GlStateManager.rotate(actions.getPartValue(LEFT_HAND_ITEM_ROT_Z), 0, 0, 1);
        GlStateManager.rotate(actions.getPartValue(LEFT_HAND_ITEM_ROT_Y), 0, 1, 0);
        GlStateManager.rotate(actions.getPartValue(LEFT_HAND_ITEM_ROT_X), 1, 0, 0);
    }
}

