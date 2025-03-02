package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class BlockIcePacked extends BlockIce {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:packed_ice");

    public BlockIcePacked() {
        super(PROPERTIES.getDefaultState());
    }

    public BlockIcePacked(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Packed Ice";
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public int onUpdate(int type) {
        return 0; //not being melted
    }

    @Override
    public boolean canHarvestWithHand() {
        return true;
    }
    
    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, Block.get(BlockID.AIR), true); //no water
        return true;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }


    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }


    @Override
    public int getLightFilter() {
        return 15;
    }
}
