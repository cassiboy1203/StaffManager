package io.github.cassiboy1203.staffmanagercore.commands;

import com.google.inject.Inject;
import io.github.cassiboy1203.staffmanagercore.IStaffMode;
import io.github.cassiboy1203.staffmanagercore.IVanish;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VanishCommand implements ICommand{

    private IStaffMode staffMode;
    private IVanish vanish;


    @Inject
    private void setStaffMode(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Inject
    private void setVanish(IVanish vanish) {
        this.vanish = vanish;
    }

    @Override
    public String getCommandName() {
        return "vanish";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(IStaffMode.VANISH_TOGGLE_PERMISSION)) {
                return false;
            } else if (!staffMode.isInStaffMode(player) && !player.hasPermission(IVanish.VANISH_TOGGLE_PERMISSION)) {
                player.sendMessage(String.format("%sYou dont have the permission to change vanish outside of staff mode.", ChatColor.RED));
                return true;
            }

            vanish.toggle(player);
            return true;
        }

        return false;
    }
}
