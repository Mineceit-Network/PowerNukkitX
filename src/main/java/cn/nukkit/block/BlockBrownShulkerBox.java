package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockBrownShulkerBox extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:brown_shulker_box");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockBrownShulkerBox() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockBrownShulkerBox(BlockState blockstate) {
        super(blockstate);
    }
}