package io.github.cassiboy1203.staffmanagercore.commands;

import com.google.inject.Inject;
import io.github.cassiboy1203.staffmanagercore.IStaffMode;
import io.github.cassiboy1203.staffmanagercore.StaffManagerCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements ICommand{

    private static final String FLY_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, "fly");
    private static final String FLY_OTHER_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, FLY_PERMISSION, "other");

    private IStaffMode staffMode;

    @Inject
    public void setStaffMode(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Override
    public String getCommandName() {
        return "fly";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(IStaffMode.FLY_PERMISSION)){
                return false;
            } else if (!staffMode.isInStaffMode(player) && !player.hasPermission(FLY_PERMISSION)){
                player.sendMessage(String.format("%sYou dont have the permission to change flight outside of staff mode.", ChatColor.RED));
                return true;
            }

            if (args.length == 1){
                if (!player.hasPermission(IStaffMode.FLY_OTHER_PERMISSION)){
                    player.sendMessage(String.format("%sYou dont have the permission to change another players flight.", ChatColor.RED));
                    return true;
                } else if (!staffMode.isInStaffMode(player) && !player.hasPermission(FLY_OTHER_PERMISSION)){
                    player.sendMessage(String.format("%sYou dont have the permission to change another players flight outside of staff mode.", ChatColor.RED));
                    return true;
                }

                var otherPlayer = Bukkit.getPlayer(args[0]);
                if (otherPlayer == null){
                    player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[0]));
                    return true;
                }

                otherPlayer.setAllowFlight(!otherPlayer.getAllowFlight());
                return true;
            } else if (args.length > 1){
                player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                player.sendMessage(String.format("%sUsage: /fly [player].", ChatColor.RED));
                return true;
            }
            // If args.length == 0
            player.setAllowFlight(!player.getAllowFlight());
            return true;
        }


        return false;
    }
}
