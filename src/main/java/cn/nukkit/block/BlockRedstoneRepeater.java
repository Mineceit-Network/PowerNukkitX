package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemRedstoneRepeater;
import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;

import static cn.nukkit.blockproperty.CommonBlockProperties.DIRECTION;


public abstract class BlockRedstoneRepeater extends BlockRedstoneDiode {


    protected static final IntBlockProperty REPEATER_DELAY = new IntBlockProperty("repeater_delay", false, 3);


    public static final BlockProperties PROPERTIES = new BlockProperties(
            CommonBlockProperties.CARDINAL_DIRECTION,
            REPEATER_DELAY
    );


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }


    public BlockRedstoneRepeater() {
        super(0);
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        int repeaterDelay = getPropertyValue(REPEATER_DELAY);
        if (repeaterDelay == 3) {
            setPropertyValue(REPEATER_DELAY, 0);
        } else {
            setPropertyValue(REPEATER_DELAY, repeaterDelay + 1);
        }

        this.level.setBlock(this, this, true, true);
        return true;
    }

    
    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        if (!isSupportValid(down())) {
            return false;
        }

        setPropertyValue(CommonBlockProperties.CARDINAL_DIRECTION, player != null ? BlockFace.fromHorizontalIndex(player.getDirection().getOpposite().getHorizontalIndex()) : BlockFace.SOUTH);
        if (!this.level.setBlock(block, this, true, true)) {
            return false;
        }

        if (this.level.getServer().isRedstoneEnabled()) {
            if (shouldBePowered()) {
                this.level.scheduleUpdate(this, 1);
            }
        }
        return true;
    }

    @Override
    public BlockFace getFacing() {
        return getPropertyValue(CommonBlockProperties.CARDINAL_DIRECTION);
    }

    @Override
    protected boolean isAlternateInput(Block block) {
        return isDiode(block);
    }

    @Override
    public Item toItem() {
        return new ItemRedstoneRepeater();
    }

    @Override
    protected int getDelay() {
        return (1 + getIntValue(REPEATER_DELAY)) * 2;
    }

    @Override
    public boolean isLocked() {
        return this.getPowerOnSides() > 0;
    }
}
