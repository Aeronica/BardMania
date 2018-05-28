package net.aeronica.mods.bardmania.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * MelodicToms - Aeronica
 * Created using Tabula 5.1.0
 */
public class ModelMelodicToms extends ModelBase
{
    public ModelRenderer vertical;
    public ModelRenderer hips;
    public ModelRenderer chest;
    public ModelRenderer shoulder_right;
    public ModelRenderer shoulder_right_top;
    public ModelRenderer shoulder_left;
    public ModelRenderer shoulder_left_top;
    public ModelRenderer frame_vert;
    public ModelRenderer shell_right;
    public ModelRenderer shell_right_outer;
    public ModelRenderer shell_right_inner;
    public ModelRenderer shell_left;
    public ModelRenderer shell_left_inner;
    public ModelRenderer shell_left_outer;

    public ModelMelodicToms()
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.shoulder_right = new ModelRenderer(this, 40, 0);
        this.shoulder_right.setRotationPoint(-3.0F, -1.0F, -1.9F);
        this.shoulder_right.addBox(-0.5F, 0.0F, -0.9F, 1, 3, 1, 0.0F);
        this.shell_right = new ModelRenderer(this, 32, 5);
        this.shell_right.setRotationPoint(-10.0F, 11.0F, -7.15F);
        this.shell_right.addBox(-3.0F, 0.0F, -3.0F, 6, 5, 6, 0.0F);
        this.shell_left_outer = new ModelRenderer(this, 0, 16);
        this.shell_left_outer.setRotationPoint(2.5F, 11.0F, -11.05F);
        this.shell_left_outer.addBox(-2.5F, 0.0F, -2.5F, 5, 5, 5, 0.0F);
        this.shoulder_right_top = new ModelRenderer(this, 48, 0);
        this.shoulder_right_top.setRotationPoint(-3.0F, -1.0F, -0.9F);
        this.shoulder_right_top.addBox(-0.5F, 0.0F, -0.9F, 1, 1, 3, 0.0F);
        this.chest = new ModelRenderer(this, 18, 0);
        this.chest.setRotationPoint(0.0F, 2.0F, -1.9F);
        this.chest.addBox(-3.5F, 0.0F, -0.9F, 7, 2, 1, 0.0F);
        this.shell_left = new ModelRenderer(this, 0, 3);
        this.shell_left.setRotationPoint(10.5F, 11.0F, -7.15F);
        this.shell_left.addBox(-4.0F, 0.0F, -4.0F, 8, 5, 8, 0.0F);
        this.frame_vert = new ModelRenderer(this, 16, 29);
        this.frame_vert.setRotationPoint(0.0F, 10.0F, -1.9F);
        this.frame_vert.addBox(-0.5F, 0.0F, -0.9F, 1, 4, 1, 0.0F);
        this.shell_right_inner = new ModelRenderer(this, 48, 17);
        this.shell_right_inner.setRotationPoint(-3.0F, 11.0F, -5.9F);
        this.shell_right_inner.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.shell_right_outer = new ModelRenderer(this, 32, 16);
        this.shell_right_outer.setRotationPoint(-3.5F, 11.0F, -11.05F);
        this.shell_right_outer.addBox(-2.0F, 0.0F, -2.0F, 4, 5, 4, 0.0F);
        this.shoulder_left = new ModelRenderer(this, 44, 0);
        this.shoulder_left.setRotationPoint(3.0F, -1.0F, -1.9F);
        this.shoulder_left.addBox(-0.5F, 0.0F, -0.9F, 1, 3, 1, 0.0F);
        this.shell_left_inner = new ModelRenderer(this, 0, 26);
        this.shell_left_inner.setRotationPoint(2.0F, 11.0F, -5.9F);
        this.shell_left_inner.addBox(-1.5F, 0.0F, -1.5F, 3, 5, 3, 0.0F);
        this.hips = new ModelRenderer(this, 0, 0);
        this.hips.setRotationPoint(0.0F, 8.0F, -1.9F);
        this.hips.addBox(-4.0F, 0.0F, -0.9F, 8, 2, 1, 0.0F);
        this.vertical = new ModelRenderer(this, 34, 0);
        this.vertical.setRotationPoint(0.0F, 4.0F, -1.9F);
        this.vertical.addBox(-1.0F, 0.0F, -0.9F, 2, 4, 1, 0.0F);
        this.shoulder_left_top = new ModelRenderer(this, 56, 0);
        this.shoulder_left_top.setRotationPoint(3.0F, -1.0F, -0.9F);
        this.shoulder_left_top.addBox(-0.5F, 0.0F, -0.9F, 1, 1, 3, 0.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        this.shoulder_right.render(f5);
        this.shell_right.render(f5);
        this.shell_left_outer.render(f5);
        this.shoulder_right_top.render(f5);
        this.chest.render(f5);
        this.shell_left.render(f5);
        this.frame_vert.render(f5);
        this.shell_right_inner.render(f5);
        this.shell_right_outer.render(f5);
        this.shoulder_left.render(f5);
        this.shell_left_inner.render(f5);
        this.hips.render(f5);
        this.vertical.render(f5);
        this.shoulder_left_top.render(f5);
    }

    /**
     * This is a helper function from Tabula to set the rotation of model parts
     */
    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z)
    {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }
}
