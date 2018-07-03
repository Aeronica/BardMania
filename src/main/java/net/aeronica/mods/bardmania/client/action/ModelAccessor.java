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


public class ModelAccessor implements TweenAccessor<ModelDummy> {

    public static final int HEAD_ACTION_ROT_X = 0;
    public static final int HEAD_ACTION_ROT_Y = 1;
    public static final int HEAD_ACTION_ROT_Z = 2;
    public static final int BODY_ACTION_ROT_X = 3;
    public static final int BODY_ACTION_ROT_Y = 4;
    public static final int BODY_ACTION_ROT_Z = 5;
    public static final int RIGHT_ARM_ACTION_ROT_X = 6;
    public static final int RIGHT_ARM_ACTION_ROT_Y = 7;
    public static final int RIGHT_ARM_ACTION_ROT_Z = 8;
    public static final int LEFT_ARM_ACTION_ROT_X = 9;
    public static final int LEFT_ARM_ACTION_ROT_Y = 10;
    public static final int LEFT_ARM_ACTION_ROT_Z = 11;
    public static final int RIGHT_LEG_ACTION_ROT_X = 12;
    public static final int RIGHT_LEG_ACTION_ROT_Y = 13;
    public static final int RIGHT_LEG_ACTION_ROT_Z = 14;
    public static final int LEFT_LEG_ACTION_ROT_X = 15;
    public static final int LEFT_LEG_ACTION_ROT_Y = 16;
    public static final int LEFT_LEG_ACTION_ROT_Z = 17;

    public static final int HEAD_POSE_ROT_X = 18;
    public static final int HEAD_POSE_ROT_Y = 19;
    public static final int HEAD_POSE_ROT_Z = 20;
    public static final int BODY_POSE_ROT_X = 21;
    public static final int BODY_POSE_ROT_Y = 22;
    public static final int BODY_POSE_ROT_Z = 23;
    public static final int RIGHT_ARM_POSE_ROT_X = 24;
    public static final int RIGHT_ARM_POSE_ROT_Y = 25;
    public static final int RIGHT_ARM_POSE_ROT_Z = 26;
    public static final int LEFT_ARM_POSE_ROT_X = 27;
    public static final int LEFT_ARM_POSE_ROT_Y = 28;
    public static final int LEFT_ARM_POSE_ROT_Z = 29;
    public static final int RIGHT_LEG_POSE_ROT_X = 30;
    public static final int RIGHT_LEG_POSE_ROT_Y = 31;
    public static final int RIGHT_LEG_POSE_ROT_Z = 32;
    public static final int LEFT_LEG_POSE_ROT_X = 33;
    public static final int LEFT_LEG_POSE_ROT_Y = 34;
    public static final int LEFT_LEG_POSE_ROT_Z = 35;

    public static final int PLAYER_POSE_ROTATION_YAW = 36;


    public void ModelAccesor() {/* NOP */}

    @Override
    public int getValues(ModelDummy target, int tweenType, float[] returnValues) {
        returnValues[0] = target.parts[tweenType];
        return 1;
    }

    @Override
    public void setValues(ModelDummy target, int tweenType, float[] newValues) {
        target.parts[tweenType] = newValues[0];
    }
}
