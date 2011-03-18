package com.minecarts.verrier.lockdown.listener;

import java.util.Arrays;

import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.minecarts.verrier.lockdown.*;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

	Lockdown plugin;
	private final Material[] materialArray = {Material.BUCKET,Material.LAVA_BUCKET,Material.WATER_BUCKET,Material.DIAMOND_HOE,
			Material.GOLD_HOE,Material.IRON_HOE,Material.WOOD_HOE,Material.SEEDS,Material.REDSTONE,
			Material.WOOD_DOOR,Material.IRON_DOOR,Material.BED,Material.SIGN,Material.SADDLE
			};
	
	
	public PlayerListener(Lockdown instance)
	{
		plugin = instance;
	}
	
	
	public void onPlayerItem(PlayerItemEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.isLocked()){
			return;
		}
		
		//See what item they used, because some things
		//	like food are still okay to use, even in lockdown
		Material itemType = event.getItem().getType();
		
		if(Arrays.asList(materialArray).contains(itemType)){
			event.setCancelled(true);
			plugin.informPlayer(event.getPlayer());
		}
	}
	
	
}