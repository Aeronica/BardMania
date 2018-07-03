package net.aeronica.mods.bardmania.object;

import com.google.gson.annotations.SerializedName;
import net.aeronica.mods.bardmania.client.HeldAnimation;
import net.aeronica.mods.bardmania.client.action.ModelDummy;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.aeronica.mods.bardmania.client.action.ModelAccessor.*;

public enum HoldType
{
    @SerializedName("one_handed")
    ONE_HANDED(new HeldAnimation()
    {

    }, false),
    @SerializedName("two_handed_horizontal")
    TWO_HANDED_HORIZONTAL(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand)
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

        @Override
        public void applyPlayerPreRender(EntityPlayer player, ModelDummy actions, float aimProgress, boolean leftHand)
        {
            player.prevRenderYawOffset = player.prevRotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
            player.renderYawOffset = player.rotationYaw + actions.getPartValue(PLAYER_POSE_ROTATION_YAW);
        }

        @Override
        public void applyHeldItemTransforms(ModelDummy actions, float aimProgress, boolean leftHand)
        {
            GlStateManager.translate(actions.getPartValue(ITEM_TRANS_X), actions.getPartValue(ITEM_TRANS_Y), actions.getPartValue(ITEM_TRANS_Z));
            GlStateManager.rotate(actions.getPartValue(ITEM_ROT_Z), 0, 0, 1);
            GlStateManager.rotate(actions.getPartValue(ITEM_ROT_Y), 0, 1, 0);
            GlStateManager.rotate(actions.getPartValue(ITEM_ROT_X), 1, 0, 0);
        }
    }, false),
    @SerializedName("two_handed_vertical")
    TWO_HANDED_VERTICAL(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand)
        {
            super.applyPlayerModelRotation(model, actions, aimProgress, leftHand);
        }

        @Override
        public void applyHeldItemTransforms(ModelDummy actions, float aimProgress, boolean leftHand)
        {
            super.applyHeldItemTransforms(actions, aimProgress, leftHand);
        }
    }, false),
    @SerializedName("two_handed_guitar")
    TWO_HANDED_GUITAR(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand) {
            model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-20F);
            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(10F);

            model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-45F) + actions.getPartValue(LEFT_ARM_ACTION_ROT_X);
            model.bipedLeftArm.rotateAngleY  =  (float) Math.toRadians(-30F) + actions.getPartValue(LEFT_ARM_ACTION_ROT_Y);
        }
    }, false),
    @SerializedName("two_handed_drum")
    TWO_HANDED_DRUM(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(ModelPlayer model, ModelDummy actions, float aimProgress, boolean leftHand)
        {
            final float range = 30F;
            final float halfRange = range / 2F;
            model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-60F - (aimProgress * range) + halfRange);
            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(-8F);
            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0F);

            model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-60F + (aimProgress * range) - halfRange);
            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(8F);
            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0F);
        }
    }, false);

    private final HeldAnimation heldAnimation;
    private final boolean renderOffhand;

    HoldType(HeldAnimation heldAnimation, boolean renderOffhand)
    {
        this.heldAnimation = heldAnimation;
        this.renderOffhand = renderOffhand;
    }

    public HeldAnimation getHeldAnimation()
    {
        return heldAnimation;
    }

    public boolean canRenderOffhand()
    {
        return renderOffhand;
    }

    @SideOnly(Side.CLIENT)
    private static void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
    }
}
