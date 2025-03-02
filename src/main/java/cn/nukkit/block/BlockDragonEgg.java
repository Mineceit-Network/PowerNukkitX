package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.event.block.BlockFromToEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.level.Level;
import cn.nukkit.network.protocol.LevelEventPacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

public class BlockDragonEgg extends BlockFallable {
    public static final BlockProperties PROPERTIES = new BlockProperties("minecraft:dragon_egg");

    public BlockDragonEgg() {
        super(PROPERTIES.getDefaultState());
    }

    public BlockDragonEgg(BlockState blockState) {
        super(blockState);
    }

    @Override
    public String getName() {
        return "Dragon Egg";
    }

    @Override
    public @NotNull BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public double getHardness() {
        return 3;
    }

    @Override
    public double getResistance() {
        return 45;
    }

    @Override
    public int getLightLevel() {
        return 1;
    }

    @Override
    public boolean isTransparent() {
        return true;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_TOUCH) {
            this.teleport();
        }
        return super.onUpdate(type);
    }


    @Override
    public int onTouch(@Nullable Player player, Action action) {
        if (player != null && (action == Action.RIGHT_CLICK_BLOCK || action == Action.LEFT_CLICK_BLOCK)) {
            if (player.isCreative() && action == Action.LEFT_CLICK_BLOCK) {
                return 0;
            }
            onUpdate(Level.BLOCK_UPDATE_TOUCH);
            return 1;
        }
        return 0;
    }

    public void teleport() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 1000; ++i) {
            Block to = this.getLevel().getBlock(this.add(random.nextInt(-16, 16), random.nextInt(0, 16), random.nextInt(-16, 16)));
            if (to.getId() == AIR) {
                BlockFromToEvent event = new BlockFromToEvent(this, to);
                this.level.getServer().getPluginManager().callEvent(event);
                if (event.isCancelled()) return;
                to = event.getTo();

                int diffX = this.getFloorX() - to.getFloorX();
                int diffY = this.getFloorY() - to.getFloorY();
                int diffZ = this.getFloorZ() - to.getFloorZ();
                LevelEventPacket pk = new LevelEventPacket();
                pk.evid = LevelEventPacket.EVENT_PARTICLE_DRAGON_EGG_TELEPORT;
                pk.data = (((((Math.abs(diffX) << 16) | (Math.abs(diffY) << 8)) | Math.abs(diffZ)) | ((diffX < 0 ? 1 : 0) << 24)) | ((diffY < 0 ? 1 : 0) << 25)) | ((diffZ < 0 ? 1 : 0) << 26);
                pk.x = this.getFloorX();
                pk.y = this.getFloorY();
                pk.z = this.getFloorZ();
                this.getLevel().addChunkPacket(this.getFloorX() >> 4, this.getFloorZ() >> 4, pk);
                this.getLevel().setBlock(this, get(AIR), true);
                this.getLevel().setBlock(to, this, true);
                return;
            }
        }
    }

    @Override

    public boolean breaksWhenMoved() {
        return true;
    }

    @Override

    public boolean sticksToPiston() {
        return false;
    }
}
