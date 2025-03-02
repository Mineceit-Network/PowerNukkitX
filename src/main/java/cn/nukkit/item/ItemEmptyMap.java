package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Level;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;

public class ItemEmptyMap extends Item {

    public ItemEmptyMap() {
        this(0, 1);
    }
    
    public ItemEmptyMap(Integer meta) {
        this(meta, 1);
    }
    
    public ItemEmptyMap(Integer meta, int count) {
        super(EMPTY_MAP, meta, count, "Empty Map");
        updateName();
    }

    @Override
    public void setAux(Integer aux) {
        super.setAux(aux);
        updateName();
    }

    private void updateName() {
        if (getAux() == 2) {
            name = "Empty Locator Map";
        } else {
            name = "Empty Map";
        }
    }

    @Override
    public boolean onActivate(Level level, Player player, Block block, Block target, BlockFace face, double fx, double fy, double fz) {
        if (!player.isCreative()) {
            this.count--;
        }
        player.getInventory().addItem(Item.get(FILLED_MAP));
        return true;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        if (!player.isCreative()) {
            this.count--;
        }
        player.getInventory().addItem(Item.get(FILLED_MAP));
        return true;
    }
}
