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
	public final Logger log = Logger.getLogger("Minecraft.Lockdown");

	private PluginManager pluginManager;

	public Configuration config;
	private List<String> requiredPlugins;
	private boolean debug = false;
	
	private boolean locked = false;
	
	private EntityListener entityListener;
	private BlockListener blockListener;
	private PlayerListener playerListener;
	
	public void onEnable(){
		PluginDescriptionFile pdf = getDescription();
	    log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
	    
		pluginManager = getServer().getPluginManager();

		loadConfig();
		// Start the world in lockdown mode?
		if(config.getBoolean("locked", false)) {
			lock("Starting in lockdown mode.");
		}
		
		//Create our listeners
			entityListener = new EntityListener(this);
			blockListener = new BlockListener(this);
			playerListener = new PlayerListener(this);
			
		
	    //Register our events
			//Player
				//TODO
				pluginManager.registerEvent(Type.PLAYER_ITEM, playerListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.ENTITY_DAMAGED, entityListener, Event.Priority.Normal, this);
			//Painting
				pluginManager.registerEvent(Type.PAINTING_CREATE, entityListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.PAINTING_REMOVE, entityListener, Event.Priority.Normal, this);
			//Explosions
				pluginManager.registerEvent(Type.EXPLOSION_PRIMED, entityListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
			//Blocks
				pluginManager.registerEvent(Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_PLACED, blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_INTERACT, blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_BURN, blockListener, Event.Priority.Normal, this);
				pluginManager.registerEvent(Type.BLOCK_FLOW, blockListener, Event.Priority.Normal, this);
		
	    //Start the timer to monitor our required plugins
		    Runnable checkLoadedPlugins = new checkLoadedPlugins();
		    getServer().getScheduler().scheduleSyncRepeatingTask(this, checkLoadedPlugins, 20, 300); //Check every 15 seconds (300 ticks)
		    
		    
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String cmdLabel, String[] args){
		if(cmdLabel.equals("lockdown")){
			if(args.length > 0){
				Player player = null;
				String msg = "Console command";
				
				if(sender instanceof Player) {
					player = (Player) sender;
					msg = "Command issued by " + player.getName();
				}
				
				if(args[0].equals("lock")){
					lock(msg);
					if(player != null) player.sendMessage("LOCKDOWN ENFORCED!");
					return true;
				} else if (args[0].equals("unlock")){
					unlock(msg);
					if(player != null) player.sendMessage("Lockdown lifted!");
					return true;
				} else if (args[0].equals("reload")) {
					loadConfig();
					if(player != null) player.sendMessage("Lockdown config reloaded.");
					return true;
				}
			}
			
		}
		return false;
	}
	
	private void loadConfig() {
		if(config == null) config = getConfiguration();
		else config.load();
		
		requiredPlugins = config.getStringList("required_plugins", new ArrayList<String>());
		debug = config.getBoolean("debug", false);
	}
	
	//Repeating plugin loaded checker 
    public class checkLoadedPlugins implements Runnable { 
    	public void run() { 
    		for(String p : Lockdown.this.requiredPlugins) { 
    			if(!Lockdown.this.pluginManager.isPluginEnabled(p)) { 
    				if(!Lockdown.this.locked) { 
    					Lockdown.this.lock("Required plugin " + p + " is not loaded or disabled."); 
    				} 
    				return; 
    			} 
    		} 
    		if(Lockdown.this.locked) { 
    			Lockdown.this.unlock("All required plugins enabled."); 
    		} 
    	} 
    }
	
	//External API
		public boolean isLocked(){
			return this.locked;
		}
		public void lock(String msg){
			this.locked = true;
			log("LOCKDOWN ENFORCED: " + msg, false);
		}
		public void unlock(String msg){
			this.locked = false;
			log("Lockdown lifted: " + msg, false);
		}
	
	//Internal logging and msging
		public void log(String msg){
			log(msg, true);
		}
		public void log(String msg, boolean debug){
			if(debug && !this.debug) return;
			log.info("Lockdown> " + msg);
		}
		
		public void informPlayer(org.bukkit.entity.Player player){
			player.sendMessage(ChatColor.RED + "The world is temporarily locked down until all plugins are properly loaded.");
		}
}
