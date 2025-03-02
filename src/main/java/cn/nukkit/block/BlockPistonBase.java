package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityMovingBlock;
import cn.nukkit.blockentity.BlockEntityPistonArm;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.CommonBlockProperties;
import cn.nukkit.blockstate.BlockState;
import cn.nukkit.blockstate.BlockStateRegistry;
import cn.nukkit.event.block.BlockPistonEvent;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.level.Sound;
import cn.nukkit.level.vibration.VibrationEvent;
import cn.nukkit.level.vibration.VibrationType;
import cn.nukkit.math.BlockFace;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Faceable;
import cn.nukkit.utils.RedstoneComponent;
import com.google.common.collect.Lists;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author CreeperFace
 */


@Log4j2
public abstract class BlockPistonBase extends BlockTransparentMeta implements Faceable, RedstoneComponent, BlockEntityHolder<BlockEntityPistonArm> {


    public static final BlockProperties PROPERTIES = CommonBlockProperties.FACING_DIRECTION_BLOCK_PROPERTIES;

    public boolean sticky = false;

    public BlockPistonBase(int meta) {
        super(meta);
    }

    /**
     * @return 指定方块是否能向指定方向推动
     */
    public static boolean canPush(Block block, BlockFace face, boolean destroyBlocks, boolean extending) {
        var min = block.level.getMinHeight();
        var max = block.level.getMaxHeight() - 1;
        if (
                block.getY() >= min && (face != BlockFace.DOWN || block.getY() != min) &&
                        block.getY() <= max && (face != BlockFace.UP || block.getY() != max)
        ) {
            if (extending && !block.canBePushed() || !extending && !block.canBePulled())
                return false;
            if (block.breaksWhenMoved())
                return destroyBlocks || block.sticksToPiston();
            var blockEntity = block.getLevelBlockEntity();
            return blockEntity == null || blockEntity.isMovable();
        }

        return false;
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }


    @NotNull
    @Override
    public Class<? extends BlockEntityPistonArm> getBlockEntityClass() {
        return BlockEntityPistonArm.class;
    }


    @NotNull
    @Override
    public String getBlockEntityType() {
        return BlockEntity.PISTON_ARM;
    }

    @Override
    public double getResistance() {
        return 2.5;
    }

    @Override
    public double getHardness() {
        return 0.5;
    }


    @Override
    public int getWaterloggingLevel() {
        return 1;
    }

    @Override
    
