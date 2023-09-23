package me.samsey.ffa.events;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.kit.Item;
import me.samsey.ffa.player.stats.Killstreak;
import me.samsey.ffa.scoreboardapi.ScoreboardFramework;
import me.samsey.ffa.task.ArrowTask;
import me.samsey.ffa.utils.CooldownUtil;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;


public class CombatEvents implements Listener {

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player victim = event.getEntity();
		FFAPlayer ffaVictim = FFA.getOnlinePlayerData(victim);

		if (event.getEntity().hasMetadata("NPC") || !ffaVictim.isInArena()) {
			return;
		}

		event.setDeathMessage("");
		event.setDroppedExp(0);
		event.getDrops().clear();

		int ks = ffaVictim.getKillstreak();
		ffaVictim.resetKillstreak();
		ffaVictim.addDeath();
		ScoreboardFramework.refreshPlayer(victim);
		victim.spawnParticle(Particle.HEART, victim.getLocation().add(0, 1, 0), 1);

		String added = "!";
		if (ffaVictim.getLastDamager() != null) {
			Player killer = Bukkit.getPlayer(ffaVictim.getLastDamager());
			if (killer != null && killer.isOnline()) {
				if (ks >= 3) {
					Bukkit.broadcastMessage(Config.getPrefix() + "§3" + killer.getName() + " §7ha rovinato le §3" + ks + " §7uccisioni consecutive di §3" + victim.getName() + "§7!");
				}
				FFAPlayer ffaKiller = FFA.getOnlinePlayerData(killer);
				double killerHealth = killer.getHealth() / 2.0;
				StringBuilder health = new StringBuilder();

				for(int i = 1; i <= 10; i++) {
					if (i < killerHealth) health.append("§4❤");
					else if (i < killerHealth + (killerHealth % 1 == 0 ? 0 : 1)) health.append("§c❤");
					else health.append("§7❤");
				}

				added = " da §3" + killer.getName() + " §7con " + health;

				killer.sendMessage(Config.getPrefix() + "Hai ucciso §3" + victim.getName() + "§7!");
				ffaKiller.addKill();

				if (ffaKiller.isInArena() && !ffaKiller.isRespawning()) {
					if (!Config.isGAppleOn()) {
						killer.setHealth(20.0);
					} else {
						event.getDrops().add(new ItemUtil(Material.GOLDEN_APPLE).name(killer.getName()).make());
					}
					killer.setFireTicks(0);

					PlayerInventory killerInv = killer.getInventory();
					int slot;
					if (killerInv.contains(Material.FLINT_AND_STEEL))
						slot = killerInv.first(Material.FLINT_AND_STEEL);
					else {
						slot = ffaKiller.getItemPosition(Item.FLINT_AND_STEEL);

						int firstEmpty = killerInv.firstEmpty();
						if (firstEmpty != slot)
							slot = firstEmpty;
					}

					killerInv.setItem(slot, Item.FLINT_AND_STEEL.getItemStack());
					killerInv.addItem(Item.ARROW.getItemStack());
					ffaKiller.addKillstreak();
					Killstreak.ks(killer);
					killer.setLevel(ffaKiller.getKillstreak());
				}
			}
			ScoreboardFramework.refreshPlayer(killer);
		}
		victim.sendMessage(Config.getPrefix() + "Sei stato ucciso" + added);
	}

	@EventHandler
	public void playerDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().hasMetadata("NPC")) return;

		Entity entityDamager = event.getDamager();
		Entity entityVictim = event.getEntity();

		if (entityDamager instanceof Projectile) {
			Projectile projectile = (Projectile)entityDamager;
			ProjectileSource shooter = projectile.getShooter();

			if (shooter instanceof Player) entityDamager = (Entity) shooter;
		}

		if (!(entityVictim instanceof Player) || !(entityDamager instanceof Player)) return;

		Player damager = (Player)entityDamager;
		Player victim = (Player)entityVictim;
		FFAPlayer ffaPlayerVictim = FFA.getOnlinePlayerData(victim);
		FFAPlayer ffaPlayerDamager = FFA.getOnlinePlayerData(damager);

		if (!ffaPlayerDamager.isInArena() || !ffaPlayerVictim.isInArena()) {
			return;
		}

		CooldownUtil cooldownUtil = CooldownUtil.getInstance();
		if (!cooldownUtil.isExpired(damager, "invincibility")) {
			damager.sendMessage(Config.getPrefix() + "Non puoi attaccare durante il periodo d'invincibilità.");
			event.setCancelled(true);
		} else if (!cooldownUtil.isExpired(victim, "invincibility")) {
			damager.sendMessage(Config.getPrefix() + "Non puoi attaccare durante il periodo d'invincibilità del nemico.");
			event.setCancelled(true);
		}

		if (!event.isCancelled()) {
			if (damager != victim) ffaPlayerVictim.setLastDamager(damager.getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event) {
		if (event.getEntity().hasMetadata("NPC") || !(event.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player)event.getEntity();

		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		if (!ffaPlayer.isInArena())
			return;

		if (event.getCause() == EntityDamageEvent.DamageCause.FALL || !CooldownUtil.getInstance().isExpired(player, "invincibility")) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onShot(EntityShootBowEvent e) {
		if (e.getEntity().hasMetadata("NPC") || !(e.getEntity() instanceof Player)) {
			return;
		}

		Player player = (Player)e.getEntity();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
	}

	@EventHandler
	public void onFnSPlace(BlockIgniteEvent event) {
		Player player = event.getPlayer();
		if (!event.getCause().equals(BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL)) return;

		ItemStack item = player.getInventory().getItemInMainHand();
		Damageable meta = (Damageable) item.getItemMeta();

		int d = meta.getDamage();
		if (d < 32) {
			meta.setDamage(32);
		} else {
			meta.setDamage(64);
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_BREAK, 2.5f, 1.8f);
		}

		item.setItemMeta((ItemMeta) meta);

		new BukkitRunnable() {
			@Override
			public void run() {
				if(event.getBlock().getType().equals(Material.FIRE)) {
					event.getBlock().setType(Material.AIR);
				}
			}
		}.runTaskLater(FFA.getInstance(), 20*10);
	}

	@EventHandler
	public void onPlayerIgnite(EntityCombustEvent e) {
		if (!(e.getEntity() instanceof Player))
			return;

		e.getEntity().setFireTicks(120);
	}

	@EventHandler
	public void onPlayerPickup(EntityPickupItemEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;

		Player player = (Player) event.getEntity();
		if (event.getItem().getItemStack().getType() == Material.GOLDEN_APPLE && event.getItem().getItemStack().getItemMeta().getDisplayName().equals(player.getName())) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 70, 4));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 70, 1));
			event.setCancelled(true);
			event.getItem().remove();
		}
	}

	@EventHandler
	public void onArrowShoot(EntityShootBowEvent event) {
		LivingEntity entity = event.getEntity();

		if (entity instanceof Player) {
			Player player = (Player) entity;
			CooldownUtil cooldownUtil = CooldownUtil.getInstance();
			if (cooldownUtil.isExpired(player, "invincibility"))
				new ArrowTask(player, (Arrow) event.getProjectile());
			else {
				player.sendMessage(Config.getPrefix() + "Non puoi usare l'arco durante il periodo di invincibilità!");
				event.setCancelled(true);
			}
		}
	}


	@EventHandler
	public void onArrowHitBlock(ProjectileHitEvent event) {
		if (event.getEntity() instanceof Arrow) {
			event.getEntity().remove();
		}
	}
}