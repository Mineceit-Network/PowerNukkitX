package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.NukkitRandom;
import org.jetbrains.annotations.NotNull;

/**
 * @author Pub4Game
 * @since 28.01.2016
 */
public class BlockHugeMushroomRed extends BlockSolidMeta {


    public static final BlockProperties PROPERTIES = BlockHugeMushroomBrown.PROPERTIES;

    public BlockHugeMushroomRed() {
        this(0);
    }

    public BlockHugeMushroomRed(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Red Mushroom Block";
    }

    @Override
    public int getId() {
        return RED_MUSHROOM_BLOCK;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public double getHardness() {
        return 0.2;
    }

    @Override
    public double getResistance() {
        return 1;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (new NukkitRandom().nextRange(1, 20) == 1) {
            return new Item[]{
                    new ItemBlock(Block.get(BlockID.RED_MUSHROOM))
            };
        } else {
            return Item.EMPTY_ARRAY;
        }
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

}
