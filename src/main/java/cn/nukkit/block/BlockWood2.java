package cn.nukkit.block;

import cn.nukkit.blockproperty.ArrayBlockProperty;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.value.WoodType;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.blockproperty.CommonBlockProperties.PILLAR_AXIS;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class BlockWood2 extends BlockWood {


    public static final BlockProperty<WoodType> NEW_LOG_TYPE = new ArrayBlockProperty<>("new_log_type", true, new WoodType[]{
            WoodType.ACACIA, WoodType.DARK_OAK
    }, 2);


    public static final BlockProperties PROPERTIES = new BlockProperties(NEW_LOG_TYPE, PILLAR_AXIS);

    public static final int ACACIA = 0;
    public static final int DARK_OAK = 1;

    public BlockWood2() {
        this(0);
    }

    public BlockWood2(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return WOOD2;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }


    @Override
    public WoodType getWoodType() {
        return getPropertyValue(NEW_LOG_TYPE);
    }


    @Override
    public void setWoodType(WoodType woodType) {
        setPropertyValue(NEW_LOG_TYPE, woodType);
    }
}
