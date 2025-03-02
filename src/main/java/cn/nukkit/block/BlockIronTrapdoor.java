package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockIronTrapdoor extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:iron_trapdoor", CommonBlockProperties.DIRECTION, CommonBlockProperties.OPEN_BIT, CommonBlockProperties.UPSIDE_DOWN_BIT);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockIronTrapdoor() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockIronTrapdoor(BlockState blockstate) {
        super(blockstate);
    }
}