package me.samsey.ffa.fileapi;

import me.samsey.ffa.FFA;

public class Data {

    public static ConfigAccessor getConfig() {
        return new ConfigAccessor(FFA.getInstance(), "config.yml");
    }
    
    public static ConfigAccessor getLog() {
        return new ConfigAccessor(FFA.getInstance(), "logsegnala.yml");
    }
    
    public static ConfigAccessor getFailedReset() {
        return new ConfigAccessor(FFA.getInstance(), "failedreset.yml");
    }
}
