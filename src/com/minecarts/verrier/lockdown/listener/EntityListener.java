package com.minecarts.verrier.lockdown.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;
import com.minecarts.verrier.lockdown.*;

import org.bukkit.event.painting.PaintingCreateEvent;
import org.bukkit.event.painting.PaintingRemoveEvent;
import org.bukkit.event.painting.PaintingRemoveEvent.RemoveCause;
import org.bukkit.event.painting.PaintingRemoveByEntityEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
	private Lockdown plugin;
	public EntityListener(Lockdown instance)
	{
		plugin = instance;
	}
//PVP
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		
		Entity attacker = event.getDamager();
	    Entity defender = event.getEntity();
		if(attacker instanceof Player && defender instanceof Player){
			event.setCancelled(true);
			plugin.msgLockdown((Player) attacker);
		}
	}
	public void onEntityDamageByProjectile(EntityDamageByProjectileEvent event){
		plugin.log("EVENT: " + event.getEventName());
		if(event.isCancelled() || !plugin.locked()){
			return;
		}
		
		Entity defender = event.getEntity();
	    Entity attacker = event.getDamager();
		if(attacker instanceof Player && defender instanceof Player){
			event.setCancelled(true);
			plugin.msgLockdown((Player) attacker);
		}
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
