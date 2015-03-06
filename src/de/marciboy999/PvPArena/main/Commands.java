package de.marciboy999.PvPArena.main;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Commands implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("yccpvp")) {
			if(cs instanceof Player){
				Player p = (Player)cs;
				if(args.length == 0){
					p.sendMessage(Main.pr + "§c/YCCPvP arena help");
					if(p.hasPermission("yccpvp.admin")){
						p.sendMessage(Main.pr + "§c/YCCPvP arena help admin");
					}
				}else{
					if(args[0].equalsIgnoreCase("arena")){
						if(args[1].equalsIgnoreCase("help")){
							if(args.length == 3){
								if(args[2].equalsIgnoreCase("admin") && p.hasPermission("yccpvp.admin")){
									p.sendMessage(Main.pr + "§c/YCCPvP admin createArena <Name>");
									p.sendMessage(Main.pr + "§c/YCCPvP admin setFirstSpawn <ArenaID> <Blau/Rot>");
									p.sendMessage(Main.pr + "§c/YCCPvP admin setArenaSpawn <ArenaID> <Blau/Rot>");
									p.sendMessage(Main.pr + "§c/YCCPvP admin setWatchSpawn <ArenaID>");
									p.sendMessage(Main.pr + "§c/YCCPvP admin setEnabled <Arena> <true/false>");
									p.sendMessage(Main.pr + "§c/YCCPvP admin saveInventory <ArenaID> §8>> §7Speichert das aktuelle Inventar für die Arena ab");
									p.sendMessage(Main.pr + "§c/YCCPvP admin resetArena <Arena> §8>> §7Nur für den Notfall");
								}
							}else{
								p.sendMessage(Main.pr + "§9>>> §1Hilfe §9<<<");
								p.sendMessage(Main.pr + "§7Um einen Spieler herauszufordern, musst du mindestens 30 Berry einsetzen");
								p.sendMessage(Main.pr + "§e/YCCPvP arena <ArenaID/Arena Name> <Spieler> <Wetteinsatz> §9-> §8Fordere einen Spieler heraus");
								p.sendMessage(Main.pr + "§e/YCCPvP arena accept [<Spieler>] §9-> §8Nimmt die Anfrage eines Spielers an");
								p.sendMessage(Main.pr + "§e/YCCPvP arena deny [<Spieler>] §9-> §8Lehnt die Anfrage eines Spielers ab");
								p.sendMessage(Main.pr + "§e/YCCPvP arena watch <ArenaID/Arena Name> §9-> §8Beobachte einen Kampf");
								p.sendMessage(Main.pr + "§e/YCCPvP watch leave §9-> §8Verlässt den Beobachter-Modus");
								p.sendMessage(Main.pr + "§e/YCCPvP arena list §9-> §8Listet alle verfügbaren Arenen auf");
							}
						}else if(args[1].equalsIgnoreCase("list")){
							p.sendMessage(Main.pr + "§5PvP Arenen:");
							for(Arena arena : Main.arenas){
								p.sendMessage(Main.pr + "§d" + arena.getName() + " §f(ID: " + arena.getID() + ")§e: §d" + arena.getStatus());
							}
						}else if(args[1].equalsIgnoreCase("accept")){
							if(args.length >= 3){
								String name = args[2];
								if(Main.ask.containsKey(name)){
									Player t = Bukkit.getPlayer(name);
									if(t != null && Main.ask.get(t.getName()).equalsIgnoreCase(p.getName())){
										
										//EconomyResponse er = Main.economy.depositPlayer(p.getName(), Main.einsatz.get(t.getName()));
										//if(!er.transactionSuccess()){
										//	p.sendMessage(Main.pr + "§cDu hast zu wenig Geld");
										//	return true;
										//}
										p.sendMessage(Main.pr + "§aDu hast die Anfrage angenommen");
										t.sendMessage(Main.pr + "§e" + p.getDisplayName() + "§a hat deine Anfrage angenommen"); 
										
										Arena arena = Main.getArena(Main.askArena.get(t.getName()));
										arena.setRedPlayer(p.getName());
										arena.setMaybe(false);
										arena.setWaiting(true);
										
										Main.inventory.put(p.getName(), p.getInventory().getContents());
										Main.armor.put(p.getName(), p.getInventory().getArmorContents());
										Main.hearts.put(p.getName(), p.getHealthScale());
										Main.foodlevel.put(p.getName(), p.getFoodLevel());
										Main.level.put(p.getName(), p.getLevel());
										Main.exp.put(p.getName(), p.getExp());
										Main.lastLoc.put(p.getName(), p.getLocation());
										
										Main.inventory.put(t.getName(), t.getInventory().getContents());
										Main.armor.put(t.getName(), t.getInventory().getArmorContents());
										Main.hearts.put(t.getName(), t.getHealthScale());
										Main.foodlevel.put(t.getName(), t.getFoodLevel());
										Main.level.put(t.getName(), t.getLevel());
										Main.exp.put(t.getName(), t.getExp());
										Main.lastLoc.put(t.getName(), t.getLocation());
										
										p.getInventory().clear();
										p.getInventory().setHelmet(null);
										p.getInventory().setChestplate(null);
										p.getInventory().setLeggings(null);
										p.getInventory().setBoots(null);
										p.setHealth(20.0D);
										p.setFoodLevel(20);
										p.setLevel(0);
										p.setExp(0.0f);
										
										t.getInventory().clear();
										t.getInventory().setHelmet(null);
										t.getInventory().setChestplate(null);
										t.getInventory().setLeggings(null);
										t.getInventory().setBoots(null);
										t.setHealth(20.0D);
										t.setFoodLevel(20);
										t.setLevel(0);
										t.setExp(0.0f);
										
										p.teleport(arena.getRedFirst());
										t.teleport(arena.getBlueFirst());
										
										for(ItemStack is : arena.inv){
											p.getInventory().addItem(is);
											t.getInventory().addItem(is);
										}
										
										p.getInventory().setHelmet(arena.helmet);
										p.getInventory().setChestplate(arena.chestplate);
										p.getInventory().setLeggings(arena.leggings);
										p.getInventory().setBoots(arena.boots);
										
										t.getInventory().setHelmet(arena.helmet);
										t.getInventory().setChestplate(arena.chestplate);
										t.getInventory().setLeggings(arena.leggings);
										t.getInventory().setBoots(arena.boots);
										
										Bukkit.broadcastMessage(Main.pr + "§e" + t.getDisplayName() + " §afordert §e" + p.getDisplayName() + " §aum §4" + Main.einsatz.get(t.getName()) + " Berry §aheraus! Der Kampf beginnt in 15 Sekunden...");
										Bukkit.broadcastMessage(Main.pr + "§dUm den Kampf zu beobachten, gib §c/YCCPvP arena watch " + arena.getID() + " §dein");
										
										Main.ask.remove(t.getName());
										Main.askArena.remove(t.getName());
										
									}else p.sendMessage(Main.pr + "§cDer Spieler §e" + name + " §chat dir keine Anfrage gesendet");
								}else p.sendMessage(Main.pr + "§cDer Spieler §e" + name + " §chat dir keine Anfrage gesendet");
							}else{
								p.sendMessage(Main.pr + "§e/YCCPvP arena accept [<Spieler>] §9-> §8Nimmt die Anfrage eines Spielers an");
							}
						}else if(args[1].equalsIgnoreCase("deny")){
							if(args.length >= 3){
								String name = args[2];
								if(Main.ask.containsKey(name)){
									Player t = Bukkit.getPlayer(name);
									if(t != null && Main.ask.get(t.getName()).equalsIgnoreCase(p.getName())){
										Main.ask.remove(t.getName());
										Main.askArena.remove(t.getName());
										Main.einsatz.remove(t.getName());
										t.sendMessage(Main.pr + "§cSpieler §e" + p.getDisplayName() + " §chat deine Anfrage abgelehnt");
										p.sendMessage(Main.pr + "§aAnfrage wurde abgelehnt");
									}else p.sendMessage(Main.pr + "§cDer Spieler §e" + name + " §chat dir keine Anfrage geschickt");
								}else p.sendMessage(Main.pr + "§cDer Spieler §e" + name + " §chat dir keine Anfrage geschickt");
							}else{
								p.sendMessage(Main.pr + "§e/YCCPvP arena deny [<Spieler>] §9-> §8Lehnt die Anfrage eines Spielers ab");
							}
						}else if(args[1].equalsIgnoreCase("watch") || args[1].equalsIgnoreCase("spectate")){
							if(args.length >= 3){
								String name = args[2];
								if(name.equalsIgnoreCase("leave")){
									if(Main.spectator.containsKey(p.getName())){
										p.setFlying(false);
										p.setAllowFlight(false);
										p.removePotionEffect(PotionEffectType.INVISIBILITY);
										p.teleport(Main.lastLoc.get(p.getName()));
										Main.lastLoc.remove(p.getName());
										p.removePotionEffect(PotionEffectType.INVISIBILITY);
										p.setFlying(false);
										p.setAllowFlight(false);
										Main.spectator.remove(p.getName());
										p.sendMessage(Main.pr + "§aDu hast den §bBeobachter-Modus §averlassen");
									}else p.sendMessage(Main.pr + "§cDu beobachtest momentan keinen Kampf");
									return true;
								}
								
								if(Main.getArena(p) != null){
									p.sendMessage(Main.pr + "§cDu bist bereits in einem Kampf");
									return true;
								}
								
								Arena arena = null;
								try{
									int id = Integer.parseInt(name);
									arena = Main.getArena(id);
								}catch(NumberFormatException e){
									arena = Main.getArena(name);
								}
								if(arena == null){
									p.sendMessage(Main.pr + "§cArena §e" + name + " §cexistiert nicht");
									return true;
								}
								if(arena.isRunning() || arena.isWaiting()){
									Main.lastLoc.remove(p.getName());
									Main.lastLoc.put(p.getName(), p.getLocation());
									p.teleport(arena.getWatch());
									Main.spectator.remove(p.getName());
									Main.spectator.put(p.getName(), arena.getID());
									p.sendMessage(Main.pr + "§cDu beobachtest nun Arena §e" + arena.getName());
									p.sendMessage(Main.pr + "§bSchreibe §9/YCCPvP arena watch leave §bzum Verlassen des Beobachter-Modus");
									p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 2));
									p.setAllowFlight(true);
									p.setFlying(true);
								}
							}else{
								p.sendMessage(Main.pr + "§e/YCCPvP arena "+args[1].toLowerCase()+" <ArenaID/Arena Name> §9-> §8Beobachte einen Kampf");
							}
						}else{
							if(args.length >= 4){
								String name = args[1];
								String spieler = args[2];
								String betragUn = args[3];
								
								Arena arena = null;
								try{
									int id = Integer.parseInt(name);
									arena = Main.getArena(id);
								}catch(NumberFormatException e){
									arena = Main.getArena(name);
								}
								if(arena == null){
									p.sendMessage(Main.pr + "§cArena §e" + name + "§c existiert nicht");
									return true;
								}
								Player t = Bukkit.getPlayer(spieler);
								if(t == null){
									p.sendMessage(Main.pr + "§cSpieler §e" + spieler + "§c ist nicht Online");
									return true;
								}
								try{
									double betrag = Double.parseDouble(betragUn);
									Main.prepareAsk(p, arena.getName(), t.getName(), betrag);
								}catch(NumberFormatException e){
									p.sendMessage(Main.pr + "§cDer Wetteinsatz muss eine Zahl sein");
									return true;
								}
							}else{
								p.sendMessage(Main.pr + "§e/YCCPvP arena <ArenaID/Arena Name> <Spieler> <Wetteinsatz> §9-> §8Fordere einen Spieler heraus");
							}
							
						}
						
					}else if(args[0].equalsIgnoreCase("admin") && p.hasPermission("yccpvp.admin")){
						if(args[1].equalsIgnoreCase("createArena")){
							if(args.length >= 3){
								int id = 1;
								for(Arena arena : Main.arenas){
									id++;
								}
								if(Main.getArena(args[2]) == null){
									Arena arena = new Arena(args[2], id, null, null, null, null, null, false, null, null, null, null, null);
									Main.arenas.add(arena);
									p.sendMessage(Main.pr + "§aArena §e" + args[2] + " §amit der ID §e"+id +" §awurde erstellt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert bereits");
							}else p.sendMessage(Main.pr + "§c/YCCPvP admin createArena <Name>");
						}else if(args[1].equalsIgnoreCase("setFirstSpawn")){
							if(args[3].equalsIgnoreCase("Blau")){
								Arena arena = Main.getArena(Integer.parseInt(args[2]));
								if(arena != null){
									arena.firstbluespawn = p.getLocation();
									p.sendMessage(Main.pr + "§aSpawn gesetzt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
							}
							if(args[3].equalsIgnoreCase("Rot")){
								Arena arena = Main.getArena(Integer.parseInt(args[2]));
								if(arena != null){
									arena.firstredspawn = p.getLocation();
									p.sendMessage(Main.pr + "§aSpawn gesetzt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
							}
						}else if(args[1].equalsIgnoreCase("setArenaSpawn")){
							if(args[3].equalsIgnoreCase("Blau")){
								Arena arena = Main.getArena(Integer.parseInt(args[2]));
								if(arena != null){
									arena.setBlue(p.getLocation());
									p.sendMessage(Main.pr + "§aSpawn gesetzt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
							}
							if(args[3].equalsIgnoreCase("Rot")){
								Arena arena = Main.getArena(Integer.parseInt(args[2]));
								if(arena != null){
									arena.setRed(p.getLocation());
									p.sendMessage(Main.pr + "§aSpawn gesetzt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
							}
						}else if(args[1].equalsIgnoreCase("setWatchSpawn")){
								Arena arena = Main.getArena(Integer.parseInt(args[2]));
								if(arena != null){
									arena.setWatch(p.getLocation());
									p.sendMessage(Main.pr + "§aSpawn gesetzt");
								}else p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
						}else if(args[1].equalsIgnoreCase("setEnabled")){
							Arena arena = Main.getArena(Integer.parseInt(args[2]));
							boolean enabled = Boolean.parseBoolean(args[3]);
							arena.setEnabled(enabled);
							p.sendMessage(Main.pr + "§aArena Status wurde auf §9" + enabled + " §agesetzt");
						}else if(args[1].equalsIgnoreCase("saveInventory")){
							Arena arena = Main.getArena(Integer.parseInt(args[2]));
							p.sendMessage(Main.pr + "§aInventar wird gespeichert...");
							List<ItemStack> inv = new ArrayList<ItemStack>();
							for(ItemStack is : p.getInventory().getContents()){
								inv.add(is);
							}
							arena.inv = inv;
							arena.helmet = p.getInventory().getHelmet();
							arena.chestplate = p.getInventory().getChestplate();
							arena.leggings = p.getInventory().getLeggings();
							arena.boots = p.getInventory().getBoots();
							p.sendMessage(Main.pr + "§aInventar wurde gespeichert.");
						}else if(args[1].equalsIgnoreCase("resetArena")){
							Arena arena = Main.getArena(Integer.parseInt(args[2]));
							arena.reset();
							p.sendMessage(Main.pr + "§bArena "+arena.getID()+" wurde resettet");
						}
					}
				}
			}else{
				//Console
			}
		}
		return false;
	}

}
