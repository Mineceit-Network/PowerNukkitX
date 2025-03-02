package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.block.property.CommonBlockProperties.PILLAR_AXIS;


public class BlockStrippedBambooBlock extends BlockLog {
    public static final BlockProperties PROPERTIES = new BlockProperties(STRIPPED_BAMBOO_BLOCK, PILLAR_AXIS);

    public BlockStrippedBambooBlock() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockStrippedBambooBlock(BlockState blockState) {
        super(blockState);
    }

    public String getName() {
        return "Stripped Bamboo Block";
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public BlockState getStrippedState() {
        return getBlockState();
    }

    @Override
    public boolean canBeActivated() {
        return false;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        return false;
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
}