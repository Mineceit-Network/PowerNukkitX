package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockChiseledNetherBricks extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:chiseled_nether_bricks");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockChiseledNetherBricks() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockChiseledNetherBricks(BlockState blockstate) {
        super(blockstate);
    }
}