package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockMagentaGlazedTerracotta extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:magenta_glazed_terracotta", CommonBlockProperties.FACING_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockMagentaGlazedTerracotta() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockMagentaGlazedTerracotta(BlockState blockstate) {
        super(blockstate);
    }
}