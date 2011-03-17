package com.minecarts.verrier.lockdown.listener;

import com.minecarts.verrier.lockdown.*;

import org.bukkit.event.block.*;
import org.bukkit.entity.Player;

public class BlockListener extends org.bukkit.event.block.BlockListener{
	private Lockdown plugin;
	public BlockListener(Lockdown plugin){
		this.plugin = plugin;
	}
	
	public void onBlockBreak(BlockBreakEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
		plugin.msgLockdown(event.getPlayer());
	}
	public void onBlockPlace(BlockPlaceEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
		plugin.msgLockdown(event.getPlayer());
	}
	public void onBlockInteract(BlockInteractEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
		if(event.getEntity() instanceof Player){ 
			plugin.msgLockdown((Player) event.getEntity());
		}
	}
	
	public void onBlockIgnite(BlockIgniteEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
	}
	
	public void onBlockBurn(BlockBurnEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
	}
	
	public void onBlockFlow(BlockFromToEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
	}
}
