package cn.nukkit.block;

import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.block.property.CommonBlockProperties.PERSISTENT_BIT;
import static cn.nukkit.block.property.CommonBlockProperties.UPDATE_BIT;

public class BlockAzaleaLeavesFlowered extends BlockAzaleaLeaves {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:azalea_leaves_flowered",PERSISTENT_BIT, UPDATE_BIT);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockAzaleaLeavesFlowered() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockAzaleaLeavesFlowered(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Azalea Leaves Flowered";
    }

    @Override
    protected Item getSapling() {
        return Block.get(FLOWERING_AZALEA).toItem();
    }
}
