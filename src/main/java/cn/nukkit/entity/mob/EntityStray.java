package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.entity.EntityWalkable;
import cn.nukkit.item.Item;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityStray extends EntityMob implements EntityWalkable, EntitySmite {

    public static final int NETWORK_ID = 46;

    public EntityStray(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(20);
        super.initEntity();
    }

    @Override
    public float getWidth() {
        return 0.6f;
    }

    @Override
    public float getHeight() {
        return 1.9f;
    }


    @Override
    public String getOriginalName() {
        return "Stray";
    }

    @Override
    public Item[] getDrops() {
        return new Item[]{Item.get(Item.BONE, Item.ARROW)};
    }


    @Override
    public boolean isUndead() {
        return true;
    }


    @Override
    public boolean isPreventingSleep(Player player) {
        return true;
    }

    @Override
    public boolean onUpdate(int currentTick) {
        burn(this);
        return super.onUpdate(currentTick);
    }
}
