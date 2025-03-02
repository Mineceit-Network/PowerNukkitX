package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockRawIronBlock extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:raw_iron_block");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockRawIronBlock() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockRawIronBlock(BlockState blockstate) {
        super(blockstate);
    }
}