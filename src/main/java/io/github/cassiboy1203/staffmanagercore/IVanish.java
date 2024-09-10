package io.github.cassiboy1203.staffmanagercore;

import org.bukkit.entity.Player;

import java.util.List;

public interface IVanish {

    String SEE_VANISH_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, "see.invisible");

    void vanish(Player player);
    void unVanish(Player player);
    void toggle(Player player);
    List<Player> getVanishedPlayers();
    void hideVanishedPlayers(Player player);
}
