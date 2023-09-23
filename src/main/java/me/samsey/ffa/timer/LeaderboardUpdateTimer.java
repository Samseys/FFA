package me.samsey.ffa.timer;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.stats.Leaderboard;
import me.samsey.ffa.scoreboardapi.ScoreboardFramework;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;

public class LeaderboardUpdateTimer extends BukkitRunnable {

    public LeaderboardUpdateTimer start() {
        this.runTaskTimerAsynchronously(FFA.getInstance(), 0, 10 * 60 * 20);
        return this;
    }

    @Override
    public void run() {
        try {
            Leaderboard.loadRankings();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ScoreboardFramework.refreshAll();
    }

}

