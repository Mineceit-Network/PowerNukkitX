package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.value.StoneSlab2Type;
import cn.nukkit.item.ItemTool;
import org.jetbrains.annotations.NotNull;

/**
 * @author CreeperFace
 * @since 26. 11. 2016
 */

public class BlockDoubleSlabRedSandstone extends BlockDoubleSlabBase {

    public BlockDoubleSlabRedSandstone() {
        this(0);
    }

    public BlockDoubleSlabRedSandstone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DOUBLE_RED_SANDSTONE_SLAB;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return BlockSlabRedSandstone.PROPERTIES;
    }


    public StoneSlab2Type getSlabType() {
        return getPropertyValue(StoneSlab2Type.PROPERTY);
    }


    public void setSlabType(StoneSlab2Type type) {
        setPropertyValue(StoneSlab2Type.PROPERTY, type);
    }


    @Override
    public String getSlabName() {
        return getSlabType().getEnglishName();
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    @Override
    public int getSingleSlabId() {
        return RED_SANDSTONE_SLAB;
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
