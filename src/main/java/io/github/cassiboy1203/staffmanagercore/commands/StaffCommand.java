package io.github.cassiboy1203.staffmanagercore.commands;

import com.google.inject.Inject;
import io.github.cassiboy1203.staffmanagercore.IStaffMode;
import io.github.cassiboy1203.staffmanagercore.IVanish;
import io.github.cassiboy1203.staffmanagercore.inventory.IStaffInventory;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class StaffCommand implements ICommand{

    private IStaffMode staffMode;
    private FileConfiguration config;
    private IStaffInventory staffInventory;
    private IVanish vanish;

    @Inject
    public void setStaffMode(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Inject
    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    @Inject
    public void setStaffInventory(IStaffInventory staffInventory) {
        this.staffInventory = staffInventory;
    }

    @Inject
    public void setVanish(IVanish vanish) {
        this.vanish = vanish;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {

        if (sender instanceof Player player) {
            if (staffMode.isInStaffMode(player)) {
                return leaveStaffMode(player);
            } else {
                if (!player.hasPermission(IStaffMode.COMMAND_PERMISSION_BASE)){
                    player.sendMessage("You dont have permission to use this command");
                    return false;
                }

                return enterStaffMode(player);
            }
        }
        return false;
    }

    private boolean leaveStaffMode(Player player) {
        staffMode.removePlayerInStaffmode(player);

        if (!staffInventory.resetPlayerInventory(player)){
            player.sendMessage("Failed to leave staff mode please try again");
            return false;
        }

        vanish.unVanish(player);
        player.setInvulnerable(false);
        player.setAllowFlight(false);
        player.sendMessage("You have left staff mode");
        return true;
    }

    private boolean enterStaffMode(Player player) {
        staffMode.setPlayerInStaffmode(player);
        if (!staffInventory.setPlayerInventory(player)){
            player.sendMessage("Failed to enter staff mode please try again");
            return false;
        }

        if (player.hasPermission(IStaffMode.COMMAND_PERMISSION_BASE))
            vanish.vanish(player);
        if (player.hasPermission(IStaffMode.FLY_PERMISSION))
            player.setAllowFlight(true);
        if (player.hasPermission(IStaffMode.GODMODE_PERMISSION)){
            player.setInvulnerable(true);
        }
        if (player.hasPermission(IStaffMode.VANISH_PERMISSION)){
            vanish.vanish(player);
        }
        player.sendMessage("Entering staff mode");
        return true;
    }

    private void setGamemode(Player player){
        switch (config.getString("staff-mode.default-gamemode", "survival")){
            case "creative":
                player.setGameMode(GameMode.CREATIVE);
                break;
            case "adventure":
                player.setGameMode(GameMode.ADVENTURE);
                break;
            case "spectator":
                player.setGameMode(GameMode.SPECTATOR);
                break;
            default:
                player.setGameMode(GameMode.SURVIVAL);
        }
    }

    @Override
    public String getCommandName() {
        return "staff";
    }
}
