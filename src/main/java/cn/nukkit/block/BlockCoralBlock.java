package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.event.block.BlockFadeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemTool;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;


public class BlockCoralBlock extends BlockSolidMeta {


    public static final BlockProperties PROPERTIES = BlockCoral.PROPERTIES;


    public BlockCoralBlock() {
        this(0);
    }


    public BlockCoralBlock(int meta) {
        super(meta);
    }
    
    @Override
    public int getId() {
        return CORAL_BLOCK;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }


    public boolean isDead() {
        return (getDamage() & 0x8) == 0x8;
    }


    public void setDead(boolean dead) {
        if (dead) {
            setDamage(getDamage() | 0x8);
        } else {
            setDamage(getDamage() ^ 0x8);
        }
    }
    
    @Override
    public String getName() {
        String[] names = new String[] {
                "Tube Coral Block",
                "Brain Coral Block",
                "Bubble Coral Block",
                "Fire Coral Block",
                "Horn Coral Block",
                // Invalid
                "Tube Coral Block",
                "Tube Coral Block",
                "Tube Coral Block"
        };
        String name = names[getDamage() & 0x7];
        if (isDead()) {
            return "Dead " + name;
        } else {
            return name;
        }
    }

    @Override
    public double getHardness() {
        return 7;
    }
    
    @Override
    public double getResistance() {
        return 6.0;
    }
    
    @Override
    public boolean canHarvestWithHand() {
        return false;
    }
    
    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }
    
    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!isDead()) {
                this.getLevel().scheduleUpdate(this, 60 + ThreadLocalRandom.current().nextInt(40));
            }
            return type;
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!isDead()) {
                for (BlockFace face : BlockFace.values()) {
                    if (getSideAtLayer(0, face) instanceof BlockWater || getSideAtLayer(1, face) instanceof BlockWater
                        || getSideAtLayer(0, face) instanceof BlockIceFrosted || getSideAtLayer(1, face) instanceof BlockIceFrosted) {
                        return type;
                    }
                }
                BlockFadeEvent event = new BlockFadeEvent(this, new BlockCoralBlock(getDamage() | 0x8));
                if (!event.isCancelled()) {
                    setDead(true);
                    this.getLevel().setBlock(this, event.getNewState(), true, true);
                }
            }
            return type;
        }
        return 0;
    }
    
    @Override
    public Item[] getDrops(Item item) {
        if (item.isPickaxe() && item.getTier() >= ItemTool.TIER_WOODEN) {
            if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) != null) {
                return new Item[]{ toItem() };
            } else {
                return new Item[]{ new ItemBlock(clone(), getDamage() | 0x8) };
            }
        } else {
            return Item.EMPTY_ARRAY;
        }
    }
}
