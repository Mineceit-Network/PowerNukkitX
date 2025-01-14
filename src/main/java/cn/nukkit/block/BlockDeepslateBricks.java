package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockDeepslateBricks extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:deepslate_bricks");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockDeepslateBricks() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockDeepslateBricks(BlockState blockstate) {
        super(blockstate);
    }
}