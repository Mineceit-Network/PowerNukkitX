package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockReserved6 extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:reserved6");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockReserved6() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockReserved6(BlockState blockstate) {
        super(blockstate);
    }
}