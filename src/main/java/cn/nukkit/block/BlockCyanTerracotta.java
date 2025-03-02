package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockCyanTerracotta extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:cyan_terracotta");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockCyanTerracotta() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockCyanTerracotta(BlockState blockstate) {
        super(blockstate);
    }
}