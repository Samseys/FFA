package me.samsey.ffa.events;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.gui.RankGui;
import me.samsey.ffa.gui.VipGui;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.timer.PlayerTrackTimer;
import me.samsey.ffa.utils.ItemUtil;
import me.samsey.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryEvents implements Listener {

	@EventHandler
	public void onInteractItems(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player player = event.getPlayer();

		ItemStack itemInHand = player.getInventory().getItemInMainHand();
		if (itemInHand.getType().equals(Material.AIR)) return;

		String nbtTag = ItemUtil.getStringDataFromItemStack(itemInHand, "clickable");
		if (nbtTag != null) {
			switch (nbtTag) {
				case "join":
					Bukkit.dispatchCommand(player, "join");
					break;
				case "ranks":
					RankGui.INVENTORY.open(player);
					break;
				case "vipgui":
					VipGui.INVENTORY.open(player);
					break;
				case "autojoin_on":
				case "autojoin_off":
					Bukkit.dispatchCommand(player, "autojoin");
					break;
				case "player_tracker":
					Player trackedPlayer = PlayerTrackTimer.getTrackedPlayer(player);
					if (trackedPlayer != null) {
						player.sendMessage(Config.getPrefix() + "Player tracciato: §c" + trackedPlayer.getName() +
								"\n§7Distanza: §c" + Utils.round(player.getLocation().distance(trackedPlayer.getLocation())));
						break;
					}
					player.sendMessage(Config.getPrefix() + "Non stai tracciando nessun player!");
					break;
				default:
					break;
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!(event.getWhoClicked() instanceof Player) || !(event.getClickedInventory() instanceof PlayerInventory))
			return;
		Player player = (Player) event.getWhoClicked();

		if (event.getSlot() > 9 && event.getSlot() < 40) {
			event.setCancelled(true);
			Utils.updateInventory(player);
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryDragEvent event) {
		if (!(event.getWhoClicked() instanceof Player) || !(event.getInventory() instanceof PlayerInventory))
			return;
		Player player = (Player) event.getWhoClicked();
		for (int slot : event.getInventorySlots()) {
			if (slot > 9 && slot < 40) {
				event.setCancelled(true);
				Utils.updateInventory(player);
				return;
			}
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		for(int i = 9; i < 36; i++) {
			ItemStack item = inventory.getItem(i);
			if (item == null)
				continue;

			inventory.setItem(inventory.firstEmpty(), item);
			player.getInventory().setItem(i, null);
		}

		ItemStack offHandItem = inventory.getItemInOffHand();
		if(offHandItem.getType().equals(Material.AIR)) return;

		inventory.setItem(inventory.firstEmpty(), offHandItem);
		player.getInventory().setItemInOffHand(null);
		Utils.updateInventory(player);
	}
}