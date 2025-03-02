package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockPinkConcretePowder extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:pink_concrete_powder");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPinkConcretePowder() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPinkConcretePowder(BlockState blockstate) {
        super(blockstate);
    }
}