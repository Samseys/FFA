package me.samsey.ffa.utils;

import me.samsey.ffa.FFA;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CooldownUtil implements Listener {
    private static CooldownUtil instance;

    private Map<UUID, Map<String, CooldownEntry>> cooldowns  = new HashMap<>();

    private CooldownUtil() {
        Bukkit.getPluginManager().registerEvents(this, FFA.getInstance());
    }

    public static CooldownUtil getInstance() {
        if(instance == null)
            synchronized(CooldownUtil.class) {
                if(instance == null)
                    instance = new CooldownUtil();
            }
        return instance;
    }

    public void putCooldown(Player player, String cooldownName, long duration) {
        putCooldown(player.getUniqueId(), cooldownName, duration);
    }
    public void putCooldown(UUID uuid, String cooldownName, long duration) {
        Map<String, CooldownEntry> playerCooldowns = cooldowns.get(uuid);
        if(playerCooldowns == null) {
            playerCooldowns = new HashMap<>();
            cooldowns.put(uuid, playerCooldowns);
        }

        playerCooldowns.put(cooldownName, new CooldownEntry(System.currentTimeMillis(), duration));
    }

    public boolean isExpired(Player player, String cooldownName) {
        return isExpired(player.getUniqueId(), cooldownName);
    }

    public boolean isExpired(UUID uuid, String cooldownName) {
        Map<String, CooldownEntry> playerCooldowns = cooldowns.get(uuid);

        if(playerCooldowns == null)
            return true;

        CooldownEntry cooldownEntry = playerCooldowns.get(cooldownName);
        if(cooldownEntry == null)
            return true;

        return System.currentTimeMillis() >= (cooldownEntry.getStartTimestamp() + cooldownEntry.getDuration());
    }

    public void removeCooldown(Player player, String cooldownName) {
        removeCooldown(player.getUniqueId(), cooldownName);
    }

    public void removeCooldown(UUID uuid, String cooldownName) {
        Map<String, CooldownEntry> playerCooldowns = cooldowns.get(uuid);
        if(playerCooldowns == null)
            return;

        playerCooldowns.remove(cooldownName);
    }

    public long getRemainingTime(Player player, String cooldownName) {
        return getRemainingTime(player.getUniqueId(), cooldownName);
    }

    public long getRemainingTime(UUID uuid, String cooldownName) {
        Map<String, CooldownEntry> playerCooldowns = cooldowns.get(uuid);
        if(playerCooldowns == null)
            return 0;

        CooldownEntry cooldownEntry = playerCooldowns.get(cooldownName);
        if(cooldownEntry == null)
            return 0;

        long remainingTime = cooldownEntry.getStartTimestamp() + cooldownEntry.getDuration() - System.currentTimeMillis();
        if (remainingTime < 0) remainingTime = 0;
        return remainingTime;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        cooldowns.remove(event.getPlayer().getUniqueId());
    }

    private class CooldownEntry {
        private long startTimestamp;
        private long duration;
        public CooldownEntry(long startTimestamp, long duration) {
            this.startTimestamp = startTimestamp;
            this.duration = duration;
        }

        public long getStartTimestamp() {
            return startTimestamp;
        }

        public long getDuration() {
            return duration;
        }
    }
}
