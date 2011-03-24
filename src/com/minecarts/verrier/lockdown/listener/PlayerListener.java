package com.minecarts.verrier.lockdown.listener;

import java.util.Arrays;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.minecarts.verrier.lockdown.*;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

	Lockdown plugin;
	
	private final Material[] materialArray = {Material.BREAD,Material.MILK_BUCKET,Material.APPLE,
	        Material.GRILLED_PORK, Material.PORK, Material.GOLDEN_APPLE};
		
	public PlayerListener(Lockdown instance)
	{
		plugin = instance;
	}
	
	
	public void onPlayerInteract(PlayerInteractEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.isLocked()){
			return;
		}
		Action playerAction = event.getAction();
		if(playerAction == Action.RIGHT_CLICK_AIR || playerAction == Action.RIGHT_CLICK_BLOCK){
		    //See if the item they're using is in the allowed lists of items above
		    //    TODO: Check air / empty hand?
		    Material itemType= event.getItem().getType();
		    if(!Arrays.asList(materialArray).contains(itemType)){
	            event.setCancelled(true);
	            plugin.informPlayer(event.getPlayer());
	        }
		} else {
		    //Cancel it. As of right now, LEFT_CLICK_BLOCK, LEFT_CLICK_AIR, PHYSICAL (pressure plates)
		    event.setCancelled(true);
		}
		
	}
	
	
}