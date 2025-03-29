package io.github.cassiboy1203.staffmanagercore.commands;

import io.github.cassiboy1203.staffManagerLib.annotations.Inject;
import io.github.cassiboy1203.staffManagerLib.annotations.command.Alias;
import io.github.cassiboy1203.staffManagerLib.annotations.command.MCCommand;
import io.github.cassiboy1203.staffmanagercore.IStaffMode;
import io.github.cassiboy1203.staffmanagercore.IVanish;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MCCommand("vanish")
public class VanishCommand{

    private final IStaffMode staffMode;
    private final IVanish vanish;

    @Inject
    public VanishCommand(IStaffMode staffMode, IVanish vanish) {
        this.staffMode = staffMode;
        this.vanish = vanish;
    }

    @Alias("v")
    public boolean onCommand(CommandSender sender, Command command, String[] args) {
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
