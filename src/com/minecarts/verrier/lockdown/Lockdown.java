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
	
	public void onEnable(){
		PluginManager pm = getServer().getPluginManager();
		this.config = getConfiguration();
		
		this.worldLocked = this.config.getBoolean("locked", true);
		this.debug = this.config.getBoolean("debug", false);
			
		//Create our listeners
			entityListener = new EntityListener(this);
			blockListener = new BlockListener(this);
			playerListener = new PlayerListener(this);
			
		
	    //Register our events
			//Player
				//TODO
				pm.registerEvent(Type.PLAYER_ITEM, this.playerListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.ENTITY_DAMAGED, this.entityListener, Event.Priority.Normal, this);
			//Painting
				pm.registerEvent(Type.PAINTING_CREATE, this.entityListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.PAINTING_REMOVE, this.entityListener, Event.Priority.Normal, this);
			//Explosions
				pm.registerEvent(Type.EXPLOSION_PRIMED, this.entityListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.ENTITY_EXPLODE, this.entityListener, Event.Priority.Normal, this);
			//Blocks
				pm.registerEvent(Type.BLOCK_BREAK, this.blockListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.BLOCK_PLACED, this.blockListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.BLOCK_INTERACT, this.blockListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.BLOCK_IGNITE, this.blockListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.BLOCK_BURN, this.blockListener, Event.Priority.Normal, this);
				pm.registerEvent(Type.BLOCK_FLOW, this.blockListener, Event.Priority.Normal, this);
		
		//Loaded OK, display some awesome messages
			PluginDescriptionFile pdf = getDescription();
		    this.log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");
	    
	    //Start the timer to monitor our required plugins
		    Runnable checkLoadedPlugins = new checkLoadedPlugins(this.config.getStringList("required_plugins",new ArrayList<String>()),pm,this);
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
			private List<String> requiredPlugins;
			private PluginManager pm;
			private Lockdown plugin;
			public checkLoadedPlugins(List<String> requiredPlugins, PluginManager pm, Lockdown plugin){
				this.requiredPlugins = requiredPlugins;
				this.pm = pm;
				this.plugin = plugin;
			}
	        public void run(){
	        	for(String p : requiredPlugins){
	        		if(!pm.isPluginEnabled(p)){
	        			//OH SHIT.
	        			log.warning("Lockdown> WORLD HAS BEEN LOCKED DUE TO A MISSING PLUGIN!");
	        			plugin.lockWorld();
	        		}
	        	}
	        }
	    }
	
	//External API
		public boolean locked(){
			return this.worldLocked;
		}
		public void lockWorld(){
			this.worldLocked = true;
		}
		public void unlockWorld(){
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
			player.sendMessage(ChatColor.RED + "World in in temporary lockdown. Most actions cannot be performed at this time.");
		}
}
