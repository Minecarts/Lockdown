package com.minecarts.lockdown.listener;

import com.minecarts.lockdown.*;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.*;

public class VehicleListener implements Listener {
    private Lockdown plugin;
    public VehicleListener(Lockdown instance)
    {
        plugin = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDamage(VehicleDamageEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleEnter(VehicleEnterEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(!plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
}
