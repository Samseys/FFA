package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.entity.Player;

public class EnableBuildCommand {

	@Command(name = "enablebuild", inGameOnly = true, permission = Permission.BUILD)
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		ffaPlayer.setBuild(!ffaPlayer.canBuild());

		if (ffaPlayer.canBuild()) {
			player.sendMessage(Config.getPrefix() + "Adesso puoi costruire!");
		} else {
			player.sendMessage(Config.getPrefix() + "Non puoi più costruire!");
		}
	}
}
