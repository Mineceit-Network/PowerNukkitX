package cn.nukkit.entity.passive;

import cn.nukkit.entity.EntityWalkable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityPolarBear extends EntityAnimal implements EntityWalkable {

    public static final int NETWORK_ID = 28;

    public EntityPolarBear(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        if (this.isBaby()) {
            return 0.65f;
        }
        return 1.3f;
    }

    @Override
    public float getHeight() {
        if (this.isBaby()) {
            return 0.7f;
        }
        return 1.4f;
    }

    @Override
    public void initEntity() {
        this.setMaxHealth(30);
        super.initEntity();
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.RAW_FISH), Item.get(Item.RAW_SALMON)};
    }


    @Override
    public String getOriginalName() {
        return "Polar Bear";
    }
}
