package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @author GoodLucky777
 */


public class BlockSlabDeepslatePolished extends BlockSlab {


    public BlockSlabDeepslatePolished() {
        this(0);
    }


    public BlockSlabDeepslatePolished(int meta) {
        super(meta, POLISHED_DEEPSLATE_SLAB);
    }
    
    @Override
    public int getId() {
        return POLISHED_DEEPSLATE_SLAB;
    }
    
    @Override
    public String getSlabName() {
        return "Polished Deepslate";
    }
    
    @Override
    public double getHardness() {
        return 3.5;
    }
    
    @Override
    public double getResistance() {
        return 6;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public boolean isSameType(BlockSlab slab) {
        return getId() == slab.getId();
    }

}
