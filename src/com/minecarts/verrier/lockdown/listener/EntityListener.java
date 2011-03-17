package com.minecarts.verrier.lockdown.listener;

import org.bukkit.entity.Player;
import com.minecarts.verrier.lockdown.*;

import org.bukkit.event.painting.PaintingCreateEvent;
import org.bukkit.event.painting.PaintingRemoveEvent;
import org.bukkit.event.painting.PaintingRemoveEvent.RemoveCause;
import org.bukkit.event.painting.PaintingRemoveByEntityEvent;
import org.bukkit.event.entity.*;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	private Lockdown plugin;
	public EntityListener(Lockdown instance)
	{
		plugin = instance;
	}
//Explosions
	public void onEntityExplode(EntityExplodeEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
	}
	/*
	public void onExplosionPrimed(EntityExplodeEvent event){
		//Creepers should still explode, but not TNT??
		if(event.getEntity() instanceof org.bukkit.entity.TNTPrimed){
			plugin.log("EVENT: " + event.getEventName());
			if(event.isCancelled() || !plugin.locked()){
				return;
			}
			event.setCancelled(true);
		}
	}
	*/
//Painting events
	public void onPaintingCreate(PaintingCreateEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
		plugin.msgLockdown(event.getPlayer());
	}
	public void onPaintingRemove(PaintingRemoveEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		event.setCancelled(true);
		
		//If it was removed by a player, let that player know
		if(event.getCause() == RemoveCause.ENTITY){
			PaintingRemoveByEntityEvent prbe = ((PaintingRemoveByEntityEvent) event);
			if(prbe.getRemover() instanceof Player){
				plugin.msgLockdown(((Player) prbe.getRemover()));
			}
		}
		//No way to determine player from remove event?
	}
	
}
