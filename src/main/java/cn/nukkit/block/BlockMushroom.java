package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.event.level.StructureGrowEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.level.Level;
import cn.nukkit.level.ListChunkManager;
import cn.nukkit.level.generator.object.mushroom.BigMushroom;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.NukkitRandom;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public abstract class BlockMushroom extends BlockFlowable implements BlockFlowerPot.FlowerPotBlock {

    public BlockMushroom() {
        this(0);
    }

    public BlockMushroom(int meta) {
        super(0);
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return CommonBlockProperties.EMPTY_PROPERTIES;
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            if (!canStay()) {
                getLevel().useBreakOn(this);

                return Level.BLOCK_UPDATE_NORMAL;
            }
        }
        return 0;
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        if (canStay()) {
            getLevel().setBlock(block, this, true, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (item.isFertilizer()) {
            if (player != null && (player.gamemode & 0x01) == 0) {
                item.count--;
            }

            if (ThreadLocalRandom.current().nextFloat() < 0.4) {
                this.grow();
            }

            this.level.addParticle(new BoneMealParticle(this));
            return true;
        }
        return false;
    }

    public boolean grow() {
        this.level.setBlock(this, Block.get(BlockID.AIR), true, false);

        BigMushroom generator = new BigMushroom(getType());

        ListChunkManager chunkManager = new ListChunkManager(this.level);
        if (generator.generate(chunkManager, new NukkitRandom(), this)) {
            StructureGrowEvent ev = new StructureGrowEvent(this, chunkManager.getBlocks());
            this.level.getServer().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                return false;
            }
            for(Block block : ev.getBlockList()) {
                this.level.setBlockAt(block.getFloorX(), block.getFloorY(), block.getFloorZ(), block.getId(), block.getDamage());
            }
            return true;
        } else {
            this.level.setBlock(this, this, true, false);
            return false;
        }
    }

    public boolean canStay() {
        Block block = this.down();
        return block.getId() == MYCELIUM || block.getId() == PODZOL || block instanceof BlockNylium || (!block.isTransparent() && this.level.getFullLight(this) < 13);
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_AXE;
    }

    @Override

    public int getToolTier() {
        return ItemTool.TIER_WOODEN;
    }

    protected abstract int getType();

    @Override
    public boolean isFertilizable() {
        return true;
    }
}
