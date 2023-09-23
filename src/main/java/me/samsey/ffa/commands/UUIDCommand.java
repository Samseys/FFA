package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class UUIDCommand {

    @Command(name = "uuid", permission = Permission.UUID_COMMAND)
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();
        
        if (args.length() != 1) {
            sender.sendMessage(Config.getPrefix() + "Digita /UUID §3<player>");
        } else {
			OfflinePlayer testPlayer = Bukkit.getServer().getOfflinePlayer(args.getArgs(0));
            
            sender.sendMessage(Config.getPrefix() + "L'UUID del giocatore §3" + args.getArgs(0) + " §7è:");
            sender.sendMessage("§3" + testPlayer.getUniqueId().toString());
        }
    }
}
