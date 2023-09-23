package me.samsey.ffa.utils;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Utils {

	public static void updateInventory(Player p) {
		new BukkitRunnable() {
			public void run() {
				p.updateInventory();
			}
		}.runTaskLater(FFA.getInstance(), 2L);
	}

	public static double getKDR(Integer kills, Integer death) {
        if (death == 0) {
            death = 1;
        }
        return round(kills / (double) death);
    }

	public static double getKDR(FFAPlayer ffaPlayer) {
		return getKDR(ffaPlayer.getKills(), ffaPlayer.getDeaths());
	}

	public static double round(double value) {
		if (Double.isNaN(value)) return value;

		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(1, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public static String locToString(Location l) {
		return l.getWorld().getName() + "@" + l.getX() + "@" + l.getY() + "@" + l.getZ() + "@" + l.getYaw() + "@" + l.getPitch();
	}

	public static Location stringToLoc(String s) {
		String[] arr = s.split("@");
		return new Location(Bukkit.getWorld(arr[0]), Double.parseDouble(arr[1]), Double.parseDouble(arr[2]), Double.parseDouble(arr[3]), Float.parseFloat(arr[4]), Float.parseFloat(arr[5]));
	}

	public static void reportAnomaly(String message) {
		Logger.getLogger("FFA").log(Level.SEVERE, message);
	}

}