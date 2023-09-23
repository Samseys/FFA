package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetSpawnCommand {
	
	@Command(name = "setspawn", inGameOnly = true, permission = Permission.ADMIN)
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		Location loc = player.getLocation();
		
		loc.setY(loc.getY() + 1.5);
		Config.setSpawnLoc(loc);
		player.sendMessage(Config.getPrefix() + "Punto di respawn impostato correttamente!");
	}
}
