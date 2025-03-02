package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import org.jetbrains.annotations.NotNull;


public class BlockCarvedPumpkin extends BlockPumpkin {


    public BlockCarvedPumpkin() {
        super();
    }

    @Override
    public int getId() {
        return CARVED_PUMPKIN;
    }
    
    @Override
    public String getName() {
        return "Carved Pumpkin";
    }
    
    @Override
    public boolean canBeActivated() {
        return false;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        return false;
    }
}
