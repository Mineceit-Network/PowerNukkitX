package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockCyanConcretePowder extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:cyan_concrete_powder");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockCyanConcretePowder() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockCyanConcretePowder(BlockState blockstate) {
        super(blockstate);
    }
}