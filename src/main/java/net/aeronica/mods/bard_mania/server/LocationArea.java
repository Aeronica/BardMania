package net.aeronica.mods.bard_mania.server;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/*
 * By thebrightspark from the mod StructuralRelocation
 * https://github.com/thebrightspark/StructuralRelocation
 * 
 *                  GNU GENERAL PUBLIC LICENSE
 *                     Version 2, June 1991
 *
 * Copyright (C) 1989, 1991 Free Software Foundation, Inc., <http://fsf.org/>
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * Everyone is permitted to copy and distribute verbatim copies
 * of this license document, but changing it is not allowed.
 * 
 * ---------------------------------------------------------------------------
 * 
 * 2017-May-20 Aeronica
 * Removed dimension and NBT serialization
 */
public class LocationArea
{

    public BlockPos pos1, pos2;

    public LocationArea(BlockPos position1, BlockPos position2)
    {
        this.pos1 = new BlockPos(position1);
        this.pos2 = new BlockPos(position2);
    }

    /**
     * Gets the position where the X, Y and Z are at their smallest within the area.
     */
    public BlockPos getStartingPoint()
    {
        int x = Math.min(pos1.getX(), pos2.getX());
        int y = Math.min(pos1.getY(), pos2.getY());
        int z = Math.min(pos1.getZ(), pos2.getZ());
        return new BlockPos(x, y, z);
    }

    /**
     * Gets the position where the X, Y and Z are at their largest within the area.
     */
    public BlockPos getEndPoint()
    {
        int x = Math.max(pos1.getX(), pos2.getX());
        int y = Math.max(pos1.getY(), pos2.getY());
        int z = Math.max(pos1.getZ(), pos2.getZ());
        return new BlockPos(x, y, z);
    }

    /**
     * Gets the a position which represents the size of the area.
     * It's basically the end point minus the start point.
     */
    public BlockPos getRelativeEndPoint()
    {
        return getEndPoint().subtract(getStartingPoint());
    }

    /**
     * Gets the position which represents the farthest block
     * from the starting point plus the size of a block.
     * Useful for rendering an area bounding box.
     */
    public BlockPos getStartPointPlusSize()
    {
        return getStartingPoint().add(1+getSize(EnumFacing.Axis.X), 1+getSize(EnumFacing.Axis.Y), 1+getSize(EnumFacing.Axis.Z));
    }
    
    /**
     * Get the length of the area along the axis given.
     */
    public int getSize(EnumFacing.Axis axis)
    {
        BlockPos minPos = getStartingPoint();
        BlockPos maxPos = getEndPoint();
        switch(axis)
        {
            case X:
                return maxPos.getX() - minPos.getX();
            case Y:
                return maxPos.getY() - minPos.getY();
            case Z:
                return maxPos.getZ() - minPos.getZ();
            default:
                ModLogger.error("Unhandled axis!?");
                return -1;
        }
    }
    
    public String getSizeString()
    {
        return getSize(EnumFacing.Axis.X) + " x " + getSize(EnumFacing.Axis.Y) + " x " + getSize(EnumFacing.Axis.Z);
    }

    public boolean isEqual(LocationArea area)
    {
        return area != null && area.pos1.equals(pos1) && area.pos2.equals(pos2);
    }

}
