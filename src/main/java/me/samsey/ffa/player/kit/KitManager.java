package me.samsey.ffa.player.kit;

import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.player.stats.Rank;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class KitManager {

    public static void giveKit(Player player) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        int[] invOrder = ffaPlayer.getInvOrder();
        for(int i = 0; i < invOrder.length; i++) {
            inventory.setItem(i, Item.getFromId(invOrder[i]).getItemStack());
        }
        replaceAutoJoinItem(player);
    }

    public static void giveArmor(Player player) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        PlayerInventory inventory = player.getInventory();

        Material material = ffaPlayer.getHat();
        if (material.equals(Material.AIR)) {
            Rank rank = Rank.getRank(player);
            material = rank.getHat();
        }

        ItemStack[] armorContents = {
                Item.BOOTS.getItemStack(),
                Item.LEGGINGS.getItemStack(),
                Item.CHESTPLATE.getItemStack(),
                new ItemUtil(material).make()};

        inventory.setArmorContents(armorContents);
    }

    public static void giveLobbyItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        inventory.clear();
        inventory.setArmorContents(null);
        inventory.setItem(1, Item.JOIN.getItemStack());
        inventory.setItem(3, Item.RANKS.getItemStack());
        ItemStack autojoin;
        if (FFA.getOnlinePlayerData(player).getAutoJoin()) {
            autojoin = Item.AUTOJOIN_ON.getItemStack();
        } else {
            autojoin = Item.AUTOJOIN_OFF.getItemStack();
        }
        inventory.setItem(5, autojoin);
        inventory.setItem(7, Item.VIP_GUI.getItemStack());
    }

    public static boolean saveOrder(Player player) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        PlayerInventory inventory = player.getInventory();

        ItemStack[] contents = inventory.getContents();
        boolean sword = false, fns = false, bow = false, arrow = false, fish = false, autojoin = false, compass = false;

        int[] invOrder = new int[9];

        for(int i = 0; i < 9; i++) {
            ItemStack itemStack = contents[i];
            if (itemStack == null || itemStack.getType().equals(Material.AIR))
                continue;

            boolean isAcceptable = false;
            for (int j = 1; j <= 8; j++) {
                Item item = Item.getFromId(j);
                if (itemStack.equals(item.getItemStack())) {
                    Material type = itemStack.getType();
                    switch (type) {
                        case IRON_SWORD:
                            if (sword) continue;
                            sword = true;
                            break;
                        case FLINT_AND_STEEL:
                            if (fns) continue;
                            fns = true;
                            break;
                        case BOW:
                            if (bow) continue;
                            bow = true;
                            break;
                        case ARROW:
                            if (arrow) continue;
                            arrow = true;
                            break;
                        case FISHING_ROD:
                            if (fish) continue;
                            fish = true;
                            break;
                        case LIME_DYE:
                        case GRAY_DYE:
                            if (autojoin) continue;
                            autojoin = true;
                            break;
                        case COMPASS:
                            if(compass) continue;
                            compass = true;
                            break;
                        default:
                            continue;
                    }
                    isAcceptable = true;
                    invOrder[i] = item.getId();
                }
            }
            if (!isAcceptable)
                return false;
        }

        if (!sword || !fns || !bow || !arrow || !fish || !autojoin || !compass)
            return false;

        ffaPlayer.setInvOrder(invOrder);
        return true;
    }

    public static void replaceAutoJoinItem(Player player) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);
        PlayerInventory inventory = player.getInventory();

        int slot = 10;
        if (inventory.contains(Material.LIME_DYE)) {
            slot = inventory.first(Material.LIME_DYE);
        } else if (inventory.contains(Material.GRAY_DYE)) {
            slot = inventory.first(Material.GRAY_DYE);
        }

        if (ffaPlayer.getAutoJoin()) {
            inventory.setItem(slot, Item.AUTOJOIN_ON.getItemStack());
        } else {
            inventory.setItem(slot, Item.AUTOJOIN_OFF.getItemStack());
        }
    }
}
