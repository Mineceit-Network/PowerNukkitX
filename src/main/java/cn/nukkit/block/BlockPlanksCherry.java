package cn.nukkit.block;

import cn.nukkit.item.ItemTool;


public class BlockPlanksCherry extends BlockSolid {
    public BlockPlanksCherry() {
    }

    @Override
    public int getId() {
        return CHERRY_PLANKS;
    }

    @Override
    public String getName() {
        return "Cherry Planks";
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 15;
    }

    @Override
    public int getBurnChance() {
        return 5;
    }

    @Override
    public int getBurnAbility() {
        return 20;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

}
