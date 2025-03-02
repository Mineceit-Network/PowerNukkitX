package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockBrownTerracotta extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:brown_terracotta");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockBrownTerracotta() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockBrownTerracotta(BlockState blockstate) {
        super(blockstate);
    }
}