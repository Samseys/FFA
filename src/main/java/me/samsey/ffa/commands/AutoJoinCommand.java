package me.samsey.ffa.commands;

import me.samsey.ffa.FFA;
import me.samsey.ffa.commandapi.Command;
import me.samsey.ffa.commandapi.CommandArgs;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.kit.KitManager;
import me.samsey.ffa.utils.CooldownUtil;
import org.bukkit.entity.Player;

public class AutoJoinCommand {

	@Command(name = "autojoin", inGameOnly = true)
	public void onCommand(CommandArgs args) {
		Player player = args.getPlayer();
		FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

		if (!CooldownUtil.getInstance().isExpired(player, args.getCommand().getName())) {
			return;
		}

		ffaPlayer.setAutoJoin(!ffaPlayer.getAutoJoin());

		KitManager.replaceAutoJoinItem(player);

		CooldownUtil.getInstance().putCooldown(player, args.getCommand().getName(), 1000L);
	}
}