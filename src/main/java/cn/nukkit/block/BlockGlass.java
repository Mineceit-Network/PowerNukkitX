package cn.nukkit.block;

import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

/**
 * @author Angelic47 (Nukkit Project)
 */
public class BlockGlass extends BlockTransparent {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:glass");

    public BlockGlass() {
        super(PROPERTIES.getDefaultState());
    }

    public BlockGlass(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Glass";
    }

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getResistance() {
        return 0.3;
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
