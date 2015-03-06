package de.marciboy999.PvPArena.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffectType;

import de.marciboy999.PvPArena.main.Arena;
import de.marciboy999.PvPArena.main.Main;

public class Quit implements Listener{
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		Arena a = Main.getArena(p);
		if(Main.spectator.containsKey(p.getName())){
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			p.teleport(Main.lastLoc.get(p.getName()));
			Main.lastLoc.remove(p.getName());
			p.removePotionEffect(PotionEffectType.INVISIBILITY);
			Main.spectator.remove(p.getName());
		}
		if(a != null){
			a.resetTime();
			Player blue = a.getBluePlayer();
			Player red = a.getRedPlayer();
			
			blue.teleport(Main.lastLoc.get(blue.getName()));
			blue.getInventory().setContents(Main.inventory.get(blue.getName()));
			blue.getInventory().setArmorContents(Main.armor.get(blue.getName()));
			blue.setHealth(Main.hearts.get(blue.getName()));
			blue.setLevel(Main.level.get(blue.getName()));
			blue.setExp(Main.exp.get(blue.getName()));
			blue.setFoodLevel(Main.foodlevel.get(blue.getName()));
			
			red.teleport(Main.lastLoc.get(red.getName()));
			red.getInventory().setContents(Main.inventory.get(red.getName()));
			red.getInventory().setArmorContents(Main.armor.get(red.getName()));
			red.setHealth(Main.hearts.get(red.getName()));
			red.setLevel(Main.level.get(red.getName()));
			red.setExp(Main.exp.get(red.getName()));
			red.setFoodLevel(Main.foodlevel.get(red.getName()));
			
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
			
			Bukkit.broadcastMessage(Main.pr + "§a" + winner.getDisplayName() + " §5hat den Kampf in Arena §6" + a.getName() + " §5gewonnen");
			Bukkit.broadcastMessage(Main.pr + "§bUm jemanden herauszufordern, schreibe /YCCPvP arena help");
			a.reset();
		}
		if(Main.askArena.containsKey(p.getName())){
			Arena arena = Main.getArena(Main.askArena.get(p.getName()));
			arena.setBluePlayer(null);
			arena.setMaybe(false);
			Main.askArena.remove(p.getName());
		}
		if(Main.ask.containsValue(p.getName())){
			String remove = "";
			for(String name : Main.ask.keySet()){
				if(Main.ask.get(name).equalsIgnoreCase(p.getName())){
					Player t = Bukkit.getPlayer(name);
					t.sendMessage(Main.pr + "§cDer Spieler §e" + t.getName() + " §chat den Server verlassen");
					remove = name;
					Arena arena = Main.getArena(Main.askArena.get(name));
					arena.setMaybe(false);
					arena.setBluePlayer(null);
					Main.askArena.remove(name);
					break;
				}
			}
			Main.ask.remove(remove);
		}
		Main.ask.remove(p.getName());
	}

}
