package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @author Nukkit Project Team
 */
public class BlockDiamond extends BlockSolid {

    public BlockDiamond() {
    }

    @Override
    public double getHardness() {
        return 5;
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
    public int getId() {
        return DIAMOND_BLOCK;
    }

    @Override
    public String getName() {
        return "Diamond Block";
    }

    @Override

    public int getToolTier() {
        return ItemTool.TIER_IRON;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
