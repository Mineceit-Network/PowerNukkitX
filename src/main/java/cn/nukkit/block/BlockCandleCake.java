package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCake;
import cn.nukkit.item.ItemID;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;


public class BlockCandleCake extends BlockTransparentMeta {


    private static final BooleanBlockProperty LIT = new BooleanBlockProperty("lit", false);


    public static final BlockProperties PROPERTIES = new BlockProperties(LIT);

    public BlockCandleCake(int meta) {
        super(meta);
    }

    public BlockCandleCake() {
        this(0);
    }

    @Override
    public String getName() {
        return "Cake Block With " + getColorName() + " Candle";
    }

    protected String getColorName() {
        return "Simple";
    }

    @Override
    public int getId() {
        return CANDLE_CAKE;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 0.5;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public double getMinX() {
        return this.x + (1 + getDamage() * 2) / 16d;
    }

    @Override
    public double getMinY() {
        return this.y;
    }

    @Override
    public double getMinZ() {
        return this.z + 0.0625;
    }

    @Override
    public double getMaxX() {
        return this.x - 0.0625 + 1;
    }

    @Override
    public double getMaxY() {
        return this.y + 0.5;
    }

    @Override
    public double getMaxZ() {
        return this.z - 0.0625 + 1;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (down().getId() != Block.AIR) {
            getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (down().getId() == Block.AIR) {
                getLevel().setBlock(this, Block.get(BlockID.AIR), true);
                return Level.BLOCK_UPDATE_NORMAL;
            }
        }

        return 0;
    }

    protected BlockCandle toCandleForm() {
        return new BlockCandle();
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{toCandleForm().toItem()};
    }

    @Override
    public Item toItem() {
        return new ItemCake();
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (getPropertyValue(LIT) && item.getId() != ItemID.FLINT_AND_STEEL) {
            setPropertyValue(LIT, false);
            getLevel().addSound(this, Sound.RANDOM_FIZZ);
            getLevel().setBlock(this, this, true, true);
            return true;
        } else if (!getPropertyValue(LIT) && item.getId() == ItemID.FLINT_AND_STEEL) {
            setPropertyValue(LIT, true);
            getLevel().addSound(this, Sound.FIRE_IGNITE);
            getLevel().setBlock(this, this, true, true);
            return true;
        } else if (player != null && (player.getFoodData().getLevel() < player.getFoodData().getMaxLevel() || player.isCreative())) {
            final Block cake = new BlockCake();
            this.getLevel().setBlock(this, cake, true, true);
            this.getLevel().dropItem(this.add(0.5, 0.5, 0.5), getDrops(null)[0]);
            return this.getLevel().getBlock(this).onActivate(Item.get(0), player);
        }
        return false;
    }

    @Override
    public int getComparatorInputOverride() {
        return 14;
    }

    @Override
    public boolean hasComparatorInputOverride() {
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
}
