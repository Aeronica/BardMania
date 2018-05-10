package net.aeronica.mods.bardmania.object;

import com.google.gson.annotations.SerializedName;
import com.sun.org.apache.bcel.internal.generic.SWAP;
import net.aeronica.mods.bardmania.client.HeldAnimation;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.util.vector.Quaternion;

public enum HoldType
{
    @SerializedName("one_handed")
    ONE_HANDED(new HeldAnimation()
    {


    }, true),
    @SerializedName("two_handed_horizontal")
    TWO_HANDED_HORIZONTAL(new HeldAnimation()
    {
        @Override
        public void applyPlayerModelRotation(ModelPlayer model, float aimProgress, boolean leftHand)
        {
            model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-80F + (leftHand ? 180F : 0F));
            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(25F + (leftHand ? 180F : 0F));
            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0F + (leftHand ? 180F : 0F));

            model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-80F + (leftHand ? 180F : 0F));
            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(55F + (leftHand ? 180F : 0F));
            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0F + (leftHand ? 180F : 0F));

            if (leftHand)
            {
                float temp =  model.bipedRightArm.rotateAngleY;
                model.bipedRightArm.rotateAngleY = model.bipedLeftArm.rotateAngleY;
                model.bipedLeftArm.rotateAngleY = temp;
            }
            // model.bipedLeftLeg.rotateAngleX = (float) Math.toRadians(-25F + aimProgress * 25F);
        }

        @Override
        public void applyPlayerPreRender(EntityPlayer player, float aimProgress, boolean leftHand)
        {
            player.prevRenderYawOffset = player.prevRotationYaw + 40 * (leftHand ? -1 : 1);
            player.renderYawOffset = player.rotationYaw + 40 * (leftHand ? -1 : 1);
        }

        @Override
        public void applyHeldItemTransforms(float aimProgress, boolean leftHand)
        {
            GlStateManager.translate(0, 0, 0);
            float invertRealProgress = 1.0F - aimProgress;
            GlStateManager.rotate(0F, 0, 0, 1);
            GlStateManager.rotate(40F * (leftHand ? -1F : 1F), 0, 1, 0);
            GlStateManager.rotate(0F, 1, 0, 0);
        }
    }, false),
    @SerializedName("two_handed_vertical")
    TWO_HANDED_VERTICAL(new HeldAnimation()
    {


    }, false),
    @SerializedName("two_handed_guitar")
    TWO_HANDED_GUITAR(new HeldAnimation()
    {


    }, false),
    @SerializedName("two_handed_drum")
    TWO_HANDED_DRUM(new HeldAnimation()
    {


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
