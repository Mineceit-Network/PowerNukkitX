package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockStrippedOakLog extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:stripped_oak_log", CommonBlockProperties.PILLAR_AXIS);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockStrippedOakLog() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockStrippedOakLog(BlockState blockstate) {
        super(blockstate);
    }
}