package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemAcaciaSign;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.block.property.CommonBlockProperties.FACING_DIRECTION;


public class BlockAcaciaWallSign extends BlockWallSign {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:acacia_wall_sign", FACING_DIRECTION);

    public BlockAcaciaWallSign() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockAcaciaWallSign(BlockState blockState) {
        super(blockState);
    }

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getWallSignId() {
        return ACACIA_WALL_SIGN;
    }


    @Override
    protected String getStandingSignId() {
        return ACACIA_STANDING_SIGN;
    }

    @Override
    public String getName() {
        return "Acacia Wall Sign";
    }

    @Override
    public Item toItem() {
        return new ItemAcaciaSign();
    }
}
