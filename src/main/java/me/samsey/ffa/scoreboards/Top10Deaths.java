package me.samsey.ffa.scoreboards;

import me.samsey.ffa.database.SQLSingleStat;
import me.samsey.ffa.player.stats.Leaderboard;
import me.samsey.ffa.scoreboardapi.BoardAnnotation;
import me.samsey.ffa.scoreboardapi.BoardHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@BoardAnnotation(objName = "FFA")
public class Top10Deaths extends BoardHandler {

    @Override
    public Map<String, Integer> createBoard(Player player) {
        Map<String, Integer> map = new HashMap<>();
        map.put("                         ", 14);
        map.put("  §c§lTOP 10 DEATHS", 13);
        map.put("  ", 12);
        int poss = 11;
        List<SQLSingleStat> top10 = Leaderboard.getTopDeaths();

        for (int i = 0; i < 10; i++) {
            String row = "N/A";
            if (i < top10.size()) {
                SQLSingleStat stat = top10.get(i);
                OfflinePlayer ofPlayer = Bukkit.getOfflinePlayer(stat.getUniqueId());

                String name = ofPlayer.getName();
                row = "§b" + name + ": §3" + stat.getValue();
            }
            map.put("§7 ▪ " + (i+1) + "» " + row, poss);
            poss--;
        }

        map.put("                       ", 1);
        map.put("   §cmc.nightcraft.it", 0);
        return map;
    }
}

