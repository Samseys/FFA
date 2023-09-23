package me.samsey.ffa.task;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowTask extends BukkitRunnable {

	private final Particle arrowEffect;
	private final Arrow arrow;
	private final World world;

	public ArrowTask(Player player, Arrow arrow) {
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		this.arrowEffect = ffaPlayer.getArrowEffect();
		this.arrow = arrow;
		world = arrow.getLocation().getWorld();

		if (this.arrowEffect != null) {
			this.runTaskTimer(FFA.getInstance(), 0L, 1L);
		}
	}

	public void run() {
		if (this.arrow != null && !this.arrow.isDead() && !this.arrow.isOnGround()) {
			world.spawnParticle(arrowEffect, arrow.getLocation(), 1);
		} else {
			this.cancel();
		}
	}
}