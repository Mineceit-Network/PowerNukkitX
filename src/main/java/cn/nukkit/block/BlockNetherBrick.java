package cn.nukkit.block;

import cn.nukkit.api.DeprecationDetails;

/**
 * @author xtypr
 * @since 2015/12/7
 */
@Deprecated
@DeprecationDetails(since = "1.5.1.0-PN", by = "PowerNukkit", 
        reason = "Duplicated of BlockBricksNether and the other one is used instead of this one.",
        replaceWith = "BlockBricksNether"
)

@SuppressWarnings({"DeprecatedIsStillUsed", "java:S1133"})
public class BlockNetherBrick extends BlockBricksNether {
    @Deprecated
    @DeprecationDetails(since = "1.5.1.0-PN", by = "PowerNukkit",
            reason = "Duplicated of BlockBricksNether and the other one is used instead of this one.",
            replaceWith = "BlockBricksNether"
    )
    public BlockNetherBrick() {
        // Does nothing
    }
}
