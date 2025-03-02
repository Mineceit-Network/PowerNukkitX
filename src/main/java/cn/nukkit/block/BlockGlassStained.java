package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.utils.DyeColor;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.blockproperty.CommonBlockProperties.COLOR;

/**
 * @author CreeperFace
 * @since 7.8.2017
 */
public class BlockGlassStained extends BlockGlass {


    public static final BlockProperties PROPERTIES = CommonBlockProperties.COLOR_BLOCK_PROPERTIES;

    public BlockGlassStained() {
        // Does nothing
    }

    public BlockGlassStained(int meta) {
        if (meta != 0) {
            getMutableState().setDataStorageFromInt(meta, true);
        }
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public int getId() {
        return STAINED_GLASS;
    }

    @Override
    public String getName() {
        return getDyeColor().getName() + " Stained Glass";
    }

    @NotNull
    public DyeColor getDyeColor() {
        return getPropertyValue(COLOR);
    }


    public void setDyeColor(@NotNull DyeColor color) {
        setPropertyValue(COLOR, color);
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
