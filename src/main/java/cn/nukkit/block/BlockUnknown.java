package cn.nukkit.block;

import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.UnsignedIntBlockProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public class BlockUnknown extends BlockMeta {


    public static final UnsignedIntBlockProperty UNKNOWN = new UnsignedIntBlockProperty("nukkit-unknown", true, 0xFFFFFFFF);


    public static final BlockProperties PROPERTIES = new BlockProperties(UNKNOWN);

    private final int id;

    public BlockUnknown(int id) {
        this(id, 0);
    }

    public BlockUnknown(int id, Integer meta) {
        super(0);
        this.id = id;
        if (meta != null && meta != 0) {
            getMutableState().setDataStorageFromInt(meta, false);
        }
    }


    public BlockUnknown(int id, Number meta) {
        super(0);
        this.id = id;
        if (meta != null) {
            getMutableState().setDataStorage(meta, false);
        }
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return "Unknown";
    }
}
