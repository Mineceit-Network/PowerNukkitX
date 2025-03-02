package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockRedWool extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:red_wool");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockRedWool() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockRedWool(BlockState blockstate) {
        super(blockstate);
    }
}