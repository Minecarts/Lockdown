package com.minecarts.lockdown.listener;

import org.bukkit.entity.Player;
import org.bukkit.entity.Entity;

import com.minecarts.lockdown.*;

import org.bukkit.entity.Wolf;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;

import org.bukkit.event.entity.*;

public class EntityListener extends org.bukkit.event.entity.EntityListener {
    private Lockdown plugin;
    public EntityListener(Lockdown instance)
    {
        plugin = instance;
    }
//PVP
    @Override
    public void onEntityDamage(EntityDamageEvent event){
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.SUICIDE){
            return; // /kill should be allowed
        }

        if(event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK){
            if(event instanceof EntityDamageByEntityEvent){
                EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                Entity attacker = e.getDamager();
                Entity defender = e.getEntity();
                if((attacker instanceof Player && defender instanceof Player) || (defender instanceof Wolf && ((Wolf)defender).isTamed())){
                    plugin.informPlayer((Player) attacker);
                } else {
                    return; //It's not a player attacking a player or a tamed wolf, return.
                }
            }
        }
        event.setCancelled(true);
    }

//Explosions
    public void onEntityExplode(EntityExplodeEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

//Painting events
    public void onPaintingPlace(PaintingPlaceEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        plugin.informPlayer(event.getPlayer());
    }
    public void onPaintingBreak(PaintingBreakEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
        
        //If it was removed by a player, let that player know
        if(event.getCause() == RemoveCause.ENTITY){
            PaintingBreakByEntityEvent prbe = ((PaintingBreakByEntityEvent) event);
            if(prbe.getRemover() instanceof Player){
                plugin.informPlayer(((Player) prbe.getRemover()));
            }
        }
    }

    public void onEndermanPickup(EndermanPickupEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
    public void onEndermanPlace(EndermanPlaceEvent event){
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

}
