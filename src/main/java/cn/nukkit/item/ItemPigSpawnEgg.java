package cn.nukkit.item;

public class ItemPigSpawnEgg extends ItemSpawnEgg {
    public ItemPigSpawnEgg() {
        super(PIG_SPAWN_EGG);
    }

    @Override
    public int getEntityNetworkId() {
        return 12;
    }

    @Override
    public void setAux(Integer aux) {
        throw new UnsupportedOperationException();
    }
}