package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockElement115 extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:element_115");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockElement115() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockElement115(BlockState blockstate) {
        super(blockstate);
    }
}