package de.marciboy999.PvPArena.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class ArenaManager {
	
	public static void start(){
		Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable(){

			@Override
			public void run() {
				for(Arena arena : Main.arenas){
					if(arena.isEnabled()){
						if(arena.isRunning()){
							arena.updateTime();
							if(arena.getMin() == 4 && arena.getSek() == 59){
								arena.sendMessage(Main.pr + "§cDiese Runde endet in 5 Minuten");
							}
							if(arena.getMin() == 0 && arena.getSek() == 59){
								arena.sendMessage(Main.pr + "§cDiese Runde endet in 1 Minute");
							}
							if(arena.getMin() == 0 && arena.getSek() == 1){
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
								List<String> remove = new ArrayList<String>();
								for(String name : Main.spectator.keySet()){
									Player all = Bukkit.getPlayer(name);
									if(Main.spectator.get(name) == arena.getID()){
										all.removePotionEffect(PotionEffectType.INVISIBILITY);
										all.teleport(Main.lastLoc.get(all.getName()));
										Main.lastLoc.remove(all.getName());
										all.removePotionEffect(PotionEffectType.INVISIBILITY);
										remove.add(name);
									}
								}
								for(String rem : remove){
									Main.spectator.remove(rem);
								}
								
								arena.sendMessage(Main.pr + "§4Das Spiel ist vorbei. Keiner hat gewonnen");
								Bukkit.broadcastMessage(Main.pr + "§3Der Kampf in Arena §e" + arena.getName() + " §3ist Unentschieden ausgegangen");
								arena.reset();
							}
						}else if(arena.isWaiting()){
							arena.updateTime();
							if(arena.getLobbyTime() == 10 || arena.getLobbyTime() == 9 || arena.getLobbyTime() == 8 || arena.getLobbyTime() == 7 || arena.getLobbyTime() == 6 || arena.getLobbyTime() == 5 || arena.getLobbyTime() == 4 || arena.getLobbyTime() == 3 || arena.getLobbyTime() == 2){
								arena.sendMessage(Main.pr + "§eDer Kampf beginnt in §6" + arena.getLobbyTime() + " §eSekunden");
								arena.getBluePlayer().setLevel(arena.getLobbyTime());
								arena.getRedPlayer().setLevel(arena.getLobbyTime());
							}
							if(arena.getLobbyTime() == 1){
								arena.sendMessage(Main.pr + "§eDer Kampf beginnt in §6" + arena.getLobbyTime() + " §eSekunde");
								arena.getBluePlayer().setLevel(arena.getLobbyTime());
								arena.getRedPlayer().setLevel(arena.getLobbyTime());
							}
							if(arena.getLobbyTime() == 0){
								arena.getBluePlayer().setLevel(arena.getLobbyTime());
								arena.getRedPlayer().setLevel(arena.getLobbyTime());
								arena.sendMessage(Main.pr + "§a§lDer Kampf hat begonnen! Viel Glück!");
								arena.setWaiting(false);
								arena.setRunning(true);
								arena.getBluePlayer().teleport(arena.getBlue());
								arena.getRedPlayer().teleport(arena.getRed());
							}
						}
					}
				}
			}
			
		}, 20, 20);
	}

}
