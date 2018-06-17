package net.aeronica.mods.bardmania.client.action;

@SuppressWarnings("unused")
public class ModelDummy
{
    public void ModelDummy() {/* NOP */}

    public float[] parts = new float[]{
            0f, 0f, 0f, // head_rot_x, head_rot_y, head_rot_z
            0f, 0f, 0f, // body_rot_x, body_rot_y, body_rot_z
            0f, 0f, 0f, // right_arm_rot_x, right_arm_rot_y, right_arm_rot_z
            0f, 0f, 0f, // left_arm_rot_x, left_arm_rot_y, left_arm_rot_z
            0f, 0f, 0f, // right_leg_rot_x, right_leg_rot_y, right_leg_rot_z
            0f, 0f, 0f, // left_leg_rot_x, left_leg_rot_y, left_leg_rot_z

            0f, 0f, 0f, // head_off_x, head_off_y, head_off_z
            0f, 0f, 0f, // body_off_x, body_off_y, body_off_z
            0f, 0f, 0f, // right_arm_off_x, right_arm_off_y, right_arm_off_z
            0f, 0f, 0f, // left_arm_off_x, left_arm_off_y, left_arm_off_z
            0f, 0f, 0f, // right_leg_off_x, right_leg_off_y, right_leg_off_z
            0f, 0f, 0f, // left_leg_off_x, left_leg_off_y, left_leg_off_z
    };

    public void reset() {for(int i = 0 ;i < parts.length;) parts[i++] = 0f;}

    public float getPartValue(ModelAccessor.Part part) {return parts[part.getTweenType()];}

    public void setPartValue(ModelAccessor.Part part, float value) {parts[part.getTweenType()] = value;}
}