    public boolean place(@NotNull Item item, @NotNull Block block, @NotNull Block target, @NotNull BlockFace face, double fx, double fy, double fz, @Nullable Player player) {
        if (player != null) {
            if (Math.abs(player.getFloorX() - this.x) <= 1 && Math.abs(player.getFloorZ() - this.z) <= 1) {
                double y = player.y + player.getEyeHeight();

                if (y - this.y > 2) {
                    this.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.UP);
                } else if (this.y - y > 0) {
                    this.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, BlockFace.DOWN);
                } else {
                    this.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, player.getHorizontalFacing());
                }
            } else {
                this.setPropertyValue(CommonBlockProperties.FACING_DIRECTION, player.getHorizontalFacing());
            }
        }
        this.level.setBlock(block, this, true, true);
        var nbt = BlockEntity.getDefaultCompound(this, BlockEntity.PISTON_ARM)
                .putInt("facing", this.getBlockFace().getIndex())
                .putBoolean("Sticky", this.sticky)
                .putBoolean("powered", isGettingPower());
        var piston = (BlockEntityPistonArm) BlockEntity.createBlockEntity(BlockEntity.PISTON_ARM, this.level.getChunk(getChunkX(), getChunkZ()), nbt);
        piston.powered = isGettingPower();
        this.checkState(piston.powered);
        return true;
    }

    @Override
    public boolean onBreak(Item item) {
        this.level.setBlock(this, Block.get(BlockID.AIR), true, true);
        var block = this.getSide(getBlockFace());
        if (block instanceof BlockPistonHead b && b.getBlockFace() == this.getBlockFace())
            block.onBreak(item);
        return true;
    }

    public boolean isExtended() {
        var face = getBlockFace();
        var block = getSide(face);
        return block instanceof BlockPistonHead b && b.getBlockFace() == face;
    }

    @Override
    
            "even if the piston can't move.", since = "1.4.0.0-PN")
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_REDSTONE || type == Level.BLOCK_UPDATE_MOVED) {
            if (!this.level.getServer().isRedstoneEnabled())
                return 0;
            level.scheduleUpdate(this, 1);
            return type;
        }
        if (type == Level.BLOCK_UPDATE_NORMAL || type == Level.BLOCK_UPDATE_SCHEDULED) {
            if (!this.level.getServer().isRedstoneEnabled())
                return 0;
            // We can't use getOrCreateBlockEntity(), because the update method is called on block place,
            // before the "real" BlockEntity is set. That means, if we'd use the other method here,
            // it would create two BlockEntities.
            var arm = this.getBlockEntity();
            if (arm == null) return 0;
            boolean powered = this.isGettingPower();
            this.updateAroundRedstoneTorches(powered);
            if (arm.state % 2 == 0 && arm.powered != powered && checkState(powered)) {
                arm.powered = powered;
                if (arm.chunk != null)
                    arm.chunk.setChanged();
                if (powered && !isExtended())
                    //推出未成功,下一个计划刻再次自检
                    //TODO: 这里可以记录阻挡的方块并在阻挡因素移除后同步更新到活塞，而不是使用计划刻
                    level.scheduleUpdate(this, 1);
                return type;
            }
            //上一次推出未成功
            if (type == Level.BLOCK_UPDATE_SCHEDULED && powered && !isExtended() && !checkState(true))
                //依然不成功，下一个计划刻继续自检
                level.scheduleUpdate(this, 1);
            return type;
        }
        return 0;
    }


    @Override
    public boolean isGettingPower() {
        var face = getBlockFace();
        for (var side : BlockFace.values()) {
            if (side == face)
                continue;
            var b = this.getSide(side);
            if (b.getId() == Block.REDSTONE_WIRE && b.getDamage() > 0)
                return true;
            if (this.level.isSidePowered(b, side))
                return true;
        }
        return false;
    }

    protected void updateAroundRedstoneTorches(boolean powered) {
        for (BlockFace side : BlockFace.values()) {
            if ((getSide(side) instanceof BlockRedstoneTorch && powered)
                    || (getSide(side) instanceof BlockRedstoneTorchUnlit && !powered)) {
                BlockTorch torch = (BlockTorch) getSide(side);

                BlockTorch.TorchAttachment torchAttachment = torch.getTorchAttachment();
                Block support = torch.getSide(torchAttachment.getAttachedFace());

                if (support.getLocation().equals(this.getLocation())) {
                    torch.onUpdate(Level.BLOCK_UPDATE_REDSTONE);
                }
            }
        }
    }

    protected boolean checkState(Boolean isPowered) {
        if (!this.level.getServer().isRedstoneEnabled())
            return false;
        if (isPowered == null)
            isPowered = this.isGettingPower();
        var face = getBlockFace();
        var block = getSide(face);
        boolean isExtended;
        if (block instanceof BlockPistonHead b) {
            if (b.getBlockFace() != face)
                return false;
            isExtended = true;
        } else isExtended = false;
        if (isPowered && !isExtended) {
            if (!this.doMove(true))
                return false;
            this.getLevel().addSound(this, Sound.TILE_PISTON_OUT);
            this.getLevel().getVibrationManager().callVibrationEvent(new VibrationEvent(this, this.add(0.5, 0.5, 0.5), VibrationType.PISTON_EXTEND));
            return true;
        } else if (!isPowered && isExtended) {
            if (!this.doMove(false))
                return false;
            this.getLevel().addSound(this, Sound.TILE_PISTON_IN);
            this.getLevel().getVibrationManager().callVibrationEvent(new VibrationEvent(this, this.add(0.5, 0.5, 0.5), VibrationType.PISTON_CONTRACT));
            return true;
        }
        return false;
    }

    protected boolean doMove(boolean extending) {
        var pistonFace = getBlockFace();
        var calculator = new BlocksCalculator(extending);
        boolean canMove = calculator.canMove();
        if (!canMove && extending)
            return false;
        List<BlockVector3> toMoveBlockVec = new ArrayList<>();
        var event = new BlockPistonEvent(this, pistonFace, calculator.getBlocksToMove(), calculator.getBlocksToDestroy(), extending);
        this.level.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled())
            return false;
        var oldPosList = new ArrayList<Vector3>();
        var blockEntityHolderList = new ArrayList<BlockEntityHolder<?>>();
        var nbtList = new ArrayList<CompoundTag>();
        if (canMove && (this.sticky || extending)) {
            var destroyBlocks = calculator.getBlocksToDestroy();
            //破坏需要破坏的方块
            for (int i = destroyBlocks.size() - 1; i >= 0; --i) {
                var block = destroyBlocks.get(i);
                //清除位置上所含的水等
                level.setBlock(block, 1, Block.get(BlockID.AIR), true, false);
                this.level.useBreakOn(block);
            }
            var blocksToMove = calculator.getBlocksToMove();
            toMoveBlockVec = blocksToMove.stream().map(Vector3::asBlockVector3).collect(Collectors.toList());
            var moveDirection = extending ? pistonFace : pistonFace.getOpposite();
            for (Block blockToMove : blocksToMove) {
                var oldPos = new Vector3(blockToMove.x, blockToMove.y, blockToMove.z);
                var newPos = blockToMove.getSidePos(moveDirection);
                //清除位置上所含的水等
                level.setBlock(newPos, 1, Block.get(AIR), true, false);
                //TODO: 使用Block-State Tag而不是id-meta
                //2023/2/8: NBTIO.getBlockHelper()有性能问题，先不换用
                CompoundTag movingBlockTag = /*NBTIO.putBlockHelper(blockToMove);*/new CompoundTag()
                        .putInt("id", blockToMove.getId()) //only for nukkit purpose
                        .putInt("meta", blockToMove.getDamage()) //only for nukkit purpose
                        .putString("name", BlockStateRegistry.getPersistenceName(blockToMove.getId()))
                        .putShort("val", blockToMove.getDamage());
                CompoundTag nbt = BlockEntity.getDefaultCompound(newPos, BlockEntity.MOVING_BLOCK)
                        .putBoolean("expanding", extending)
                        .putInt("pistonPosX", this.getFloorX())
                        .putInt("pistonPosY", this.getFloorY())
                        .putInt("pistonPosZ", this.getFloorZ())
                        .putCompound("movingBlock", movingBlockTag);
                var blockEntity = this.level.getBlockEntity(oldPos);
                //移动方块实体
                if (blockEntity != null && !(blockEntity instanceof BlockEntityMovingBlock)) {
                    blockEntity.saveNBT();
                    nbt.putCompound("movingEntity", new CompoundTag(blockEntity.namedTag.getTags()));
                    if (blockEntity instanceof InventoryHolder)
                        ((InventoryHolder) blockEntity).getInventory().clearAll();
                    blockEntity.close();
                }
                oldPosList.add(oldPos);
                blockEntityHolderList.add((BlockEntityHolder<?>) BlockState.of(BlockID.MOVING_BLOCK).getBlock(Position.fromObject(newPos, this.level)));
                nbtList.add(nbt);
            }
        }
        this.getBlockEntity().preMove(extending, toMoveBlockVec);
        //生成moving_block
        if (!oldPosList.isEmpty()) {
            for (int i = 0; i < oldPosList.size(); i++) {
                var oldPos = oldPosList.get(i);
                var blockEntityHolder = blockEntityHolderList.get(i);
                var nbt = nbtList.get(i);
                BlockEntityHolder.setBlockAndCreateEntity(blockEntityHolder, true, true, nbt);
                if (this.level.getBlock(oldPos).getId() != BlockID.MOVING_BLOCK)
                    this.level.setBlock(oldPos, Block.get(BlockID.AIR));
            }
        }
        //创建活塞臂方块
        if (extending) {
            var pistonArmPos = this.getSide(pistonFace);
            //清除位置上所含的水等
            level.setBlock(pistonArmPos, 1, Block.get(AIR), true, false);
            this.level.setBlock(pistonArmPos, createHead(this.getDamage()));
        }
        //开始移动
        this.getBlockEntity().move();
        return true;
    }


    protected BlockPistonHead createHead(int damage) {
        return (BlockPistonHead) Block.get(getPistonHeadBlockId(), damage);
    }


    public abstract int getPistonHeadBlockId();

    @Override
    public BlockFace getBlockFace() {
        var face = BlockFace.fromIndex(this.getDamage());
        return face.getHorizontalIndex() >= 0 ? face.getOpposite() : face;
    }

    @Override
    public boolean isSolid() {
        return false;
    }

    public class BlocksCalculator {

        private static int MOVE_BLOCK_LIMIT = 12;

        public static int getMoveBlockLimit() {
            return MOVE_BLOCK_LIMIT;
        }

        public static void setMoveBlockLimit(int moveBlockLimit) {
            if (moveBlockLimit < 0)
                throw new IllegalArgumentException("The move block limit must be greater than or equal to 0");
            MOVE_BLOCK_LIMIT = moveBlockLimit;
        }

        private final Vector3 pistonPos;
        private final Block blockToMove;
        private final BlockFace moveDirection;
        private final boolean extending;
        private final List<Block> toMove = new ArrayList<>() {
            @Override
            public int indexOf(Object o) {
                if (o == null) {
                    for (int i = 0; i < size(); i++)
                        if (get(i) == null)
                            return i;
                } else {
                    for (int i = 0; i < size(); i++) {
                        if (o.equals(get(i)))
                            return i;
                    }
                }
                return -1;
            }

            @Override //以防万一
            public boolean contains(Object o) {
                return indexOf(o) >= 0;
            }
        };
        private final List<Block> toDestroy = new ArrayList<>();
        private Vector3 armPos;

        public BlocksCalculator(boolean extending) {
            this.pistonPos = getLocation();
            this.extending = extending;

            BlockFace face = getBlockFace();
            if (!extending) {
                this.armPos = pistonPos.getSideVec(face);
            }

            if (extending) {
                this.moveDirection = face;
                this.blockToMove = getSide(face);
            } else {
                this.moveDirection = face.getOpposite();
                if (sticky) {
                    this.blockToMove = getSide(face, 2);
                } else {
                    this.blockToMove = null;
                }
            }
        }

        public boolean canMove() {
            if (!sticky && !extending)
                return true;
            this.toMove.clear();
            this.toDestroy.clear();
            var block = this.blockToMove;
            if (!canPush(block, this.moveDirection, true, extending))
                return false;
            if (block.breaksWhenMoved()) {
                if (extending || block.sticksToPiston())
                    this.toDestroy.add(this.blockToMove);
                return true;
            }
            if (!this.addBlockLine(this.blockToMove, this.blockToMove.getSide(this.moveDirection.getOpposite()), true))
                return false;
            for (int i = 0; i < this.toMove.size(); ++i) {
                var b = this.toMove.get(i);
                if (b.canSticksBlock() && !this.addBranchingBlocks(b)) {
                    return false;
                }
            }
            return true;
        }

        protected boolean addBlockLine(Block origin, Block from, boolean mainBlockLine) {
            var block = origin.clone();
            if (block.getId() == AIR)
                return true;
            if (!mainBlockLine && block.canSticksBlock() && from.canSticksBlock() && block.getId() != from.getId())
                return true;
            if (!canPush(origin, this.moveDirection, false, extending))
                return true;
            if (origin.equals(this.pistonPos))
                return true;
            if (this.toMove.contains(origin))
                return true;
            if (this.toMove.size() >= MOVE_BLOCK_LIMIT)
                return false;
            this.toMove.add(block);
            var count = 1;
            var beStuck = new ArrayList<Block>();
            while (block.canSticksBlock()) {
                var oldBlock = block.clone();
                block = origin.getSide(this.moveDirection.getOpposite(), count);
                if ((!extending || !mainBlockLine) && block.canSticksBlock() && oldBlock.canSticksBlock() && block.getId() != oldBlock.getId())
                    break;
                if (block.getId() == AIR || !canPush(block, this.moveDirection, false, extending) || block.equals(this.pistonPos))
                    break;
                if (block.breaksWhenMoved() && block.sticksToPiston()) {
                    this.toDestroy.add(block);
                    break;
                }
                if (count + this.toMove.size() > MOVE_BLOCK_LIMIT)
                    return false;
                count++;
                beStuck.add(block);
            }
            int beStuckCount = beStuck.size();
            if (beStuckCount > 0)
                this.toMove.addAll(Lists.reverse(beStuck));
            int step = 1;
            while (true) {
                var nextBlock = origin.getSide(this.moveDirection, step);
                int index = this.toMove.indexOf(nextBlock);
                if (index > -1) {
                    this.reorderListAtCollision(beStuckCount, index);
                    for (int i = 0; i <= index + beStuckCount; ++i) {
                        var b = this.toMove.get(i);
                        if ((b.canSticksBlock()) && !this.addBranchingBlocks(b))
                            return false;
                    }
                    return true;
                }
                if (nextBlock.getId() == AIR || nextBlock.equals(armPos))
                    return true;
                if (!canPush(nextBlock, this.moveDirection, true, extending) || nextBlock.equals(this.pistonPos))
                    return false;
                if (nextBlock.breaksWhenMoved()) {
                    this.toDestroy.add(nextBlock);
                    return true;
                }
                if (this.toMove.size() >= MOVE_BLOCK_LIMIT)
                    return false;
                this.toMove.add(nextBlock);
                ++beStuckCount;
                ++step;
            }
        }

        private void reorderListAtCollision(int count, int index) {
            var list = new ArrayList<>(this.toMove.subList(0, index));
            var list1 = new ArrayList<>(this.toMove.subList(this.toMove.size() - count, this.toMove.size()));
            var list2 = new ArrayList<>(this.toMove.subList(index, this.toMove.size() - count));
            this.toMove.clear();
            this.toMove.addAll(list);
            this.toMove.addAll(list1);
            this.toMove.addAll(list2);
        }

        protected boolean addBranchingBlocks(Block block) {
            for (BlockFace face : BlockFace.values()) {
                if (face.getAxis() != this.moveDirection.getAxis() && !this.addBlockLine(block.getSide(face), block, false))
                    return false;
            }
            return true;
        }

        public List<Block> getBlocksToMove() {
            return this.toMove;
        }

        public List<Block> getBlocksToDestroy() {
            return this.toDestroy;
        }
    }
}
