package me.samsey.ffa.timer;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.database.SQLManager;
import org.bukkit.scheduler.BukkitRunnable;

public class MySQLKeepAliveTimer extends BukkitRunnable {

    public MySQLKeepAliveTimer start() {
        if (Config.isMySQLEnabled())
            this.runTaskTimer(FFA.getInstance(), 10 * 60 * 20, 10 * 60 * 20);
        return this;
    }

    @Override
    public void run() {
        SQLManager.checkConnection();
    }

}
