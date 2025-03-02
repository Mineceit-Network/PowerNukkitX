package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockIronOre extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:iron_ore");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockIronOre() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockIronOre(BlockState blockstate) {
        super(blockstate);
    }
}