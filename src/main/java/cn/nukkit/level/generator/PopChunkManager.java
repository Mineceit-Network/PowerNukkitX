package cn.nukkit.level.generator;

import cn.nukkit.level.format.generic.BaseFullChunk;

import java.util.Arrays;

public class PopChunkManager extends SimpleChunkManager {
    private final BaseFullChunk[] chunks = new BaseFullChunk[9];
    private boolean clean = true;
    private int CX = Integer.MAX_VALUE;
    private int CZ = Integer.MAX_VALUE;

    public PopChunkManager(long seed) {
        super(seed);
    }

    @Override
    public void cleanChunks(long seed) {
        super.cleanChunks(seed);
        if (!clean) {
            Arrays.fill(chunks, null);
            CX = Integer.MAX_VALUE;
            CZ = Integer.MAX_VALUE;
            clean = true;
        }
    }

    @Override
    public BaseFullChunk getChunk(int chunkX, int chunkZ) {
        int index;
        switch (chunkX - CX) {
            case 0:
                index = 0;
                break;
            case 1:
                index = 1;
                break;
            case 2:
                index = 2;
                break;
            default:
                return null;
        }
        switch (chunkZ - CZ) {
            case 0:
                break;
            case 1:
                index += 3;
                break;
            case 2:
                index += 6;
                break;
            default:
                return null;
        }
        return chunks[index];
    }

    @Override
    public void setChunk(int chunkX, int chunkZ, BaseFullChunk chunk) {
        if (CX == Integer.MAX_VALUE) {
            CX = chunkX;
            CZ = chunkZ;
        }
        int index;
        switch (chunkX - CX) {
            case 0:
                index = 0;
                break;
            case 1:
                index = 1;
                break;
            case 2:
                index = 2;
                break;
            default:
                throw new UnsupportedOperationException("Chunk is outside population area");
        }
        switch (chunkZ - CZ) {
            case 0:
                break;
            case 1:
                index += 3;
                break;
            case 2:
                index += 6;
                break;
            default:
                throw new UnsupportedOperationException("Chunk is outside population area");
        }
        clean = false;
        chunks[index] = chunk;
    }


    @Override
    public boolean isOverWorld() {
        for (var c : chunks) {
            if (c != null) {
                return c.isOverWorld();
            }
        }
        return false;
    }


    @Override
    public boolean isNether() {
        for (var c : chunks) {
            if (c != null) {
                return c.isNether();
            }
        }
        return false;
    }


    @Override
    public boolean isTheEnd() {
        for (var c : chunks) {
            if (c != null) {
                return c.isTheEnd();
            }
        }
        return false;
    }
}
