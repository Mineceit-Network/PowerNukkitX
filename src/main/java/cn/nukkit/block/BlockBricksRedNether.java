package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

@SuppressWarnings("deprecation")
public class BlockBricksRedNether extends BlockNetherBrick {

    public BlockBricksRedNether() {
        // Does nothing
    }

    @Override
    public String getName() {
        return "Red Nether Bricks";
    }

    @Override
    public int getId() {
        return RED_NETHER_BRICK;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override

    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

}
