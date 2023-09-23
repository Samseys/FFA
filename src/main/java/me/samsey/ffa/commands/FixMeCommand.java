package me.samsey.ffa.commands;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.utils.CooldownUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class FixMeCommand {
	
    @Command(name = "fix", aliases = {"fixme"}, inGameOnly = true)
    public void onCommand(CommandArgs args) {
        Player player = args.getPlayer();
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        Location loc = player.getLocation();
        
        if (!CooldownUtil.getInstance().isExpired(player, args.getCommand().getName())) {
            return;
        }
        
        loc.setY(loc.getY() + 1.5);
        player.teleport(loc);
        player.sendMessage(Config.getPrefix() + "Sei stato fixato!");
        CooldownUtil.getInstance().putCooldown(player, args.getCommand().getName(), 500L);
    }
}
