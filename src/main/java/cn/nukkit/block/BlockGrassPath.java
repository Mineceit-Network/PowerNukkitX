package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;

/**
 * @author xtypr
 * @since 2015/11/22
 */
public class BlockGrassPath extends BlockGrass {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:grass_path");

    public BlockGrassPath() {
        super(PROPERTIES.getDefaultState());
    }

    public BlockGrassPath(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Dirt Path";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_SHOVEL;
    }

    @Override
    public double getMaxY() {
        return this.y + 1;
    }
    
    @Override
    public double getHardness() {
        return 0.65;
    }

    @Override
    public double getResistance() {
        return 0.65;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (this.up().isSolid()) {
                this.level.setBlock(this, Block.get(BlockID.DIRT), false, true);
            }
            
            return Level.BLOCK_UPDATE_NORMAL;
        }
        return 0;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (item.isHoe()) {
            item.useOn(this);
            this.getLevel().setBlock(this, get(FARMLAND), true);
            if (player != null) {
                player.getLevel().addSound(player, Sound.USE_GRASS);
            }
            return true;
        }

        return false;
    }


    @Override
    public boolean isSolid(BlockFace side) {
        return true;
    }

    
    @Override
    public boolean isTransparent() {
        return true;
    }
}
