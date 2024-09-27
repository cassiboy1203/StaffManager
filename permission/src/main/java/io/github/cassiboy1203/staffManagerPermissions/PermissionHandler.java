package io.github.cassiboy1203.staffManagerPermissions;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.cassiboy1203.staffManagerPermissions.configs.IGroupConfig;
import io.github.cassiboy1203.staffManagerPermissions.exceptions.GroupException;
import io.github.cassiboy1203.staffManagerPermissions.exceptions.GroupNotFoundException;
import io.github.cassiboy1203.staffManagerPermissions.records.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Singleton
public class PermissionHandler implements IPermissionHandler {

    private List<Group> groups = new ArrayList<>();
    private static IPermissionHandler instance;
    private IGroupConfig groupConfig;
    private Map<UUID, PermissionAttachment> players;
    private JavaPlugin plugin;

    public static IPermissionHandler getInstance() {
        if (instance == null) {
            instance = new PermissionHandler();
        }
        return instance;
    }

    @Inject
    public void setGroupConfig(IGroupConfig groupConfig) {
        this.groupConfig = groupConfig;
    }

    @Inject
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void addPlayer(Player player) {
        var attachment = player.addAttachment(plugin);
        players.put(player.getUniqueId(), attachment);
    }

    @Override
    public void removePlayer(Player player) {
        player.removeAttachment(players.get(player.getUniqueId()));
        players.remove(player.getUniqueId());
    }

    @Override
    public void removePlayer(UUID uuid) {
        var player = Bukkit.getPlayer(uuid);
        if (player != null) {
            removePlayer(player);
        }
    }

    @Override
    public void removeAllPlayers() {
        for (var player : players.keySet()) {
            removePlayer(player);
        }
    }

    @Override
    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public void addGroup(Group group) {
        this.groups.add(group);
    }

    @Override
    public void removeGroup(Group group) {
        this.groups.remove(group);
    }

    @Override
    public void addPermission(String group, String permission) {
        var groupGroup = groups.stream().filter(g -> g.name().equalsIgnoreCase(group)).findFirst().orElse(null);
        if (groupGroup == null) {
            throw new GroupNotFoundException();
        }

        groupGroup.permissions().add(permission);
        groupConfig.updatePermissions(groupGroup);
    }

    @Override
    public void removePermission(String group, String permission) {
        var groupGroup = groups.stream().filter(g -> g.name().equalsIgnoreCase(group)).findFirst().orElse(null);
        if (groupGroup == null) {
            throw new GroupNotFoundException();
        }

        if (!groupGroup.permissions().contains(permission)) {
            throw new GroupException(String.format("Group does not have the permission: %s", permission));
        }

        groupGroup.permissions().remove(permission);

        groupConfig.updatePermissions(groupGroup);
    }

    @Override
    public boolean hasPermission(String group, String permission) {
        var groupGroup = groups.stream().filter(g -> g.name().equalsIgnoreCase(group)).findFirst().orElse(null);
        if (groupGroup == null) {
            throw new GroupNotFoundException();
        }

        return groupGroup.permissions().contains(permission);
    }

    @Override
    public void addPermission(Player player, String permissions) {

    }

    @Override
    public void removePermission(Player player, String permissions) {

    }
}
