package cn.nukkit.level.biome.type;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public abstract class SandyBiome extends CoveredBiome {


    @Override
    public int getSurfaceDepth(int y) {
        if (useNewRakNetSurfaceDepth()) {
            return getSurfaceDepth(0, y, 0);
        }
        return 3;
    }


    @Override
    public int getSurfaceBlock(int y) {
        if (useNewRakNetSurface()) {
            return getSurfaceId(0, y, 0) >> 4;
        }
        return SAND;
    }


    @Override
    public int getGroundDepth(int y) {
        if (useNewRakNetGroundDepth()) {
            return getGroundDepth(0, y, 0);
        }
        return 2;
    }


    @Override
    public int getGroundBlock(int y) {
        if (useNewRakNetGround()) {
            return getGroundId(0, y, 0) >> 4;
        }
        return SANDSTONE;
    }
}
