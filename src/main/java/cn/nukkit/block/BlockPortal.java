package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockPortal extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:portal", CommonBlockProperties.PORTAL_AXIS);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockPortal() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockPortal(BlockState blockstate) {
        super(blockstate);
    }
}