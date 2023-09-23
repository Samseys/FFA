package me.samsey.ffa.player.kit;

import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public enum Item {
    NULL(null),
    SWORD(new ItemUtil(Material.IRON_SWORD)
            .name("§7Spada")
            .setUnbreakable(true)
            .make()),
    FLINT_AND_STEEL(new ItemUtil(Material.FLINT_AND_STEEL)
            .name("§7Acciarino")
            .make()),
    FISHING_ROD(new ItemUtil(Material.FISHING_ROD)
            .name("§7Canna da pesca")
            .setUnbreakable(true)
            .make()),
    BOW(new ItemUtil(Material.BOW)
            .name("§7Arco")
            .setUnbreakable(true)
            .make()),
    ARROW(new ItemUtil(Material.ARROW)
            .name("§7Freccia")
            .amount(5)
            .make()),
    AUTOJOIN_ON(new ItemUtil(Material.LIME_DYE)
            .name("§3AutoJoin: §aON")
            .addStringData("clickable", "autojoin_on")
            .make()),
    AUTOJOIN_OFF(new ItemUtil(Material.GRAY_DYE)
            .name("§3AutoJoin: §cOFF")
            .addStringData("clickable", "autojoin_off")
            .make()),
    COMPASS(new ItemUtil(Material.COMPASS)
            .name("§7Player Tracker")
            .addStringData("clickable", "player_tracker")
            .make()),
    CHESTPLATE(new ItemUtil(Material.IRON_CHESTPLATE)
            .name("§7Corazza")
            .setUnbreakable(true)
            .make()),
    LEGGINGS(new ItemUtil(Material.IRON_LEGGINGS)
            .name("§7Gambiera")
            .setUnbreakable(true)
            .make()),
    BOOTS(new ItemUtil(Material.IRON_BOOTS)
            .name("§7Stivali")
            .setUnbreakable(true)
            .make()),
    JOIN(new ItemUtil(Material.BLAZE_POWDER)
            .name("§3Gioca")
            .addStringData("clickable", "join")
            .make()),
    RANKS(new ItemUtil(Material.NAME_TAG)
            .name("§3Ranks")
            .addStringData("clickable", "ranks")
            .make()),
    VIP_GUI(new ItemUtil(Material.NETHER_STAR)
            .name("§3Opzioni VIP")
            .addStringData("clickable", "vipgui")
            .make()),
    KS_1(new ItemUtil(Material.GOLDEN_SWORD)
            .name("§c§lSpada infuocata")
            .damage(28)
            .enchantment(Enchantment.FIRE_ASPECT, 2)
            .make()),
    KS_2(new ItemUtil(Material.GOLDEN_SWORD)
            .name("§c§lSpada pesante")
            .damage(32)
            .enchantment(Enchantment.DAMAGE_ALL, 4)
            .make()),
    KS_3(new ItemUtil(Material.DIAMOND_SWORD)
            .name("§a§lMaster Sword")
            .damage(1559)
            .enchantment(Enchantment.DAMAGE_ALL, 10)
            .make()),
    ;

    ItemStack itemStack;

    Item(ItemStack item) {
        this.itemStack = item;
    }

    public int getId() {
        return this.ordinal();
    }

    public ItemStack getItemStack() {
        if(itemStack == null) return null;
        return itemStack.clone();
    }

    public static Item getFromId(int id) {
        return values()[id];
    }
}