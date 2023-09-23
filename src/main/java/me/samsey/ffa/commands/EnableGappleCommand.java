package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.command.CommandSender;

public class EnableGappleCommand {
	
	@Command(name = "enablegapple", permission = Permission.ADMIN)
	public void onCommand(CommandArgs args) {
		CommandSender sender = args.getSender();
		
		Config.setGApple(!Config.isGAppleOn());
		
		if (Config.isGAppleOn()) {
			sender.sendMessage(Config.getPrefix() + "Golden apple §3attivata§7!");
		} else {
			sender.sendMessage(Config.getPrefix() + "Golden apple §3disabilitata§7!");
		}
	}
}
