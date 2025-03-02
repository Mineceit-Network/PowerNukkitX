package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockBirchButton extends BlockWoodenButton {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:birch_button", CommonBlockProperties.BUTTON_PRESSED_BIT, CommonBlockProperties.FACING_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockBirchButton() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockBirchButton(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public String getName() {
        return "Birch Button";
    }
}