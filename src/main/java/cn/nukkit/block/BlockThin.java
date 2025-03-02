package cn.nukkit.block;

import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.utils.LevelException;

import static cn.nukkit.math.VectorMath.calculateFace;

/**
 * @author xtypr
 * @since 2015/12/6
 * @apiNote Implements BlockConnectable only in PowerNukkit
 */

public abstract class BlockThin extends BlockTransparent implements BlockConnectable {

    protected BlockThin() {
    }

    @Override
    public boolean isSolid() {
        return false;
    }


    @Override
    public boolean isSolid(BlockFace side) {
        return false;
    }

    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        final double offNW = 7.0 / 16.0;
        final double offSE = 9.0 / 16.0;
        final double onNW = 0.0;
        final double onSE = 1.0;
        double w = offNW;
        double e = offSE;
        double n = offNW;
        double s = offSE;
        try {
            boolean north = this.canConnect(this.north());
            boolean south = this.canConnect(this.south());
            boolean west = this.canConnect(this.west());
            boolean east = this.canConnect(this.east());
            w = west ? onNW : offNW;
            e = east ? onSE : offSE;
            n = north ? onNW : offNW;
            s = south ? onSE : offSE;
        } catch (LevelException ignore) {
            //null sucks
        }
        return new SimpleAxisAlignedBB(
                this.x + w,
                this.y,
                this.z + n,
                this.x + e,
                this.y + 1,
                this.z + s
        );
    }

    
    @Override
    public boolean canConnect(Block block) {
        switch (block.getId()) {
            case GLASS_PANE:
            case STAINED_GLASS_PANE:
            case IRON_BARS:
            case COBBLE_WALL:
                return true;
            default:
                if (block instanceof BlockTrapdoor) {
                    BlockTrapdoor trapdoor = (BlockTrapdoor) block;
                    return trapdoor.isOpen() && trapdoor.getBlockFace() == calculateFace(this, trapdoor);
                }
                return block.isSolid();
        }
    }
}
