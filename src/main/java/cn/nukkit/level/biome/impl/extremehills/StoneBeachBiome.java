package cn.nukkit.level.biome.impl.extremehills;

import cn.nukkit.level.biome.type.CoveredBiome;

/**
 * @author DaPorkchop_
 * <p>
 * Occurs when Extreme hills and variants touch the ocean.
 * <p>
 * Nearly ertical cliffs, but no overhangs. Height difference is 2-7 near ocean, and pretty much flat everywhere else
 */
public class StoneBeachBiome extends CoveredBiome {
    public StoneBeachBiome() {
        this.setBaseHeight(0.1f);
        this.setHeightVariation(0.8f);
    }


    @Override
    public int getSurfaceDepth(int y) {
        if (useNewRakNetSurfaceDepth()) {
            return getSurfaceDepth(0, y, 0);
        }
        return 0;
    }


    @Override
    public int getSurfaceBlock(int y) {
        if (useNewRakNetSurface()) {
            return getSurfaceId(0, y, 0) >> 4;
        }
        return 0;
    }


    @Override
    public int getGroundDepth(int y) {
        if (useNewRakNetGroundDepth()) {
            return getGroundDepth(0, y, 0);
        }
        return 0;
    }


    @Override
    public int getGroundBlock(int y) {
        if (useNewRakNetGround()) {
            return getGroundId(0, y, 0) >> 4;
        }
        return 0;
    }

    @Override
    public String getName() {
        return "Stone Beach";
    }
}
