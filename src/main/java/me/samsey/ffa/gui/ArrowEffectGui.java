package me.samsey.ffa.gui;

import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.InventoryContents;
import fr.minuskube.inv.content.InventoryProvider;
import me.samsey.ffa.Config;
import me.samsey.ffa.FFA;
import me.samsey.ffa.player.FFAPlayer;
import me.samsey.ffa.utils.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ArrowEffectGui implements InventoryProvider {
    private final Map<String, Particle> allowed = new HashMap<String, Particle>() {{
        put("Lava", Particle.LAVA);
        put("Fiamme", Particle.FLAME);
        put("Slime", Particle.SLIME);
        put("Note", Particle.NOTE);
        put("Cuori", Particle.HEART);
        put("Fumo", Particle.SMOKE_NORMAL);
    }};

    public static final SmartInventory INVENTORY = SmartInventory.builder()
            .id("arrowEffect")
            .provider(new ArrowEffectGui())
            .size(1, 9)
            .title(ChatColor.BLACK + "Arrow Effect")
            .build();

    @Override
    public void init(Player player, InventoryContents inventoryContents) {
        FFAPlayer ffaPlayer = FFA.getOnlinePlayerData(player);

        inventoryContents.set(0, 0, ClickableItem.of(
                new ItemUtil(Material.BARRIER).name("§cRimuovi Effetto").make(),
                e -> {
                    ffaPlayer.setArrowEffect((Particle) null);
                    player.sendMessage(Config.getPrefix() + "Effetto rimosso!");
                    INVENTORY.close(player);
                }
        ));

        Particle playerParticle = ffaPlayer.getArrowEffect();
        int col = 1, row = 0;
        for (Map.Entry<String, Particle> entry : allowed.entrySet()) {
            String name = entry.getKey();
            Particle particle = entry.getValue();
            Material material;
            if (particle.equals(playerParticle)) material = Material.LIME_DYE;
            else material = Material.GRAY_DYE;

            inventoryContents.set(row, col, ClickableItem.of(
                    new ItemUtil(material).name("§7" + name).make(),
                    e -> {
                        if (material.equals(Material.LIME_DYE)) return;
                        ffaPlayer.setArrowEffect(particle);
                        player.sendMessage(Config.getPrefix() + "Hai scelto l'effetto §3" + name);
                        INVENTORY.close(player);
                    }
            ));
            if (++col == 9) {
                col = 0;
                row++;
            }
        }

    }

    @Override
    public void update(Player player, InventoryContents inventoryContents) {

    }
}
