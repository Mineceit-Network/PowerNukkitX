package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.value.StoneSlab1Type;
import cn.nukkit.item.ItemTool;
import org.jetbrains.annotations.NotNull;

/**
 * @author MagicDroidX (Nukkit Project)
 */

public class BlockDoubleSlabStone extends BlockDoubleSlabBase {
    public static final int STONE = 0;
    public static final int SANDSTONE = 1;
    public static final int WOODEN = 2;
    public static final int COBBLESTONE = 3;
    public static final int BRICK = 4;
    public static final int STONE_BRICK = 5;
    public static final int QUARTZ = 6;
    public static final int NETHER_BRICK = 7;

    public BlockDoubleSlabStone() {
        this(0);
    }

    public BlockDoubleSlabStone(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return DOUBLE_STONE_SLAB;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return BlockSlabStone.PROPERTIES;
    }

    @Override
    public double getResistance() {
        return getToolType() > ItemTool.TIER_WOODEN ? 30 : 15;
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    public StoneSlab1Type getSlabType() {
        return getPropertyValue(StoneSlab1Type.PROPERTY);
    }


    @Override
    public int getSingleSlabId() {
        return STONE_SLAB;
    }


    public void setSlabType(StoneSlab1Type type) {
        setPropertyValue(StoneSlab1Type.PROPERTY, type);
    }


    @Override
    public String getSlabName() {
        return getSlabType().getEnglishName();
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
