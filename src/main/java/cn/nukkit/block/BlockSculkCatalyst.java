package cn.nukkit.block;

import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySculkCatalyst;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.item.ItemTool;
import org.jetbrains.annotations.NotNull;


public class BlockSculkCatalyst extends BlockSolid implements BlockEntityHolder<BlockEntitySculkCatalyst> {

    public static final BooleanBlockProperty BLOOM = new BooleanBlockProperty("bloom", false);
    public static final BlockProperties PROPERTIES = new BlockProperties(BLOOM);

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getName() {
        return "Sculk Catalyst";
    }

    @Override
    public int getId() {
        return SCULK_CATALYST;
    }

    @Override
    public boolean canBePulled() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public int getLightLevel() {
        return 6;
    }

    @Override
    public double getResistance() {
        return 3;
    }

    @Override
    public double getHardness() {
        return 3.0;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_HOE;
    }

    @NotNull
    @Override
    public Class<? extends BlockEntitySculkCatalyst> getBlockEntityClass() {
        return BlockEntitySculkCatalyst.class;
    }

    @NotNull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.SCULK_CATALYST;
    }

}
