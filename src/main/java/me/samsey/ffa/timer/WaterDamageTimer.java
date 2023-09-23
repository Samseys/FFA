package me.samsey.ffa.timer;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class WaterDamageTimer extends BukkitRunnable {

    public WaterDamageTimer start() {
        this.runTaskTimer(FFA.getInstance(), 0, 10);
        return this;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            Material blockType = player.getLocation().getBlock().getType();
            if (blockType.equals(Material.WATER)) {
                FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
                if (ffaPlayer.isInArena() && !ffaPlayer.isRespawning())
                    player.damage(6);
            }
        }
    }
}
