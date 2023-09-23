package me.samsey.ffa.scoreboardapi;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TaskChange extends BukkitRunnable {

	private Player player;

	public TaskChange(Player player) {
		this.player = player;
		this.runTaskTimer(ScoreboardFramework.framework.plugin, 20L * ScoreboardFramework.getSeconds(), 20L * ScoreboardFramework.getSeconds());
		if (ScoreboardFramework.framework.list.size() == 0) {
			return;
		}
		if (player != null && player.isOnline()) updateBoard();
	}

	@Override
	public void run() {
		if (player != null && player.isOnline()) {
			if (ScoreboardFramework.framework.list.size() == 0) {
				return;
			}
			
			ScoreboardPlayer scoreboardPlayer = ScoreboardFramework.getRp(player);
			scoreboardPlayer.increaseValue();
			
			if (scoreboardPlayer.getValue() == ScoreboardFramework.framework.list.size()) {
				scoreboardPlayer.setValue(0);
			}

			updateBoard();

		} else {
			this.cancel();
		}
	}

	private void updateBoard() {
		ScoreboardPlayer scoreboardPlayer = ScoreboardFramework.getRp(player);
		ScoreboardFramework.ObjectiveCreator objectiveCreator = ScoreboardFramework.framework.list.get(scoreboardPlayer.getValue());
		BoardAnnotation annot = objectiveCreator.getBoard();
		scoreboardPlayer.setActualScroll(new TaskGlow.Scroller(annot.textColor(), annot.objName(), annot.colorMid(), annot.colorBefore(), annot.colorAfter(), annot.bold(), annot.upperCaseMid()));
		BPlayerBoard board = scoreboardPlayer.getBoard();
		for (int index : board.getLines().keySet()) {
			board.remove(index);
		}

		BoardHandler boardHandler = (BoardHandler) objectiveCreator.getObject();
		boardHandler.show(player);
	}
}
