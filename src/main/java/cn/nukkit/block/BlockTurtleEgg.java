package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.blockproperty.ArrayBlockProperty;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BlockProperty;
import cn.nukkit.blockproperty.value.CrackState;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.entity.mob.EntityGhast;
import cn.nukkit.entity.mob.EntityPhantom;
import cn.nukkit.entity.passive.EntityBat;
import cn.nukkit.entity.passive.EntityChicken;
import cn.nukkit.entity.passive.EntityTurtle;
import cn.nukkit.event.Event;
import cn.nukkit.event.block.BlockGrowEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.TurtleEggHatchEvent;
import cn.nukkit.event.entity.CreatureSpawnEvent;
import cn.nukkit.event.entity.EntityInteractEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.enchantment.Enchantment;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.*;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;


public class BlockTurtleEgg extends BlockFlowable {


    public static final BlockProperty<Integer> EGG_COUNT = new ArrayBlockProperty<>("turtle_egg_count", false,
            new Integer[]{1,2,3,4}, 2, "turtle_egg_count", false,
            new String[]{"one_egg", "two_egg", "three_egg", "four_egg"});


    public static final ArrayBlockProperty<CrackState> CRACK_STATE = new ArrayBlockProperty<>("cracked_state", false, CrackState.class);


    public static final BlockProperties PROPERTIES = new BlockProperties(EGG_COUNT, CRACK_STATE);

    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "New property system", replaceWith = "CrackState.NO_CRACKS")
    public static final int CRACK_STATE_NO_CRACKS = 0;
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "New property system", replaceWith = "CrackState.CRACKED")
    public static final int CRACK_STATE_CRACKED = 1;
    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "New property system", replaceWith = "CrackState.MAX_CRACKED")
    public static final int CRACK_STATE_MAX_CRACKED = 2;


    public BlockTurtleEgg() {
        this(0);
    }


    public BlockTurtleEgg(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return TURTLE_EGG;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }
    
    @Override
    public String getName() {
        return "Turtle Egg";
    }


    public CrackState getCracks() {
        return getPropertyValue(CRACK_STATE);
    }


    public void setCracks(@Nullable CrackState cracks) {
        setPropertyValue(CRACK_STATE, cracks);
    }


    @Override
    public double getHardness() {
        return 0.5;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }


    public int getEggCount() {
        return getPropertyValue(EGG_COUNT);
    }


    public void setEggCount(int eggCount) {
        setPropertyValue(EGG_COUNT, eggCount);
    }

    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Magic values", replaceWith = "getCracks()")

    public int getCrackState() {
        return Math.min(getDamage() >> 2 & 0b11, CRACK_STATE_MAX_CRACKED);
    }

    @Deprecated
    @DeprecationDetails(since = "1.4.0.0-PN", reason = "Magic values", replaceWith = "setCracks(CrackState)")

    public void setCrackState(int crackState) {
        crackState = MathHelper.clamp(crackState, 0, 2);
        setDamage(getDamage() & (DATA_MASK ^ 0b1100) | (crackState << 2));
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        if (item.getBlockItem() != null && item.getBlockId() == TURTLE_EGG && (player == null || !player.isSneaking())) {
            int eggCount = getEggCount();
            if (eggCount >= 4) {
                return false;
            }
            Block newState = getBlockState().withProperty(EGG_COUNT, eggCount + 1).getBlock(this);
            BlockPlaceEvent placeEvent = new BlockPlaceEvent(
                    player,
                    newState,
                    this,
                    down(),
                    item
            );
            if (placeEvent.isCancelled()) {
                return false;
            }
            if (!this.level.setBlock(this, placeEvent.getBlock(), true, true)) {
                return false;
            }
            Block placeBlock = placeEvent.getBlock();
            this.level.addLevelSoundEvent(this,
                    LevelSoundEventPacket.SOUND_PLACE,
                    placeBlock.getRuntimeId());
            item.setCount(item.getCount() - 1);

            if (down().getId() == SAND) {
                this.level.addParticle(new BoneMealParticle(this));
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean hasEntityCollision() {
        return true;
    }

    @Override
    public double getMinX() {
        return x + (3.0/16);
    }

    @Override
    public double getMinZ() {
        return z + (3.0/16);
    }

    @Override
    public double getMaxX() {
        return x + (12.0/16);
    }

    @Override
    public double getMaxZ() {
        return z + (12.0/16);
    }

    @Override
    public double getMaxY() {
        return y + (7.0/16);
    }

    @Override
    protected AxisAlignedBB recalculateCollisionBoundingBox() {
        return new SimpleAxisAlignedBB(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY() + 0.25, getMaxZ());
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_RANDOM) {
            if (down().getId() == BlockID.SAND) {
                float celestialAngle = level.calculateCelestialAngle(level.getTime(), 1);
                ThreadLocalRandom random = ThreadLocalRandom.current();
                if (0.70 > celestialAngle && celestialAngle > 0.65 || random.nextInt(500) == 0) {
                    CrackState crackState = getCracks();
                    if (crackState != CrackState.MAX_CRACKED) {
                        BlockTurtleEgg newState = clone();
                        newState.setCracks(crackState.getNext());
                        BlockGrowEvent event = new BlockGrowEvent(this, newState);
                        this.level.getServer().getPluginManager().callEvent(event);
                        if (!event.isCancelled()) {
                            level.addSound(this, Sound.BLOCK_TURTLE_EGG_CRACK, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                            this.level.setBlock(this, event.getNewState(), true, true);
                        }
                    } else {
                        hatch();
                    }
                }
            }
            return type;
        }
        return 0;
    }


    public void hatch() {
        hatch(getEggCount());
    }


    public void hatch(int eggs) {
        hatch(eggs, new BlockAir());
    }



    public void hatch(int eggs, Block newState) {
        TurtleEggHatchEvent turtleEggHatchEvent = new TurtleEggHatchEvent(this, eggs, newState);
        //TODO Cancelled by default because EntityTurtle doesn't have AI yet, remove it when AI is added
        turtleEggHatchEvent.setCancelled(true);
        this.level.getServer().getPluginManager().callEvent(turtleEggHatchEvent);
        int eggsHatching = turtleEggHatchEvent.getEggsHatching();
        if (!turtleEggHatchEvent.isCancelled()) {
            level.addSound(this, Sound.BLOCK_TURTLE_EGG_CRACK);

            boolean hasFailure = false;
            for (int i = 0; i < eggsHatching; i++) {

                this.level.addSound(this, Sound.BLOCK_TURTLE_EGG_CRACK);

                CreatureSpawnEvent creatureSpawnEvent = new CreatureSpawnEvent(
                        EntityTurtle.NETWORK_ID,
                        add(0.3 + i * 0.2,
                                0,
                                0.3
                        ),
                        CreatureSpawnEvent.SpawnReason.TURTLE_EGG);
                this.level.getServer().getPluginManager().callEvent(creatureSpawnEvent);

                if (!creatureSpawnEvent.isCancelled()) {
                    EntityTurtle turtle = (EntityTurtle) Entity.createEntity(
                            creatureSpawnEvent.getEntityNetworkId(),
                            creatureSpawnEvent.getPosition());
                    if (turtle != null) {
                        turtle.setBreedingAge(-24000);
                        turtle.setHomePos(new Vector3(x, y, z));
                        turtle.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_BABY, true);
                        turtle.setScale(0.16f);
                        turtle.spawnToAll();
                        continue;
                    }
                }

                if (turtleEggHatchEvent.isRecalculateOnFailure()) {
                    turtleEggHatchEvent.setEggsHatching(turtleEggHatchEvent.getEggsHatching() - 1);
                    hasFailure = true;
                }
            }

            if (hasFailure) {
                turtleEggHatchEvent.recalculateNewState();
            }

            this.level.setBlock(this, turtleEggHatchEvent.getNewState(), true, true);
        }
    }

    @Override
    public void onEntityCollide(Entity entity) {
        if (entity instanceof EntityLiving
                && !(entity instanceof EntityChicken)
                && !(entity instanceof EntityBat)
                && !(entity instanceof EntityGhast)
                && !(entity instanceof EntityPhantom)
                && entity.getY() >= this.getMaxY()) {
            Event ev;

            if (entity instanceof Player) {
                ev = new PlayerInteractEvent((Player) entity, null, this, null, PlayerInteractEvent.Action.PHYSICAL);
            } else {
                ev = new EntityInteractEvent(entity, this);
            }

            ev.setCancelled(ThreadLocalRandom.current().nextInt(200) > 0);
            this.level.getServer().getPluginManager().callEvent(ev);
            if (!ev.isCancelled()) {
                this.level.useBreakOn(this, null, null, true);
            }
        }
    }

    @Override
    public Item toItem() {
        return new ItemBlock(new BlockTurtleEgg());
    }

    @Override
    public boolean onBreak(Item item) {
        int eggCount = getEggCount();
        if (item.getEnchantment(Enchantment.ID_SILK_TOUCH) == null) {
            this.level.addSound(this, Sound.BLOCK_TURTLE_EGG_CRACK);
        }
        if (eggCount == 1) {
            return super.onBreak(item);
        } else {
            setEggCount(eggCount - 1);
            return this.level.setBlock(this, this, true, true);
        }
    }

    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, Player player) {
        if (!isValidSupport(block.down(1, 0))) {
            return false;
        }

        if (this.level.setBlock(this, this, true, true)) {
            if (down().getId() == BlockID.SAND) {
                this.level.addParticle(new BoneMealParticle(this));
            }
            return true;
        } else {
            return false;
        }
    }


    public boolean isValidSupport(Block support) {
        return support.isSolid(BlockFace.UP) || support instanceof BlockWallBase;
    }

    @Override
    public Item[] getDrops(Item item) {
        return Item.EMPTY_ARRAY;
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }

    @Override
    public boolean canBeFlowedInto() {
        return false;
    }

    @Override
    public BlockTurtleEgg clone() {
        return (BlockTurtleEgg) super.clone();
    }

}
