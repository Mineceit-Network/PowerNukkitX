package cn.nukkit.level.format;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockState;
import cn.nukkit.level.format.palette.Palette;
import cn.nukkit.level.util.NibbleArray;
import cn.nukkit.math.BlockVector3;
import cn.nukkit.registry.BlockRegistry;
import io.netty.buffer.ByteBuf;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.StampedLock;
import java.util.function.BiPredicate;

import static cn.nukkit.level.format.IChunk.index;

/**
 * Allay Project 2023/5/30
 *
 * @author Cool_Loong
 */
@NotThreadSafe
public record ChunkSection(byte y,
                           Palette<BlockState>[] blockLayer,
                           Palette<Integer> biomes,
                           NibbleArray blockLights,
                           NibbleArray skyLights,
                           StampedLock lock) {
    public static final int SIZE = 16 * 16 * 16;
    public static final int LAYER_COUNT = 2;
    public static final int VERSION = 9;
    public ChunkSection(byte sectionY) {
        this(sectionY,
                new Palette[]{new Palette<>(BlockAir.PROPERTIES.getDefaultState()), new Palette<>(BlockAir.PROPERTIES.getDefaultState())},
                new Palette<>(1),
                new NibbleArray(SIZE),
                new NibbleArray(SIZE),
                new StampedLock());
    }

    public ChunkSection(byte sectionY, Palette<BlockState>[] blockLayer) {
        this(sectionY, blockLayer,
                new Palette<>(1),
                new NibbleArray(SIZE),
                new NibbleArray(SIZE),
                new StampedLock());
    }

    public BlockState getBlockState(int x, int y, int z) {
        return getBlockState(x, y, z, 0);
    }

    public BlockState getBlockState(int x, int y, int z, int layer) {
        long stamp = lock.tryOptimisticRead();
        try {
            for (; ; stamp = lock.readLock()) {
                if (stamp == 0L) continue;
                BlockState result = blockLayer[layer].get(index(x, y, z));
                if (!lock.validate(stamp)) continue;
                return result;
            }
        } finally {
            if (StampedLock.isReadLockStamp(stamp)) lock.unlockRead(stamp);
        }
    }

    public void setBlockState(int x, int y, int z, BlockState blockState, int layer) {
        long stamp = lock.writeLock();
        try {
            blockLayer[layer].set(index(x, y, z), blockState);
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public BlockState getAndSetBlockState(int x, int y, int z, BlockState blockstate, int layer) {
        long stamp = lock.writeLock();
        try {
            BlockState result = blockLayer[layer].get(index(x, y, z));
            blockLayer[layer].set(index(x, y, z), blockstate);
            return result;
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    public void setBiomeId(int x, int y, int z, int biomeId) {
        biomes.set(index(x, y, z), biomeId);
    }

    public int getBiomeId(int x, int y, int z) {
        return biomes.get(index(x, y, z));
    }

    public byte getBlockLight(int x, int y, int z) {
        return blockLights.get(index(x, y, z));
    }

    public byte getBlockSkyLight(int x, int y, int z) {
        return skyLights.get(index(x, y, z));
    }

    public void setBlockLight(int x, int y, int z, byte light) {
        blockLights.set(index(x, y, z), light);
    }

    public void setBlockSkyLight(int x, int y, int z, byte light) {
        skyLights.set(index(x, y, z), light);
    }

    public List<Block> scanBlocks(LevelProvider provider, int offsetX, int offsetZ, BlockVector3 min, BlockVector3 max, BiPredicate<BlockVector3, BlockState> condition) {
        long stamp = lock.writeLock();
        try {
            final List<Block> results = new ArrayList<>();
            final BlockVector3 current = new BlockVector3();
            int offsetY = y << 4;
            int minX = Math.max(0, min.x - offsetX);
            int minY = Math.max(0, min.y - offsetY);
            int minZ = Math.max(0, min.z - offsetZ);
            for (int x = Math.min(max.x - offsetX, 15); x >= minX; x--) {
                current.x = offsetX + x;
                for (int z = Math.min(max.z - offsetZ, 15); z >= minZ; z--) {
                    current.z = offsetZ + z;
                    for (int y = Math.min(max.y - offsetY, 15); y >= minY; y--) {
                        current.y = offsetY + y;
                        BlockState state = blockLayer[0].get(index(x, y, z));
                        if (condition.test(current, state)) {
                            current.y -= provider.getDimensionData().getMinHeight();
                            results.add(BlockRegistry.get(state, current.x, current.y, current.z, provider.getLevel()));
                            current.y += provider.getDimensionData().getMinHeight();
                        }
                    }
                }
            }
            return results;
        } finally {
            lock.unlock(stamp);
        }
    }

    public boolean isEmpty() {
        return blockLayer[0].isEmpty() && blockLayer[0].get(0) == BlockAir.PROPERTIES.getDefaultState();
    }

    public void writeToNetwork(ByteBuf byteBuf) {
        long stamp = lock.writeLock();
        try {
            byteBuf.writeByte(VERSION);
            //block layer count
            byteBuf.writeByte(LAYER_COUNT);
            byteBuf.writeByte(y & 0xFF);

            blockLayer[0].writeToNetwork(byteBuf, BlockState::blockStateHash);
            blockLayer[1].writeToNetwork(byteBuf, BlockState::blockStateHash);
        } finally {
            lock.unlockWrite(stamp);
        }
    }
}
