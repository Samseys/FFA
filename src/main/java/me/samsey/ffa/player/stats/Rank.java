package me.samsey.ffa.player.stats;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public enum Rank {

	APPRENDISTA("§2[Apprendista]", 0, Material.AIR),
	ESPERTO("§1[Esperto]", 500, Material.GRASS),
	VETERANO("§d[Professionista]", 1000, Material.BEDROCK),
	PROFESSIONISTA("§3[Veterano]", 1500, Material.DIAMOND_ORE),
	MAESTRO("§5[Maestro]", 2000, Material.SPONGE),
	LEGGENDA("§6[Leggenda]", 3000, Material.ICE),
	IMMORTALE("§8[Immortale]", 5000, Material.GLASS),
	DIO("§c[Dio]", 7000, Material.DIAMOND_BLOCK),
	DEMONE("§4[Demone]", 10000, Material.EMERALD_BLOCK);

	private int kills;
	private String prefix;
	private Material hat;

	Rank(String prefix, int kills, Material hat) {
		this.prefix = prefix;
		this.kills = kills;
		this.hat = hat;
	}

	public int getNeededKills() {
		return this.kills;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public Material getHat() {
		return this.hat;
	}

	public static Rank getRank(Player player) {
		Rank prevRank = Rank.APPRENDISTA;
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		for (Rank rank : values()) {
			if(ffaPlayer.getKills() < rank.getNeededKills())
				break;
			prevRank = rank;
		}
		return prevRank;
	}
}
