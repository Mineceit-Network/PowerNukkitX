package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockGreenWool extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:green_wool");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockGreenWool() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockGreenWool(BlockState blockstate) {
        super(blockstate);
    }
}