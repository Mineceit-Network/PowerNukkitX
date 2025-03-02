package cn.nukkit.block;

import cn.nukkit.item.Item;

//和pm源码有点出入，这里参考了wiki

/**
 * @author xtypr
 * @since 2015/12/6
 */

public class BlockOreRedstoneGlowing extends BlockOreRedstone implements IBlockOreRedstoneGlowing {

    public BlockOreRedstoneGlowing() {
    }

    @Override
    public String getName() {
        return "Glowing Redstone Ore";
    }

    @Override
    public int getId() {
        return LIT_REDSTONE_ORE;
    }

    @Override
    public int getLightLevel() {
        return 9;
    }

    @Override
    public Item toItem() {
        return IBlockOreRedstoneGlowing.super.toItem();
    }

    @Override
    public int onUpdate(int type) {
        return IBlockOreRedstoneGlowing.super.onUpdate(type);
    }
}
