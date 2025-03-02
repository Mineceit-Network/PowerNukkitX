package cn.nukkit.block;

import cn.nukkit.Server;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.block.BlockBurnEvent;
import cn.nukkit.event.block.BlockFadeEvent;
import cn.nukkit.event.block.BlockIgniteEvent;
import cn.nukkit.event.entity.EntityCombustByBlockEvent;
import cn.nukkit.event.entity.EntityDamageByBlockEvent;
import cn.nukkit.event.entity.EntityDamageEvent.DamageCause;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class BlockFire extends BlockFlowable {


    public static final IntBlockProperty FIRE_AGE = CommonBlockProperties.AGE_15;


    public static final BlockProperties PROPERTIES = new BlockProperties(FIRE_AGE);

    public BlockFire() {
        this(0);
    }

    public BlockFire(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return FIRE;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public String getName() {
        return "Fire Block";
    }

    @Override
    public int getLightLevel() {
        return 15;
    }

    @Override
    public boolean isBreakable(Item item) {
        return false;
    }

    @Override
    public boolean canBeReplaced() {
        return true;
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (!entity.hasEffect(Effect.FIRE_RESISTANCE)) {
            entity.attack(new EntityDamageByBlockEvent(this, entity, DamageCause.FIRE, 1));
        }

        EntityCombustByBlockEvent ev = new EntityCombustByBlockEvent(this, entity, 8);
        if (entity instanceof EntityArrow) {
            ev.setCancelled();
        }
        Server.getInstance().getPluginManager().callEvent(ev);
        if (!ev.isCancelled() && entity.isAlive() && entity.noDamageTicks == 0) {
            entity.setOnFire(ev.getDuration());
        }
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }


    @Override
    public int onUpdate(int type) {

        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_RANDOM) {
            Block down = down();

            if (type == Level.BLOCK_UPDATE_NORMAL) {
                int downId = down.getId();
                if (downId == Block.SOUL_SAND || downId == Block.SOUL_SOIL) {
                    this.getLevel().setBlock(this, getBlockState().withBlockId(BlockID.SOUL_FIRE).getBlock(this));
                    return type;
                }
            }

            if (!this.isBlockTopFacingSurfaceSolid(down) && !this.canNeighborBurn()) {
                BlockFadeEvent event = new BlockFadeEvent(this, get(AIR));
                level.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    level.setBlock(this, event.getNewState(), true);
                }
            } else if (this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK) && !level.isUpdateScheduled(this, this)) {
                level.scheduleUpdate(this, tickRate());
            }

            //在第一次放置时就检查下雨
            checkRain();

            return Level.BLOCK_UPDATE_NORMAL;
        } else if (type == Level.BLOCK_UPDATE_SCHEDULED && this.level.gameRules.getBoolean(GameRule.DO_FIRE_TICK)) {
            Block down = down();
            int downId = down.getId();

            ThreadLocalRandom random = ThreadLocalRandom.current();

            //TODO: END

            if (!this.isBlockTopFacingSurfaceSolid(down) && !this.canNeighborBurn()) {
                BlockFadeEvent event = new BlockFadeEvent(this, get(AIR));
                level.getServer().getPluginManager().callEvent(event);
                if (!event.isCancelled()) {
                    level.setBlock(this, event.getNewState(), true);
                }
            }

            boolean forever = downId == BlockID.NETHERRACK || downId == BlockID.MAGMA
                    || downId == BlockID.BEDROCK && ((BlockBedrock) down).getBurnIndefinitely();

            if (!checkRain()) {
                int meta = this.getDamage();

                if (meta < 15) {
                    int newMeta = meta + random.nextInt(3);
                    this.setDamage(Math.min(newMeta, 15));
                    this.getLevel().setBlock(this, this, true);
                }

                this.getLevel().scheduleUpdate(this, this.tickRate() + random.nextInt(10));

                if (!forever && !this.canNeighborBurn()) {
                    if (!this.isBlockTopFacingSurfaceSolid(down) || meta > 3) {
                        BlockFadeEvent event = new BlockFadeEvent(this, get(AIR));
                        level.getServer().getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            level.setBlock(this, event.getNewState(), true);
                        }
                    }
                } else if (!forever && !(down.getBurnAbility() > 0) && meta == 15 && random.nextInt(4) == 0) {
                    BlockFadeEvent event = new BlockFadeEvent(this, get(AIR));
                    level.getServer().getPluginManager().callEvent(event);
                    if (!event.isCancelled()) {
                        level.setBlock(this, event.getNewState(), true);
                    }
                } else {
                    int o = 0;

                    //TODO: decrease the o if the rainfall values are high

                    this.tryToCatchBlockOnFire(this.east(), 300 + o, meta);
                    this.tryToCatchBlockOnFire(this.west(), 300 + o, meta);
                    this.tryToCatchBlockOnFire(down, 250 + o, meta);
                    this.tryToCatchBlockOnFire(this.up(), 250 + o, meta);
                    this.tryToCatchBlockOnFire(this.south(), 300 + o, meta);
                    this.tryToCatchBlockOnFire(this.north(), 300 + o, meta);

                    for (int x = (int) (this.x - 1); x <= (int) (this.x + 1); ++x) {
                        for (int z = (int) (this.z - 1); z <= (int) (this.z + 1); ++z) {
                            for (int y = (int) (this.y - 1); y <= (int) (this.y + 4); ++y) {
                                if (x != (int) this.x || y != (int) this.y || z != (int) this.z) {
                                    int k = 100;

                                    if (y > this.y + 1) {
                                        k += (y - (this.y + 1)) * 100;
                                    }

                                    Block block = this.getLevel().getBlock(new Vector3(x, y, z));
                                    int chance = this.getChanceOfNeighborsEncouragingFire(block);

                                    if (chance > 0) {
                                        int t = (chance + 40 + this.getLevel().getServer().getDifficulty() * 7) / (meta + 30);

                                        //TODO: decrease the t if the rainfall values are high

                                        if (t > 0 && random.nextInt(k) <= t) {
                                            int damage = meta + random.nextInt(5) / 4;

                                            if (damage > 15) {
                                                damage = 15;
                                            }

                                            BlockIgniteEvent e = new BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.SPREAD);
                                            this.level.getServer().getPluginManager().callEvent(e);

                                            if (!e.isCancelled()) {
                                                this.getLevel().setBlock(block, Block.get(BlockID.FIRE, damage), true);
                                                this.getLevel().scheduleUpdate(block, this.tickRate());
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    private void tryToCatchBlockOnFire(Block block, int bound, int damage) {
        int burnAbility = block.getBurnAbility();

        Random random = ThreadLocalRandom.current();

        if (random.nextInt(bound) < burnAbility) {

            if (random.nextInt(damage + 10) < 5) {
                int meta = damage + random.nextInt(5) / 4;

                if (meta > 15) {
                    meta = 15;
                }

                BlockIgniteEvent e = new BlockIgniteEvent(block, this, null, BlockIgniteEvent.BlockIgniteCause.SPREAD);
                this.level.getServer().getPluginManager().callEvent(e);

                if (!e.isCancelled()) {
                    this.getLevel().setBlock(block, Block.get(BlockID.FIRE, meta), true);
                    this.getLevel().scheduleUpdate(block, this.tickRate());
                }
            } else {
                BlockBurnEvent ev = new BlockBurnEvent(block);
                this.getLevel().getServer().getPluginManager().callEvent(ev);

                if (!ev.isCancelled()) {
                    this.getLevel().setBlock(block, Block.get(BlockID.AIR), true);
                }
            }

            if (block instanceof BlockTNT) {
                ((BlockTNT) block).prime();
            }
        }
    }

    private int getChanceOfNeighborsEncouragingFire(Block block) {
        if (block.getId() != AIR) {
            return 0;
        } else {
            int chance = 0;
            chance = Math.max(chance, block.east().getBurnChance());
            chance = Math.max(chance, block.west().getBurnChance());
            chance = Math.max(chance, block.down().getBurnChance());
            chance = Math.max(chance, block.up().getBurnChance());
            chance = Math.max(chance, block.south().getBurnChance());
            chance = Math.max(chance, block.north().getBurnChance());
            return chance;
        }
    }

    public boolean canNeighborBurn() {
        for (BlockFace face : BlockFace.values()) {
            if (this.getSide(face).getBurnChance() > 0) {
                return true;
            }
        }

        return false;
    }

    public boolean isBlockTopFacingSurfaceSolid(Block block) {
        if (block != null) {
            if (block instanceof BlockStairs) {
                return false;
            } else if (block instanceof BlockSlab && !((BlockSlab) block).isOnTop()) {
                return false;
            } else if (block instanceof BlockSnowLayer) {
                return false;
            } else if (block instanceof BlockFenceGate) {
                return false;
            } else if (block instanceof BlockTrapdoor) {
                return false;
            } else if (block instanceof BlockMossCarpet) {
                return false;
            } else if (block instanceof BlockAzalea) {
                return false;
            } else return block.isSolid();
        }
        return false;
    }

    @Override
    public int tickRate() {
        return 30;
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return this;
    }

    @Override
    public Item toItem() {
        return new ItemBlock(Block.get(BlockID.AIR));
    }

    /**
     * 检查火焰是否应被雨水浇灭
     * @return 是否应被雨水浇灭
     */


    protected boolean checkRain() {
        var down = down();
        int downId = down.getId();
        boolean forever = downId == BlockID.NETHERRACK || downId == BlockID.MAGMA
                || downId == BlockID.BEDROCK && ((BlockBedrock) down).getBurnIndefinitely();

        if (!forever && this.getLevel().isRaining() &&
                (this.getLevel().canBlockSeeSky(this) ||
                        this.getLevel().canBlockSeeSky(this.east()) ||
                        this.getLevel().canBlockSeeSky(this.west()) ||
                        this.getLevel().canBlockSeeSky(this.south()) ||
                        this.getLevel().canBlockSeeSky(this.north()))
        ) {
            BlockFadeEvent event = new BlockFadeEvent(this, get(AIR));
            level.getServer().getPluginManager().callEvent(event);
            if (!event.isCancelled()) {
                level.setBlock(this, event.getNewState(), true);
            }
            return true;
        }
        return false;
    }
}
