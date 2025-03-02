package cn.nukkit.entity.mob;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityFlyable;
import cn.nukkit.entity.EntitySmite;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author PikyCZ
 */
public class EntityWither extends EntityMob implements EntityFlyable, EntitySmite {

    public static final int NETWORK_ID = 52;

    public EntityWither(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    @Override
    public int getNetworkId() {
        return NETWORK_ID;
    }

    @Override
    public float getWidth() {
        return 1.0f;
    }

    @Override
    public float getHeight() {
        return 3.0f;
    }

    @Override
    protected void initEntity() {
        this.setMaxHealth(600);
        super.initEntity();
    }


    @Override
    public String getOriginalName() {
        return "Wither";
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
    public boolean isBoss() {
        return true;
    }
}
