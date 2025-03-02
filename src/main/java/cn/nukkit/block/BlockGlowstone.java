package cn.nukkit.block;

import cn.nukkit.item.Item;
import cn.nukkit.item.ItemGlowstoneDust;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.math.MathHelper;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * @author xtypr
 * @since 2015/12/6
 */
public class BlockGlowstone extends BlockTransparent {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:glowstone");
    public BlockGlowstone() {
        super(PROPERTIES.getDefaultState());
    }

    public BlockGlowstone(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Glowstone";
    }

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getResistance() {
        return 1.5;
    }

    @Override
    public double getHardness() {
        return 0.3;
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public Item[] getDrops(Item item) {
        Random random = new Random();
        int count = 2 + random.nextInt(3);

        Enchantment fortune = item.getEnchantment(Enchantment.ID_FORTUNE_DIGGING);
        if (fortune != null && fortune.getLevel() >= 1) {
            count += random.nextInt(fortune.getLevel() + 1);
        }

        return new Item[]{
                new ItemGlowstoneDust(0, MathHelper.clamp(count, 1, 4))
        };
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }
}
