package me.samsey.ffa.scoreboards;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.stats.Rank;
import me.samsey.ffa.scoreboardapi.BoardAnnotation;
import me.samsey.ffa.scoreboardapi.BoardHandler;
import me.samsey.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@BoardAnnotation(objName = "FFA")
public class StatsBoard extends BoardHandler {
	
    @Override
    public Map<String, Integer> createBoard(Player player) {
        Map<String, Integer> map = new HashMap<>();
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

        map.put("                   ", 12);
        map.put("§7  ▪§f Nome §c" + player.getName(), 11);
        map.put("§7  ▪§f Rank §r" + Rank.getRank(player).getPrefix(), 10);
        map.put("  ▪ ", 9);
        map.put("§7  ▪§f Kills §a" + ffaPlayer.getKills(), 8);
        map.put("§7  ▪§f Deaths §c" + ffaPlayer.getDeaths(), 7);
        double kdr = Utils.getKDR(ffaPlayer);
        map.put("§7  ▪§f KDR §e" + kdr, 6);
        map.put("  ▪", 5);
        map.put("§7  ▪§f UC Massima: §a" + ffaPlayer.getMaxKillstreak(), 4);
        map.put("§7  ▪§f UC Attuali: §a" + ffaPlayer.getKillstreak(), 3);
        map.put("§7  ▪§f Online §6" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers(), 2);
        map.put("                      ", 1);
        map.put("   §cmc.nightcraft.it", 0);
        return map;
    }
}

