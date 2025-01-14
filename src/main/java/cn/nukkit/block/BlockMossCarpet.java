package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;


public class BlockMossCarpet extends BlockCarpet {


    public static final BlockProperties PROPERTIES = CommonBlockProperties.EMPTY_PROPERTIES;

    @Override
    public int getId() {
        return MOSS_CARPET;
    }

    @Override
    public double getResistance() {
        return 0.1;
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{toItem()};
    }

    @Override
    public String getName() {
        return "Moss Carpet";
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
}
