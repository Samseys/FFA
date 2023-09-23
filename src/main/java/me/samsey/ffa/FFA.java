package me.samsey.ffa;

import com.google.common.collect.Maps;
import de.myzelyam.api.vanish.VanishAPI;
import me.samsey.ffa.commandapi.CommandFramework;
import me.samsey.ffa.commands.*;
import me.samsey.ffa.database.SQLManager;
import me.samsey.ffa.events.*;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.kit.KitManager;
import me.samsey.ffa.scoreboardapi.ScoreboardFramework;
import me.samsey.ffa.scoreboards.StatsBoard;
import me.samsey.ffa.scoreboards.Top10Deaths;
import me.samsey.ffa.scoreboards.Top10Kills;
import me.samsey.ffa.timer.LeaderboardUpdateTimer;
import me.samsey.ffa.timer.MySQLKeepAliveTimer;
import me.samsey.ffa.timer.PlayerTrackTimer;
import me.samsey.ffa.timer.WaterDamageTimer;
import me.samsey.ffa.utils.CooldownUtil;
import me.samsey.ffa.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class FFA extends JavaPlugin {

	private static FFA plugin;
	private static Map<UUID, FFAPlayer> ffaPlayers = Maps.newConcurrentMap();

	public void onEnable() {
		plugin = this;
		initListeners();
		initCommandAPI();
		initScoreboardAPI();
		Config.load();

		try {
			SQLManager.connect();
			SQLManager.createTables();
		} catch (Exception e) {
			e.printStackTrace();
			criticalShutdown("Impossibile connettersi al database");
			return;
		}

		new MySQLKeepAliveTimer().start();
		new LeaderboardUpdateTimer().start();
		new PlayerTrackTimer().start();
		new WaterDamageTimer().start();

		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-off");
		World world = Bukkit.getServer().getWorld("world");
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setGameRule(GameRule.DO_FIRE_TICK, false);
		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
	}

	public void initListeners() {
		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(new ChatEvents(), plugin);
		pluginManager.registerEvents(new CombatEvents(), plugin);
		pluginManager.registerEvents(new InventoryEvents(), plugin);
		pluginManager.registerEvents(new JoinAndQuitEvents(), plugin);
		pluginManager.registerEvents(new OtherEvents(), plugin);
		pluginManager.registerEvents(new PlayerEvents(), plugin);
		pluginManager.registerEvents(new RespawnManager(), plugin);
	}

	private void initCommandAPI() {
		CommandFramework commandFramework = new CommandFramework(FFA.getInstance());
		commandFramework.registerCommands(new AddRandomSpawnCommand());
		commandFramework.registerCommands(new AutoJoinCommand());
		commandFramework.registerCommands(new EnableBuildCommand());
		commandFramework.registerCommands(new EnableGappleCommand());
		commandFramework.registerCommands(new FindCommand());
		commandFramework.registerCommands(new FixMeCommand());
		commandFramework.registerCommands(new JoinCommand());
		commandFramework.registerCommands(new PingCommand());
		commandFramework.registerCommands(new RemoveRandomSpawnCommand());
		commandFramework.registerCommands(new ResetInvCommand());
		commandFramework.registerCommands(new ResetStatsCommand());
		commandFramework.registerCommands(new SaveInvCommand());
		commandFramework.registerCommands(new SetSpawnCommand());
		commandFramework.registerCommands(new UUIDCommand());
		commandFramework.registerHelp();
	}

	public void initScoreboardAPI() {
		ScoreboardFramework frame = new ScoreboardFramework(FFA.getInstance(), 10);
		frame.registerObjective(new StatsBoard());
		frame.registerObjective(new Top10Kills());
		frame.registerObjective(new Top10Deaths());
	}

	public void onDisable() {
		Config.save();
		for (Map.Entry<UUID, FFAPlayer> entry : ffaPlayers.entrySet()) {
			if (entry.getValue().needSave()) {
				try {
					SQLManager.savePlayerData(entry.getKey(), entry.getValue());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		SQLManager.close();
	}
	public static FFA getInstance() {
		return plugin;
	}

	public static FFAPlayer getOnlinePlayerData(Player player) {
		return getOnlinePlayerData(player.getUniqueId());
	}

	public static FFAPlayer getOnlinePlayerData(UUID uuid) {
		FFAPlayer ffaPlayer = ffaPlayers.get(uuid);
		if(ffaPlayer == null) {
			Utils.reportAnomaly("player data not loaded, username: " + Bukkit.getPlayer(uuid).getName());
			ffaPlayer = new FFAPlayer(uuid);
		}
		return ffaPlayer;
	}

	public static FFAPlayer loadStatsFromDatabase(UUID playerUUID) throws SQLException {
		FFAPlayer ffaPlayer = SQLManager.getStats(playerUUID);
		ffaPlayers.put(playerUUID, ffaPlayer);
		return ffaPlayer;
	}

	public static void unloadAndSaveStats(UUID playerUUID) {
		FFAPlayer ffaPlayer = ffaPlayers.remove(playerUUID);
		if (ffaPlayer == null) {
			throw new IllegalStateException(playerUUID + "'s stats were not loaded");
		}

		if (ffaPlayer.needSave()) {
			Bukkit.getScheduler().runTaskAsynchronously(FFA.getInstance(), () -> {
				try {
					SQLManager.savePlayerData(playerUUID, ffaPlayer);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static boolean isLoaded(UUID uuid) {
		return ffaPlayers.containsKey(uuid);
	}

	public static void setupToLobby(Player player) {
		FFAPlayer ffaPlayer = getOnlinePlayerData(player);

		player.setHealth(20);
		genericSetup(player);

		ffaPlayer.setInArena(false);
		ffaPlayer.setLastDamager(null);

		KitManager.giveLobbyItems(player);
		Utils.updateInventory(player);
	}

	public static void setupToArena(Player player) {
		FFAPlayer ffaPlayer = getOnlinePlayerData(player);

		genericSetup(player);

		if (VanishAPI.isInvisible(player))
			VanishAPI.showPlayer(player);

		ffaPlayer.setInArena(true);
		ffaPlayer.setLastDamager(null);

		KitManager.giveKit(player);
		KitManager.giveArmor(player);
		Utils.updateInventory(player);

		CooldownUtil.getInstance().putCooldown(player, "invincibility", 4000L);
	}

	private static void genericSetup(Player player) {
		for (PotionEffect effect : player.getActivePotionEffects()) {
			player.removePotionEffect(effect.getType());
		}

		player.setFoodLevel(20);
		player.setSaturation(0);
		player.setExhaustion(0);
		player.setLevel(0);
		player.setExp(0.0f);
		player.setGameMode(GameMode.SURVIVAL);
		player.setAllowFlight(false);
		player.setFlying(false);
	}

	private void criticalShutdown(String message) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + this.getName() + "] " + message);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException ignored) { }
		setEnabled(false);
		Bukkit.shutdown();
	}
}
