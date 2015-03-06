package de.marciboy999.PvPArena.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import de.marciboy999.PvPArena.main.Arena;
import de.marciboy999.PvPArena.main.Main;

public class Death implements Listener{
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e){
		Player p = e.getEntity();
		Arena arena = Main.getArena(p);
		if(arena != null){
			if(arena.isRunning()){
				arena.sendMessage(Main.pr + "§e" + p.getDisplayName() + " §cist gestorben");
				p.setHealth(20.0D);
				arena.resetTime();
				Player blue = arena.getBluePlayer();
				Player red = arena.getRedPlayer();
				
				blue.teleport(Main.lastLoc.get(blue.getName()));
				blue.getInventory().setContents(Main.inventory.get(blue.getName()));
				blue.getInventory().setArmorContents(Main.armor.get(blue.getName()));
				blue.setHealth(Main.hearts.get(blue.getName()));
				blue.setLevel(Main.level.get(blue.getName()));
				blue.setExp(Main.exp.get(blue.getName()));
				blue.setFoodLevel(Main.foodlevel.get(blue.getName()));
				blue.setFireTicks(0);
				
				red.teleport(Main.lastLoc.get(red.getName()));
				red.getInventory().setContents(Main.inventory.get(red.getName()));
				red.getInventory().setArmorContents(Main.armor.get(red.getName()));
				red.setHealth(Main.hearts.get(red.getName()));
				red.setLevel(Main.level.get(red.getName()));
				red.setExp(Main.exp.get(red.getName()));
				red.setFoodLevel(Main.foodlevel.get(red.getName()));
				red.setFireTicks(0);
				
				Main.inventory.remove(blue.getName());
				Main.armor.remove(blue.getName());
				Main.hearts.remove(blue.getName());
				Main.foodlevel.remove(blue.getName());
				Main.level.remove(blue.getName());
				Main.exp.remove(blue.getName());
				Main.lastLoc.remove(blue.getName());
				
				Main.inventory.remove(red.getName());
				Main.armor.remove(red.getName());
				Main.hearts.remove(red.getName());
				Main.foodlevel.remove(red.getName());
				Main.level.remove(red.getName());
				Main.exp.remove(red.getName());
				Main.lastLoc.remove(red.getName());
				
				Main.ask.remove(blue.getName());
				Main.askArena.remove(blue.getName());
				
				//Geld geben
				
				Main.einsatz.remove(blue.getName());
				
				Player winner = null;
				if(blue.getName().equalsIgnoreCase(p.getName())){
					winner = red;
				}else{
					winner = blue;
				}
				
				Bukkit.broadcastMessage(Main.pr + "§a" + winner.getDisplayName() + " §5hat den Kampf in Arena §6" + arena.getName() + " §5gewonnen");
				Bukkit.broadcastMessage(Main.pr + "§bUm jemanden herauszufordern, schreibe /YCCPvP arena help");
				arena.reset();
			}
		}
	}

}
