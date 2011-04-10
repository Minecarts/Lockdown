package com.minecarts.verrier.lockdown.listener;

import java.util.Arrays;

import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

import com.minecarts.verrier.lockdown.*;

public class PlayerListener extends org.bukkit.event.player.PlayerListener{

    Lockdown plugin;
    
    //Items that can be used despite lockdown
    private final Material[] allowedItems = {Material.BREAD,Material.MILK_BUCKET,Material.APPLE,
            Material.GRILLED_PORK, Material.PORK, Material.GOLDEN_APPLE};
        
    public PlayerListener(Lockdown instance)
    {
        plugin = instance;
    }
    
    @Override
    public void onPlayerInteract(PlayerInteractEvent event){
        plugin.log("EVENT: " + event.getEventName());
        
        //The event is always canceled because you can't
        //    use an item on a far away block, so cancelled == true.

        //Only cancel the event if we can't use the item in the hand
        //    because the event is ALWAYS canceled if we try to interact
        //    with a block outside our range
        if((event.isCancelled() && event.useItemInHand() == Result.DENY) || !plugin.isLocked()){
            return;
        }
        
        Action eventAction = event.getAction();
        
        //Explicitly deny interacting with all blocks, even if the item they're using is
        // a white listed item, this includes pressure plates, buttons, switches, doors.. etc.
        //event.setUseInteractedBlock(Result.DENY);
        
        if(event.getItem() != null && (Arrays.asList(allowedItems).contains(event.getItem().getType()))){
            //It's a whitelisted item, let them use it
            event.setUseItemInHand(Result.ALLOW);
            System.out.println("Allowed!");
        } else {
            event.setCancelled(true);
            event.setUseItemInHand(Result.DENY);
            event.setUseInteractedBlock(Result.DENY);
        }
        
        //If it's a left click, see if we need to send a notice to the player
        //  only certain blocks send the notice to prevent messaging when running around punching
        if(eventAction == Action.LEFT_CLICK_BLOCK || eventAction == Action.RIGHT_CLICK_BLOCK){
            plugin.informPlayer(event.getPlayer());
        }
        
    }
}