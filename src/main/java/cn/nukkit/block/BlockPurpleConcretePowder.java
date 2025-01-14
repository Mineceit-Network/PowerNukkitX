package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockPurpleConcretePowder extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:purple_concrete_powder");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPurpleConcretePowder() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPurpleConcretePowder(BlockState blockstate) {
        super(blockstate);
    }
}