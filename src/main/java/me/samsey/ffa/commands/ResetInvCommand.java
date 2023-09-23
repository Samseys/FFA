package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.entity.Player;

public class ResetInvCommand {
    @Command(name = "resetinv", inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        ffaPlayer.resetInvOrder();
        player.sendMessage(Config.getPrefix() + "Ordine dell'inventario ripristinato!");
    }
}
