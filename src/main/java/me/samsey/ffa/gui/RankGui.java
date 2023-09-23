package me.samsey.ffa.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import fr.minuskube.inv.content.SlotIterator;
import fr.minuskube.inv.content.SlotIterator.Type;
import me.samsey.ffa.player.stats.Rank;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class RankGui implements InventoryProvider {

	public static final SmartInventory INVENTORY = SmartInventory.builder()
			.id("ranksGui")
			.title(ChatColor.BLACK + "Ranks")
			.provider(new RankGui())
			.size(3, 9)
			.build();


	@Override
	public void init(Player player, InventoryContents inventoryContents) {
		inventoryContents.set(2, 8, ClickableItem.of(
				new ItemUtil(Material.REDSTONE_BLOCK).name("§cChiudi").make(),
				event -> INVENTORY.close(player)));
		inventoryContents.newIterator("ranks", Type.HORIZONTAL, 1, 0);
	}

	@Override
	public void update(Player player, InventoryContents inventoryContents) {
		int state = inventoryContents.property("state", 0);
		inventoryContents.setProperty("state", state + 1);

		if(state % 5 != 0) return;

		int rankIndex = inventoryContents.property("rank", 0);
		SlotIterator iter= inventoryContents.iterator("ranks").get();

		if(rankIndex > Rank.values().length - 1) return;

		ItemUtil item;
		Rank rank = Rank.values()[rankIndex];
		Rank userRank = Rank.getRank(player);

		if (rank.equals(userRank)) item = new ItemUtil(Material.PLAYER_HEAD).skullOwner(player.getName()).lore("§7Fai parte di questo rank!");
		else item = new ItemUtil(Material.MAP);

		String added;
		if(userRank.ordinal() >= rank.ordinal()) added = "§a✔";
		else added =  "§c❌";
		item.name(rank.getPrefix() + "§7: " + added);
		item.lore("§7 Kill necessarie: §3" + rank.getNeededKills());
		item.lore("§7 Hat: §3" + rank.getHat().name());

		iter.next().set(ClickableItem.empty(item.make()));
		inventoryContents.setProperty("rank", rankIndex + 1);
	}
}