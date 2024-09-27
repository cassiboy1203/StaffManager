package io.github.cassiboy1203.staffmanagercore;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IVanish {

    String SEE_VANISH_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, "see.invisible");
    String VANISH_TOGGLE_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, "vanish.toggle");

    void vanish(Player player);
    void unVanish(Player player);
    void toggle(Player player);
    List<UUID> getVanishedPlayers();
    void hideVanishedPlayers(Player player);
}
