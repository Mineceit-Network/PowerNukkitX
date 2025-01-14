package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockRedConcretePowder extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:red_concrete_powder");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockRedConcretePowder() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockRedConcretePowder(BlockState blockstate) {
        super(blockstate);
    }
}