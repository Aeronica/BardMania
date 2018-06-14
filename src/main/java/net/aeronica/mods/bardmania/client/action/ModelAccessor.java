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

package net.aeronica.mods.bardmania.client.action;

import net.aeronica.dorkbox.tweenEngine.TweenAccessor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;

/**
 * Major portions of this class/enum was created by <Vazkii>.
 * It's distributed as part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * <p>
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * <p>
 * File Created @ [26/03/2016, 21:37:50 (GMT)]
 * <p>
 * Modified by Paul Boese a.k.a Aeronica
 */
class Part
{
    static final int ROT_X = 0;
    static final int ROT_Y = 1;
    static final int ROT_Z = 2;
    static final int OFF_X = 3;
    static final int OFF_Y = 4;
    static final int OFF_Z = 5;

    static final int MODEL_PROPS = 6;
    private static final int BODY_PARTS = 6;
    static final int STATE_COUNT = MODEL_PROPS * BODY_PARTS;

    static final int HEAD = 0 * MODEL_PROPS;
    static final int BODY = 1 * MODEL_PROPS;
    static final int RIGHT_ARM = 2 * MODEL_PROPS;
    static final int LEFT_ARM = 3 * MODEL_PROPS;
    static final int RIGHT_LEG = 4 * MODEL_PROPS;
    static final int LEFT_LEG = 5 * MODEL_PROPS;
}

public enum ModelAccessor implements TweenAccessor<ModelBiped>
{
    HEAD(Part.HEAD, "head"),
    BODY(Part.BODY, "body"),
    RIGHT_ARM(Part.RIGHT_ARM, "right_arm"),
    LEFT_ARM(Part.LEFT_ARM, "left_arm"),
    RIGHT_LEG(Part.RIGHT_LEG, "right_leg"),
    LEFT_LEG(Part.LEFT_LEG, "left_leg"),
    HEAD_ROT_X(Part.HEAD + Part.ROT_X, "head_rot_x"),
    HEAD_ROT_Y(Part.HEAD + Part.ROT_Y, "head_rot_y"),
    HEAD_ROT_Z(Part.HEAD + Part.ROT_Z, "head_rot_z"),
    BODY_ROT_X(Part.BODY + Part.ROT_X, "body_rot_x"),
    BODY_ROT_Y(Part.BODY + Part.ROT_Y, "body_rot_y"),
    BODY_ROT_Z(Part.BODY + Part.ROT_Z, "body_rot_z"),
    RIGHT_ARM_ROT_X(Part.RIGHT_ARM + Part.ROT_X, "right_arm_rot_x"),
    RIGHT_ARM_ROT_Y(Part.RIGHT_ARM + Part.ROT_Y, "right_arm_rot_y"),
    RIGHT_ARM_ROT_Z(Part.RIGHT_ARM + Part.ROT_Z, "right_arm_rot_z"),
    LEFT_ARM_ROT_X(Part.LEFT_ARM + Part.ROT_X, "left_arm_rot_x"),
    LEFT_ARM_ROT_Y(Part.LEFT_ARM + Part.ROT_Y, "left_arm_rot_y"),
    LEFT_ARM_ROT_Z(Part.LEFT_ARM + Part.ROT_Z, "left_arm_rot_z"),
    RIGHT_LEG_ROT_X(Part.RIGHT_LEG + Part.ROT_X, "right_leg_rot_x"),
    RIGHT_LEG_ROT_Y(Part.RIGHT_LEG + Part.ROT_Y, "right_leg_rot_y"),
    RIGHT_LEG_ROT_Z(Part.RIGHT_LEG + Part.ROT_Z, "right_leg_rot_z"),
    LEFT_LEG_ROT_X(Part.LEFT_LEG + Part.ROT_X, "left_leg_rot_x"),
    LEFT_LEG_ROT_Y(Part.LEFT_LEG + Part.ROT_Y, "left_leg_rot_y"),
    LEFT_LEG_ROT_Z(Part.LEFT_LEG + Part.ROT_Z, "left_leg_rot_z"),

    HEAD_OFF_X(Part.HEAD + Part.OFF_X, "head_off_x"),
    HEAD_OFF_Y(Part.HEAD + Part.OFF_Y, "head_off_y"),
    HEAD_OFF_Z(Part.HEAD + Part.OFF_Z, "head_off_z"),
    BODY_OFF_X(Part.BODY + Part.OFF_X, "body_off_x"),
    BODY_OFF_Y(Part.BODY + Part.OFF_Y, "body_off_y"),
    BODY_OFF_Z(Part.BODY + Part.OFF_Z, "body_off_z"),
    RIGHT_ARM_OFF_X(Part.RIGHT_ARM + Part.OFF_X, "right_arm_off_x"),
    RIGHT_ARM_OFF_Y(Part.RIGHT_ARM + Part.OFF_Y, "right_arm_off_y"),
    RIGHT_ARM_OFF_Z(Part.RIGHT_ARM + Part.OFF_Z, "right_arm_off_z"),
    LEFT_ARM_OFF_X(Part.LEFT_ARM + Part.OFF_X, "left_arm_off_x"),
    LEFT_ARM_OFF_Y(Part.LEFT_ARM + Part.OFF_Y, "left_arm_off_y"),
    LEFT_ARM_OFF_Z(Part.LEFT_ARM + Part.OFF_Z, "left_arm_off_z"),
    RIGHT_LEG_OFF_X(Part.RIGHT_LEG + Part.OFF_X, "right_leg_off_x"),
    RIGHT_LEG_OFF_Y(Part.RIGHT_LEG + Part.OFF_Y, "right_leg_off_y"),
    RIGHT_LEG_OFF_Z(Part.RIGHT_LEG + Part.OFF_Z, "right_leg_off_z"),
    LEFT_LEG_OFF_X(Part.LEFT_LEG + Part.OFF_X, "left_leg_off_x"),
    LEFT_LEG_OFF_Y(Part.LEFT_LEG + Part.OFF_Y, "left_leg_off_y"),
    LEFT_LEG_OFF_Z(Part.LEFT_LEG + Part.OFF_Z, "left_leg_off_z"),;

