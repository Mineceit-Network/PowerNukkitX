package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockBlackstoneStairs extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:blackstone_stairs", CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockBlackstoneStairs() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockBlackstoneStairs(BlockState blockstate) {
        super(blockstate);
    }
}