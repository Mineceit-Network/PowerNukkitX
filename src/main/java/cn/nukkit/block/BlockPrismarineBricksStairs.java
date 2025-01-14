package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockPrismarineBricksStairs extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:prismarine_bricks_stairs", CommonBlockProperties.UPSIDE_DOWN_BIT, CommonBlockProperties.WEIRDO_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPrismarineBricksStairs() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPrismarineBricksStairs(BlockState blockstate) {
        super(blockstate);
    }
}