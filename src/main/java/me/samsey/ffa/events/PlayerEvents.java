package me.samsey.ffa.events;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;

import java.util.Arrays;
import java.util.List;


public class PlayerEvents implements Listener {
	public List<Material> blockInteract = Arrays.asList(Material.OAK_FENCE_GATE, Material.FURNACE, Material.CRAFTING_TABLE, Material.CHEST, Material.LEVER, Material.STONE_BUTTON, Material.OAK_BUTTON);

	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		Location loc;
		if (!ffaPlayer.getAutoJoin()) {
			FFA.setupToLobby(player);
			loc = Config.getSpawnLoc();
		} else {
			FFA.setupToArena(player);
			loc = Config.extractRandomSpawn();
			if (loc == null) {
				player.sendMessage(Config.getPrefix() + "Non ci sono random spawn settati!");
				return;
			}
		}
		event.setRespawnLocation(loc);
	}

	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (!ffaPlayer.canBuild()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onPickupItem(EntityPickupItemEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (!ffaPlayer.canBuild()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onHunger(FoodLevelChangeEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		event.setCancelled(true);
		player.setFoodLevel(20);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (player.getInventory().getItemInMainHand().getType() != Material.FLINT_AND_STEEL) {
			if (!ffaPlayer.canBuild()) {
				event.setCancelled(true);
				Utils.updateInventory(player);
			}
		}
	}

	@EventHandler
	public void onBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		if (!ffaPlayer.canBuild()) {
			event.setCancelled(true);
			Utils.updateInventory(player);
		}
	}

	@EventHandler
	public void onBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (!ffaPlayer.canBuild()) {
			event.setCancelled(true);
			Utils.updateInventory(player);
		}
	}

	@EventHandler
	public void playerBedEnter(PlayerBedEnterEvent event) {
		if (!FFA.getOnlinePlayerData(event.getPlayer()).canBuild()) event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInteractItems(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!FFA.getOnlinePlayerData(player).canBuild() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && this.blockInteract.contains(event.getClickedBlock().getType())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		Block b = event.getBlock();

		if (!ffaPlayer.canBuild())
			if (b.getWorld().getBlockAt(b.getLocation()).getType() != Material.FIRE) event.setCancelled(true);
			else b.setType(Material.AIR);
	}
}
