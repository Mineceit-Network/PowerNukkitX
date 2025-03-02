package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;
import cn.nukkit.math.BlockFace;
import cn.nukkit.utils.Faceable;
import org.jetbrains.annotations.NotNull;

/**
 * @author xtypr
 * @since 2015/12/8
 */
public class BlockPumpkin extends BlockSolidMeta implements Faceable {


    public static final BlockProperties PROPERTIES = new BlockProperties(
            CommonBlockProperties.CARDINAL_DIRECTION
    );

    public BlockPumpkin() {
        this(0);
    }

    public BlockPumpkin(int meta) {
        super(meta);
    }

    @Override
    public String getName() {
        return "Pumpkin";
    }

    @Override
    public int getId() {
        return PUMPKIN;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getHardness() {
        return 1;
    }

    @Override
    public double getResistance() {
        return 1;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(this, 0);
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (item.isShears()) {
            BlockCarvedPumpkin carvedPumpkin = new BlockCarvedPumpkin();
            // TODO: Use the activated block face not the player direction
            if (player == null) {
                carvedPumpkin.setBlockFace(BlockFace.SOUTH);
            } else {
                carvedPumpkin.setBlockFace(player.getDirection().getOpposite());
            }
            item.useOn(this);
            this.level.setBlock(this, carvedPumpkin, true, true);
            this.getLevel().dropItem(add(0.5, 0.5, 0.5), Item.get(ItemID.PUMPKIN_SEEDS)); // TODO: Get correct drop item position
            return true;
        }
        return false;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        if (player == null) {
            setBlockFace(BlockFace.SOUTH);
        } else {
            setBlockFace(player.getDirection().getOpposite());
        }
        this.level.setBlock(block, this, true, true);
        return true;
    }

    @Override

    public boolean breaksWhenMoved() {
        return true;
    }

    @Override

    public boolean sticksToPiston() {
        return false;
    }

    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(CommonBlockProperties.CARDINAL_DIRECTION);
    }


    @Override
    public void setBlockFace(BlockFace face) {
        setPropertyValue(CommonBlockProperties.CARDINAL_DIRECTION, face);
    }
}
