package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.kit.KitManager;
import org.bukkit.entity.Player;

public class SaveInvCommand {

    @Command(name = "saveinv", inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();

        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        if (!ffaPlayer.isInArena() || ffaPlayer.isRespawning()) {
            player.sendMessage(Config.getPrefix() + "Non puoi usare questo comando ora!");
            return;
        }

        boolean saved = KitManager.saveOrder(player);
        if (saved)
            player.sendMessage(Config.getPrefix() + "Inventario salvato correttamente!");
        else
            player.sendMessage(Config.getPrefix() + "Non è stato possibile salvare il tuo inventario!");
    }
}
