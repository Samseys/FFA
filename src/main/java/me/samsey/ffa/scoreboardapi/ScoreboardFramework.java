package me.samsey.ffa.scoreboardapi;

import fr.minuskube.netherboard.bukkit.BPlayerBoard;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardFramework implements Listener {

	private Map<String, ScoreboardPlayer> playerMap = new HashMap<>();
	public List<ObjectiveCreator> list = new ArrayList<>();
	public Plugin plugin;
	public static ScoreboardFramework framework;
	public int seconds;

	public ScoreboardFramework(Plugin plugin, int seconds) {
		if (framework != null) {
			throw new IllegalPluginAccessException("ScoreboardAPI già inizializzata!");
		}

		this.plugin = plugin;
		framework = this;
		this.seconds = seconds;
		Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public ScoreboardFramework(Plugin plugin) {
		this(plugin, 25);
	}

	public void registerObjective(BoardHandler obj) {
		if (obj.getClass().getAnnotation(BoardAnnotation.class) != null) {
			BoardAnnotation boardAnnotation = obj.getClass().getAnnotation(BoardAnnotation.class);
			list.add(new ObjectiveCreator(boardAnnotation, obj));
		} else {
			System.out.println("Invalid Object, it needs the BoardAnnotation");
		}
	}

	public static ScoreboardPlayer getRp(Player player) {
		return framework.playerMap.get(player.getName());
	}

	public static void removeRp(Player player) {
		framework.playerMap.remove(player.getName());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void playerJoinEvent(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ScoreboardPlayer scoreboardPlayer = new ScoreboardPlayer(player);
		playerMap.put(player.getName(), scoreboardPlayer);
		BPlayerBoard board = new BPlayerBoard(player, "ScoreboardAPI");
		scoreboardPlayer.setBoard(board);

		scoreboardPlayer.setChangeBoardTask(new TaskChange(player));
		scoreboardPlayer.setGlowingBoardTask(new TaskGlow(player));
	}

	public static void refreshAll() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			refreshPlayer(player);
		}
	}

	public static int getSeconds() {
		return framework.seconds;
	}

	@EventHandler
	private void playerQuitEvent(PlayerQuitEvent event) {
		playerMap.get(event.getPlayer().getName()).stopTasks();
		playerMap.remove(event.getPlayer().getName());
	}

	public static void refreshPlayer(Player player) {
		ScoreboardPlayer scoreboardPlayer = ScoreboardFramework.getRp(player);
		
		if (ScoreboardFramework.framework.list.size() == 0 || scoreboardPlayer == null) return;
		
		ObjectiveCreator objectiveCreator = ScoreboardFramework.framework.list.get(scoreboardPlayer.getValue());
		BoardHandler boardHandler = (BoardHandler) objectiveCreator.getObject();
		boardHandler.show(player);

	}

	public class ObjectiveCreator {

		private BoardAnnotation boardAnnotation;
		private Object object;

		public ObjectiveCreator(BoardAnnotation boardAnnotation, Object obj) {
			setBoard(boardAnnotation);
			setObject(obj);
		}

		public BoardAnnotation getBoard() {
			return boardAnnotation;
		}

		public void setBoard(BoardAnnotation boardAnnotation) {
			this.boardAnnotation = boardAnnotation;
		}

		public Object getObject() {
			return object;
		}

		public void setObject(Object object) {
			this.object = object;
		}
	}

}