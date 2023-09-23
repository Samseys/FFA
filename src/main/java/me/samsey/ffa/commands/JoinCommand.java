package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

public class JoinCommand {

	@Command(name = "join", inGameOnly = true)
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (!ffaPlayer.isInArena()) {
			Location loc = Config.extractRandomSpawn();
			if (loc == null) {
				player.sendMessage(Config.getPrefix() + "Non ci sono random spawn settati!");
				return;
			}
			FFA.setupToArena(player);
			player.teleport(loc, PlayerTeleportEvent.TeleportCause.PLUGIN);
		}
	}
}