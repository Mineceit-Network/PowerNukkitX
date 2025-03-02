package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemDarkOakDoor;
import org.jetbrains.annotations.NotNull;

public class BlockDarkOakDoor extends BlockWoodenDoor {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:dark_oak_door", CommonBlockProperties.DIRECTION, CommonBlockProperties.DOOR_HINGE_BIT, CommonBlockProperties.OPEN_BIT, CommonBlockProperties.UPPER_BLOCK_BIT);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockDarkOakDoor() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockDarkOakDoor(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public String getName() {
        return "Dark Oak Door Block";
    }

    @Override
    public Item toItem() {
        return new ItemDarkOakDoor();
    }
}