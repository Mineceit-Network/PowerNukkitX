package cn.nukkit.item;

import cn.nukkit.block.BlockCampfireSoul;

public class ItemSoulCampfire extends Item {
    public ItemSoulCampfire() {
        this(0, 1);
    }


    public ItemSoulCampfire(Integer meta) {
        this(meta, 1);
    }


    public ItemSoulCampfire(Integer meta, int count) {
        super(SOUL_CAMPFIRE, meta, count, "Soul Campfire");
        this.block = new BlockCampfireSoul();
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}