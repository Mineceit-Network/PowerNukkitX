package cn.nukkit.blockentity;

import cn.nukkit.Server;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.format.IChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.ChunkException;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.extern.log4j.Log4j2;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MagicDroidX
 */
@Log4j2
public abstract class BlockEntity extends Position {
    //WARNING: DO NOT CHANGE ANY NAME HERE, OR THE CLIENT WILL CRASH
    public static final String CHEST = "Chest";
    public static final String ENDER_CHEST = "EnderChest";
    public static final String FURNACE = "Furnace";
    public static final String BLAST_FURNACE = "BlastFurnace";

    public static final String SMOKER = "Smoker";
    public static final String SIGN = "Sign";
    public static final String HANGING_SIGN = "HangingSign";
    public static final String MOB_SPAWNER = "MobSpawner";
    public static final String ENCHANT_TABLE = "EnchantTable";
    public static final String SKULL = "Skull";
    public static final String FLOWER_POT = "FlowerPot";
    public static final String BREWING_STAND = "BrewingStand";
    public static final String DAYLIGHT_DETECTOR = "DaylightDetector";
    public static final String MUSIC = "Music";
    public static final String ITEM_FRAME = "ItemFrame";


    public static final String GLOW_ITEM_FRAME = "GlowItemFrame";
    public static final String CAULDRON = "Cauldron";
    public static final String BEACON = "Beacon";
    public static final String PISTON_ARM = "PistonArm";
    public static final String MOVING_BLOCK = "MovingBlock";
    public static final String COMPARATOR = "Comparator";
    public static final String HOPPER = "Hopper";
    public static final String BED = "Bed";
    public static final String JUKEBOX = "Jukebox";
    public static final String SHULKER_BOX = "ShulkerBox";
    public static final String BANNER = "Banner";
    public static final String LECTERN = "Lectern";
    public static final String BEEHIVE = "Beehive";
    public static final String CONDUIT = "Conduit";
    public static final String BARREL = "Barrel";
    public static final String CAMPFIRE = "Campfire";
    public static final String BELL = "Bell";
    public static final String DISPENSER = "Dispenser";
    public static final String DROPPER = "Dropper";










    public static final String SCULK_SHRIEKER = "SculkShrieker";


    public static final String STRUCTURE_BLOCK = "StructureBlock";


    public static final String CHISELED_BOOKSHELF = "ChiseledBookshelf";


    public static final String DECORATED_POT = "DecoratedPot";

    public static long count = 1;

    private static final BiMap<String, Class<? extends BlockEntity>> knownBlockEntities = HashBiMap.create(35);

    public IChunk chunk;
    public String name;
    public long id;

    public boolean movable;

    public boolean closed = false;
    public CompoundTag namedTag;
    @Deprecated @DeprecationDetails(since = "1.3.1.2-PN", reason = "Not necessary and causes slowdown")
    
    protected long lastUpdate;
    protected Server server;

