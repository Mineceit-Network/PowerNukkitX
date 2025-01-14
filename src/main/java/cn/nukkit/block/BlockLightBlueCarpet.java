package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockLightBlueCarpet extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:light_blue_carpet");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockLightBlueCarpet() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockLightBlueCarpet(BlockState blockstate) {
        super(blockstate);
    }
}