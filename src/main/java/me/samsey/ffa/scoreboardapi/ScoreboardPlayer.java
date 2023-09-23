package me.samsey.ffa.scoreboardapi;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import me.samsey.ffa.scoreboardapi.TaskGlow.Scroller;
import org.bukkit.entity.Player;

public class ScoreboardPlayer {

	private String playerName;
	private BPlayerBoard board;
	private int value = 0;
	private TaskChange changeBoardTask = null;
	private TaskGlow glowingBoardTask = null;
	private Scroller actualScroll = null;


	public ScoreboardPlayer(Player player) {
		setPlayerName(player.getName());
	}

	public String getPlayerName() {
		return playerName;
	}
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	public TaskChange getChangeBoardTask() {
		return changeBoardTask;
	}
	public void setChangeBoardTask(TaskChange changeBoardTask) {
		this.changeBoardTask = changeBoardTask;
	}
	public TaskGlow getGlowingBoardTask() {
		return glowingBoardTask;
	}
	public void setGlowingBoardTask(TaskGlow glowingBoardTask) {
		this.glowingBoardTask = glowingBoardTask;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public void increaseValue() {
		value++;
	}
	public Scroller getActualScroll() {
		return actualScroll;
	}
	public void setActualScroll(Scroller actualScroll) {
		this.actualScroll = actualScroll;
	}
	public void stopTasks() {
		changeBoardTask.cancel();
		glowingBoardTask.cancel();
	}
	public BPlayerBoard getBoard() {
		return board;
	}
	public void setBoard(BPlayerBoard board) {
		this.board = board;
	}


}
