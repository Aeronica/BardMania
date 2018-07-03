package net.aeronica.mods.bardmania.client.render;

import com.mrcrayfish.obfuscate.client.event.ModelPlayerEvent;
import com.mrcrayfish.obfuscate.client.event.RenderItemEvent;
import com.mrcrayfish.obfuscate.common.event.EntityLivingInitEvent;
import net.aeronica.mods.bardmania.client.action.ActionManager;
import net.aeronica.mods.bardmania.client.action.ModelDummy;
import net.aeronica.mods.bardmania.common.IPlaceableBounding;
import net.aeronica.mods.bardmania.common.LocationArea;
import net.aeronica.mods.bardmania.item.ItemHandHeld;
import net.aeronica.mods.bardmania.object.Instrument;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderSpecificHandEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Iterator;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.*;

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
    private static Minecraft mc = Minecraft.getMinecraft();
    private static float motionIncDec = 0.05F;
    private static float motionSimple = 0F;

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
    public static void onRenderOverlay(RenderSpecificHandEvent event)
    {
        ItemStack heldItem = event.getItemStack();

        if (!(heldItem.getItem() instanceof ItemHandHeld)) return;

        if (event.getHand().equals(EnumHand.OFF_HAND)) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event)
    {
        motionSimple += motionIncDec;
        if (motionSimple > 1F) motionIncDec = -0.0125F;
        if (motionSimple < -1F) motionIncDec = 0.0125F;
    }

    @SubscribeEvent
    public static void onRenderHeldItem(RenderItemEvent.Held.Pre event)
    {
        // Offhand ONLY instruments render normally. TODO: Simplify
        if(!(event.getEntity() instanceof EntityPlayer && event.getEntity().getHeldItemMainhand().getItem() instanceof ItemHandHeld))
        {
            event.setCanceled(false);
            return;
        }

        ItemStack heldItem;
        ItemStack itemMain = event.getEntity().getHeldItemMainhand();
        ItemStack itemOff = event.getEntity().getHeldItemOffhand();
        boolean isMainHandHeld = !itemMain.isEmpty() && itemMain.getItem() instanceof ItemHandHeld;
        boolean isOffHandHeld = !itemOff.isEmpty() && itemOff.getItem() instanceof ItemHandHeld;
        boolean renderLeft = event.getHandSide().equals(EnumHandSide.LEFT);
        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (event.getEntity().getPrimaryHand() != event.getHandSide())
        {
            if (isMainHandHeld)
            {
                Instrument instrument = ((ItemHandHeld) itemMain.getItem()).getInstrument();
                if (!instrument.general.holdType.canRenderOffhand())
                {
                    event.setCanceled(true);
                    return;
                }
            }
        }

        heldItem = event.getItem();
        if (heldItem.getItem() instanceof ItemHandHeld && event.getHandSide().equals(event.getEntity().getPrimaryHand()))
        {
            event.setCanceled(true);

            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            if (instrument.general.wearable) return;
            instrument.general.holdType.getHeldAnimation().applyHeldItemTransforms(ActionManager.getModelDummy(player), motionSimple, renderLeft);
            // RenderUtil.applyTransformType(heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(event.getEntity(), heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, renderLeft);
        }

        if (heldItem.getItem() instanceof ItemHandHeld && !event.getHandSide().equals(event.getEntity().getPrimaryHand()))
        {
            event.setCanceled(true);

            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            if (instrument.general.wearable) return;
            instrument.general.holdType.getHeldAnimation().applyHeldItemTransforms(ActionManager.getModelDummy(player), motionSimple, renderLeft);
            // RenderUtil.applyTransformType(heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
            Minecraft.getMinecraft().getItemRenderer().renderItemSide(event.getEntity(), heldItem, renderLeft ? ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND : ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, renderLeft);
        }
    }

    @SubscribeEvent
    public static void onSetupAngles(ModelPlayerEvent.SetupAngles.Post event)
    {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack heldItem = player.getHeldItemMainhand();
        if (!heldItem.isEmpty() && (heldItem.getItem() instanceof ItemHandHeld))
        {
            ModelPlayer model = event.getModelPlayer();
            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            instrument.general.holdType.getHeldAnimation().applyPlayerModelRotation(model, ActionManager.getModelDummy(player), motionSimple, player.getPrimaryHand().equals(EnumHandSide.LEFT));
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
        if (!heldItem.isEmpty() && heldItem.getItem() instanceof ItemHandHeld)
        {
            Instrument instrument = ((ItemHandHeld) heldItem.getItem()).getInstrument();
            instrument.general.holdType.getHeldAnimation().applyPlayerPreRender(player, ActionManager.getModelDummy(player), motionSimple, player.getPrimaryHand().equals(EnumHandSide.LEFT));
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
        if (stack.getItem() instanceof ItemHandHeld)
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

    @SubscribeEvent
    public static void EntityLivingInitEvent(EntityLivingInitEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerSP)
        {
            LayerWearableInstrument.addLayer();
        }
    }

    private static void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand)
    {
        model.bipedHead.rotateAngleX += actions.getPartValue(HEAD_POSE_ROT_X) - actions.getPartValue(HEAD_ACTION_ROT_X);
        model.bipedHead.rotateAngleY += actions.getPartValue(HEAD_POSE_ROT_Y) - actions.getPartValue(HEAD_ACTION_ROT_Y);
        model.bipedHead.rotateAngleZ += actions.getPartValue(HEAD_POSE_ROT_Z) - actions.getPartValue(HEAD_ACTION_ROT_Z);

        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_X) - actions.getPartValue(BODY_ACTION_ROT_X);
        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_Y) - actions.getPartValue(BODY_ACTION_ROT_Y);
        model.bipedBody.rotateAngleX += actions.getPartValue(BODY_POSE_ROT_Z) - actions.getPartValue(BODY_ACTION_ROT_Z);

        model.bipedRightArm.rotateAngleX = actions.getPartValue(RIGHT_ARM_POSE_ROT_X) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_X) + aimProgress / 30f;
        model.bipedRightArm.rotateAngleY = actions.getPartValue(RIGHT_ARM_POSE_ROT_Y) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_Y) - aimProgress / 30f;
        model.bipedRightArm.rotateAngleZ = actions.getPartValue(RIGHT_ARM_POSE_ROT_Z) - actions.getPartValue(RIGHT_ARM_ACTION_ROT_Z);

        model.bipedLeftArm.rotateAngleX = actions.getPartValue(LEFT_ARM_POSE_ROT_X) - actions.getPartValue(LEFT_ARM_ACTION_ROT_X) - aimProgress / 30f;
        model.bipedLeftArm.rotateAngleY = actions.getPartValue(LEFT_ARM_POSE_ROT_Y) - actions.getPartValue(LEFT_ARM_ACTION_ROT_Y) + aimProgress / 30f;
        model.bipedLeftArm.rotateAngleZ = actions.getPartValue(LEFT_ARM_POSE_ROT_Z) - actions.getPartValue(LEFT_ARM_ACTION_ROT_Z);

        model.bipedRightLeg.rotateAngleX += actions.getPartValue(RIGHT_LEG_POSE_ROT_X) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_X);
        model.bipedRightLeg.rotateAngleY += actions.getPartValue(RIGHT_LEG_POSE_ROT_Y) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_Y);
        model.bipedRightLeg.rotateAngleZ += actions.getPartValue(RIGHT_LEG_POSE_ROT_Z) - actions.getPartValue(RIGHT_LEG_ACTION_ROT_Z);

        model.bipedLeftLeg.rotateAngleX += actions.getPartValue(LEFT_LEG_POSE_ROT_X) - actions.getPartValue(LEFT_LEG_ACTION_ROT_X);
        model.bipedLeftLeg.rotateAngleY += actions.getPartValue(LEFT_LEG_POSE_ROT_Y) - actions.getPartValue(LEFT_LEG_ACTION_ROT_Y);
        model.bipedLeftLeg.rotateAngleZ += actions.getPartValue(LEFT_LEG_POSE_ROT_Z) - actions.getPartValue(LEFT_LEG_ACTION_ROT_Z);
    }

    private static void applyPlayerPreRender(EntityPlayer player, ModelDummy actions, float aimProgress, boolean leftHand)
    {
        player.prevRenderYawOffset = player.prevRotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
        player.renderYawOffset = player.rotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
    }

    private static void applyHeldItemTransforms(ModelDummy actions, float aimProgress, boolean leftHand)
    {
        GlStateManager.translate(actions.getPartValue(ITEM_TRANS_X), actions.getPartValue(ITEM_TRANS_Y), actions.getPartValue(ITEM_TRANS_Z));
        GlStateManager.rotate(actions.getPartValue(ITEM_ROT_Z), 0, 0, 1);
        GlStateManager.rotate(actions.getPartValue(ITEM_ROT_Y), 0, 1, 0);
        GlStateManager.rotate(actions.getPartValue(ITEM_ROT_X), 1, 0, 0);
    }
}

