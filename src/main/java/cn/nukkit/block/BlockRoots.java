package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;


public abstract class BlockRoots extends BlockFlowable {


    protected BlockRoots() {
        super(0);
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return CommonBlockProperties.EMPTY_PROPERTIES;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL && !isSupportValid()) {
            level.useBreakOn(this);
            return type;
        }
        return 0;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        return isSupportValid() && super.place(item, block, target, face, fx, fy, fz, player);
    }


    protected boolean isSupportValid() {
        switch (down().getId()) {
            case WARPED_NYLIUM:
            case CRIMSON_NYLIUM:
            case GRASS:
            case PODZOL:
            case DIRT:
            case SOUL_SOIL:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getBurnChance() {
        return 5;
    }
}
