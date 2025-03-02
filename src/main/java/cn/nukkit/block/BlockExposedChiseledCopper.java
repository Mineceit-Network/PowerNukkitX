package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockExposedChiseledCopper extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:exposed_chiseled_copper");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockExposedChiseledCopper() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockExposedChiseledCopper(BlockState blockstate) {
        super(blockstate);
    }
}