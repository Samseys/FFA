package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.Permission;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class FindCommand {

    @Command(name = "find", permission = Permission.UUID_COMMAND)
    public void onCommand(CommandArgs args) {
        CommandSender sender = args.getSender();

        if (args.length() == 1) {
            try {
                UUID uuid = UUID.fromString(args.getArgs(0));
                OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(uuid);
                if (offlinePlayer.hasPlayedBefore()) sender.sendMessage(Config.getPrefix() + "Player associato all'UUID: §3" + offlinePlayer.getName());
                else sender.sendMessage(Config.getPrefix() + "Questo giocatore non ha mai giocato qui");
            } catch (IllegalArgumentException ignored) {
                sender.sendMessage(Config.getPrefix() + "Inserisci un UUID valido!");
            }
        } else {
            sender.sendMessage(Config.getPrefix() + "Digita: /find §3<UUID>");
        }
    }
}
