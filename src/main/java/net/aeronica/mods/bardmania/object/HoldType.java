package net.aeronica.mods.bardmania.object;

import com.google.gson.annotations.SerializedName;
import net.aeronica.mods.bardmania.client.HeldAnimation;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        public void applyPlayerModelRotation(ModelPlayer model, float aimProgress)
        {
            model.bipedRightArm.rotateAngleX = (float) Math.toRadians(-80F);
            model.bipedRightArm.rotateAngleY = (float) Math.toRadians(25F);
            model.bipedRightArm.rotateAngleZ = (float) Math.toRadians(0F);

            model.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-80F);
            model.bipedLeftArm.rotateAngleY = (float) Math.toRadians(45F);
            model.bipedLeftArm.rotateAngleZ = (float) Math.toRadians(0F);

            model.bipedLeftLeg.rotateAngleX = (float) Math.toRadians(-25F + aimProgress * 25F);
        }

        @Override
        public void applyPlayerPreRender(EntityPlayer player, float aimProgress)
        {
            player.prevRenderYawOffset = player.prevRotationYaw + 40 * aimProgress;
            player.renderYawOffset = player.rotationYaw + 40 * aimProgress;
        }

        @Override
        public void applyHeldItemTransforms(float aimProgress)
        {
            GlStateManager.translate(-0.33, 0.05, 0.1);
            float invertRealProgress = 1.0F - aimProgress;
            GlStateManager.rotate(0F * invertRealProgress, 0, 0, 1);
            GlStateManager.rotate(50F * invertRealProgress + aimProgress * -5F, 0, 1, 0);
            GlStateManager.rotate(0F * invertRealProgress + aimProgress, 1, 0, 0);
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
