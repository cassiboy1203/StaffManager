package io.github.cassiboy1203.staffManagerPermissions;

import io.github.cassiboy1203.staffManagerPermissions.records.Group;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface IPermissionHandler {
    void addPlayer(Player player);
    void removePlayer(Player player);
    void removePlayer(UUID uuid);
    void removeAllPlayers();
    void setGroups(List<Group> groups);
    void addGroup(Group group);
    void removeGroup(Group group);
    void addPermission(String group, String permission);
    void removePermission(String group, String permission);
    boolean hasPermission(String group, String permission);
    void addPermission(Player player, String permissions);
    void removePermission(Player player, String permissions);
}
