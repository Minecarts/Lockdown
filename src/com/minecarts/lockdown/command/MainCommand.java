package com.minecarts.lockdown.command;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.minecarts.lockdown.*;

public class MainCommand extends CommandHandler {

    private boolean locked = false;

    public MainCommand(Lockdown plugin) {
        super(plugin);
    }

    public boolean isLocked() {
        return locked;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equals("lockdown") && sender.isOp()){
            String reason = "Console command";
            if(sender instanceof Player) reason = "Command issued by " + ((Player) sender).getName();

            if(args.length > 0) {
                if(args[0].equals("lock")) {
                    locked = true;
                    plugin.log("LOCKDOWN ENFORCED: " + reason, false);
                    sender.sendMessage("LOCKDOWN ENFORCED!");
                    return true;
                } else if (args[0].equals("unlock")) {
                    locked = false;
                    plugin.log("Lockdown lifted: " + reason, false);
                    sender.sendMessage("Lockdown lifted!");
                    return true;
                }
            } else {
                reason = "Lockdown status: " + (plugin.isLocked() ? "LOCKED" : "UNLOCKED");
                ArrayList<String> plugins = plugin.getLockedPlugins();
                if(!plugins.isEmpty()) reason += "\n   Locked plugins: " + plugins;
                sender.sendMessage(reason);
                return true;
            }
        }
        return false;
    }
}