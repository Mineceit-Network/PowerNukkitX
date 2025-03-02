package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockPurpurStairs extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:purpur_stairs", CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPurpurStairs() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPurpurStairs(BlockState blockstate) {
        super(blockstate);
    }
}