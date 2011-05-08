package com.minecarts.verrier.lockdown;

import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.event.Event;
import org.bukkit.event.Event.Type;

import org.bukkit.util.config.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import java.util.Date;

import com.minecarts.lockdown.command.MainCommand;
import com.minecarts.verrier.lockdown.listener.*;

public class Lockdown extends JavaPlugin {
    public final Logger log = Logger.getLogger("Minecraft.Lockdown");

    private PluginManager pluginManager;

    public Configuration config;
    private boolean debug = false;

    private boolean locked = false;
    public List<String> requiredPlugins;
    public ArrayList<String> lockedPlugins = new ArrayList<String>();

    private HashMap<String, Date> msgThrottle = new HashMap<String,Date>();

    private EntityListener entityListener;
    private BlockListener blockListener;
    private PlayerListener playerListener;
    private VehicleListener vehicleListener;
    
    public void onEnable() {
        try {
            PluginDescriptionFile pdf = getDescription();
            pluginManager = getServer().getPluginManager();

            config = getConfiguration();
            requiredPlugins = config.getStringList("required_plugins", new ArrayList<String>());
            debug = config.getBoolean("debug", false);

            if(config.getBoolean("start_locked", true)) {
                //lock("Starting world locked due to configuration setting");
                lockedPlugins = new ArrayList<String>(requiredPlugins);                
            }

            //Create our listeners
                entityListener = new EntityListener(this);
                blockListener = new BlockListener(this);
                playerListener = new PlayerListener(this);
                vehicleListener = new VehicleListener(this);

            //Register our events
                //Player
                    pluginManager.registerEvent(Type.PLAYER_INTERACT, playerListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
                //Vehicles
                    pluginManager.registerEvent(Type.VEHICLE_COLLISION_ENTITY, vehicleListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.VEHICLE_DAMAGE, vehicleListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.VEHICLE_DESTROY, vehicleListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.VEHICLE_ENTER, vehicleListener, Event.Priority.Normal, this);
                //Painting
                    pluginManager.registerEvent(Type.PAINTING_PLACE, entityListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.PAINTING_BREAK, entityListener, Event.Priority.Normal, this);
                //Explosions
                    pluginManager.registerEvent(Type.EXPLOSION_PRIME, entityListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.ENTITY_EXPLODE, entityListener, Event.Priority.Normal, this);
                //Blocks
                    pluginManager.registerEvent(Type.BLOCK_BREAK, blockListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.BLOCK_PLACE, blockListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.BLOCK_IGNITE, blockListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.BLOCK_BURN, blockListener, Event.Priority.Normal, this);
                    pluginManager.registerEvent(Type.BLOCK_FROMTO, blockListener, Event.Priority.Normal, this);

            //Register our command to our commandHandler
                getCommand("lockdown").setExecutor(new MainCommand(this));

            //Start the timer to monitor our required plugins
                Runnable checkLoadedPlugins = new checkLoadedPlugins();
                getServer().getScheduler().scheduleSyncRepeatingTask(this, checkLoadedPlugins, 20, 300); //Check after one second, then every 15 seconds (300 ticks)

                log.info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");

        } catch (Error e) {
            getServer().dispatchCommand(new org.bukkit.command.ConsoleCommandSender(getServer()), "stop");
            log.severe("**** CRITICAL ERROR, LOCKDOWN FAILED TO LOAD CORRECTLY *****");
            e.printStackTrace();
        } catch (Exception e) {
            getServer().dispatchCommand(new org.bukkit.command.ConsoleCommandSender(getServer()), "stop");
            log.severe("**** CRITICAL ERROR, LOCKDOWN FAILED TO LOAD CORRECTLY *****");
            e.printStackTrace();
        }


    }

    public void onDisable(){}

    //Repeating plugin loaded checker 
    public class checkLoadedPlugins implements Runnable {
        public void run() {
            for(String p : requiredPlugins) { 
                if(!pluginManager.isPluginEnabled(p)) {
                    if(!lockedPlugins.contains(p)){
                        lockedPlugins.add(p);
                    }
                    log("Required plugin " + p + " is not loaded or disabled.", false);
                }
            }

            //Display some helpful logging
            if(!lockedPlugins.isEmpty()) {
                log(String.format("%d/%d plugins not enabled or still locked", lockedPlugins.size(), requiredPlugins.size()));
                int i = 0;
                for(String p : lockedPlugins){
                    log(String.format("\t%d. %s", ++i, p));
                }
            }
        }
    }

    // Internal lock/unlock -- public for use by MainCommand.java
    public void lock(String reason) {
        locked = true;
        log("LOCKDOWN ENFORCED: " + reason, false);
    }
    public void unlock(String reason) {
        locked = false;
        log("Lockdown lifted: " + reason, false);
    }
    
    //External API
    public boolean isLocked() {
        return locked || !lockedPlugins.isEmpty();
    }
    public boolean isLocked(Plugin p) {
        return isLocked(p.getDescription().getName());
    }
    public boolean isLocked(String pluginName) {
        return lockedPlugins.contains(pluginName);
    }

    public void lock(Plugin p, String reason) {
        lock(p.getDescription().getName(), reason);
    }
    public void lock(String pluginName, String reason) {
        lockedPlugins.add(pluginName);
        log(pluginName + " PLUGIN LOCK: " + reason, false);
    }

    public void unlock(Plugin p, String reason) {
        unlock(p.getDescription().getName(), reason);
    }
    public void unlock(String pluginName, String reason) {
        lockedPlugins.remove(pluginName);
        log(pluginName + " plugin lock lifted: " + reason, false);
    }
    
    //Internal logging and messaging
    public void log(String msg) {
        log(msg, true);
    }
    public void log(String msg, boolean debug) {
        if(debug && !this.debug) return;
        log.info("Lockdown> " + msg);
    }
    
    public void informPlayer(org.bukkit.entity.Player player) {
        String playerName = player.getName();
        long time = new Date().getTime();
        if(!this.msgThrottle.containsKey(playerName)) {
            this.msgThrottle.put(playerName, new Date());
            notify(player);
        }
        if((time - (this.msgThrottle.get(player.getName()).getTime())) > 1000 * 3) { //every 3 seconds
            notify(player);
            this.msgThrottle.put(player.getName(), new Date());
        }
            
    }
    
    private void notify(org.bukkit.entity.Player player) {
        player.sendMessage(ChatColor.GRAY + "The world is in " + ChatColor.YELLOW + "temporary lockdown mode" + ChatColor.GRAY + " while all plugins are");
        player.sendMessage(ChatColor.GRAY + "   properly loaded. Please try again in a few seconds.");
    }
}
