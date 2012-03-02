package com.minecarts.lockdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import java.util.Date;

import com.minecarts.lockdown.command.MainCommand;
import com.minecarts.lockdown.listener.*;

public class Lockdown extends JavaPlugin {
    private PluginManager pluginManager;

    private boolean debug = false;

    private List<String> requiredPlugins;
    private ArrayList<String> lockedPlugins = new ArrayList<String>();

    private HashMap<String, Date> msgThrottle = new HashMap<String, Date>();

    private MainCommand command = new MainCommand(this);

    
    public void onEnable() {
        try {
            PluginDescriptionFile pdf = getDescription();
            pluginManager = getServer().getPluginManager();

            requiredPlugins = getConfig().getStringList("required_plugins");
            debug = getConfig().getBoolean("debug");


            //Register our command to our commandHandler
            getCommand("lockdown").setExecutor(command);

            getLogger().info("[" + pdf.getName() + "] version " + pdf.getVersion() + " enabled.");

            if(getConfig().getBoolean("start_locked", true)) {
                lockedPlugins = new ArrayList<String>(requiredPlugins);
                log("Starting locked, these plugins must unlock themselves manually: " + lockedPlugins, false);
            }

            // Start a task to monitor our required plugins
            // Check after one second, then every 10 seconds (200 ticks)
            getServer().getScheduler().scheduleSyncRepeatingTask(this, new checkLoadedPlugins(), 20, 200);

            Bukkit.getPluginManager().registerEvents(new BlockListener(this),this);
            Bukkit.getPluginManager().registerEvents(new EntityListener(this),this);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this),this);
            Bukkit.getPluginManager().registerEvents(new VehicleListener(this),this);

        } catch (Error e) {
            getLogger().severe("**** CRITICAL ERROR, LOCKDOWN FAILED TO LOAD CORRECTLY *****");
            e.printStackTrace();
            getServer().shutdown();
        } catch (Exception e) {
            getLogger().severe("**** CRITICAL EXCEPTION, LOCKDOWN FAILED TO LOAD CORRECTLY *****");
            e.printStackTrace();
            getServer().shutdown();
        }


    }

    public void onDisable(){}

    //Repeating plugin loaded checker 
    public class checkLoadedPlugins implements Runnable {
        public void run() {
            for(String p : requiredPlugins) { 
                if(!pluginManager.isPluginEnabled(p) && !isLocked(p)) {
                    lock(p, "Required plugin is not loaded or disabled.");
                }
            }

            //Display some helpful logging
            if(!lockedPlugins.isEmpty()) {
                log(String.format("%d/%d plugins not enabled or still locked:", lockedPlugins.size(), requiredPlugins.size()));
                int i = 0;
                for(String p : lockedPlugins){
                    log(String.format("\t%d. %s", ++i, p));
                }
            }
        }
    }

    
    //External API
    public boolean isLocked() {
        return command.isLocked() || !lockedPlugins.isEmpty();
    }
    public boolean isLocked(Plugin p) {
        return isLocked(p.getDescription().getName());
    }
    public boolean isLocked(String pluginName) {
        return lockedPlugins.contains(pluginName);
    }
    
    public ArrayList<String> getLockedPlugins() {
        return new ArrayList<String>(lockedPlugins);
    }

    public void lock(Plugin p, String reason) {
        lock(p.getDescription().getName(), reason);
    }
    public void lock(String pluginName, String reason) {
        if(!lockedPlugins.contains(pluginName)){
            lockedPlugins.add(pluginName);
            log(pluginName + " PLUGIN LOCK: " + reason, false);
        }
    }

    public void unlock(Plugin p, String reason) {
        unlock(p.getDescription().getName(), reason);
    }
    public void unlock(String pluginName, String reason) {
        if(lockedPlugins.contains(pluginName)){
            lockedPlugins.remove(pluginName);
            log(pluginName + " plugin lock lifted: " + reason, false);
        }
    }

    //Internal logging and messaging
    public void log(String msg) {
        log(msg, true);
    }
    public void log(String msg, boolean debug) {
        if(debug && !this.debug) return;
        getLogger().info("Lockdown> " + msg);
    }

    public void informPlayer(Player player) {
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

    private void notify(Player player) {
        player.sendMessage(ChatColor.GRAY + "The world is in " + ChatColor.YELLOW + "temporary lockdown mode" + ChatColor.GRAY + " while all plugins are");
        player.sendMessage(ChatColor.GRAY + "   properly loaded. Please try again in a few seconds.");
    }
}
