package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockExposedCopper extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:exposed_copper");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockExposedCopper() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockExposedCopper(BlockState blockstate) {
        super(blockstate);
    }
}