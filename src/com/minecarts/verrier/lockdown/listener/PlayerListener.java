package com.minecarts.verrier.lockdown.listener;

import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.minecarts.verrier.lockdown.*;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

	Lockdown plugin;
	
	public PlayerListener(Lockdown instance)
	{
		plugin = instance;
	}
	
	
	public void onPlayerItem(PlayerItemEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		
		//See what item they used, because some things
		//	like food are still okay to use, even in lockdown
		Material itemType = event.getItem().getType();
		if(itemType == Material.BUCKET 
				|| itemType== Material.LAVA_BUCKET 
				|| itemType == Material.WATER_BUCKET
				|| itemType == Material.DIAMOND_HOE
				|| itemType == Material.GOLD_HOE
				|| itemType == Material.IRON_HOE
				|| itemType == Material.WOOD_HOE
				|| itemType == Material.SEEDS
				|| itemType == Material.REDSTONE
				|| itemType == Material.WOOD_DOOR
				|| itemType == Material.IRON_DOOR
				|| itemType == Material.BED
				|| itemType == Material.SADDLE //Not implemented in craftbukkit :(
			){
				event.setCancelled(true);
				plugin.msgLockdown(event.getPlayer());
			 }
	}
	
	
}