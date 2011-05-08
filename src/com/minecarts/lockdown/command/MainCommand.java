package com.minecarts.lockdown.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.minecarts.verrier.lockdown.*;

public class MainCommand extends CommandHandler{

    public MainCommand(Lockdown plugin){
        super(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("lockdown") && sender.isOp()){
            String msg = "Console command";
            if(sender instanceof Player) msg = "Command issued by " + ((Player) sender).getName();
            
            if(args.length > 0) {
                if(args[0].equals("lock")) {
                    plugin.lock(msg);
                    sender.sendMessage("LOCKDOWN ENFORCED!");
                    return true;
                } else if (args[0].equals("unlock")) {
                    plugin.unlock(msg);
                    sender.sendMessage("Lockdown lifted!");
                    return true;
                }
            } else {
                msg = "Lockdown status: " + (plugin.isLocked() ? "LOCKED" : "UNLOCKED");
                if(!plugin.lockedPlugins.isEmpty()) msg += "\n   Locked plugins: " + plugin.lockedPlugins;
                
                sender.sendMessage(msg);
                
                return true;
            }
        }
        return false;
    }
}