package cn.nukkit.blockentity;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.api.DeprecationDetails;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockCauldron;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.BlockColor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

/**
 * @author CreeperFace (Nukkit Project)
 */
public class BlockEntityCauldron extends BlockEntitySpawnable {

    
    @Deprecated @DeprecationDetails(by = "PowerNukkit", since = "1.4.0.0-PN",
            reason = "Magic value", replaceWith = "PotionType")

    public static final int POTION_TYPE_EMPTY = -1;

    @Deprecated @DeprecationDetails(by = "PowerNukkit", since = "1.4.0.0-PN",
            reason = "Magic value", replaceWith = "PotionType")

    public static final int POTION_TYPE_NORMAL = 0;

    @Deprecated @DeprecationDetails(by = "PowerNukkit", since = "1.4.0.0-PN",
            reason = "Magic value", replaceWith = "PotionType")

    public static final int POTION_TYPE_SPLASH = 1;

    @Deprecated @DeprecationDetails(by = "PowerNukkit", since = "1.4.0.0-PN",
            reason = "Magic value", replaceWith = "PotionType")

    public static final int POTION_TYPE_LINGERING = 2;

    @Deprecated @DeprecationDetails(by = "PowerNukkit", since = "1.4.0.0-PN",
            reason = "Magic value", replaceWith = "PotionType")

    public static final int POTION_TYPE_LAVA = 0xF19B;

    public BlockEntityCauldron(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }


    @Override
    public void loadNBT() {
        super.loadNBT();
        int potionId;
        if (!namedTag.contains("PotionId")) {
            namedTag.putShort("PotionId", 0xffff);
        }
        potionId = namedTag.getShort("PotionId");

        int potionType = (potionId & 0xFFFF) == 0xFFFF? POTION_TYPE_EMPTY : POTION_TYPE_NORMAL;
        if (namedTag.getBoolean("SplashPotion")) {
            potionType = POTION_TYPE_SPLASH;
            namedTag.remove("SplashPotion");
        }

        if (!namedTag.contains("PotionType")) {
            namedTag.putShort("PotionType", potionType);
        }
    }

    @Override
    public void saveNBT() {
        super.saveNBT();
        namedTag.putShort("PotionId", getPotionId());
        int potionId = namedTag.getShort("PotionId");
        int potionType = (potionId & 0xFFFF) == 0xFFFF? POTION_TYPE_EMPTY : POTION_TYPE_NORMAL;
        namedTag.putShort("PotionType", potionType);
    }

    public int getPotionId() {
        return namedTag.getShort("PotionId");
    }

    public void setPotionId(int potionId) {
        namedTag.putShort("PotionId", potionId);
        this.spawnToAll();
    }

    public boolean hasPotion() {
        return (getPotionId() & 0xffff) != 0xffff;
    }


    public void setPotionType(int potionType) {
        this.namedTag.putShort("PotionType", (short)(potionType & 0xFFFF));
    }


    public int getPotionType() {
        return (short)(this.namedTag.getShort("PotionType") & 0xFFFF);
    }


    public PotionType getType() {
        return PotionType.getByTypeData(getPotionType());
    }


    public void setType(PotionType type) {
        setPotionType(type.potionTypeData);
    }

    public boolean isSplashPotion() {
        return namedTag.getShort("PotionType") == POTION_TYPE_SPLASH;
    }
    
    /**
     * @deprecated Use {@link #setPotionType(int)} instead.
     */
    @Deprecated
    public void setSplashPotion(boolean value) {
        namedTag.putShort("PotionType", value ? 1 : 0);
    }

    public BlockColor getCustomColor() {
        if (isCustomColor()) {
            int color = namedTag.getInt("CustomColor");

            int red = (color >> 16) & 0xff;
            int green = (color >> 8) & 0xff;
            int blue = (color) & 0xff;

            return new BlockColor(red, green, blue);
        }

        return null;
    }

    public boolean isCustomColor() {
        return namedTag.contains("CustomColor");
    }

    public void setCustomColor(BlockColor color) {
        setCustomColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setCustomColor(int r, int g, int b) {
        int color = (r << 16 | g << 8 | b) & 0xffffff;

        namedTag.putInt("CustomColor", color);

        spawnToAll();
    }

    public void clearCustomColor() {
        namedTag.remove("CustomColor");
        spawnToAll();
    }

    @Override
    public void spawnToAll() {
        if (!this.isBlockEntityValid()) {
            return;
        }
        BlockCauldron block = (BlockCauldron) getBlock();
        Player[] viewers = this.level.getChunkPlayers(getChunkX(), getChunkZ()).values().toArray(Player.EMPTY_ARRAY);
        this.level.sendBlocks(viewers, new Vector3[]{block});
        super.spawnToAll();
        Location location = getLocation();
        Server.getInstance().getScheduler().scheduleTask(null, ()-> {
            if (isValid()) {
                BlockEntity cauldron = this.level.getBlockEntity(location);
                if (cauldron == BlockEntityCauldron.this) {
                    this.level.sendBlocks(viewers, new Vector3[]{location});
                    super.spawnToAll();
                }
            }
        });
    }

    @Override
    public boolean isBlockEntityValid() {
        int id = getBlock().getId();
        return id == Block.CAULDRON_BLOCK || id == Block.LAVA_CAULDRON;
    }

    @Override
    public CompoundTag getSpawnCompound() {
        CompoundTag compoundTag = new CompoundTag()
                .putString("id", BlockEntity.CAULDRON)
                .putInt("x", (int) this.x)
                .putInt("y", (int) this.y)
                .putInt("z", (int) this.z)
                .putBoolean("isMovable", isMovable())
                .putList(new ListTag<>("Items"))
                .putShort("PotionId", (short) namedTag.getShort("PotionId"))
                .putShort("PotionType", (short) namedTag.getShort("PotionType"));
        if (namedTag.contains("CustomColor")) {
            compoundTag.putInt("CustomColor", namedTag.getInt("CustomColor") << 8 >> 8);
        }
        return compoundTag;
    }


    @RequiredArgsConstructor
    public enum PotionType {


        private final int potionTypeData;
        private static final Int2ObjectMap<PotionType> BY_DATA;
        static {
            PotionType[] types = values();
            BY_DATA = new Int2ObjectOpenHashMap<>(types.length);
            for (PotionType type : types) {
                BY_DATA.put(type.potionTypeData, type);
            }
        }


        @NotNull
        public static PotionType getByTypeData(int typeData) {
            return BY_DATA.getOrDefault(typeData, PotionType.UNKNOWN);
        }
    }
}
