package me.samsey.ffa.player.stats;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.kit.Item;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Killstreak {

	public static void ks(Player killer) {
		FFAPlayer ffaKiller = FFA.getOnlinePlayerData(killer);
		int tempStreak = ffaKiller.getKillstreak();

		if (tempStreak > ffaKiller.getMaxKillstreak()) {
			ffaKiller.setMaxKillstreak(tempStreak);
		}

		switch (tempStreak) {
			case 3:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 500, 0));
				Config.ksMessage(3, killer.getName());
				break;
			case 5:
				killer.getInventory().addItem(Item.KS_1.getItemStack());
				Config.ksMessage(5, killer.getName());
				break;
			case 10:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 400, 1));
				Config.ksMessage(10, killer.getName());
				break;
			case 15:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 500, 0));
				Config.ksMessage(15, killer.getName());
				break;
			case 20:
				killer.getInventory().addItem(Item.KS_2.getItemStack());
				Config.ksMessage(20, killer.getName());
				break;
			case 25:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 800, 0));
				Config.ksMessage(25, killer.getName());
				break;
			case 30:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 1));
				Config.ksMessage(30, killer.getName());
				break;
			case 35:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 500, 0));
				Config.ksMessage(35, killer.getName());
				break;
			case 40:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 500, 0));
				Config.ksMessage(40, killer.getName());
				break;
			case 45:
				killer.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 500, 0));
				Config.ksMessage(45, killer.getName());
			case 50:
				killer.getInventory().addItem(Item.KS_3.getItemStack());
				Config.ksMessage(50, killer.getName());
				break;
		}
	}
}
