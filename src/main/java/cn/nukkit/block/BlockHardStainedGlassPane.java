package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockHardStainedGlassPane extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:hard_stained_glass_pane", CommonBlockProperties.COLOR);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockHardStainedGlassPane() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockHardStainedGlassPane(BlockState blockstate) {
        super(blockstate);
    }
}