    private final int tweenType;
    private final String partName;
    private static final ModelAccessor[] MODEL_ACCESSORS = new ModelAccessor[values().length];

    static
    {
        for (ModelAccessor value : values())
        {
            MODEL_ACCESSORS[value.tweenType] = value;
        }
    }

    public static ModelAccessor byTweenType(int tweenType)
    {
        int tweenTypeLocal = tweenType;
        if (tweenTypeLocal < 0 || tweenTypeLocal >= MODEL_ACCESSORS.length)
        {
            tweenTypeLocal = 0;
        }
        return MODEL_ACCESSORS[tweenTypeLocal];
    }

    public String getPartName()
    {
        return this.partName;
    }

    ModelAccessor(int tweenType, String partName)
    {
        this.tweenType = tweenType;
        this.partName = partName;
    }

    @Override
    public int getValues(ModelBiped target, int tweenType, float[] returnValues)
    {
        int axis = tweenType % Part.MODEL_PROPS;
        int bodyPart = tweenType / Part.MODEL_PROPS * Part.MODEL_PROPS;
        ModelRenderer model = getBodyPart(target, bodyPart);
        if (model == null)
            return 0;

        switch (axis)
        {
            case Part.ROT_X:
                returnValues[0] = model.rotateAngleX;
                break;
            case Part.ROT_Y:
                returnValues[0] = model.rotateAngleY;
                break;
            case Part.ROT_Z:
                returnValues[0] = model.rotateAngleZ;
                break;
            case Part.OFF_X:
                returnValues[0] = model.offsetX;
                break;
            case Part.OFF_Y:
                returnValues[0] = model.offsetY;
                break;
            case Part.OFF_Z:
                returnValues[0] = model.offsetZ;
                break;
        }

        return 1;
    }

    private static ModelRenderer getBodyPart(ModelBiped model, int part)
    {
        switch (byTweenType(part))
        {
            case HEAD:
                return model.bipedHead;
            case BODY:
                return model.bipedBody;
            case RIGHT_ARM:
                return model.bipedRightArm;
            case LEFT_ARM:
                return model.bipedLeftArm;
            case RIGHT_LEG:
                return model.bipedRightLeg;
            case LEFT_LEG:
                return model.bipedLeftLeg;
        }
        return null;
    }

    @Override
    public void setValues(ModelBiped target, int tweenType, float[] newValues)
    {
        int axis = tweenType % Part.MODEL_PROPS;
        int bodyPart = tweenType / Part.MODEL_PROPS * Part.MODEL_PROPS;
        ModelRenderer model = getBodyPart(target, bodyPart);
        messWithModel(target, model, axis, newValues[0]);
    }

    private void messWithModel(ModelBiped biped, ModelRenderer part, int axis, float val)
    {
        setPartAxis(part, axis, val);

        if (part == biped.bipedHead)
            messWithModel(biped, biped.bipedHeadwear, axis, val);

        else if (biped instanceof ModelPlayer)
            messWithPlayerModel((ModelPlayer) biped, part, axis, val);
    }

    private void messWithPlayerModel(ModelPlayer biped, ModelRenderer part, int axis, float val)
    {
        ModelRenderer newPart = null;

        if (part == biped.bipedLeftArm)
            newPart = biped.bipedLeftArmwear;
        else if (part == biped.bipedRightArm)
            newPart = biped.bipedRightArmwear;
        else if (part == biped.bipedLeftLeg)
            newPart = biped.bipedLeftLegwear;
        else if (part == biped.bipedRightLeg)
            newPart = biped.bipedRightLegwear;
        else if (part == biped.bipedBody)
            newPart = biped.bipedBodyWear;

        setPartAxis(newPart, axis, val);
    }

    private void setPartAxis(ModelRenderer part, int axis, float val)
    {
        if (part == null)
            return;

        switch (axis)
        {
            case Part.ROT_X:
                part.rotateAngleX = val;
                break;
            case Part.ROT_Y:
                part.rotateAngleY = val;
                break;
            case Part.ROT_Z:
                part.rotateAngleZ = val;
                break;
            case Part.OFF_X:
                part.offsetX = val;
                break;
            case Part.OFF_Y:
                part.offsetY = val;
                break;
            case Part.OFF_Z:
                part.offsetZ = val;
                break;
        }
    }

    @Override
    public String toString()
    {
        return super.toString();
    }
}
