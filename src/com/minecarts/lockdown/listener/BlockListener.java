package com.minecarts.lockdown.listener;

import com.minecarts.lockdown.*;

import org.bukkit.event.block.*;

public class BlockListener extends org.bukkit.event.block.BlockListener{
    private Lockdown plugin;
    public BlockListener(Lockdown plugin){
        this.plugin = plugin;
    }
    
    @Override
    public void onBlockBreak(BlockBreakEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        plugin.informPlayer(event.getPlayer());
    }
    
    //Also handled by player Interact
    @Override
    public void onBlockPlace(BlockPlaceEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        plugin.informPlayer(event.getPlayer());
    }
    
    //Also handled by player Interact
    /*
    public void onBlockInteract(BlockInteractEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        if(event.getEntity() instanceof Player){ 
            plugin.informPlayer((Player) event.getEntity());
        }
    }
    */
    
    @Override
    public void onBlockIgnite(BlockIgniteEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
    
    @Override
    public void onBlockBurn(BlockBurnEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
    
    @Override
    public void onBlockFromTo(BlockFromToEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
    
    @Override
    public void onBlockPistonExtend(BlockPistonExtendEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onBlockPistonRetract(BlockPistonRetractEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
}
