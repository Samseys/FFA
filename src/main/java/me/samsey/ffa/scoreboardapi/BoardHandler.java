package me.samsey.ffa.scoreboardapi;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class BoardHandler {

	public void show(Player player) {
		ScoreboardPlayer scoreboardPlayer = ScoreboardFramework.getRp(player);
		BPlayerBoard board = scoreboardPlayer.getBoard();
		
		ScoreboardFramework.ObjectiveCreator objectiveCreator = ScoreboardFramework.framework.list.get(scoreboardPlayer.getValue());

		Map<String, Integer> map = createBoard(player);

		if (map.size() > 15) {
			player.sendMessage("§9ScoreboardAPI> §7Numero di righe maggiore di 15, la Scoreboard verrà tagliata!");
		}

		int i = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			if (entry.getValue() != null && entry.getKey() != null && i < 15) {
				board.set(entry.getKey(), entry.getValue());
				i++;
			}
		}
		
	}

	public abstract Map<String, Integer> createBoard(Player player);

}
