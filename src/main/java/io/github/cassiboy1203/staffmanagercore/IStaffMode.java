package io.github.cassiboy1203.staffmanagercore;

import org.bukkit.entity.Player;

public interface IStaffMode {
    String COMMAND_PERMISSION_BASE = String.join(".", StaffManagerCore.PERMISSION_BASE, "staff");
    String CREATIVE_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "creative");
    String SPECTATOR_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "spectator");
    String GODMODE_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "god");
    String FLY_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "fly");
    String VANISH_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "vanish");
    String SEE_VANISH_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "see.invisible");
    String GAMEMODE_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, "gamemode");
    String GAMEMODE_OTHER_PERMISSION = String.join(".", COMMAND_PERMISSION_BASE, GAMEMODE_PERMISSION,"other");

    boolean isInStaffMode(Player player);
    void setPlayerInStaffmode(Player player);
    void removePlayerInStaffmode(Player player);
}