    public BlockEntity(FullChunk chunk, CompoundTag nbt) {
        if (chunk == null || chunk.getProvider() == null) {
            throw new ChunkException("Invalid garbage Chunk given to Block Entity");
        }

        this.server = chunk.getProvider().getLevel().getServer();
        this.chunk = chunk;
        this.setLevel(chunk.getProvider().getLevel());
        this.namedTag = nbt;
        this.name = "";
        this.id = BlockEntity.count++;
        this.x = this.namedTag.getInt("x");
        this.y = this.namedTag.getInt("y");
        this.z = this.namedTag.getInt("z");

        if (namedTag.contains("isMovable")) {
            this.movable = this.namedTag.getBoolean("isMovable");
        } else {
            this.movable = true;
            namedTag.putBoolean("isMovable", true);
        }

        this.initBlockEntity();
        
        if (closed) {
            throw new IllegalStateException("Could not create the entity "+getClass().getName()+", the initializer closed it on construction.");
        }
        
        this.chunk.addBlockEntity(this);
        this.getLevel().addBlockEntity(this);
    }

    
    public static void init() {
        registerBlockEntity(FURNACE, BlockEntityFurnace.class);
        registerBlockEntity(CHEST, BlockEntityChest.class);
        registerBlockEntity(SIGN, BlockEntitySign.class);
        registerBlockEntity(ENCHANT_TABLE, BlockEntityEnchantTable.class);
        registerBlockEntity(SKULL, BlockEntitySkull.class);
        registerBlockEntity(FLOWER_POT, BlockEntityFlowerPot.class);
        registerBlockEntity(BREWING_STAND, BlockEntityBrewingStand.class);
        registerBlockEntity(ITEM_FRAME, BlockEntityItemFrame.class);
        registerBlockEntity(CAULDRON, BlockEntityCauldron.class);
        registerBlockEntity(ENDER_CHEST, BlockEntityEnderChest.class);
        registerBlockEntity(BEACON, BlockEntityBeacon.class);
        registerBlockEntity(PISTON_ARM, BlockEntityPistonArm.class);
        registerBlockEntity(COMPARATOR, BlockEntityComparator.class);
        registerBlockEntity(HOPPER, BlockEntityHopper.class);
        registerBlockEntity(BED, BlockEntityBed.class);
        registerBlockEntity(JUKEBOX, BlockEntityJukebox.class);
        registerBlockEntity(SHULKER_BOX, BlockEntityShulkerBox.class);
        registerBlockEntity(BANNER, BlockEntityBanner.class);
        registerBlockEntity(MUSIC, BlockEntityMusic.class);
        registerBlockEntity(LECTERN, BlockEntityLectern.class);
        registerBlockEntity(BLAST_FURNACE, BlockEntityBlastFurnace.class);
        registerBlockEntity(SMOKER, BlockEntitySmoker.class);
        registerBlockEntity(BEEHIVE, BlockEntityBeehive.class);
        registerBlockEntity(CONDUIT, BlockEntityConduit.class);
        registerBlockEntity(BARREL, BlockEntityBarrel.class);
        registerBlockEntity(CAMPFIRE, BlockEntityCampfire.class);
        registerBlockEntity(BELL, BlockEntityBell.class);
        registerBlockEntity(DAYLIGHT_DETECTOR, BlockEntityDaylightDetector.class);
        registerBlockEntity(DISPENSER, BlockEntityDispenser.class);
        registerBlockEntity(DROPPER, BlockEntityDropper.class);
        registerBlockEntity(MOVING_BLOCK, BlockEntityMovingBlock.class);
        registerBlockEntity(NETHER_REACTOR, BlockEntityNetherReactor.class);
        registerBlockEntity(LODESTONE, BlockEntityLodestone.class);
        registerBlockEntity(TARGET, BlockEntityTarget.class);
        registerBlockEntity(END_PORTAL, BlockEntityEndPortal.class);
        registerBlockEntity(END_GATEWAY, BlockEntityEndGateway.class);
        //powernukkitx only
        registerBlockEntity(COMMAND_BLOCK, BlockEntityCommandBlock.class);
        registerBlockEntity(SCULK_SENSOR, BlockEntitySculkSensor.class);
        registerBlockEntity(SCULK_CATALYST, BlockEntitySculkCatalyst.class);
        registerBlockEntity(SCULK_SHRIEKER, BlockEntitySculkShrieker.class);
        registerBlockEntity(STRUCTURE_BLOCK, BlockEntityStructBlock.class);
        registerBlockEntity(GLOW_ITEM_FRAME, BlockEntityGlowItemFrame.class);
        registerBlockEntity(HANGING_SIGN, BlockEntityHangingSign.class);
        registerBlockEntity(CHISELED_BOOKSHELF, BlockEntityChiseledBookshelf.class);
        registerBlockEntity(DECORATED_POT, BlockEntityDecoratedPot.class);
    }

    protected void initBlockEntity() {
        loadNBT();
    }


    public static BlockEntity createBlockEntity(String type, Position position, Object... args) {
        return createBlockEntity(type, position, BlockEntity.getDefaultCompound(position, type), args);
    }


    public static BlockEntity createBlockEntity(String type, Position pos, CompoundTag nbt, Object... args) {
        return createBlockEntity(type, pos.getLevel().getChunk(pos.getFloorX() >> 4, pos.getFloorZ() >> 4), nbt, args);
    }

