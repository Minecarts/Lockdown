package com.minecarts.verrier.lockdown;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;

import org.bukkit.util.config.Configuration;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import com.minecarts.verrier.lockdown.listener.*;

public class Lockdown extends JavaPlugin {
	public final Logger log = Logger.getLogger("Minecraft");
	public Configuration config;
	
	//Temporary config values (to be put in config later)
		private boolean worldLocked = true;
		private boolean debug = false;
	
	//Listeners
		private EntityListener entityListener;
		private BlockListener blockListener;
		private PlayerListener playerListener;
		
		private PluginManager pluginManager;
		private List<String> requiredPlugins;
	
	public void onEnable(){
		this.pluginManager = getServer().getPluginManager();		
		this.config = getConfiguration();
		
		this.requiredPlugins = this.config.getStringList("required_plugins",new ArrayList<String>());
		this.worldLocked = this.config.getBoolean("locked", true);
		this.debug = this.config.getBoolean("debug", false);
			
		
		//Create our listeners
			entityListener = new EntityListener(this);
			blockListener = new BlockListener(this);
			playerListener = new PlayerListener(this);
			
		
	    //Register our events
			//Player
				//TODO
				pluginManager.registerEvent(Type.PLAYER_ITEM, this.playerListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.ENTITY_DAMAGED, this.entityListener, Event.Priority.Normal, this);
			//Painting
				pluginManager.registerEvent(Type.PAINTING_CREATE, this.entityListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.PAINTING_REMOVE, this.entityListener, Event.Priority.Normal, this);
			//Explosions
				pluginManager.registerEvent(Type.EXPLOSION_PRIMED, this.entityListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
			//Blocks
				pluginManager.registerEvent(Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_PLACED, this.blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_INTERACT, this.blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_IGNITE, this.blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_BURN, this.blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_FLOW, this.blockListener, Event.Priority.Normal, this);
		
		//Loaded OK, display some awesome messages
			PluginDescriptionFile pdf = getDescription();
		    this.log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
	    
	    //Start the timer to monitor our required plugins
		    Runnable checkLoadedPlugins = new checkLoadedPlugins();
		    getServer().getScheduler().scheduleSyncRepeatingTask(this, checkLoadedPlugins, 20, 300); //Check every 15 seconds (300 ticks)
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		if(cmdLabel.equals("lockdown")){
			if(args.length > 0){
				if(args[0].equals("lock")){
					this.worldLocked = true;
					if(sender instanceof Player){
						((Player) sender).sendMessage("World LOCKED!");
						log.info("WORLD LOCKED BY " + ((Player) sender).getName());
					} else {
						log.info("World LOCKED!");
					}
					
					return true;
				} else if (args[0].equals("unlock")){
					this.worldLocked = false;
					if(sender instanceof Player){
						((Player) sender).sendMessage("World UNLOCKED!");
						log.info("WORLD UNLOCKED BY " + ((Player) sender).getName());
					} else {
						log.info("World UNLOCKED!");
					}
					return true;
				}
			}
			
		}
		return false;
	}
	
	//Repeating plugin loaded checker 
    public class checkLoadedPlugins implements Runnable { 
    	public void run() { 
    		for(String p : Lockdown.this.requiredPlugins) { 
    			if(!Lockdown.this.pluginManager.isPluginEnabled(p)) { 
    				if(!Lockdown.this.worldLocked) { 
    					Lockdown.this.lockWorld("Required plugin " + p + " is not loaded or disabled."); 
    				} 
    				return; 
    			} 
    		} 
    		if(Lockdown.this.worldLocked) { 
    			Lockdown.this.unlockWorld("All required plugins enabled."); 
    		} 
    	} 
    }
	
	//External API
		public boolean locked(){
			return this.worldLocked;
		}
		public void lockWorld(String msg){
			log(msg,false);
			this.worldLocked = true;
		}
		public void unlockWorld(String msg){
			log(msg,false);
			this.worldLocked = false;
		}
	
	//Internal logging and msging
		public void log(String msg){
			log(msg, true);
		}
		public void log(String msg, boolean debug){
			if(debug && !this.debug) return;
			log.info("Lockdown> " + msg);
		}
		
		public void msgLockdown(org.bukkit.entity.Player player){
			player.sendMessage(ChatColor.RED + "World is in temporary lockdown. Most actions cannot be performed at this time.");
		}
}
