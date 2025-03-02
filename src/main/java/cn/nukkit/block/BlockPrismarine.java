package cn.nukkit.block;

import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.blockproperty.ArrayBlockProperty;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.value.PrismarineBlockType;
import cn.nukkit.item.ItemTool;
import org.jetbrains.annotations.NotNull;


public class BlockPrismarine extends BlockSolidMeta {


    public static final BlockProperty<PrismarineBlockType> PRISMARINE_BLOCK_TYPE = new ArrayBlockProperty<>("prismarine_block_type", true, PrismarineBlockType.class);


    public static final BlockProperties PROPERTIES = new BlockProperties(PRISMARINE_BLOCK_TYPE);

    @Deprecated
    @DeprecationDetails(since = "1.5.0.0-PN", replaceWith = "getPrismarineBlockType()", reason = "Use the BlockProperty API instead")
    public static final int NORMAL = 0;

    @DeprecationDetails(since = "1.5.0.0-PN", replaceWith = "getPrismarineBlockType()", reason = "Use the BlockProperty API instead")
    public static final int DARK = 1;

    @DeprecationDetails(since = "1.5.0.0-PN", replaceWith = "getPrismarineBlockType()", reason = "Use the BlockProperty API instead")
    public static final int BRICKS = 2;

    public BlockPrismarine() {
        this(0);
    }

    public BlockPrismarine(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return PRISMARINE;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getHardness() {
        return 1.5;
    }

    @Override
    public double getResistance() {
        return 30;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }

    @Override
    public String getName() {
        return getPrismarineBlockType().getEnglishName();
    }


    public void setPrismarineBlockType(PrismarineBlockType prismarineBlockType) {
        setPropertyValue(PRISMARINE_BLOCK_TYPE, prismarineBlockType);
    }


    public PrismarineBlockType getPrismarineBlockType() {
        return getPropertyValue(PRISMARINE_BLOCK_TYPE);
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
