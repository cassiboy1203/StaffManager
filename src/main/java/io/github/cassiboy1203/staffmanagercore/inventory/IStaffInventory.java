package io.github.cassiboy1203.staffmanagercore.inventory;

import org.bukkit.entity.Player;

public interface IStaffInventory {
    boolean resetPlayerInventory(Player player);
    boolean setPlayerInventory(Player player);
}
