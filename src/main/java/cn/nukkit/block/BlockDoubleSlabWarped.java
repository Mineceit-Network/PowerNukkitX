package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;


public class BlockDoubleSlabWarped extends BlockDoubleSlabBase {


    public BlockDoubleSlabWarped() {
        super(0);
    }

    @Override
    public int getId() {
        return WARPED_DOUBLE_SLAB;
    }


    @Override
    public String getSlabName() {
        return "Warped";
    }


    @Override
    public int getSingleSlabId() {
        return WARPED_SLAB;
    }

    //TODO Adjust or remove this when merging https://github.com/PowerNukkit/PowerNukkit/pull/370
    @Override

    protected boolean isCorrectTool(Item item) {
        return true;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 3;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public int getBurnChance() {
        return 0;
    }
    
    @Override
    public int getBurnAbility() {
        return 0;
    }

}
