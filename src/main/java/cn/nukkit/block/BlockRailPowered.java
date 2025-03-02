package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.OptionalBoolean;
import cn.nukkit.utils.Rail;
import cn.nukkit.utils.RedstoneComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author Snake1999, larryTheCoder (Nukkit Project, Minecart and Riding Project)
 * @since 2016/1/11
 */

public class BlockRailPowered extends BlockRail implements RedstoneComponent {

    public BlockRailPowered() {
        this(0);
        canBePowered = true;
    }

    public BlockRailPowered(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POWERED_RAIL;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return ACTIVABLE_PROPERTIES;
    }

    @Override
    public String getName() {
        return "Powered Rail";
    }

    @Override

    public int onUpdate(int type) {
        // Warning: I din't recommended this on slow networks server or slow client
        //          Network below 86Kb/s. This will became unresponsive to clients 
        //          When updating the block state. Espicially on the world with many rails. 
        //          Trust me, I tested this on my server.
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_REDSTONE || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (super.onUpdate(type) == Level.BLOCK_UPDATE_NORMAL) {
                return 0; // Already broken
            }

            if (!this.level.getServer().isRedstoneEnabled()) {
                return 0;
            }
            boolean wasPowered = isActive();
            boolean isPowered = this.isGettingPower()
                    || checkSurrounding(this, true, 0)
                    || checkSurrounding(this, false, 0);

            // Avoid Block mistake
            if (wasPowered != isPowered) {
                setActive(isPowered);
                RedstoneComponent.updateAroundRedstone(down());
                if (getOrientation().isAscending()) {
                    RedstoneComponent.updateAroundRedstone(up());
                }
            }
            return type;
        }
        return 0;
    }


    @Override
    public void afterRemoval(Block newBlock, boolean update) {
        RedstoneComponent.updateAroundRedstone(down());
        if (getOrientation().isAscending()) {
            RedstoneComponent.updateAroundRedstone(up());
        }
        super.afterRemoval(newBlock, update);
    }

    /**
     * Check the surrounding of the rail
     *
     * @param pos      The rail position
     * @param relative The relative of the rail that will be checked
     * @param power    The count of the rail that had been counted
     * @return Boolean of the surrounding area. Where the powered rail on!
     */
    private boolean checkSurrounding(Vector3 pos, boolean relative, int power) {
        // The powered rail can power up to 8 blocks only
        if (power >= 8) {
            return false;
        }
        // The position of the floor numbers
        int dx = pos.getFloorX();
        int dy = pos.getFloorY();
        int dz = pos.getFloorZ();
        // First: get the base block
        BlockRail block;
        Block block2 = level.getBlock(new Vector3(dx, dy, dz));

        // Second: check if the rail is Powered rail
        if (Rail.isRailBlock(block2)) {
            block = (BlockRail) block2;
        } else {
            return false;
        }

        // Used to check if the next ascending rail should be what
        Rail.Orientation base = block.getOrientation();
        boolean onStraight = true;
        // Third: Recalculate the base position
        switch (base) {
            case STRAIGHT_NORTH_SOUTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                }
                break;
            case STRAIGHT_EAST_WEST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                }
                break;
            case ASCENDING_EAST:
                if (relative) {
                    dx--;
                } else {
                    dx++;
                    dy++;
                    onStraight = false;
                }
                break;
            case ASCENDING_WEST:
                if (relative) {
                    dx--;
                    dy++;
                    onStraight = false;
                } else {
                    dx++;
                }
                break;
            case ASCENDING_NORTH:
                if (relative) {
                    dz++;
                } else {
                    dz--;
                    dy++;
                    onStraight = false;
                }
                break;
            case ASCENDING_SOUTH:
                if (relative) {
                    dz++;
                    dy++;
                    onStraight = false;
                } else {
                    dz--;
                }
                break;
            default:
                // Unable to determinate the rail orientation
                // Wrong rail?
                return false;
        }
        // Next check the if rail is on power state
        return canPowered(new Vector3(dx, dy, dz), base, power, relative)
                || onStraight && canPowered(new Vector3(dx, dy - 1.0D, dz), base, power, relative);
    }


    protected boolean canPowered(Vector3 pos, Rail.Orientation state, int power, boolean relative) {
        Block block = level.getBlock(pos);
        // What! My block is air??!! Impossible! XD
        if (!(block instanceof BlockRailPowered)) {
            return false;
        }

        // Sometimes the rails are diffrent orientation
        Rail.Orientation base = ((BlockRailPowered) block).getOrientation();

        // Possible way how to know when the rail is activated is rail were directly powered
        // OR recheck the surrounding... Which will returns here =w=
        return (state != Rail.Orientation.STRAIGHT_EAST_WEST
                || base != Rail.Orientation.STRAIGHT_NORTH_SOUTH
                && base != Rail.Orientation.ASCENDING_NORTH
                && base != Rail.Orientation.ASCENDING_SOUTH)
                && (state != Rail.Orientation.STRAIGHT_NORTH_SOUTH
                || base != Rail.Orientation.STRAIGHT_EAST_WEST
                && base != Rail.Orientation.ASCENDING_EAST
                && base != Rail.Orientation.ASCENDING_WEST)
                && (block.isGettingPower() || checkSurrounding(pos, relative, power + 1));

    }

    @Override
    public boolean isActive() {
        return getBooleanValue(ACTIVE);
    }


    @Override
    public OptionalBoolean isRailActive() {
        return OptionalBoolean.of(getBooleanValue(ACTIVE));
    }


    @Override
    public void setRailActive(boolean active) {
        setBooleanValue(ACTIVE, active);
    }
}
