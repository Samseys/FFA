package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.fileapi.ConfigAccessor;
import me.samsey.ffa.fileapi.Data;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.scoreboardapi.ScoreboardFramework;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResetStatsCommand {

	@Command(name = "resetstats", permission = Permission.RESET_STATS)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();

		if (args.length() == 0) {
			if (sender instanceof Player) {
				this.reset((Player)sender);
				sender.sendMessage(Config.getPrefix() + "Statistiche resettate correttamente!");
			} else {
				sender.sendMessage(Config.getPrefix() + "Inserisci il nome di un giocatore online!");
			}
		} else {
			OfflinePlayer ofPlayer = Bukkit.getOfflinePlayer(args.getArgs(0));

			if (ofPlayer.isOnline()) {
				this.reset((Player)ofPlayer);
				sender.sendMessage(Config.getPrefix() + "Statistiche di §3" + ofPlayer.getName() + " resettate correttamente!");
			} else {
				sender.sendMessage(Config.getPrefix() + "Il giocatore non è online!");
				if (args.length() >= 2 && args.getArgs(1).equalsIgnoreCase("buycraft")) {
					ConfigAccessor configAccessor = Data.getFailedReset();
					FileConfiguration fileConfiguration = configAccessor.getConfig();
					List<String> messaggi = new ArrayList<>();

					if (fileConfiguration.isSet("FailedReset")) {
						messaggi = fileConfiguration.getStringList("FailedReset");
					}

					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
					Date date = new Date();

					messaggi.add(dateFormat.format(date) + " | " + args.getArgs(0));
					fileConfiguration.set("FailedReset", messaggi);
					configAccessor.saveConfig();
				}
			}
		}
	}

	public void reset(Player player) {
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		ffaPlayer.setKills(0);
		ffaPlayer.setDeaths(0);
		ffaPlayer.setMaxKillstreak(0);
		ffaPlayer.setKillstreak(0);
		FFA.setupToLobby(player);
		player.teleport(Config.getSpawnLoc());
		ScoreboardFramework.refreshPlayer(player);
		player.kickPlayer(Config.getPrefix() + "Le tue statistiche sono state resettate!");
	}
}
