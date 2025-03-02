package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockTuffSlab extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:tuff_slab", CommonBlockProperties.MINECRAFT_VERTICAL_HALF);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockTuffSlab() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockTuffSlab(BlockState blockstate) {
        super(blockstate);
    }
}