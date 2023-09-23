package me.samsey.ffa.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class NameColorGui implements InventoryProvider {
    private static final List<ChatColor> allowed = Arrays.asList(
            ChatColor.DARK_BLUE,
            ChatColor.DARK_GREEN,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE,
            ChatColor.GOLD,
            ChatColor.GRAY,
            ChatColor.DARK_GRAY,
            ChatColor.BLUE,
            ChatColor.BLACK,
            ChatColor.GREEN,
            ChatColor.AQUA,
            ChatColor.RED,
            ChatColor.LIGHT_PURPLE,
            ChatColor.YELLOW,
            ChatColor.WHITE);

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("nameColorGui")
            .provider(new NameColorGui())
            .size(2, 9)
            .title(ChatColor.BLACK + "Colore del nome")
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        ChatColor playerColor = ffaPlayer.getNameColor();
        int col = 0, row = 0;
        for(ChatColor color : allowed) {
            Material material;
            if (color.equals(playerColor)) material = Material.LIME_DYE;
            else material = Material.GRAY_DYE;

            inventoryContents.set(row, col, ClickableItem.of(
                    new ItemUtil(material).name("§7Seleziona " + color + "questo §7colore!").make(),
                    e -> {
                        if (material.equals(Material.LIME_DYE)) return;
                        ffaPlayer.setNameColor(color);
                        player.sendMessage(Config.getPrefix() + "Hai scelto " + color + "questo §7colore!");
                        INVENTORY.close(player);
                    }
            ));
            if (++col == 9) {
                col = 0;
                row++;
            }
        }
    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}