    public static BlockEntity createBlockEntity(String type, IChunk chunk, CompoundTag nbt, Object... args) {
        BlockEntity blockEntity = null;

        Class<? extends BlockEntity> clazz = knownBlockEntities.get(type);
        if (clazz != null) {
            List<Exception> exceptions = null;

            for (Constructor constructor : clazz.getConstructors()) {
                if (blockEntity != null) {
                    break;
                }

                if (constructor.getParameterCount() != (args == null ? 2 : args.length + 2)) {
                    continue;
                }

                try {
                    if (args == null || args.length == 0) {
                        blockEntity = (BlockEntity) constructor.newInstance(chunk, nbt);
                    } else {
                        Object[] objects = new Object[args.length + 2];

                        objects[0] = chunk;
                        objects[1] = nbt;
                        System.arraycopy(args, 0, objects, 2, args.length);
                        blockEntity = (BlockEntity) constructor.newInstance(objects);

                    }
                } catch (Exception e) {
                    if (exceptions == null) {
                        exceptions = new ArrayList<>();
                    }
                    exceptions.add(e);
                }

            }
            if (blockEntity == null) {
                Exception cause = new IllegalArgumentException("Could not create a block entity of type "+type, exceptions != null && exceptions.size() > 0? exceptions.get(0) : null);
                if (exceptions != null && exceptions.size() > 1) {
                    for (int i = 1; i < exceptions.size(); i++) {
                        cause.addSuppressed(exceptions.get(i));
                    }
                }
                log.error("Could not create a block entity of type {} with {} args", type, args == null? 0 : args.length, cause);
            }
        } else {
            log.debug("Block entity type {} is unknown", type);
        }


        return blockEntity;
    }

    public static boolean registerBlockEntity(String name, Class<? extends BlockEntity> c) {
        if (c == null) {
            return false;
        }

        knownBlockEntities.put(name, c);
        return true;
    }

    public final String getSaveId() {
        return knownBlockEntities.inverse().get(getClass());
    }

    public long getId() {
        return id;
    }

    /**
     * 存储方块实体数据到namedtag
     */
    public void saveNBT() {
        this.namedTag.putString("id", this.getSaveId());
        this.namedTag.putInt("x", (int) this.getX());
        this.namedTag.putInt("y", (int) this.getY());
        this.namedTag.putInt("z", (int) this.getZ());
        this.namedTag.putBoolean("isMovable", this.movable);
    }

    /**
     * 从方块实体的namedtag中读取数据
     */


    public void loadNBT() {}

    public CompoundTag getCleanedNBT() {
        this.saveNBT();
        CompoundTag tag = this.namedTag.clone();
        tag.remove("x").remove("y").remove("z").remove("id");
        if (tag.getTags().size() > 0) {
            return tag;
        } else {
            return null;
        }
    }

    public Block getBlock() {
        return this.getLevelBlock();
    }

    public abstract boolean isBlockEntityValid();

    public boolean onUpdate() {
        return false;
    }

    public final void scheduleUpdate() {
        this.level.scheduleBlockEntityUpdate(this);
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            if (this.chunk != null) {
                this.chunk.removeBlockEntity(this);
            }
            if (this.level != null) {
                this.level.removeBlockEntity(this);
            }
            this.level = null;
        }
    }

    public void onBreak() {

    }


    public void onBreak(boolean isSilkTouch) {
        onBreak();
    }

    public void setDirty() {
        chunk.setChanged();

        if (this.getLevelBlock().getId() != BlockID.AIR) {
            getLevel().getServer().getScheduler().scheduleTask(new Task() {
                @Override
                public void onRun(int currentTick) {
                    if (isValid() && isBlockEntityValid()) {
                        getLevel().updateComparatorOutputLevelSelective(BlockEntity.this, isObservable());
                    }
                }
            });
        }
    }

    /**
     * Indicates if an observer blocks that are looking at this block should blink when {@link #setDirty()} is called.
     */


    public boolean isObservable() {
        return true;
    }

    public String getName() {
        return name;
    }

    public boolean isMovable() {
        return movable;
    }

    public static CompoundTag getDefaultCompound(Vector3 pos, String id) {
        return new CompoundTag()
                .putString("id", id)
                .putInt("x", pos.getFloorX())
                .putInt("y", pos.getFloorY())
                .putInt("z", pos.getFloorZ());
    }


    @Nullable
    @Override
    public final BlockEntity getLevelBlockEntity() {
        return super.getLevelBlockEntity();
    }
}
