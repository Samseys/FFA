package me.samsey.ffa.events;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class OtherEvents implements Listener {

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (event.getEntity() instanceof Player)
			return;

		event.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent event) {
		if (event.toWeatherState()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onLeafDecay(LeavesDecayEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void noUpRoot(PlayerInteractEvent event) {
		if(event.getAction() == Action.PHYSICAL && event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.FARMLAND) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event){
		event.setCancelled(true);
	}
}
