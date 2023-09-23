package me.samsey.ffa.events;

import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.Permission;
import me.samsey.ffa.scoreboardapi.ScoreboardFramework;
import me.samsey.ffa.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;

public class JoinAndQuitEvents implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            try {
                FFA.loadStatsFromDatabase(event.getUniqueId());
            } catch (SQLException e) {
                e.printStackTrace();
                event.setKickMessage("Non è stato possibile caricare i tuoi dati");
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!FFA.isLoaded(event.getPlayer().getUniqueId())) {
            Utils.reportAnomaly("player data not loaded on join");
            player.kickPlayer(ChatColor.RED + "I tuoi dati non erano caricati al tuo ingresso, errore interno");
            return;
        }
        String message = "";
        if (!player.hasPermission(Permission.JOIN_QUIT_SILENT))
            message =  "§8§o" + player.getName() + " è entrato";

        event.setJoinMessage(message);
        FFA.setupToLobby(player);
        player.teleport(Config.getSpawnLoc());
        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 3.0f, 1.5f);
        joinMessage(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        String message = "";
        if (!player.hasPermission(Permission.JOIN_QUIT_SILENT))
            message =  "§8§o" + player.getName() + " è uscito";

        event.setQuitMessage(message);
        FFA.unloadAndSaveStats(player.getUniqueId());

        ScoreboardFramework.refreshAll();
    }

    private void joinMessage(Player player) {
        player.sendMessage(
                "         §7Benvenuto su §a§lNIGHT§f§lCRAFT\n" +
                        " \n" +
                        "  §fPer qualsiasi informazione o dubbio non\n" +
                        "  §fesitare a contattare uno §cStaffer§7!\n" +
                        " \n" +
                        "  §7Telegram§8: §b@NightCraftIta\n" +
                        "  §7Discord§8: §cds.nightcraft.it \n" +
                        "  §7Shop§8: §6shop.nightcraft.it\n" +
                        "  §7Vota§8: §a/vote\n" +
                        " \n" +
                        "  §7Connecting to §c§n#FFA");
    }
}