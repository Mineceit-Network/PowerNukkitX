package cn.nukkit.item;

import cn.nukkit.Player;
import cn.nukkit.event.player.PlayerItemConsumeEvent;
import cn.nukkit.level.vibration.VibrationEvent;
import cn.nukkit.level.vibration.VibrationType;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Potion;
import cn.nukkit.utils.ServerException;

import javax.annotation.Nullable;
import java.util.Objects;

public class ItemPotion extends Item {
    public static final int NO_EFFECTS = 0;
    public static final int MUNDANE = 1;
    public static final int MUNDANE_II = 2;
    public static final int THICK = 3;
    public static final int AWKWARD = 4;
    public static final int NIGHT_VISION = 5;
    public static final int NIGHT_VISION_LONG = 6;
    public static final int INVISIBLE = 7;
    public static final int INVISIBLE_LONG = 8;
    public static final int LEAPING = 9;
    public static final int LEAPING_LONG = 10;
    public static final int LEAPING_II = 11;
    public static final int FIRE_RESISTANCE = 12;
    public static final int FIRE_RESISTANCE_LONG = 13;
    public static final int SPEED = 14;
    public static final int SPEED_LONG = 15;
    public static final int SPEED_II = 16;
    public static final int SLOWNESS = 17;
    public static final int SLOWNESS_LONG = 18;
    public static final int WATER_BREATHING = 19;
    public static final int WATER_BREATHING_LONG = 20;
    public static final int INSTANT_HEALTH = 21;
    public static final int INSTANT_HEALTH_II = 22;
    public static final int HARMING = 23;
    public static final int HARMING_II = 24;
    public static final int POISON = 25;
    public static final int POISON_LONG = 26;
    public static final int POISON_II = 27;
    public static final int REGENERATION = 28;
    public static final int REGENERATION_LONG = 29;
    public static final int REGENERATION_II = 30;
    public static final int STRENGTH = 31;
    public static final int STRENGTH_LONG = 32;
    public static final int STRENGTH_II = 33;
    public static final int WEAKNESS = 34;
    public static final int WEAKNESS_LONG = 35;
    public static final int DECAY = 36;

    public ItemPotion() {
        this(0, 1);
    }

    public ItemPotion(Integer meta) {
        this(meta, 1);
    }

    public ItemPotion(Integer meta, int count) {
        super(POTION, meta, count, "Potion");
        updateName();
    }

    @Override
    public void setAux(Integer aux) {
        super.setAux(aux);
        updateName();
    }

    private void updateName() {
        int potionId = getAux();
        if (potionId == Potion.WATER) {
            name = buildName(potionId, "Bottle", true);
        } else {
            name = buildName(potionId, "Potion", true);
        }
    }

    static String buildName(int potionId, String type, boolean includeLevel) {
        switch (potionId) {
            case Potion.WATER:
                return "Water " + type;
            case Potion.MUNDANE:
            case Potion.MUNDANE_II:
                return "Mundane " + type;
            case Potion.THICK:
                return "Thick " + type;
            case Potion.AWKWARD:
                return "Awkward " + type;
            case Potion.TURTLE_MASTER:
            case Potion.TURTLE_MASTER_II:
            case Potion.TURTLE_MASTER_LONG: {
                String name = type + " of the Turtle Master";
                if (!includeLevel) {
                    return name;
                }
                Potion potion = Objects.requireNonNull(getPotion(potionId));
                if (potion.getLevel() <= 1) {
                    return name;
                }
                return name + " " + potion.getRomanLevel();
            }
            default: {
                Potion potion = getPotion(potionId);
                String finalName = potion != null ? potion.getPotionTypeName() : "";
                if (finalName.isEmpty()) {
                    finalName = type;
                } else {
                    finalName = type + " of " + finalName;
                }
                if (includeLevel && potion != null && potion.getLevel() > 1) {
                    finalName += " " + potion.getRomanLevel();
                }
                return finalName;
            }
        }
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean onClickAir(Player player, Vector3 directionVector) {
        return true;
    }

    @Override
    public boolean onUse(Player player, int ticksUsed) {
        PlayerItemConsumeEvent consumeEvent = new PlayerItemConsumeEvent(player, this);
        player.getServer().getPluginManager().callEvent(consumeEvent);
        if (consumeEvent.isCancelled()) {
            return false;
        }
        Potion potion = Potion.getPotion(this.getAux()).setSplash(false);

        player.level.getVibrationManager().callVibrationEvent(new VibrationEvent(player, player.clone(), VibrationType.DRINKING));

        if (player.isAdventure() || player.isSurvival()) {
            --this.count;
            player.getInventory().setItemInHand(this);
            player.getInventory().addItem(new ItemGlassBottle());
        }

        if (potion != null) {
            potion.applyPotion(player);
        }
        return true;
    }


    @Nullable
    public Potion getPotion() {
        return getPotion(getAux());
    }


    public static ItemPotion fromPotion(Potion potion) {
        return new ItemPotion(potion.getId());
    }

    static Potion getPotion(int damage) {
        try {
            return Potion.getPotion(damage);
        } catch (ServerException ignored) {
            return null;
        }
    }
}
