package cn.nukkit.block;

public class BlockFloweringAzalea extends BlockAzalea{


    public BlockFloweringAzalea() {
        this(0);
    }


    public BlockFloweringAzalea(int meta) {
        super(meta);
    }


    @Override
    public String getName() {
        return "FloweringAzalea";
    }

    @Override
    public int getId() {
        return FLOWERING_AZALEA;
    }
}
