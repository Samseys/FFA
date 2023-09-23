package me.samsey.ffa.database;

import com.google.common.collect.Lists;
import me.samsey.ffa.Config;
import me.samsey.ffa.database.api.Database;
import me.samsey.ffa.database.api.SQLResult;
import me.samsey.ffa.database.api.type.MySQL;
import me.samsey.ffa.database.api.type.SQLite;
import me.samsey.ffa.player.FFAPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class SQLManager {
    private static final String TABLE_MAIN = "ffa_players";
    private static Database database;

    public static void connect() throws SQLException {
        if (Config.isMySQLEnabled()) SQLManager.database = new MySQL(Config.getMySQLHost(), Config.getMySQLPort(), Config.getMySQLDatabase(), Config.getMySQLUser(), Config.getMySQLPass());
        else SQLManager.database = new SQLite();

        SQLManager.database.connect();
    }

    public static void checkConnection() {
        database.isConnectionValid();
    }

    public static void close() {
        if (database != null) {
            database.close();
        }
    }

    public static void createTables() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_MAIN + " (" +
                SQLColumns.PLAYERS_UUID + " varchar(36) NOT NULL, " +
                SQLColumns.PLAYERS_KILLS + " INT NOT NULL, " +
                SQLColumns.PLAYERS_DEATHS + " INT NOT NULL, " +
                SQLColumns.PLAYERS_MAX_KS + " INT NOT NULL, " +
                SQLColumns.PLAYERS_INV_ORDER + " varchar(30) NOT NULL, " +
                SQLColumns.PLAYERS_NAME_COLOR + " varchar(20) NOT NULL, " +
                SQLColumns.PLAYERS_CHAT_COLOR + " varchar(20) NOT NULL, " +
                SQLColumns.PLAYERS_HAT + " varchar(20) NOT NULL, " +
                SQLColumns.PLAYERS_ARROW_EFFECT + " varchar(20) NOT NULL, " +
                "PRIMARY KEY(" + SQLColumns.PLAYERS_UUID + "))";
        if (Config.isMySQLEnabled())
            sql += " ENGINE = InnoDB DEFAULT CHARSET = UTF8";
        database.update(sql);
    }

    public static FFAPlayer getStats(UUID playerUUID) throws SQLException {
        try (SQLResult result = database.preparedQuery("SELECT * FROM " + TABLE_MAIN +
                " WHERE " + SQLColumns.PLAYERS_UUID + " = ?", playerUUID.toString())) {
            if (result.next()) {
                return new FFAPlayer(playerUUID,
                        result.getInt(SQLColumns.PLAYERS_KILLS),
                        result.getInt(SQLColumns.PLAYERS_DEATHS),
                        result.getInt(SQLColumns.PLAYERS_MAX_KS),
                        ChatColor.valueOf(result.getString(SQLColumns.PLAYERS_CHAT_COLOR)),
                        ChatColor.valueOf(result.getString(SQLColumns.PLAYERS_NAME_COLOR)),
                        Material.valueOf(result.getString(SQLColumns.PLAYERS_HAT)),
                        result.getString(SQLColumns.PLAYERS_ARROW_EFFECT),
                        result.getString(SQLColumns.PLAYERS_INV_ORDER));
            } else {
                FFAPlayer ffaPlayer = new FFAPlayer(playerUUID);
                ffaPlayer.setNeedSave(true); // Perché è un giocatore nuovo, va salvato la prima volta
                return ffaPlayer;
            }
        }
    }

    public static void savePlayerData(UUID playerUUID, FFAPlayer ffaPlayer) throws SQLException {
        if (Config.isMySQLEnabled())
            database.preparedUpdate(
                    "INSERT INTO " + TABLE_MAIN + " (" +
                            SQLColumns.PLAYERS_UUID + ", " +
                            SQLColumns.PLAYERS_KILLS + ", " +
                            SQLColumns.PLAYERS_DEATHS + ", " +
                            SQLColumns.PLAYERS_MAX_KS + ", " +
                            SQLColumns.PLAYERS_CHAT_COLOR + ", " +
                            SQLColumns.PLAYERS_NAME_COLOR + ", " +
                            SQLColumns.PLAYERS_HAT + ", " +
                            SQLColumns.PLAYERS_ARROW_EFFECT + ", " +
                            SQLColumns.PLAYERS_INV_ORDER +
                            ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE " +
                            SQLColumns.PLAYERS_UUID + " = ?, " +
                            SQLColumns.PLAYERS_KILLS + " = ?, " +
                            SQLColumns.PLAYERS_DEATHS + " = ?, " +
                            SQLColumns.PLAYERS_MAX_KS + " = ?, " +
                            SQLColumns.PLAYERS_CHAT_COLOR + " = ?, " +
                            SQLColumns.PLAYERS_NAME_COLOR + " = ?, " +
                            SQLColumns.PLAYERS_HAT + " = ?, " +
                            SQLColumns.PLAYERS_ARROW_EFFECT + " = ?, " +
                            SQLColumns.PLAYERS_INV_ORDER + " = ?;",

                    playerUUID.toString(), ffaPlayer.getKills(), ffaPlayer.getDeaths(), ffaPlayer.getMaxKillstreak(),
                    ffaPlayer.getPureChatColor().name(), ffaPlayer.getPureNameColor().name(),
                    ffaPlayer.getPureHat().toString(), ffaPlayer.getSaveArrowEffect(), ffaPlayer.getInvOrderAsString(),
                    playerUUID.toString(), ffaPlayer.getKills(), ffaPlayer.getDeaths(), ffaPlayer.getMaxKillstreak(),
                    ffaPlayer.getPureChatColor().name(), ffaPlayer.getPureNameColor().name(),
                    ffaPlayer.getPureHat().toString(), ffaPlayer.getSaveArrowEffect(), ffaPlayer.getInvOrderAsString());
        else
            database.preparedUpdate(
                    "INSERT OR REPLACE INTO " + TABLE_MAIN + " (" +
                            SQLColumns.PLAYERS_UUID + ", " +
                            SQLColumns.PLAYERS_KILLS + ", " +
                            SQLColumns.PLAYERS_DEATHS + ", " +
                            SQLColumns.PLAYERS_MAX_KS + ", " +
                            SQLColumns.PLAYERS_CHAT_COLOR + ", " +
                            SQLColumns.PLAYERS_NAME_COLOR + ", " +
                            SQLColumns.PLAYERS_HAT + ", " +
                            SQLColumns.PLAYERS_ARROW_EFFECT + ", " +
                            SQLColumns.PLAYERS_INV_ORDER +
                            ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    playerUUID.toString(), ffaPlayer.getKills(), ffaPlayer.getDeaths(), ffaPlayer.getMaxKillstreak(),
                    ffaPlayer.getPureChatColor().name(), ffaPlayer.getPureNameColor().name(),
                    ffaPlayer.getPureHat().toString(), ffaPlayer.getSaveArrowEffect(), ffaPlayer.getInvOrderAsString());

    }

    public static List<SQLSingleStat> getTop(String statSQLColumn, int limit) throws SQLException {
        try(SQLResult result = database.preparedQuery("SELECT " + SQLColumns.PLAYERS_UUID + ", " + statSQLColumn +
                " FROM " + TABLE_MAIN + " ORDER BY " + statSQLColumn + " DESC LIMIT " + limit)) {

            List<SQLSingleStat> stats = Lists.newArrayList();
            while (result.next()) {
                stats.add(new SQLSingleStat(UUID.fromString(result.getString(SQLColumns.PLAYERS_UUID)), result.getInt(statSQLColumn)));
            }

            return stats;
        }
    }

    //autodistruzione
    public static void resetStats() throws SQLException {
        database.update("UPDATE " + TABLE_MAIN + " SET " +
                SQLColumns.PLAYERS_KILLS + " = 0, " +
                SQLColumns.PLAYERS_DEATHS + " = 0, " +
                SQLColumns.PLAYERS_MAX_KS + " = 0, " +
                SQLColumns.PLAYERS_CHAT_COLOR + " = 'GRAY', " +
                SQLColumns.PLAYERS_NAME_COLOR + " = 'GRAY', " +
                SQLColumns.PLAYERS_HAT + " = 'AIR', " +
                SQLColumns.PLAYERS_ARROW_EFFECT + " = 'NAN', " +
                SQLColumns.PLAYERS_INV_ORDER + " = '1\\;2\\;3\\;4\\;0\\;0\\;0\\;7\\;5';"
        );
    }
}