package me.samsey.ffa.player.stats;

import me.samsey.ffa.database.SQLColumns;
import me.samsey.ffa.database.SQLManager;
import me.samsey.ffa.database.SQLSingleStat;

import java.sql.SQLException;
import java.util.List;

public class Leaderboard {
	private static List<SQLSingleStat> topKills;
	private static List<SQLSingleStat> topDeaths;


	public static List<SQLSingleStat> getTopKills() {
		return topKills;
	}

	public static List<SQLSingleStat> getTopDeaths() {
		return topDeaths;
	}

	public static void loadRankings() throws SQLException {
		topKills = SQLManager.getTop(SQLColumns.PLAYERS_KILLS, 10);
		topDeaths = SQLManager.getTop(SQLColumns.PLAYERS_DEATHS, 10);
	}
}