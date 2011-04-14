package com.minecarts.verrier.lockdown.listener;

import com.minecarts.verrier.lockdown.*;

import org.bukkit.event.vehicle.*;

public class VehicleListener extends org.bukkit.event.vehicle.VehicleListener {
    private Lockdown plugin;
    public VehicleListener(Lockdown instance)
    {
        plugin = instance;
    }

    @Override
    public void onVehicleEntityCollision(VehicleEntityCollisionEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onVehicleDamage(VehicleDamageEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onVehicleDestroy(VehicleDestroyEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }

    @Override
    public void onVehicleEnter(VehicleEnterEvent event) {
        plugin.log("EVENT: " + event.getEventName());
        if(event.isCancelled() || !plugin.isLocked()){
            return;
        }
        event.setCancelled(true);
    }
}
