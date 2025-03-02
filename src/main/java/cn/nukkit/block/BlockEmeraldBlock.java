package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockEmeraldBlock extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:emerald_block");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockEmeraldBlock() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockEmeraldBlock(BlockState blockstate) {
        super(blockstate);
    }
}