package de.marciboy999.PvPArena.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import de.marciboy999.PvPArena.listeners.Death;
import de.marciboy999.PvPArena.listeners.Move;
import de.marciboy999.PvPArena.listeners.Quit;

public class Main extends JavaPlugin{
	
	public static Main m;
	
	public static String pr = "§0[§cYYCArena§0] §a";
	
	public static List<Arena> arenas = new ArrayList<Arena>();
	public static HashMap<String, String> ask = new HashMap<String, String>();
	public static HashMap<String, Integer> askArena = new HashMap<String, Integer>();
	public static HashMap<String, Double> einsatz = new HashMap<String, Double>();
	
	public static HashMap<String, ItemStack[]> inventory = new HashMap<String, ItemStack[]>();
	public static HashMap<String, ItemStack[]> armor = new HashMap<String, ItemStack[]>();
	public static HashMap<String, Double> hearts = new HashMap<String, Double>();
	public static HashMap<String, Integer> foodlevel = new HashMap<String, Integer>();
	public static HashMap<String, Integer> level = new HashMap<String, Integer>();
	public static HashMap<String, Float> exp = new HashMap<String, Float>();
	public static HashMap<String, Location> lastLoc = new HashMap<String, Location>();
	
	public static HashMap<String, Integer> spectator = new HashMap<String, Integer>();
	
	public static Economy economy = null;
	
	@Override
	public void onEnable(){
		m = this;
		setupEconomy();
		
		File path = new File("plugins/YCCPvP/arenas");
		path.mkdirs();
		int id = 0;
		for(File file : path.listFiles()){
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			id++;
			String name = cfg.getString("name");
			boolean enabled = cfg.getBoolean("enabled");
			Location bluespawn = analyseLocation(cfg.getString("spawn.blue"));
			Location redspawn = analyseLocation(cfg.getString("spawn.red"));
			Location firstbluespawn = analyseLocation(cfg.getString("firstspawn.blue"));
			Location firstredspawn = analyseLocation(cfg.getString("spawn.red"));
			Location watch = analyseLocation(cfg.getString("watch"));
			List<ItemStack> inv = new ArrayList<ItemStack>();
			for(String stack : cfg.getConfigurationSection("inventory").getKeys(false)){
				inv.add(cfg.getItemStack("inventory." + stack));
			}
			ItemStack helmet = cfg.getItemStack("helmet");
			ItemStack chestplate = cfg.getItemStack("chestplate");
			ItemStack leggings = cfg.getItemStack("leggings");
			ItemStack boots = cfg.getItemStack("boots");
			
			Arena arena = new Arena(name, id, bluespawn, redspawn, firstredspawn, firstbluespawn, watch, enabled, inv, helmet, chestplate, leggings, boots);
			arenas.add(arena);
		}
		System.out.println("[YCCPvP] " + id + " Arenen wurden geladen");
		ArenaManager.start();
		this.getCommand("yccpvp").setExecutor(new Commands());
		
		PluginManager pm = Bukkit.getServer().getPluginManager();
		pm.registerEvents(new Death(), this);
		pm.registerEvents(new Move(), this);
		pm.registerEvents(new Quit(), this);
	}
	
