package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @author Nukkit Project Team
 */
public class BlockBricks extends BlockSolid {

    public BlockBricks() {
    }

    @Override
    public String getName() {
        return "Bricks";
    }

    @Override
    public int getId() {
        return BRICKS_BLOCK;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override

    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
