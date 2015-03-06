package de.marciboy999.PvPArena.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Arena {
	
	String name;
	int id;
	Location bluespawn;
	Location firstbluespawn;
	Location redspawn;
	Location firstredspawn;
	Location watchspawn;
	int timeMin = 15;
	int timeSek = 1;
	String redPlayer;
	String bluePlayer;
	boolean running;
	boolean waiting;
	boolean enabled;
	boolean maybe;
	int lobbyTime = 15;
	List<ItemStack> inv;
	ItemStack helmet;
	ItemStack chestplate;
	ItemStack leggings;
	ItemStack boots;
	
	public Arena(String name, int id, Location bluespawn, Location redspawn, Location firstredspawn, Location firstbluespawn, Location watchspawn, boolean enabled, List<ItemStack> inventory, ItemStack helm, ItemStack chest, ItemStack leg, ItemStack boot){
		this.name = name;
		this.id = id;
		this.bluespawn = bluespawn;
		this.redspawn = redspawn;
		this.firstbluespawn = firstbluespawn;
		this.firstredspawn = firstredspawn;
		this.watchspawn = watchspawn;
		this.enabled = enabled;
		running = false;
		waiting = false;
		maybe = false;
		redPlayer = null;
		bluePlayer = null;
		this.inv = inventory;
		helmet = helm;
		chestplate = chest;
		leggings = leg;
		boots = boot;
		
	}
	
	public void reset(){
		running = false;
		waiting = false;
		maybe = false;
		redPlayer = null;
		bluePlayer = null;
	}
	
	public void resetTime(){
		timeMin = 15;
		timeSek = 1;
		lobbyTime = 15;
	}
	
	public int getID(){
		return id;
	}
	
	public String getName(){
		return name;
	}
	
	public Location getBlue(){
		return bluespawn;
	}
	
	public Location getRed(){
		return redspawn;
	}
	
	public Location getBlueFirst(){
		return firstbluespawn;
	}
	
	public Location getRedFirst(){
		return firstredspawn;
	}
	
	public Location getWatch(){
		return watchspawn;
	}
	
	public boolean isEnabled(){
		return enabled;
	}
	
	public boolean isWaiting(){
		return waiting;
	}
	
	public boolean isRunning(){
		return running;
	}
	
	public boolean isMaybe(){
		return maybe;
	}
	
	public void setBlue(Location loc){
		bluespawn = loc;
	}
	
	public void setRed(Location loc){
		redspawn = loc;
	}
	
	public void setBluePlayer(String name){
		this.bluePlayer = name;
	}
	
	public void setRedPlayer(String name){
		this.redPlayer = name;
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	
	public void setRunning(boolean running){
		this.running = running;
	}
	
	public void setWaiting(boolean waiting){
		this.waiting = waiting;
	}
	public void setMaybe(boolean maybe){
		this.maybe = maybe;
	}
	
	public void setWatch(Location loc){
		watchspawn = loc;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Player getBluePlayer(){
		if(bluePlayer == null){
			return null;
		}
		return Bukkit.getPlayer(bluePlayer);
	}
	
	public Player getRedPlayer(){
		if(redPlayer == null){
			return null;
		}
		return Bukkit.getPlayer(redPlayer);
	}
	
	public void updateTime(){
		if(waiting){
			lobbyTime--;
			return;
		}
		timeSek--;
		if(timeSek == 0){
			timeMin--;
			timeSek = 59;
		}
	}
	
	public int getMin(){
		return this.timeMin;
	}
	
	public int getSek(){
		return this.timeSek;
	}
	
	public int getLobbyTime(){
		return this.lobbyTime;
	}
	
	public String getStatus(){
		if(!this.enabled){
			return "§4Deaktiviert";
		}
		if(this.maybe || this.running || this.waiting){
			return "§cBelegt";
		}
		return "§aFrei";
	}
	
	public void sendMessage(String message){
		Bukkit.getPlayer(bluePlayer).sendMessage(message);
		Bukkit.getPlayer(redPlayer).sendMessage(message);
	}

}
