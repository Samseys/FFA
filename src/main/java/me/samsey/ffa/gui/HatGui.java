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

public class HatGui implements InventoryProvider {
    private static final List<Material> list = Arrays.asList(
            Material.GRASS,
            Material.GRASS_BLOCK,
            Material.BEDROCK,
            Material.COAL_ORE,
            Material.IRON_ORE,
            Material.GOLD_ORE,
            Material.DIAMOND_ORE,
            Material.EMERALD_ORE,
            Material.SPONGE,
            Material.WET_SPONGE,
            Material.GLASS,
            Material.COAL_BLOCK,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.OBSIDIAN,
            Material.ICE,
            Material.GLOWSTONE,
            Material.HAY_BLOCK,
            Material.MAGMA_BLOCK,
            Material.CHEST,
            Material.ENDER_CHEST,
            Material.SLIME_BLOCK,
            Material.TNT,
            Material.BEACON,
            Material.DRAGON_HEAD
    );

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("hatGui")
            .provider(new HatGui())
            .size(4, 9)
            .title(ChatColor.BLACK + "Hats")
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        inventoryContents.set(0, 0, ClickableItem.of(
                new ItemUtil(Material.BARRIER).name("§cRimuovi Hat personalizzato").make(),
                e -> {
                    ffaPlayer.setHat(Material.AIR);
                    player.sendMessage(Config.getPrefix() + "Blocco rimosso!");
                    INVENTORY.close(player);
                }
        ));
        int col = 1, row = 0;
        for (Material mat : list) {
            inventoryContents.set(row, col, ClickableItem.of(
                    new ItemUtil(mat).name("§7Seleziona questo blocco!").make(),
                    e -> {
                        ffaPlayer.setHat(mat);
                        player.sendMessage(Config.getPrefix() + "Blocco scelto!");
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
