package me.samsey.ffa.player;

import me.samsey.ffa.Permission;
import me.samsey.ffa.player.kit.Item;
import me.samsey.ffa.player.stats.Rank;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.UUID;

public class FFAPlayer {

    private UUID uuid;
    private int kills = 0, deaths = 0, killstreak = 0, maxKillstreak = 0;
    private boolean inArena = false, canBuild = false, autoJoin = false, respawning = false, needSave;
    private ChatColor chatColor = ChatColor.WHITE;
    private ChatColor nameColor = ChatColor.GRAY;
    private UUID lastDamager;
    private Material hat = Material.AIR;
    private Particle arrowEffect = null;
    private int[] invOrder = {1, 2, 3, 4, 8, 0, 0, 7, 5};

    public FFAPlayer(Player player) {
        uuid = player.getUniqueId();
    }

    public FFAPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public FFAPlayer(UUID uuid, int kills, int deaths, int killstreakPerm, ChatColor chatColor, ChatColor nameColor, Material hat, String arrowEffect, String invOrder) {
        this(uuid);
        this.kills = kills;
        this.deaths = deaths;
        this.maxKillstreak = killstreakPerm;
        this.chatColor = chatColor;
        this.nameColor = nameColor;
        this.hat = hat;
        setArrowEffect(arrowEffect);
        setInvOrder(invOrder);
        needSave = false;
    }

    public int getKills() {
        return this.kills;
    }

    public void setKills(int kills) {
        needSave = true;
        this.kills = kills;
    }

    public void addKill() {
        needSave = true;
        ++this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public void setDeaths(int deaths) {
        needSave = true;
        this.deaths = deaths;
    }

    public void addDeath() {
        needSave = true;
        ++this.deaths;
    }

    public int getKillstreak() {
        return this.killstreak;
    }

    public void setKillstreak(int killstreak) {
        this.killstreak = killstreak;
    }

    public void resetKillstreak() {
        this.killstreak = 0;
    }

    public void addKillstreak() {
        ++this.killstreak;
    }

    public int getMaxKillstreak() {
        return this.maxKillstreak;
    }

    public void setMaxKillstreak(int killstreakPerm) {
        needSave = true;
        this.maxKillstreak = killstreakPerm;
    }

    public boolean isInArena() {
        return this.inArena;
    }

    public void setInArena(boolean inArena) {
        this.inArena = inArena;
    }

    public boolean canBuild() {
        return this.canBuild;
    }

    public void setBuild(boolean canBuild) {
        this.canBuild = canBuild;
    }

    public boolean getAutoJoin() {
        return this.autoJoin;
    }

    public void setAutoJoin(boolean autoJoin) {
        this.autoJoin = autoJoin;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }

    public boolean needSave() {
        return needSave;
    }

    public void setNeedSave(boolean needSave) {
        this.needSave = needSave;
    }

    public ChatColor getChatColor() {
        if (Bukkit.getPlayer(uuid).hasPermission(Permission.CHAT_COLOR)) {
            return this.chatColor;
        }
        return ChatColor.WHITE;
    }

    public ChatColor getPureChatColor() {
        return this.chatColor;
    }

    public void setChatColor(ChatColor chatColor) {
        needSave = true;
        this.chatColor = chatColor;
    }

    public ChatColor getNameColor() {
        if (Bukkit.getPlayer(uuid).hasPermission(Permission.NAME_COLOR)) {
            return this.nameColor;
        }
        return ChatColor.GRAY;
    }

    public ChatColor getPureNameColor() {
        return this.nameColor;
    }

    public void setNameColor(ChatColor nameColor) {
        needSave = true;
        this.nameColor = nameColor;
    }

    public Material getHat() {
        if (Bukkit.getPlayer(uuid).hasPermission(Permission.HAT)) {
            return this.hat;
        }
        return Material.AIR;
    }

    public UUID getLastDamager() {
        return this.lastDamager;
    }

    public void setLastDamager(UUID lastDamager) {
        this.lastDamager = lastDamager;
    }

    public Material getPureHat() {
        return this.hat;
    }

    public void setHat(Material hat) {
        needSave = true;
        this.hat = hat;
    }

    public Particle getArrowEffect() {
        if (Bukkit.getPlayer(uuid).hasPermission(Permission.HAT)) {
            return this.arrowEffect;
        }
        return null;
    }

    public Particle getPureArrowEffect() {
        return this.arrowEffect;
    }

    public String getSaveArrowEffect() {
        if (arrowEffect == null) return "NAN";
        else return arrowEffect.toString();
    }

    public void setArrowEffect(String arrowEffect) {
        needSave = true;
        if (arrowEffect.equals("NAN")) this.arrowEffect = null;
        else this.arrowEffect = Particle.valueOf(arrowEffect);
    }

    public void setArrowEffect(Particle arrowEffect) {
        needSave = true;
        this.arrowEffect = arrowEffect;
    }

    public int[] getInvOrder() {
        return invOrder;
    }

    public void setInvOrder(int[] invOrder) {
        needSave = true;
        this.invOrder = invOrder;
    }

    public void resetInvOrder() {
        needSave = true;
        invOrder = new int[] {1, 2, 3, 4, 8, 0, 0, 7, 5};
    }

    public String getInvOrderAsString() {
        StringBuilder invOrder = new StringBuilder(String.valueOf(this.invOrder[0]));
        for (int i = 1; i < this.invOrder.length; i++) {
            invOrder.append(";").append(this.invOrder[i]);
        }
        return invOrder.toString();
    }

    public void setInvOrder(String invOrderStr) {
        needSave = true;
        String[] invOrderSplit = invOrderStr.split(";");
        for (int i = 0; i < invOrderSplit.length; i++) {
            invOrder[i] = Integer.valueOf(invOrderSplit[i]);
        }
    }

    public int getItemPosition(Item item) {
        for (int i = 0; i < invOrder.length; i++) {
            if (Item.getFromId(invOrder[i]).equals(item))
                return i;
        }
        return -1;
    }

    public String getPrefix() {
        Player player = Bukkit.getPlayer(uuid);
        if (player.hasPermission(Permission.SHOW_PREFIX)) {
            String prefix = LuckPermsProvider.get().getUserManager().getUser(uuid).getCachedData().getMetaData(QueryOptions.nonContextual()).getPrefix();
            if (prefix != null && !prefix.equals("")) {
                prefix = ChatColor.translateAlternateColorCodes('&', prefix);
                return prefix;
            }
        }
        return Rank.getRank(player).getPrefix();
    }
}

