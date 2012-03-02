package com.minecarts.lockdown.listener;

import com.minecarts.lockdown.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;

public class BlockListener implements Listener {
    private Lockdown plugin;
    public BlockListener(Lockdown plugin){
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        plugin.informPlayer(event.getPlayer());
    }
    
    //Also handled by player Interact
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        plugin.informPlayer(event.getPlayer());
    }
    
    //Also handled by player Interact
    /*
    @EventHandler(ignoreCancelled = true)
    public void onBlockInteract(BlockInteractEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        if(event.getEntity() instanceof Player){ 
            plugin.informPlayer((Player) event.getEntity());
        }
    }
    */

    @EventHandler(ignoreCancelled = true)
    public void onBlockIgnite(BlockIgniteEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockBurn(BlockBurnEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonExtend(BlockPistonExtendEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBlockPistonRetract(BlockPistonRetractEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
}
