package cn.nukkit.command.defaults;

import cn.nukkit.Player;
import cn.nukkit.block.BlockUnknown;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.tree.ParamList;
import cn.nukkit.command.tree.node.PlayersNode;
import cn.nukkit.command.tree.node.RemainStringNode;
import cn.nukkit.command.utils.CommandLogger;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author xtypr
 * @since 2015/12/9
 */
public class GiveCommand extends VanillaCommand {
    public GiveCommand(String name) {
        super(name, "commands.give.description");
        this.setPermission("nukkit.command.give");
        this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                CommandParameter.newType("player", CommandParamType.TARGET, new PlayersNode()),
                ITEM_NAME.get(false),
                CommandParameter.newType("amount", true, CommandParamType.INT),
                CommandParameter.newType("data", true, CommandParamType.INT),
                CommandParameter.newType("components", true, CommandParamType.JSON, new RemainStringNode())
        });
        this.enableParamTree();
    }

    @Override
    public int execute(CommandSender sender, String commandLabel, Map.Entry<String, ParamList> result, CommandLogger log) {
        var list = result.getValue();
        List<Player> players = list.getResult(0);

        if (players.isEmpty()) {
            log.addNoTargetMatch().output();
            return 0;
        }

        Item item = list.getResult(1);
        if (item.isNull() && item.getId() != 0) {
            log.addError("commands.give.item.notFound", item.getDisplayName()).output();
            return 0;
        }
        if (item instanceof ItemBlock && item.getBlockItem() instanceof BlockUnknown) {
            log.addError("commands.give.block.notFound", item.getDisplayName()).output();
            return 0;
        }
        int count;
        if (list.hasResult(2)) {
            count = list.getResult(2);
            if (count <= 0) {
                log.addNumTooSmall(2, 1).output();
                return 0;
            }
            item.setCount(count);
        }
        if (list.hasResult(3)) {
            int damage = list.getResult(3);
            item.setAux(damage);
        }
        if (list.hasResult(4)) {
            String json = list.getResult(4);
            Item.ItemJsonComponents components = Item.ItemJsonComponents.fromJson(json);
            item.readItemJsonComponents(components);
        }

        for (Player player : players) {
            Item[] returns = player.getInventory().addItem(item.clone());
            List<Item> drops = new ArrayList<>();
            for (Item returned : returns) {
                int maxStackSize = returned.getMaxStackSize();
                if (returned.getCount() <= maxStackSize) {
                    drops.add(returned);
                } else {
                    while (returned.getCount() > maxStackSize) {
                        Item drop = returned.clone();
                        int toDrop = Math.min(returned.getCount(), maxStackSize);
                        drop.setCount(toDrop);
                        returned.setCount(returned.getCount() - toDrop);
                        drops.add(drop);
                    }
                    if (!returned.isNull()) {
                        drops.add(returned);
                    }
                }
            }

            for (Item drop : drops) {
                player.dropItem(drop);
            }
            log.outputObjectWhisper(player, "commands.give.successRecipient", item.getDisplayName() + " (" + item.getNamespaceId() + (item.getAux() != 0 ? ":" + item.getAux() : "") + ")",
                    String.valueOf(item.getCount()));
        }
        log.addSuccess("commands.give.success", item.getDisplayName() + " (" + item.getNamespaceId() + (item.getAux() != 0 ? ":" + item.getAux() : "") + ")",
                String.valueOf(item.getCount()),
                players.stream().map(Player::getName).collect(Collectors.joining(","))).successCount(players.size()).output(true);
        return players.size();
    }
}