	@Override
	public void onDisable(){
		for(Arena arena : arenas){
			File file = new File("plugins/YCCPvP/arenas", arena.getName() + ".yml");
			if(!file.exists()){
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			cfg.set("name", arena.getName());
			cfg.set("enabled", arena.isEnabled());
			cfg.set("spawn.blue", arena.getBlue().getWorld().getName() + "," + arena.getBlue().getX() + "," + arena.getBlue().getY() + "," + arena.getBlue().getZ() + "," + arena.getBlue().getYaw() + "," + arena.getBlue().getPitch());
			cfg.set("spawn.red", arena.getRed().getWorld().getName() + "," + arena.getRed().getX() + "," + arena.getRed().getY() + "," + arena.getRed().getZ() + "," + arena.getRed().getYaw() + "," + arena.getRed().getPitch());
			
			cfg.set("firstspawn.blue", arena.getBlueFirst().getWorld().getName() + "," + arena.getBlueFirst().getX() + "," + arena.getBlueFirst().getY() + "," + arena.getBlueFirst().getZ() + "," + arena.getBlueFirst().getYaw() + "," + arena.getBlueFirst().getPitch());
			cfg.set("firstspawn.red", arena.getRedFirst().getWorld().getName() + "," + arena.getRedFirst().getX() + "," + arena.getRedFirst().getY() + "," + arena.getRedFirst().getZ() + "," + arena.getRedFirst().getYaw() + "," + arena.getRedFirst().getPitch());
			
			cfg.set("watch", arena.getWatch().getWorld().getName() + "," + arena.getWatch().getX() + "," + arena.getWatch().getY() + "," + arena.getWatch().getZ() + "," + arena.getWatch().getYaw() + "," + arena.getWatch().getPitch());
			
			int slot = 0;
			for(ItemStack is : arena.inv){
				cfg.set("inventory." + slot, is);
				slot++;
			}
			
			cfg.set("helmet", arena.helmet);
			cfg.set("chestplate", arena.chestplate);
			cfg.set("leggings", arena.leggings);
			cfg.set("boots", arena.boots);
			
			try {
				cfg.save(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Location analyseLocation(String raw){
		String[] raws = raw.split(",");
		String world = raws[0];
		double x = Double.parseDouble(raws[1]);
		double y = Double.parseDouble(raws[2]);
		double z = Double.parseDouble(raws[3]);
		double yaw = Double.parseDouble(raws[4]);
		double pitch = Double.parseDouble(raws[5]);
		
		Location loc = new Location(Bukkit.getWorld(world), x, y, z);
		loc.setYaw((float)yaw);
		loc.setPitch((float)pitch);
		return loc;
	}
	
	public static Main getInstance(){
		return m;
	}
	
	private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public static Arena getArena(int id){
		for(Arena a : arenas){
			if(a.getID() == id){
				return a;
			}
		}
		return null;
	}
	
	public static Arena getArena(String name){
		for(Arena a : arenas){
			if(a.getName().equalsIgnoreCase(name)){
				return a;
			}
		}
		return null;
	}
	
	public static Arena getArena(Player p){
		for(Arena a : arenas){
			if(a.getBluePlayer() == null || a.getRedPlayer() == null){
				continue;
			}
			if(a.getBluePlayer().getName().equals( p.getName()) || a.getRedPlayer().getName().equals( p.getName())){
				return a;
			}
		}
		return null;
	}
	
	public static void prepareAsk(final Player p, String name, String tName, double betrag){
		final Player t = Bukkit.getPlayer(tName);
		if(t== null){
			p.sendMessage(Main.pr + "§cSpieler §e" + tName + " §cist nicht Online");
			return;
		}
		if(ask.containsKey(p.getName())){
			p.sendMessage(Main.pr + "§cDu hast bereits einem Spieler eine Anfrage gesendet");
			return;
		}
		if(getArena(p) != null){
			p.sendMessage(Main.pr + "§cDu bist bereits in einem Kampf");
			return;
		}
//		if(betrag >= 30.0){
//			
//			if(!economy.has(p.getName(), betrag)){
//				p.sendMessage(Main.pr + "§cDu hast nicht §e" + betrag + " Berry");
//				return;
//			}
//			if(!economy.has(t.getName(), betrag)){
//				p.sendMessage(Main.pr + "§cDein Gegner hat nicht genug Geld dafür");
//				return;
//			}
//		}else{
//			p.sendMessage(Main.pr + "§cDer Wettseinsatz muss mindestens 30 Berry betragen");
//			return;
//		}
		if(ask.containsKey(t.getName())){
			p.sendMessage(Main.pr + "§cSpieler §e" + t.getName() + "§c hat bereits selber eine Anfrage an einen Spieler geschickt");
			return;
		}
		if(getArena(t) != null){
			p.sendMessage(Main.pr + "§cSpieler §e" + t.getName() + " §cist bereits in einem Kampf");
			return;
		}
		
		Arena arena = null;
		try{
			int id = Integer.parseInt(name);
			arena = getArena(id);
		}catch(NumberFormatException e){
			arena = getArena(name);
		}
		final Arena test = arena;
		
		if(arena == null){
			p.sendMessage(Main.pr + "§cDiese Arena existiert nicht");
			return;
		}
		if(arena.isWaiting() || arena.isRunning() || arena.isMaybe()){
			p.sendMessage(Main.pr + "§cDiese Arena ist momentan belegt");
			return;
		}
//		economy.withdrawPlayer(p.getName(), betrag);
		einsatz.put(p.getName(), betrag);
		ask.put(p.getName(), t.getName());
		askArena.put(p.getName(), arena.getID());
		t.sendMessage(Main.pr + "§aSpieler §e" + p.getDisplayName() + " §afordert dich zu einem Kampf in der §5Arena " + arena.getName() + " §aum §4"+betrag+" Berry §aheraus");
		t.sendMessage(Main.pr + "§3Um den Kampf anzunehmen, tippe §2§o/yccpvp arena accept " + p.getName());
		t.sendMessage(Main.pr + "§3Um den Kampf abzulehnen, tippe §2§o/yccpvp arena deny " + p.getName());
		p.sendMessage(Main.pr + "§aAnfrage wurde verschickt");
		arena.setBluePlayer(p.getName());
		arena.setMaybe(true);
		Bukkit.getScheduler().scheduleSyncDelayedTask(m, new Runnable(){

			@Override
			public void run() {
				if(p != null && t != null){
					if(ask.containsKey(p.getName())){
						if(ask.get(p.getName()).equalsIgnoreCase(t.getName())){
							ask.remove(p.getName());
							askArena.remove(p.getName());
							p.sendMessage(Main.pr + "§cSpieler §e" + t.getName() + "§c hat auf deine Anfrage nicht reagiert, daher wurde diese gelöscht.");
							test.setBluePlayer(null);
							test.setMaybe(false);
						}
					}
				}
				
			}
			
		}, 20*60);
	}

}
