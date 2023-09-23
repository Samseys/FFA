package me.samsey.ffa.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class VipGui implements InventoryProvider {

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("vipGui")
            .provider(new VipGui())
            .size(1, 9)
            .title(ChatColor.BLACK + "VIP")
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        inventoryContents.set(0, 1, ClickableItem.of(
                new ItemUtil(Material.DIAMOND_HELMET).name("§7Hat").make(),
                e -> {
                    if (!player.hasPermission(Permission.HAT)) {
                        player.sendMessage(Config.getPrefix() + "Non hai i permessi per usare gli Hat!");
                        INVENTORY.close(player);
                        return;
                    }

                    HatGui.INVENTORY.open(player);
                }
        ));
        inventoryContents.set(0, 3, ClickableItem.of(
                new ItemUtil(Material.RED_TERRACOTTA).name("§7Name Color").make(),
                e -> {
                    if (!player.hasPermission(Permission.NAME_COLOR)) {
                        player.sendMessage(Config.getPrefix() + "Non hai i permessi per cambiare Name Color!");
                        INVENTORY.close(player);
                        return;
                    }

                    NameColorGui.INVENTORY.open(player);
                }
        ));
        inventoryContents.set(0, 5, ClickableItem.of(
                new ItemUtil(Material.LIME_TERRACOTTA).name("§7Chat Color").make(),
                e -> {
                    if (!player.hasPermission(Permission.CHAT_COLOR)) {
                        player.sendMessage(Config.getPrefix() + "Non hai i permessi per cambiare Chat Color!");
                        INVENTORY.close(player);
                        return;
                    }

                    ChatColorGui.INVENTORY.open(player);
                }
        ));
        inventoryContents.set(0, 7, ClickableItem.of(
                new ItemUtil(Material.ARROW).name("§7Arrow Effect").make(),
                e -> {
                    if (!player.hasPermission(Permission.ARROW_EFFECTS)) {
                        player.sendMessage(Config.getPrefix() + "Non hai i permessi per cambiare Arrow Effect!");
                        INVENTORY.close(player);
                        return;
                    }

                    ArrowEffectGui.INVENTORY.open(player);
                }
        ));
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
