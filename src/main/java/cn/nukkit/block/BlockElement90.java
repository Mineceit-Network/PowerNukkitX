package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockElement90 extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:element_90");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockElement90() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockElement90(BlockState blockstate) {
        super(blockstate);
    }
}