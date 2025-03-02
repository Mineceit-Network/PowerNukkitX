package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMangroveDoor;
import org.jetbrains.annotations.NotNull;

public class BlockMangroveDoor extends BlockWoodenDoor {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:mangrove_door", CommonBlockProperties.DIRECTION, CommonBlockProperties.DOOR_HINGE_BIT, CommonBlockProperties.OPEN_BIT, CommonBlockProperties.UPPER_BLOCK_BIT);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockMangroveDoor() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockMangroveDoor(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public String getName() {
        return "Mangrove Door Block";
    }

    @Override
    public Item toItem() {
        return new ItemMangroveDoor();
    }
}