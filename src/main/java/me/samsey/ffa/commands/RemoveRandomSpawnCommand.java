package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.command.CommandSender;

public class RemoveRandomSpawnCommand {
	
	@Command(name = "removerandomspawn", permission = Permission.ADMIN)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		boolean hasRemoved = Config.removeRandomSpawn();
		
		if (hasRemoved) {
			sender.sendMessage(Config.getPrefix() + "Punto di spawn rimosso correttamente!");
		} else {
			sender.sendMessage(Config.getPrefix() + "Non ci sono punti di spawn impostati!");
		}
	}
}