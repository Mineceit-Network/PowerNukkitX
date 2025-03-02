package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityItemFrame;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.BooleanBlockProperty;
import cn.nukkit.event.block.ItemFrameUseEvent;
import cn.nukkit.event.player.PlayerInteractEvent.Action;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemID;
import cn.nukkit.item.ItemItemFrame;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.math.AxisAlignedBB;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.Tag;
import cn.nukkit.network.protocol.LevelEventPacket;
import cn.nukkit.utils.Faceable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.concurrent.ThreadLocalRandom;

import static cn.nukkit.blockproperty.CommonBlockProperties.FACING_DIRECTION;
import static cn.nukkit.math.BlockFace.AxisDirection.POSITIVE;

/**
 * @author Pub4Game
 * @since 03.07.2016
 */

public class BlockItemFrame extends BlockTransparentMeta implements BlockEntityHolder<BlockEntityItemFrame>, Faceable {


    public static final BooleanBlockProperty HAS_MAP = new BooleanBlockProperty("item_frame_map_bit", false);


    public static final BooleanBlockProperty HAS_PHOTO = new BooleanBlockProperty("item_frame_photo_bit", false);


    public static final BlockProperties PROPERTIES = new BlockProperties(FACING_DIRECTION, HAS_MAP, HAS_PHOTO);

    public BlockItemFrame() {
        this(0);
    }

    public BlockItemFrame(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return ITEM_FRAME_BLOCK;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }


    @NotNull
    @Override
    public BlockFace getBlockFace() {
        return getPropertyValue(FACING_DIRECTION);
    }


    @Override
    public void setBlockFace(@NotNull BlockFace face) {
        setPropertyValue(FACING_DIRECTION, face);
    }


    public boolean isStoringMap() {
        return getBooleanValue(HAS_MAP);
    }


    public void setStoringMap(boolean map) {
        setBooleanValue(HAS_MAP, map);
    }


    public boolean isStoringPhoto() {
        return getBooleanValue(HAS_PHOTO);
    }


    public void setStoringPhoto(boolean hasPhoto) {
        setBooleanValue(HAS_PHOTO, hasPhoto);
    }


    @NotNull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.ITEM_FRAME;
    }


    @NotNull
    @Override
    public Class<? extends BlockEntityItemFrame> getBlockEntityClass() {
        return BlockEntityItemFrame.class;
    }

    @Override
    public String getName() {
        return "Item Frame";
    }


    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_NORMAL) {
            Block support = this.getSideAtLayer(0, getFacing().getOpposite());
            if (!support.isSolid() && support.getId() != COBBLE_WALL) {
                this.level.useBreakOn(this);
                return type;
            }
        }

        return 0;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }


    @Override
    public int onTouch(@Nullable Player player, Action action) {
        onUpdate(Level.BLOCK_UPDATE_TOUCH);
        if (player != null && action == Action.LEFT_CLICK_BLOCK) {
            return getOrCreateBlockEntity().dropItem(player) ? 1 : 0;
        }
        return 0;
    }

    @Override
    public boolean onActivate(@NotNull Item item, Player player) {
        BlockEntityItemFrame itemFrame = getOrCreateBlockEntity();
        if (itemFrame.getItem().isNull()) {
            Item itemOnFrame = item.clone();
            ItemFrameUseEvent event = new ItemFrameUseEvent(player, this, itemFrame, itemOnFrame, ItemFrameUseEvent.Action.PUT);
            this.getLevel().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return false;
            if (player != null && !player.isCreative()) {
                itemOnFrame.setCount(itemOnFrame.getCount() - 1);
                player.getInventory().setItemInHand(itemOnFrame);
            }
            itemOnFrame.setCount(1);
            itemFrame.setItem(itemOnFrame);
            if (itemOnFrame.getId() == ItemID.MAP) {
                setStoringMap(true);
                this.getLevel().setBlock(this, this, true);
            }
            this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ADDED);
        } else {
            ItemFrameUseEvent event = new ItemFrameUseEvent(player, this, itemFrame, null, ItemFrameUseEvent.Action.ROTATION);
            this.getLevel().getServer().getPluginManager().callEvent(event);
            if (event.isCancelled()) return false;
            itemFrame.setItemRotation((itemFrame.getItemRotation() + 1) % 8);
            if (isStoringMap()) {
                setStoringMap(false);
                this.getLevel().setBlock(this, this, true);
            }
            this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_ITEM_ROTATED);
        }
        return true;
    }


    @Override
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if ((!(target.isSolid() || target instanceof BlockWall) && !target.equals(block) || (block.isSolid() && !block.canBeReplaced()))) {
            return false;
        }

        if (target.equals(block) && block.canBeReplaced()) {
            face = BlockFace.UP;
            target = block.down();
            if (!target.isSolid() && !(target instanceof BlockWall)) {
                return false;
            }
        }

        setBlockFace(face);
        setStoringMap(item.getId() == ItemID.MAP);
        CompoundTag nbt = new CompoundTag()
                .putByte("ItemRotation", 0)
                .putFloat("ItemDropChance", 1.0f);
        if (item.hasCustomBlockData()) {
            for (Tag aTag : item.getCustomBlockData().getAllTags()) {
                nbt.put(aTag.getName(), aTag);
            }
        }
        BlockEntityItemFrame frame = BlockEntityHolder.setBlockAndCreateEntity(this, true, true, nbt);
        if (frame == null) {
            return false;
        }

        this.getLevel().addSound(this, Sound.BLOCK_ITEMFRAME_PLACE);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.getLevel().setBlock(this, layer, Block.get(BlockID.AIR), true, true);
        this.getLevel().addLevelEvent(this, LevelEventPacket.EVENT_SOUND_ITEM_FRAME_REMOVED);
        return true;
    }

    @Override
    public Item[] getDrops(Item item) {
        BlockEntityItemFrame itemFrame = getBlockEntity();
        if (itemFrame != null && ThreadLocalRandom.current().nextFloat() <= itemFrame.getItemDropChance()) {
            return new Item[]{
                    toItem(), itemFrame.getItem().clone()
            };
        } else {
            return new Item[]{
                    toItem()
            };
        }
    }

    @Override
    public Item toItem() {
        return new ItemItemFrame();
    }

    @Override
    public boolean canPassThrough() {
        return true;
    }

    @Override
    public boolean hasComparatorInputOverride() {
        return true;
    }

    @Override
    public int getComparatorInputOverride() {
        BlockEntityItemFrame blockEntity = getBlockEntity();

        if (blockEntity != null) {
            return blockEntity.getAnalogOutput();
        }

        return super.getComparatorInputOverride();
    }

    public BlockFace getFacing() {
        return getBlockFace();
    }

    @Override
    public double getHardness() {
        return 0.25;
    }

    @Override

    public boolean breaksWhenMoved() {
        return true;
    }

    @Override

    public boolean sticksToPiston() {
        return false;
    }


    @Override
    protected AxisAlignedBB recalculateBoundingBox() {
        double[][] aabb = {
                {2.0 / 16, 14.0 / 16},
                {2.0 / 16, 14.0 / 16},
                {2.0 / 16, 14.0 / 16}
        };

        BlockFace facing = getFacing();
        if (facing.getAxisDirection() == POSITIVE) {
            int axis = facing.getAxis().ordinal();
            aabb[axis][0] = 0;
            aabb[axis][1] = 1.0 / 16;
        }

        return new SimpleAxisAlignedBB(
                aabb[0][0] + x, aabb[1][0] + y, aabb[2][0] + z,
                aabb[0][1] + x, aabb[1][1] + y, aabb[2][1] + z
        );
    }
}
