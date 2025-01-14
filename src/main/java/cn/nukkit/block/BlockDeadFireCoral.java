package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockDeadFireCoral extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:dead_fire_coral");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockDeadFireCoral() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockDeadFireCoral(BlockState blockstate) {
        super(blockstate);
    }
}