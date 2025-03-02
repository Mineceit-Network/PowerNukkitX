package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockYellowCarpet extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:yellow_carpet");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockYellowCarpet() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockYellowCarpet(BlockState blockstate) {
        super(blockstate);
    }
}