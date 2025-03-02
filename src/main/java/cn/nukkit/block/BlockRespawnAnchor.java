/*
 * https://PowerNukkit.org - The Nukkit you know but Powerful!
 * Copyright (C) 2020  José Roberto de Araújo Júnior
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.nukkit.block;

import cn.nukkit.Player;
import cn.nukkit.blockproperty.BlockProperties;
import cn.nukkit.blockproperty.IntBlockProperty;
import cn.nukkit.blockproperty.exception.InvalidBlockPropertyMetaException;
import cn.nukkit.event.block.BlockExplosionPrimeEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemTool;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Explosion;
import cn.nukkit.level.GameRule;
import cn.nukkit.level.Level;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.TextFormat;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * @author joserobjr
 * @since 2020-10-06
 */


public class BlockRespawnAnchor extends BlockMeta {


    public static final IntBlockProperty RESPAWN_ANCHOR_CHARGE = new IntBlockProperty("respawn_anchor_charge", true, 4);


    public static final BlockProperties PROPERTIES = new BlockProperties(RESPAWN_ANCHOR_CHARGE);

    @Override
    public int getId() {
        return RESPAWN_ANCHOR;
    }


    public BlockRespawnAnchor() {
        this(0);
    }


    public BlockRespawnAnchor(int meta) throws InvalidBlockPropertyMetaException {
        super(meta);
    }


    @NotNull
    @Override
    public BlockProperties getProperties() {
        return PROPERTIES;
    }

    @Override
    public String getName() {
        return "Respawn Anchor";
    }

    @Override
    public boolean onActivate(@NotNull Item item, @Nullable Player player) {
        int charge = getCharge();
        if (item.getBlockId() == BlockID.GLOWSTONE && charge < RESPAWN_ANCHOR_CHARGE.getMaxValue()) {
            if (player == null || !player.isCreative()) {
                item.count--;
            }

            setCharge(charge + 1);
            getLevel().setBlock(this, this);
            getLevel().addSound(this, Sound.RESPAWN_ANCHOR_CHARGE);
            return true;
        }

        if (player == null) {
            return false;
        }

        if (charge > 0) {
            return attemptToSetSpawn(player);
        } else {
            return false;
        }
    }


    protected boolean attemptToSetSpawn(@NotNull Player player) {
        if (this.level.getDimension() != Level.DIMENSION_NETHER) {
            if (this.level.getGameRules().getBoolean(GameRule.TNT_EXPLODES)) {
                explode(player);
            }
            return true;
        }

        if (Objects.equals(player.getSpawn(), this)) {
            return false;
        }
        player.setSpawnBlock(this);
        getLevel().addSound(this, Sound.RESPAWN_ANCHOR_SET_SPAWN);
        player.sendMessage(new TranslationContainer(TextFormat.GRAY + "%tile.respawn_anchor.respawnSet"));
        return true;
    }

    @Deprecated


    public void explode() {
        explode(null);
    }


    public void explode(Player player) {
        BlockExplosionPrimeEvent event = new BlockExplosionPrimeEvent(this, player, 5);
        event.setIncendiary(true);
        if (event.isCancelled()) {
            return;
        }

        level.setBlock(this, get(AIR));
        Explosion explosion = new Explosion(this, event.getForce(), this);
        explosion.setFireChance(event.getFireChance());
        if (event.isBlockBreaking()) {
            explosion.explodeA();
        }
        explosion.explodeB();
    }


    public int getCharge() {
        return getIntValue(RESPAWN_ANCHOR_CHARGE);
    }


    public void setCharge(int charge) {
        setIntValue(RESPAWN_ANCHOR_CHARGE, charge);
    }

    @Override
    public int getToolType() {
        return ItemTool.TYPE_PICKAXE;
    }


    @Override
    public int getToolTier() {
        return ItemTool.TIER_DIAMOND;
    }

    @Override
    public double getResistance() {
        return 1200;
    }

    @Override
    public double getHardness() {
        return 50;
    }

    @Override
    public int getLightLevel() {
        switch (getCharge()) {
            case 0:
                return 0;
            case 1:
                return 3;
            case 2:
                return 7;
            default:
                return 15;
        }
    }

    @Override
    public int onUpdate(int type) {
        if (type == Level.BLOCK_UPDATE_SCHEDULED) {
            getLevel().addSound(this, Sound.RESPAWN_ANCHOR_DEPLETE);
            return type;
        }
        return super.onUpdate(type);
    }

    @Override
    public boolean canSilkTouch() {
        return true;
    }

    @Override
    public boolean canHarvestWithHand() {
        return false;
    }

    @Override
    public boolean canBeActivated() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override

    public boolean canBePulled() {
        return false;
    }

    @Override
    public Item[] getDrops(Item item) {
        if (canHarvest(item)) {
            return new Item[]{Item.getBlockItem(getId())};
        }
        return Item.EMPTY_ARRAY;
    }

}
