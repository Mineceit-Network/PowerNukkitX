package cn.nukkit.inventory;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockID;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.entity.EntityInventoryChangeEvent;
import cn.nukkit.event.inventory.InventoryOpenEvent;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.item.ItemID;
import cn.nukkit.network.protocol.InventoryContentPacket;
import cn.nukkit.network.protocol.InventorySlotPacket;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author MagicDroidX (Nukkit Project)
 */
public abstract class BaseInventory implements Inventory {

    public final static Item AIR_ITEM = new ItemBlock(Block.get(BlockID.AIR), null, 0);

    protected final InventoryType type;

    protected int maxStackSize = Inventory.MAX_STACK;

    protected int size;

    protected final String name;

    protected final String title;

    public final Map<Integer, Item> slots = new HashMap<>();

    protected final Set<Player> viewers = new HashSet<>();

    protected InventoryHolder holder;

    private List<InventoryListener> listeners;

    public BaseInventory(InventoryHolder holder, InventoryType type) {
        this(holder, type, new HashMap<>());
    }

    public BaseInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items) {
        this(holder, type, items, null);
    }

    public BaseInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items, Integer overrideSize) {
        this(holder, type, items, overrideSize, null);
    }

    public BaseInventory(InventoryHolder holder, InventoryType type, Map<Integer, Item> items, Integer overrideSize, String overrideTitle) {
        this.holder = holder;

        this.type = type;

        if (overrideSize != null) {
            this.size = overrideSize;
        } else {
            this.size = this.type.getDefaultSize();
        }

        if (overrideTitle != null) {
            this.title = overrideTitle;
        } else {
            this.title = this.type.getDefaultTitle();
        }

        this.name = this.type.getDefaultTitle();

        if (!(this instanceof DoubleChestInventory)) {
            this.setContents(items);
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getMaxStackSize() {
        return maxStackSize;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @NotNull
    @Override
    public Item getItem(int index) {
        return this.slots.containsKey(index) ? this.slots.get(index).clone() : AIR_ITEM.clone();
    }


    @Override
    public Item getUnclonedItem(int index) {
        return this.slots.getOrDefault(index, AIR_ITEM);
    }

    @Override
    public Map<Integer, Item> getContents() {
        return new HashMap<>(this.slots);
    }

    @Override
    public void setContents(Map<Integer, Item> items) {
        if (items.size() > this.size) {
            TreeMap<Integer, Item> newItems = new TreeMap<>();
            for (Map.Entry<Integer, Item> entry : items.entrySet()) {
                newItems.put(entry.getKey(), entry.getValue());
            }
            items = newItems;
            newItems = new TreeMap<>();
            int i = 0;
            for (Map.Entry<Integer, Item> entry : items.entrySet()) {
                newItems.put(entry.getKey(), entry.getValue());
                i++;
                if (i >= this.size) {
                    break;
                }
            }
            items = newItems;
        }

        for (int i = 0; i < this.size; ++i) {
            if (!items.containsKey(i)) {
                if (this.slots.containsKey(i)) {
                    this.clear(i);
                }
            } else {
                if (!this.setItem(i, items.get(i))) {
                    this.clear(i);
                }
            }
        }
    }

    @Override
    public boolean setItem(int index, Item item, boolean send) {
        item = item.clone();
        if (index < 0 || index >= this.size) {
            return false;
        } else if (item.getId() == 0 || item.getCount() <= 0) {
            return this.clear(index, send);
        }

        InventoryHolder holder = this.getHolder();
        if (holder instanceof Entity) {
            EntityInventoryChangeEvent ev = new EntityInventoryChangeEvent((Entity) holder, this.getItem(index), item, index);
            Server.getInstance().getPluginManager().callEvent(ev);
            if (ev.isCancelled()) {
                this.sendSlot(index, this.getViewers());
                return false;
            }

            item = ev.getNewItem();
        }

        if (holder instanceof BlockEntity) {
            ((BlockEntity) holder).setDirty();
        }

        Item old = this.getUnclonedItem(index);
        this.slots.put(index, item.clone());
        this.onSlotChange(index, old, send);

        return true;
    }

    @Override
    public boolean contains(Item item) {
        int count = Math.max(1, item.getCount());
        boolean checkDamage = item.hasMeta() && item.getAux() >= 0;
        boolean checkTag = item.getCompoundTag() != null;
        for (Item i : this.getContents().values()) {
            if (item.equals(i, checkDamage, checkTag)) {
                count -= i.getCount();
                if (count <= 0) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Map<Integer, Item> all(Item item) {
        Map<Integer, Item> slots = new HashMap<>();
        boolean checkDamage = item.hasMeta() && item.getAux() >= 0;
        boolean checkTag = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (item.equals(entry.getValue(), checkDamage, checkTag)) {
                slots.put(entry.getKey(), entry.getValue());
            }
        }

        return slots;
    }

    @Override
    public void remove(Item item) {
        boolean checkDamage = item.hasMeta();
        boolean checkTag = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (item.equals(entry.getValue(), checkDamage, checkTag)) {
                this.clear(entry.getKey());
            }
        }
    }

    @Override
    public int first(Item item, boolean exact) {
        int count = Math.max(1, item.getCount());
        boolean checkDamage = item.hasMeta();
        boolean checkTag = item.getCompoundTag() != null;
        for (Map.Entry<Integer, Item> entry : this.getContents().entrySet()) {
            if (item.equals(entry.getValue(), checkDamage, checkTag) && (entry.getValue().getCount() == count || (!exact && entry.getValue().getCount() > count))) {
                return entry.getKey();
            }
        }

        return -1;
    }

    @Override
    public int firstEmpty(Item item) {
        for (int i = 0; i < this.size; ++i) {
            if (this.getUnclonedItem(i).getId() == Item.AIR) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public void decreaseCount(int slot) {
        Item item = this.getUnclonedItem(slot);

        if (item.getCount() > 0) {
            item = item.clone();
            item.count--;
            this.setItem(slot, item);
        }
    }

    @
    @Override
    public boolean canAddItem(Item item) {
        item = item.clone();
        boolean checkDamage = item.hasMeta();
        boolean checkTag = item.getCompoundTag() != null;
        for (int i = 0; i < this.getSize(); ++i) {
            Item slot = this.getUnclonedItem(i);
            if (item.equals(slot, checkDamage, checkTag)) {
                int diff;
                if ((diff = Math.min(slot.getMaxStackSize(), this.getMaxStackSize()) - slot.getCount()) > 0) {
                    item.setCount(item.getCount() - diff);
                }
            } else if (slot.getId() == Item.AIR) {
                item.setCount(item.getCount() - Math.min(slot.getMaxStackSize(), this.getMaxStackSize()));
            }

            if (item.getCount() <= 0) {
                return true;
            }
        }

        return false;
    }

    @Override
    public Item[] addItem(Item... slots) {
        List<Item> itemSlots = new ArrayList<>();
        for (Item slot : slots) {
            if (slot.getId() != 0 && slot.getCount() > 0) {
                //todo: clone only if necessary
                itemSlots.add(slot.clone());
            }
        }

        //使用FastUtils的IntArrayList提高性能
        IntList emptySlots = new IntArrayList(this.getSize());

        for (int i = 0; i < this.getSize(); ++i) {
            //获取未克隆Item对象
            Item item = this.getUnclonedItem(i);
            if (item.getId() == Item.AIR || item.getCount() <= 0) {
                emptySlots.add(i);
            }

            //使用迭代器而不是新建一个ArrayList
            for (Iterator<Item> iterator = itemSlots.iterator(); iterator.hasNext(); ) {
                Item slot = iterator.next();
                if (slot.equals(item)) {
                    int maxStackSize = Math.min(this.getMaxStackSize(), item.getMaxStackSize());
                    if (item.getCount() < maxStackSize) {
                        int amount = Math.min(maxStackSize - item.getCount(), slot.getCount());
                        amount = Math.min(amount, this.getMaxStackSize());
                        if (amount > 0) {
                            //在需要clone时再clone
                            item = item.clone();
                            slot.setCount(slot.getCount() - amount);
                            item.setCount(item.getCount() + amount);
                            this.setItem(i, item);
                            if (slot.getCount() <= 0) {
//                                itemSlots.remove(slot);
                                iterator.remove();
                            }
                        }
                    }
                }
            }
            if (itemSlots.isEmpty()) {
                break;
            }
        }

        if (!itemSlots.isEmpty() && !emptySlots.isEmpty()) {
            for (int slotIndex : emptySlots) {
                if (!itemSlots.isEmpty()) {
                    Item slot = itemSlots.get(0);
                    int maxStackSize = Math.min(slot.getMaxStackSize(), this.getMaxStackSize());
                    int amount = Math.min(maxStackSize, slot.getCount());
                    amount = Math.min(amount, this.getMaxStackSize());
                    slot.setCount(slot.getCount() - amount);
                    Item item = slot.clone();
                    item.setCount(amount);
                    this.setItem(slotIndex, item);
                    if (slot.getCount() <= 0) {
                        itemSlots.remove(slot);
                    }
                }
            }
        }

        return itemSlots.toArray(Item.EMPTY_ARRAY);
    }

    @Override
    public Item[] removeItem(Item... slots) {
        List<Item> itemSlots = new ArrayList<>();
        for (Item slot : slots) {
            if (slot.getId() != 0 && slot.getCount() > 0) {
                itemSlots.add(slot.clone());
            }
        }

        for (int i = 0; i < this.size; ++i) {
            Item item = this.getUnclonedItem(i);
            if (item.getId() == Item.AIR || item.getCount() <= 0) {
                continue;
            }

            for (Iterator<Item> iterator = itemSlots.iterator(); iterator.hasNext(); ) {
                Item slot = iterator.next();
                if (slot.equals(item, item.hasMeta(), item.getCompoundTag() != null)) {
                    item = item.clone();
                    int amount = Math.min(item.getCount(), slot.getCount());
                    slot.setCount(slot.getCount() - amount);
                    item.setCount(item.getCount() - amount);
                    this.setItem(i, item);
                    if (slot.getCount() <= 0) {
//                        itemSlots.remove(slot);
                        iterator.remove();
                    }

                }
            }

            if (itemSlots.size() == 0) {
                break;
            }
        }

        return itemSlots.toArray(Item.EMPTY_ARRAY);
    }

    @Override
    public boolean clear(int index, boolean send) {
        if (this.slots.containsKey(index)) {
            Item item = new ItemBlock(Block.get(BlockID.AIR), null, 0);
            Item old = this.slots.get(index);
            InventoryHolder holder = this.getHolder();
            if (holder instanceof Entity) {
                EntityInventoryChangeEvent ev = new EntityInventoryChangeEvent((Entity) holder, old, item, index);
                Server.getInstance().getPluginManager().callEvent(ev);
                if (ev.isCancelled()) {
                    this.sendSlot(index, this.getViewers());
                    return false;
                }
                item = ev.getNewItem();
            }

            if (item.getId() != Item.AIR) {
                this.slots.put(index, item.clone());
            } else {
                this.slots.remove(index);
            }

            this.onSlotChange(index, old, send);
        }

        return true;
    }

    @Override
    public void clearAll() {
        for (Integer index : this.getContents().keySet()) {
            this.clear(index);
        }
    }

    @Override
    public Set<Player> getViewers() {
        return viewers;
    }

    @Override
    public InventoryHolder getHolder() {
        return holder;
    }

    @Override
    public void setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
    }

    @Override
    public boolean open(Player who) {
        //if (this.viewers.contains(who)) return false;
        InventoryOpenEvent ev = new InventoryOpenEvent(this, who);
        who.getServer().getPluginManager().callEvent(ev);
        if (ev.isCancelled()) {
            return false;
        }
        this.onOpen(who);

        return true;
    }

    @Override
    public void close(Player who) {
        this.onClose(who);
    }

    @Override
    public void onOpen(Player who) {
        this.viewers.add(who);
    }

    @Override
    public void onClose(Player who) {
        this.viewers.remove(who);
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        if (send) {
            this.sendSlot(index, this.getViewers());
        }

        if (holder instanceof BlockEntity) {
            ((BlockEntity) holder).setDirty();
        }

        if (before.getId() == ItemID.LODESTONE_COMPASS || getUnclonedItem(index).getId() == ItemID.LODESTONE_COMPASS) {
            if (holder instanceof Player) {
                ((Player) holder).updateTrackingPositions(true);
            }

            getViewers().forEach(p -> p.updateTrackingPositions(true));
        }


        if (this.listeners != null) {
            for (InventoryListener listener : listeners) {
                listener.onInventoryChanged(this, before, index);
            }
        }
    }

    @Override
    public void sendContents(Player player) {
        this.sendContents(new Player[]{player});
    }

    @Override
    public void sendContents(Player... players) {
        InventoryContentPacket pk = new InventoryContentPacket();
        pk.slots = new Item[this.getSize()];
        for (int i = 0; i < this.getSize(); ++i) {
            pk.slots[i] = this.getUnclonedItem(i);
        }

        for (Player player : players) {
            int id = player.getWindowId(this);
            if (id == -1 || !player.spawned) {
                this.close(player);
                continue;
            }
            pk.inventoryId = id;
            player.dataPacket(pk);
        }
    }

    @Override
    public boolean isFull() {
        if (this.slots.size() < this.getSize()) {
            return false;
        }

        for (Item item : this.slots.values()) {
            if (item == null || item.getId() == 0 || item.getCount() < item.getMaxStackSize() || item.getCount() < this.getMaxStackSize()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean isEmpty() {
        if (this.getMaxStackSize() <= 0) {
            return false;
        }

        for (Item item : this.slots.values()) {
            if (item != null && item.getId() != 0 && item.getCount() > 0) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检测指定物品能在该库存所能存放的空余数量
     *
     * @param item 要检测的物品
     * @return 所能存放的空余数量
     */
    public int getFreeSpace(Item item) {
        int maxStackSize = Math.min(item.getMaxStackSize(), this.getMaxStackSize());
        int space = (this.getSize() - this.slots.size()) * maxStackSize;

        for (Item slot : this.getContents().values()) {
            if (slot == null || slot.getId() == 0) {
                space += maxStackSize;
                continue;
            }

            if (slot.equals(item, true, true)) {
                space += maxStackSize - slot.getCount();
            }
        }

        return space;
    }

    @Override
    public void sendContents(Collection<Player> players) {
        this.sendContents(players.toArray(Player.EMPTY_ARRAY));
    }

    @Override
    public void sendSlot(int index, Player player) {
        this.sendSlot(index, new Player[]{player});
    }

    @Override
    public void sendSlot(int index, Player... players) {
        InventorySlotPacket pk = new InventorySlotPacket();
        pk.slot = index;
        pk.item = this.getUnclonedItem(index);

        for (Player player : players) {
            int id = player.getWindowId(this);
            if (id == -1) {
                this.close(player);
                continue;
            }
            pk.inventoryId = id;
            player.dataPacket(pk);
        }
    }

    @Override
    public void sendSlot(int index, Collection<Player> players) {
        this.sendSlot(index, players.toArray(Player.EMPTY_ARRAY));
    }


    @Override
    public void addListener(InventoryListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<>();
        }

        this.listeners.add(listener);
    }


    @Override
    public void removeListener(InventoryListener listener) {
        if (this.listeners != null) {
            this.listeners.remove(listener);
        }
    }

    @Override
    public InventoryType getType() {
        return type;
    }
}
