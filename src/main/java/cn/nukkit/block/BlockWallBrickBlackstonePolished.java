package cn.nukkit.block;


public class BlockWallBrickBlackstonePolished extends BlockWallBlackstonePolished {


    public BlockWallBrickBlackstonePolished() {
    }


    public BlockWallBrickBlackstonePolished(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return POLISHED_BLACKSTONE_BRICK_WALL;
    }

    @Override
    public String getName() {
        return "Polished Blackstone Brick Wall";
    }

    @Override
    public double getHardness() {
        return 1.5;
    }
}
