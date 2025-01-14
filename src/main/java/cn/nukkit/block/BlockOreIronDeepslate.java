package cn.nukkit.block;

/**
 * @author joserobjr
 * @since 2021-06-13
 */


public class BlockOreIronDeepslate extends BlockOreIron {


    public BlockOreIronDeepslate() {
        // Does nothing
    }

    @Override
    public int getId() {
        return DEEPSLATE_IRON_ORE;
    }

    @Override
    public double getHardness() {
        return 4.5;
    }

    @Override
    public String getName() {
        return "Deepslate Iron Ore";
    }

}
