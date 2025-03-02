package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockStrippedCrimsonStem extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:stripped_crimson_stem", CommonBlockProperties.PILLAR_AXIS);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockStrippedCrimsonStem() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockStrippedCrimsonStem(BlockState blockstate) {
        super(blockstate);
    }
}