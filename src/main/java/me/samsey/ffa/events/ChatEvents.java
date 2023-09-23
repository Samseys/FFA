package me.samsey.ffa.events;

import me.samsey.ffa.FFA;
import me.samsey.ffa.Permission;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatEvents implements Listener {

	@EventHandler(priority = EventPriority.MONITOR)
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
		String message;

		if (player.hasPermission(Permission.CHAT_COLOR)) message = ChatColor.translateAlternateColorCodes('&', event.getMessage());
		else message = event.getMessage();

		String prefix = ffaPlayer.getPrefix();
		String format = prefix + " " + ffaPlayer.getNameColor() + "%s §7» " + ffaPlayer.getChatColor() + "%s";
		event.setMessage(message);
		event.setFormat(format);
	}
}
