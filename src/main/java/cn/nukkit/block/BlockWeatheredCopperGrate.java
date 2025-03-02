package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockWeatheredCopperGrate extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:weathered_copper_grate");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockWeatheredCopperGrate() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockWeatheredCopperGrate(BlockState blockstate) {
        super(blockstate);
    }
}