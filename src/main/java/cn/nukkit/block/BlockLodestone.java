package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityLodestone;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemCompassLodestone;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Sound;
import cn.nukkit.math.BlockFace;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * @author joserobjr
 */


@Log4j2
public class BlockLodestone extends BlockSolid implements BlockEntityHolder<BlockEntityLodestone> {


    public BlockLodestone() {
        // Does nothing
    }
    
    @Override
    public int getId() {
        return LODESTONE;
    }


    @NotNull
    @Override
    public Class<? extends BlockEntityLodestone> getBlockEntityClass() {
        return BlockEntityLodestone.class;
    }


    @NotNull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.LODESTONE;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        return BlockEntityHolder.setBlockAndCreateEntity(this) != null;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        if (player == null || item.isNull() || item.getId() != ItemID.COMPASS && item.getId() != ItemID.LODESTONE_COMPASS) {
            return false;
        }


        ItemCompassLodestone compass = (ItemCompassLodestone) Item.get(ItemID.LODESTONE_COMPASS);
        if (item.hasCompoundTag()) {
            compass.setCompoundTag(item.getCompoundTag().clone());
        }
        
        int trackingHandle;
        try {
            trackingHandle = getOrCreateBlockEntity().requestTrackingHandler();
            compass.setTrackingHandle(trackingHandle);
        } catch (Exception e) {
            log.warn("Could not create a lodestone compass to {} for {}", getLocation(), player.getName(), e);
            return false;
        }

        boolean added = true;
        if (item.getCount() == 1) {
            player.getInventory().setItemInHand(compass);
        } else {
            Item clone = item.clone();
            clone.count--;
            player.getInventory().setItemInHand(clone);
            for (Item failed : player.getInventory().addItem(compass)) {
                added = false;
                player.getLevel().dropItem(player.getPosition(), failed);
            }
        }
        
        getLevel().addSound(player.getPosition(), Sound.LODESTONE_COMPASS_LINK_COMPASS_TO_LODESTONE);
        
        if (added) {
            try {
                getLevel().getServer().getPositionTrackingService().startTracking(player, trackingHandle, false);
            } catch (IOException e) {
                log.warn("Failed to make the player {} track {} at {}", player.getName(), trackingHandle, getLocation(),  e);
            }
            getLevel().getServer().getScheduler().scheduleTask(null, player::updateTrackingPositions);
        }
        
        return true;
    }

    @Override
    public String getName() {
        return "Lodestone";
    }

    @Override
    public double getHardness() {
        return 2;
    }

    @Override
    public double getResistance() {
        return 3.5;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    @Override
    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override

    public  boolean sticksToPiston() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }
}
