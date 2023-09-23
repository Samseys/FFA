package me.samsey.ffa;

import me.samsey.ffa.fileapi.ConfigAccessor;
import me.samsey.ffa.fileapi.Data;
import me.samsey.ffa.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Config {
    private static String prefix = "§3§lFFA§7 » ", mysql_host = "127.0.0.1", mysql_database = "ffa" , mysql_user = "root", mysql_password = "password";
    private static int mysql_port = 3306;
    private static Location spawnLoc = Bukkit.getWorlds().get(0).getSpawnLocation();
    private static List<Location> randomSpawn = new ArrayList<>();
    private static boolean gApple = false, mysql_enabled = false;;

    public static String getMySQLHost() {
        return mysql_host;
    }

    public static String getMySQLDatabase() {
        return mysql_database;
    }

    public static String getMySQLUser() {
        return mysql_user;
    }

    public static String getMySQLPass() {
        return mysql_password;
    }

    public static int getMySQLPort() {
        return mysql_port;
    }

    public static Location getSpawnLoc() {
        return Config.spawnLoc;
    }

    public static void setSpawnLoc(Location spawnLoc) {
        Config.spawnLoc = spawnLoc;
    }

    public static void addRandomSpawn(Location randomSpawn) {
        Config.randomSpawn.add(randomSpawn);
    }

    public static boolean removeRandomSpawn() {
        int index = Config.randomSpawn.size();

        if (index > 0) {
            Config.randomSpawn.remove(index - 1);
            return true;
        }
        return false;
    }

    private static Random r = new Random();
    public static Location extractRandomSpawn() {
        int high = Config.randomSpawn.size();
        if (high == 0) {
            return null;
        }

        return Config.randomSpawn.get(r.nextInt(high));
    }

    public static String getPrefix() {
        return Config.prefix;
    }

    public static void ksMessage(int ks, String killer) {
        Bukkit.broadcastMessage(getPrefix() + "§3" + killer + " §7ha fatto §3" + ks + " §7uccisioni consecutive!");
    }

    public static boolean isGAppleOn() {
        return Config.gApple;
    }

    public static void setGApple(boolean gApple) {
        Config.gApple = gApple;
    }

    public static boolean isMySQLEnabled() {
        return mysql_enabled;
    }

    public static void load() {
        ConfigAccessor configAccessor = Data.getConfig();
        if(!configAccessor.exists()) {
            save();
            return;
        }
        FileConfiguration fileConfiguration = configAccessor.getConfig();

        mysql_enabled = fileConfiguration.getBoolean("mysql_enabled");
        mysql_host = fileConfiguration.getString("mysql_host");
        mysql_port = fileConfiguration.getInt("mysql_port");
        mysql_database = fileConfiguration.getString("mysql_database");
        mysql_user = fileConfiguration.getString("mysql_user");
        mysql_password = fileConfiguration.getString("mysql_password");

        gApple = fileConfiguration.getBoolean("gapple");

        spawnLoc = Utils.stringToLoc(fileConfiguration.getString("spawn"));

        List<String> spawnList = fileConfiguration.getStringList("randomSpawn");
        for (String locString : spawnList)
            randomSpawn.add(Utils.stringToLoc(locString));

    }

    public static void save() {
        ConfigAccessor configAccessor = Data.getConfig();
        configAccessor.deleteConfig();
        FileConfiguration fileConfiguration = configAccessor.getConfig();

        fileConfiguration.set("mysql_enabled", mysql_enabled);
        fileConfiguration.set("mysql_host", mysql_host);
        fileConfiguration.set("mysql_port", mysql_port);
        fileConfiguration.set("mysql_database", mysql_database);
        fileConfiguration.set("mysql_user", mysql_user);
        fileConfiguration.set("mysql_password", mysql_password);

        fileConfiguration.set("gapple", gApple);

        fileConfiguration.set("spawn", Utils.locToString(spawnLoc));

        List<String> spawnList = new ArrayList<>();
        for (Location loc : randomSpawn)
            spawnList.add(Utils.locToString(loc));
        fileConfiguration.set("randomSpawn", spawnList);

        configAccessor.saveConfig();
    }
}
