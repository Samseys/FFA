package me.samsey.ffa.events;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class RespawnManager implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void falseDeath(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();

		if (player.getGameMode().equals(GameMode.SPECTATOR)) {
			event.setCancelled(true);
			return;
		}

		if ((player.getHealth() - event.getFinalDamage()) > 0)
			return;

		event.setCancelled(true);
		Bukkit.getPluginManager().callEvent(new PlayerDeathEvent(player, new ArrayList<>(), 0, null));
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void falseRespawn(PlayerDeathEvent event) {
		Player player = event.getEntity();
		customRespawn(player, (player.getBedSpawnLocation() == null) ? Bukkit.getWorlds().get(0).getSpawnLocation() : player.getBedSpawnLocation());
	}

	private void customRespawn(Player player, Location spawn) {
		player.setGameMode(GameMode.SPECTATOR);
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		ffaPlayer.setRespawning(true);
		new BukkitRunnable() {
			int i = 3;
			@Override
			public void run() {
				if (!player.isOnline()) {
					this.cancel();
					return;
				}
				if (i > 1) {
					player.sendTitle("§cSei morto!", "§7Respawnerai tra §3" + i + " §7secondi", 0, 25, 0);
				} else if (i == 1) {
					player.sendTitle("§cSei morto!", "§7Respawnerai tra §31 §7secondo", 0, 25, 0);
				} else {
					player.setGameMode(GameMode.SURVIVAL);
					FFA instance = FFA.getInstance();
					for (Player online : Bukkit.getOnlinePlayers()) {
						online.hidePlayer(instance, player);
						online.showPlayer(instance, player);
					}
					player.setHealth(20);
					for (PotionEffect effect : player.getActivePotionEffects())
						player.removePotionEffect(effect.getType());
					player.setFoodLevel(20);
					PlayerRespawnEvent event = new PlayerRespawnEvent(player, spawn, false);
					Bukkit.getPluginManager().callEvent(event);
					player.teleport(event.getRespawnLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN);
					ffaPlayer.setRespawning(false);
					player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3.0f, 1.5f);
					this.cancel();
				}
				i--;
			}
		}.runTaskTimer(FFA.getInstance(), 0L, 20L);
	}
}