package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockWhiteStainedGlass extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:white_stained_glass");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockWhiteStainedGlass() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockWhiteStainedGlass(BlockState blockstate) {
        super(blockstate);
    }
}