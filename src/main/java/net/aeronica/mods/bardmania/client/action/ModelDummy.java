package net.aeronica.mods.bardmania.client.action;

@SuppressWarnings("unused")
public class ModelDummy
{
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
    };

    public void reset() {for(int i = 0 ;i < parts.length;) parts[i++] = 0f;}

    public float getPartValue(int part) {return parts[part];}

    public void setPartValue(int part, float value) {parts[part] = value;}
}
