package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockWarpedButton extends BlockWoodenButton {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:warped_button", CommonBlockProperties.BUTTON_PRESSED_BIT, CommonBlockProperties.FACING_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockWarpedButton() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockWarpedButton(BlockState blockstate) {
        super(blockstate);
    }

    @Override
    public String getName() {
        return "Warped Button";
    }
}