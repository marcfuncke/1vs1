package de.marciboy999.PvPArena.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import de.marciboy999.PvPArena.main.Main;

public class FoodLevelChange implements Listener{
	
	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e){
		Player p = (Player)e.getEntity();
		if(Main.getArena(p) != null){
			e.setCancelled(true);
		}
		if(Main.spectator.containsKey(p.getName())){
			e.setCancelled(true);
		}
	}

}
