package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PingCommand {

	@Command(name = "ping", inGameOnly = true)
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		if (args.length() == 0) {
			try {
				int ping = ((CraftPlayer) player).getHandle().ping;
				player.sendMessage(Config.getPrefix() + "Il tuo ping è di: " + getColor(ping) + ping + " MS");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args.length() == 1) {
			Player target = Bukkit.getPlayer(args.getArgs(0));
			if(target != null) {
				try {
					int ping = ((CraftPlayer) target).getHandle().ping;
					player.sendMessage(Config.getPrefix() + "Il ping di §3" + target.getName() + " §7è di: " + getColor(ping) + ping + " MS");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else {
				player.sendMessage(Config.getPrefix() + "Il giocatore non è online!");
			}
		} else {
			try {
				player.sendMessage(Config.getPrefix() + "Utilizzo: §3/ping [player]");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getColor(int ping) {
		String color;
		if(ping <= 50) {
			color = "§a§o";
		} else if(ping <= 80) {
			color = "§e§o";
		} else {
			color = "§4§o";
		}
		return color;
	}
}