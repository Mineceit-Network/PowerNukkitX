package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.entity.item.EntityFallingBlock;
import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import org.jetbrains.annotations.NotNull;


public class BlockSuspiciousSand extends BlockFallableMeta {
    public static final IntBlockProperty BRUSHED_PROGRESS = new IntBlockProperty("brushed_progress", false, 3);
    public static final BlockProperties PROPERTIES = new BlockProperties(CommonBlockProperties.HANGING, BRUSHED_PROGRESS);

    public BlockSuspiciousSand() {
    }

    public BlockSuspiciousSand(int meta) {
        super(meta);
    }

    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    public int getId() {
        return SUSPICIOUS_SAND;
    }

    public String getName() {
        return "Suspicious Sand";
    }

    @Override
    public double getHardness() {
        return 0.25;
    }

    @Override
    public double getResistance() {
        return 1.25;
    }


    @Override
    protected EntityFallingBlock createFallingEntity(CompoundTag customNbt) {
        customNbt.putBoolean("BreakOnGround", true);
        return super.createFallingEntity(customNbt);
    }

    @Override
    public Item[] getDrops(Item item) {
        return new Item[]{Item.AIR_ITEM};
    }
}