package cn.nukkit.entity;

/**
 * 实体可剪切<p/>
 * 例如羊就可被剪羊毛<p/>
 * 若作用于此实体的物品的isShears()为true，则将会调用此方法
 */


public interface EntityShearable {
    /**
     * @return 此次操作是否有效。若有效，将会减少物品耐久
     */
    boolean shear();
}
