package cn.nukkit.block;

import cn.nukkit.item.ItemTool;

/**
 * @author LoboMetalurgico
 * @since 08/06/2021
 */


public class BlockDeepslateCobbled extends BlockSolid {


    public BlockDeepslateCobbled(){
    }

    @Override
    public String getName() {
        return "Cobbled Deepslate";
    }

    @Override
    public int getId() {
        return ItemTool.COBBLED_DEEPSLATE;
    }

    @Override
    public double getHardness() {
        return 3.5;
    }

    @Override
    public double getResistance() {
        return 6.0;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
}
