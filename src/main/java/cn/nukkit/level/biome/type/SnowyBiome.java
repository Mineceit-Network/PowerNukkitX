package cn.nukkit.level.biome.type;

import cn.nukkit.level.generator.populator.impl.WaterIcePopulator;

/**
 * @author DaPorkchop_ (Nukkit Project)
 */
public abstract class SnowyBiome extends GrassyBiome {
    public SnowyBiome() {
        super();

        WaterIcePopulator waterIce = new WaterIcePopulator();
        this.addPopulator(waterIce);
    }


    @Override
    public int getCoverBlock() {
        if (useNewRakNetCover()) {
            return getCoverId(0, 0);
        }
        return SNOW_LAYER;
    }

    @Override
    public boolean canRain() {
        return false;
    }
}
