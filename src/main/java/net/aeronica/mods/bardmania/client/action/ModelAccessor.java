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
    private static final int ROT_X = 0;
    private static final int ROT_Y = 1;
    private static final int ROT_Z = 2;
    private static final int OFF_X = 3;
    private static final int OFF_Y = 4;
    private static final int OFF_Z = 5;

    private static final int MODEL_PROPS = 6;
    private static final int BODY_PARTS = 6;
    static final int STATE_COUNT = MODEL_PROPS * BODY_PARTS;

    private static final int _HEAD = 0 * MODEL_PROPS;
    private static final int _BODY = 1 * MODEL_PROPS;
    private static final int _RIGHT_ARM = 2 * MODEL_PROPS;
    private static final int _LEFT_ARM = 3 * MODEL_PROPS;
    private static final int _RIGHT_LEG = 4 * MODEL_PROPS;
    private static final int _LEFT_LEG = 5 * MODEL_PROPS;

    public void ModelAccesor() {/* NOP */}

    public enum Part {
        HEAD_ROT_X(_HEAD + ROT_X, "head_rot_x"),
        HEAD_ROT_Y(_HEAD + ROT_Y, "head_rot_y"),
        HEAD_ROT_Z(_HEAD + ROT_Z, "head_rot_z"),
        BODY_ROT_X(_BODY + ROT_X, "body_rot_x"),
        BODY_ROT_Y(_BODY + ROT_Y, "body_rot_y"),
        BODY_ROT_Z(_BODY + ROT_Z, "body_rot_z"),
        RIGHT_ARM_ROT_X(_RIGHT_ARM + ROT_X, "right_arm_rot_x"),
        RIGHT_ARM_ROT_Y(_RIGHT_ARM + ROT_Y, "right_arm_rot_y"),
        RIGHT_ARM_ROT_Z(_RIGHT_ARM + ROT_Z, "right_arm_rot_z"),
        LEFT_ARM_ROT_X(_LEFT_ARM + ROT_X, "left_arm_rot_x"),
        LEFT_ARM_ROT_Y(_LEFT_ARM + ROT_Y, "left_arm_rot_y"),
        LEFT_ARM_ROT_Z(_LEFT_ARM + ROT_Z, "left_arm_rot_z"),
        RIGHT_LEG_ROT_X(_RIGHT_LEG + ROT_X, "right_leg_rot_x"),
        RIGHT_LEG_ROT_Y(_RIGHT_LEG + ROT_Y, "right_leg_rot_y"),
        RIGHT_LEG_ROT_Z(_RIGHT_LEG + ROT_Z, "right_leg_rot_z"),
        LEFT_LEG_ROT_X(_LEFT_LEG + ROT_X, "left_leg_rot_x"),
        LEFT_LEG_ROT_Y(_LEFT_LEG + ROT_Y, "left_leg_rot_y"),
        LEFT_LEG_ROT_Z(_LEFT_LEG + ROT_Z, "left_leg_rot_z"),

        HEAD_OFF_X(_HEAD + OFF_X, "head_off_x"),
        HEAD_OFF_Y(_HEAD + OFF_Y, "head_off_y"),
        HEAD_OFF_Z(_HEAD + OFF_Z, "head_off_z"),
        BODY_OFF_X(_BODY + OFF_X, "body_off_x"),
        BODY_OFF_Y(_BODY + OFF_Y, "body_off_y"),
        BODY_OFF_Z(_BODY + OFF_Z, "body_off_z"),
        RIGHT_ARM_OFF_X(_RIGHT_ARM + OFF_X, "right_arm_off_x"),
        RIGHT_ARM_OFF_Y(_RIGHT_ARM + OFF_Y, "right_arm_off_y"),
        RIGHT_ARM_OFF_Z(_RIGHT_ARM + OFF_Z, "right_arm_off_z"),
        LEFT_ARM_OFF_X(_LEFT_ARM + OFF_X, "left_arm_off_x"),
        LEFT_ARM_OFF_Y(_LEFT_ARM + OFF_Y, "left_arm_off_y"),
        LEFT_ARM_OFF_Z(_LEFT_ARM + OFF_Z, "left_arm_off_z"),
        RIGHT_LEG_OFF_X(_RIGHT_LEG + OFF_X, "right_leg_off_x"),
        RIGHT_LEG_OFF_Y(_RIGHT_LEG + OFF_Y, "right_leg_off_y"),
        RIGHT_LEG_OFF_Z(_RIGHT_LEG + OFF_Z, "right_leg_off_z"),
        LEFT_LEG_OFF_X(_LEFT_LEG + OFF_X, "left_leg_off_x"),
        LEFT_LEG_OFF_Y(_LEFT_LEG + OFF_Y, "left_leg_off_y"),
        LEFT_LEG_OFF_Z(_LEFT_LEG + OFF_Z, "left_leg_off_z"),;

        private final int tweenType;
        private final String partName;
        private static final Part[] MODEL_ACCESSORS = new Part[values().length];

        static {
            for (Part value : values()) {
                MODEL_ACCESSORS[value.tweenType] = value;
            }
        }

        public static Part byTweenType(int tweenType) {
            int tweenTypeLocal = tweenType;
            if (tweenTypeLocal < 0 || tweenTypeLocal >= MODEL_ACCESSORS.length) {
                tweenTypeLocal = 0;
            }
            return MODEL_ACCESSORS[tweenTypeLocal];
        }

        public String getPartName() {
            return this.partName;
        }

        public int getTweenType() {
            return this.tweenType;
        }

        Part(int tweenType, String partName) {
            this.tweenType = tweenType;
            this.partName = partName;
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }

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
