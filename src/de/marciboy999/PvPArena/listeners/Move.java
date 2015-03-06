package de.marciboy999.PvPArena.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.marciboy999.PvPArena.main.Arena;
import de.marciboy999.PvPArena.main.Main;

public class Move implements Listener{
	
	@EventHandler
	public void onMove(PlayerMoveEvent e){
		Player p = e.getPlayer();
		if(Main.spectator.containsKey(p.getName())){
			Arena arena = Main.getArena(Main.spectator.get(p.getName()));
			Location loc = p.getLocation();
			if(loc.getY() < arena.getWatch().getY() || loc.getY() > arena.getWatch().getY()){
				loc.setY(arena.getWatch().getY());
				p.teleport(loc);
				p.setFlying(true);
			}
			
		}
	}

}
