package cn.nukkit.block;

import cn.nukkit.block.property.CommonBlockProperties;
import org.jetbrains.annotations.NotNull;

public class BlockRepeatingCommandBlock extends Block {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:repeating_command_block", CommonBlockProperties.CONDITIONAL_BIT, CommonBlockProperties.FACING_DIRECTION);

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    public BlockRepeatingCommandBlock() {
        this(PROPERTIES.getDefaultState());
    }

    public BlockRepeatingCommandBlock(BlockState blockstate) {
        super(blockstate);
    }
}