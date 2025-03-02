package cn.nukkit.block;

import cn.nukkit.item.Item;

/**
 * @author xtypr
 * @since 2015/12/6
 */
public class BlockGlassPane extends BlockThin {

    public BlockGlassPane() {
    }

    @Override
    public String getName() {
        return "Glass Pane";
    }

    @Override
    public int getId() {
        return GLASS_PANE;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
