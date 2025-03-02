package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityWalkable;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author joserobjr
 * @since 2020-11-20
 */


public class EntityPiglinBrute extends EntityMob implements EntityWalkable {


    public final static int NETWORK_ID = 127;


    public EntityPiglinBrute(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(50);
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
    public boolean isPreventingSleep(Player player) {
        return true;
    }


    @Override
    public String getOriginalName() {
        return "Piglin Brute";
    }
}
