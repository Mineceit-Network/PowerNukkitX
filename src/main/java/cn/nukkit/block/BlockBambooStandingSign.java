package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBambooSign;
import org.jetbrains.annotations.NotNull;


public class BlockBambooStandingSign extends BlockStandingSign {
    public static final BlockProperties PROPERTIES = new BlockProperties(BAMBOO_STANDING_SIGN, CommonBlockProperties.GROUND_SIGN_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockBambooStandingSign() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockBambooStandingSign(BlockState blockstate) {
        super(blockstate);
    }

    public String getName() {
        return "Bamboo Standing Sign";
    }

    @Override
    public Item toItem() {
        return new ItemBambooSign();
    }
}