package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockLimeConcretePowder extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:lime_concrete_powder");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockLimeConcretePowder() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockLimeConcretePowder(BlockState blockstate) {
        super(blockstate);
    }
}