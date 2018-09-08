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

package net.aeronica.mods.bard_mania.client.actions.base;

import net.minecraft.item.ItemStack;

@SuppressWarnings("unused")
public class ModelDummy
{
    // Timer to keep entities dancing between notes.
    private static final int PLAY_TICKS = 15; // 3/4 second
    private int playTimer = 0;
    // Current active tween count
    private int tweenCount = 0;
    private ItemStack currentInstrument = ItemStack.EMPTY;

    public void ModelDummy() {/* NOP */}

    public float[] parts = new float[]{
            0f, 0f, 0f, // head_action_rot_x, head_action_rot_y, head_action_rot_z
            0f, 0f, 0f, // body_action_rot_x, body_action_rot_y, body_action_rot_z
            0f, 0f, 0f, // right_arm_action_rot_x, right_arm_action_rot_y, right_arm_action_rot_z
            0f, 0f, 0f, // left_arm_action_rot_x, left_arm_action_rot_y, left_arm_action_rot_z
            0f, 0f, 0f, // right_leg_action_rot_x, right_leg_action_rot_y, right_leg_action_rot_z
            0f, 0f, 0f, // left_leg_action_rot_x, left_leg_action_rot_y, left_leg_action_rot_z

            0f, 0f, 0f, // head_pose_rot_x, head_pose_rot_y, head_pose_rot_z
            0f, 0f, 0f, // body_pose_rot_x, body_pose_rot_y, body_pose_rot_z
            0f, 0f, 0f, // right_arm_pose_rot_x, right_arm_pose_rot_y, right_arm_pose_rot_z
            0f, 0f, 0f, // left_arm_pose_rot_x, left_arm_pose_rot_y, left_arm_pose_rot_z
            0f, 0f, 0f, // right_leg_pose_rot_x, right_leg_pose_rot_y, right_leg_pose_rot_z
            0f, 0f, 0f, // left_leg_pose_rot_x, left_leg_pose_rot_y, left_leg_pose_rot_z
            0f, // player_pose_rotation_yaw
            0f, 0f, 0f, // right_hand_item_trans_x, right_hand_item_trans_y, right_hand_item_trans_z
            0f, 0f, 0f, // right_hand_item_rot_x, right_hand_item_rot_y, right_hand_item_rot_z
            0f, 0f, 0f, // left_hand_item_trans_x, left_hand_item_trans_y, left_hand_item_trans_z
            0f, 0f, 0f, // left_hand_item_rot_x, left_hand_item_rot_y, left_hand_item_rot_z
            0f, 0f, // worn_item_scale, apply
    };

    public void reset()
    {
        for(int i = 0 ;i < parts.length;) parts[i++] = 0f;
        tweenCount = 0;
        playTimer = 0;
        currentInstrument = ItemStack.EMPTY;
    }

    /**
     * Adds an active tween
     */
    public void tweenStart() { tweenCount++; }

    /**
     * Removes an expired tween
     */
    public void tweenStop() { if (tweenCount > 0) tweenCount--; }

    public int getTweenCount() { return tweenCount; }

    public boolean hasTween() { return tweenCount > 0; }

    /**
     * Start / Continue Dancing timer
     */
    public void resetPlayTimer() { playTimer = PLAY_TICKS; }

    /**
     * Decrement the Dancing timer
     */
    public void decrementPlayTimer() { if (playTimer > 0) playTimer--;  }

    /** canDance, isPlaying
     * @return true if playTimer > 0
     */
    public boolean hasPlayTicks() { return playTimer > 0; }

    public void setInstrumentStack(ItemStack itemStack) { currentInstrument = itemStack; }

    public ItemStack getInstrumentStack() { return currentInstrument; }

    public float getPartValue(int part) { return parts[part]; }

    public void setPartValue(int part, float value) { parts[part] = value; }
}
