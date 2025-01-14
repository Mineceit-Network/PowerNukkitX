package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockPurpleStainedGlass extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:purple_stained_glass");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPurpleStainedGlass() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPurpleStainedGlass(BlockState blockstate) {
        super(blockstate);
    }
}