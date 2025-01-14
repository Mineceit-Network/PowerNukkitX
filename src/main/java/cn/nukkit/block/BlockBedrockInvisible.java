package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

/**
 * @author Pub4Game
 * @since 03.01.2016
 */
public class BlockBedrockInvisible extends BlockSolid {

    public BlockBedrockInvisible() {
    }

    @Override
    public int getId() {
        return INVISIBLE_BEDROCK;
    }

    @Override
    public String getName() {
        return "Invisible Bedrock";
    }


    @Override
    public int getWaterloggingLevel() {
        return 2;
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }

    @Override
    public double getHardness() {
        return -1;
    }

    @Override
    public double getResistance() {
        return 18000000;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override

    public  boolean canBePulled() {
        return false;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(BlockID.AIR));
    }
}
