package me.samsey.ffa.utils;

import me.samsey.ffa.FFA;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * NOTICE: This utility was developer as part of AeolusLib. While you can use it for your own projects, You are NOT allowed to delete or move this header comment.
 *
 * Utility:
 * Chainable {@link ItemStack}s
 *
 * Example Usage(s):
 * {@code ItemStack itemStack = new ItemBuilder(Material.SKULL_ITEM).amount(1).durability(3).skullOwner("MCAeolus").name(ChatColor.RED+"MCAeolus's Skull").make())}
 * {@code ItemStack itemStack = new ItemBuilder().type(Material.BEDROCK).lores(new String[]{"Lore1",ChatColor.RED+"Lore2"}).enchantment(Enchantment.DAMAGE_ALL, 99).make()}
 *
 * @author MCAeolus
 * @version 1.0
 */

public class ItemUtil {

    private final ItemStack item;
    private final ItemMeta itemM;

    /**
     * Init item chainable via given Material parameter.
     *
     * @param itemType
     *              the {@link Material} to initiate the instance with.
     *
     * @since 1.0
     */
    public ItemUtil(final Material itemType){
        item = new ItemStack(itemType);
        itemM = item.getItemMeta();
    }

    /**
     * Init item chainable via given ItemStack parameter.
     *
     * @param itemStack
     *              the {@link ItemStack} to initialize the instance with.
     *
     * @since 1.0
     */
    public ItemUtil(final ItemStack itemStack){
        item = itemStack;
        itemM = item.getItemMeta();
    }

    /**
     * Changes the Material type of the {@link ItemStack}
     *
     * @param material
     *              the new {@link Material} to set for the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil type(final Material material){
        make().setType(material);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s size.
     *
     * @param itemAmt
     *              the new Integer count of the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil amount(final Integer itemAmt){
        make().setAmount(itemAmt);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s display name.
     *
     * @param name
     *          the new String for the ItemStack's display name to be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil name(final String name){
        meta().setDisplayName(name);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore
     *          String you want to add to the ItemStack's lore.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil lore(final String lore){
        List<String> lores = meta().getLore();
        if(lores == null){lores = new ArrayList<>();}
        lores.add(lore);
        meta().setLore(lores);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Clears the {@link ItemStack}s lore and replaces it with the defined String array.
     *
     * @param lores
     *            String array you want to set the ItemStack's lore to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil lores(final String lores){
        List<String> loresList = meta().getLore();
        if(loresList == null){loresList = new ArrayList<>();}
        else{loresList.clear();}
        Collections.addAll(loresList, lores);
        meta().setLore(loresList);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Clears the {@link ItemStack}s lore and replaces it with the defined String arrayList.
     *
     * @param lores
     *            String List you want to set the ItemStack's lore to.
     *s
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil lores(final List<String> lores){
        meta().setLore(lores);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Changes the durability of the current {@link ItemStack}
     *
     * @param damage
     *              the new int amount to set the ItemStack's durability to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil damage(final int damage){
        Damageable meta = (Damageable) meta();
        meta.setDamage(damage);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@link ItemStack} with a defined level int value.
     *
     * @param enchantment
     *              the {@link Enchantment} to add to the ItemStack.
     *
     * @param level
     *          the int amount that the Enchantment's level will be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil enchantment(final Enchantment enchantment, final int level){
        make().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@Link} with a level int value of 1.
     *
     * @param enchantment
     *              the {@link Enchantment} to add to the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil enchantment(final Enchantment enchantment){
        make().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack.
     *
     * @param enchantments
     *              the Enchantment array to replace any current enchantments applied on the ItemStack.
     *
     * @param level
     *              the int level value for all Enchantments to be set to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil enchantments(final Enchantment[] enchantments, final int level){
        make().getEnchantments().clear();
        for(Enchantment enchantment : enchantments){
            make().addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack with a level int value of 1.
     *
     * @param enchantments
     *              the Enchantment array to replace any current enchantments applied on the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil enchantments(final Enchantment[] enchantments){
        make().getEnchantments().clear();
        for(Enchantment enchantment : enchantments){
            make().addUnsafeEnchantment(enchantment, 1);
        }
        return this;
    }

    /**
     * Clears the defined {@link Enchantment} from the {@link ItemStack}
     *
     * @param enchantment
     *              the Enchantment to remove from the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil clearEnchantment(final Enchantment enchantment){
        Map<Enchantment, Integer> itemEnchantments = make().getEnchantments();
        for(Enchantment enchantmentC : itemEnchantments.keySet()){
            if(enchantment == enchantmentC){
                itemEnchantments.remove(enchantmentC);
            }
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil clearEnchantments(){
        make().getEnchantments().clear();
        return this;
    }

    /**
     * Add a flag to the {@link ItemStack}
     *
     * @param flags
     *          Flags you want to set to the itemstack
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil addFlag(ItemFlag... flags) {
        meta().addItemFlags(flags);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Clears the defined {@link String} of lore from the {@link ItemStack}
     *
     * @param lore
     *          the String to be removed from the ItemStack.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil clearLore(final String lore){
        meta().getLore().remove(lore);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Clears all lore {@link String}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil clearLores(){
        meta().getLore().clear();
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Sets the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @param color
     *          the Color to set the LEATHER_ARMOR ItemStack to.
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil color(final Color color){
        if(make().getType() == Material.LEATHER_HELMET
                || make().getType() == Material.LEATHER_CHESTPLATE
                || make().getType() == Material.LEATHER_LEGGINGS
                || make().getType() == Material.LEATHER_BOOTS){
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(color);
            make().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clears the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil clearColor(){
        if(make().getType() == Material.LEATHER_HELMET
                || make().getType() == Material.LEATHER_CHESTPLATE
                || make().getType() == Material.LEATHER_LEGGINGS
                || make().getType() == Material.LEATHER_BOOTS ){
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(null);
            make().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Sets the skullOwner {@link SkullMeta} of the current PLAYER_HEAD {@link Material} type {@link ItemStack}
     *
     * @param name
     *          the {@link String} value to set the SkullOwner meta to for the PLAYER_HEAD Material type ItemStack.
     *
     * @return the current instance for chainable application
     * @since 1.0
     */
    public ItemUtil skullOwner(final String name){
        if(make().getType() == Material.PLAYER_HEAD){
            SkullMeta skullMeta = (SkullMeta) meta();
            skullMeta.setOwner(name);
            make().setItemMeta(meta());
        }
        return this;
    }

