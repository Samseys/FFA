package me.samsey.ffa.timer;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.WeakHashMap;

public class PlayerTrackTimer extends BukkitRunnable implements Listener {

    private static Map<Player, Player> trackedPlayers = new WeakHashMap<>();

    public PlayerTrackTimer start() {
        this.runTaskTimerAsynchronously(FFA.getInstance(), 20, 20);
        Bukkit.getPluginManager().registerEvents(this, FFA.getInstance());
        return this;
    }

    @Override
    public void run() {
        for (Player player1 : Bukkit.getOnlinePlayers()) {
            FFAPlayer ffaPlayer1 = FFA.getOnlinePlayerData(player1);
            if (!ffaPlayer1.isInArena() || ffaPlayer1.isRespawning()) continue;
            Player nearestPlayer = null;
            double distance = Double.MAX_VALUE;
            for (Player player2 : Bukkit.getOnlinePlayers()) {
                if (player1.equals(player2)) continue;

                FFAPlayer ffaPlayer2 = FFA.getOnlinePlayerData(player2);
                if (!ffaPlayer2.isInArena() || ffaPlayer2.isRespawning()) continue;
                double tempDistance;
                if ((tempDistance = player1.getLocation().distanceSquared(player2.getLocation())) < distance) {
                    nearestPlayer = player2;
                    distance = tempDistance;
                }
            }

            if (nearestPlayer == null) continue;
            trackedPlayers.put(player1, nearestPlayer);
        }
    }

    public static Player getTrackedPlayer(Player player) {
        Player trackedPlayer;
        if ((trackedPlayer = trackedPlayers.get(player)) != null && trackedPlayer.isOnline()) {
            FFAPlayer ffaTrackedPlayer = FFA.getOnlinePlayerData(trackedPlayer);
            if (ffaTrackedPlayer.isInArena() && !ffaTrackedPlayer.isRespawning())
                return trackedPlayer;
        }

        return null;
    }

    @EventHandler
    private void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        if (ffaPlayer.isInArena() && !ffaPlayer.isRespawning())
            for (Map.Entry<Player, Player> entry : trackedPlayers.entrySet()) {
                if (entry.getValue().equals(player)) {
                    entry.getKey().setCompassTarget(location);
                }
            }
    }
}
