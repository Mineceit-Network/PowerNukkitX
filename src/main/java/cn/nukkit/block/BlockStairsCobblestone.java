package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @author xtypr
 * @since 2015/11/25
 */
public class BlockStairsCobblestone extends BlockStairs {
    public BlockStairsCobblestone() {
        this(0);
    }

    public BlockStairsCobblestone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return STONE_STAIRS;
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
    public String getName() {
        return "Cobblestone Stairs";
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