    /**
     *  Add a {@link String} value linked to {@link String} key
     *
     * @param keyString
     *          the {@link String} key of the NBT tag
     * @param data
     *          the {@link String} value
     * @return the current instance for chainable application
     * @since 1.0
     */
    public ItemUtil addStringData(String keyString, String data) {
        NamespacedKey key = new NamespacedKey(FFA.getInstance(), keyString);
        ItemMeta meta = meta();
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, data);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Make item unbreakable or not
     *
     * @param unbreakable
     *          Booelan value that sets if the item is unbreakable
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemUtil setUnbreakable(boolean unbreakable) {
        ItemMeta meta = meta();
        meta.setUnbreakable(unbreakable);
        make().setItemMeta(meta());
        return this;
    }

    /**
     * Returns the {@link ItemMeta} of the {@link ItemStack}
     *
     * @return the ItemMeta of the ItemStack.
     */
    public ItemMeta meta(){
        return itemM;
    }

    /**
     * Returns the {@link ItemStack} of the {@link ItemUtil} instance.
     *
     * @return the ItemStack of the ItemBuilder instance.
     */
    public ItemStack make(){
        return item;
    }

    public static String getStringDataFromItemStack(ItemStack item, String keyString) {
        NamespacedKey key = new NamespacedKey(FFA.getInstance(), keyString);
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer tagContainer = meta.getPersistentDataContainer();

        if(tagContainer.has(key, PersistentDataType.STRING)) {
            return tagContainer.get(key, PersistentDataType.STRING);
        }
        return null;
    }
}
