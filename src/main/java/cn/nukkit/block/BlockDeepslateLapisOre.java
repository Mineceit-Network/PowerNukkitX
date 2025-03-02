package cn.nukkit.block;

import org.jetbrains.annotations.NotNull;

public class BlockDeepslateLapisOre extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:deepslate_lapis_ore");

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockDeepslateLapisOre() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockDeepslateLapisOre(BlockState blockstate) {
        super(blockstate);
    }
}