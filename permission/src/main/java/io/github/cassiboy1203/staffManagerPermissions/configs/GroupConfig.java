package io.github.cassiboy1203.staffManagerPermissions.configs;

import com.google.inject.Singleton;
import io.github.cassiboy1203.staffManagerPermissions.records.Group;
import io.github.cassiboy1203.staffManagerPermissions.exceptions.GroupException;
import io.github.cassiboy1203.staffManagerPermissions.exceptions.GroupNotFoundException;
import io.github.cassiboy1203.staffManagerPermissions.records.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Singleton
public class GroupConfig extends CustomConfig implements IGroupConfig{

    public GroupConfig() {
        this.fileName = "groups.yml";
    }

    @Override
    public List<Group> getGroups() {
        var groupsSection = config.getConfigurationSection("groups");
        var groupMap = new HashMap<Group, List<String>>();

        var hasDefault = false;

        for (var groupName : groupsSection.getKeys(false)){
            var groupSection = groupsSection.getConfigurationSection(groupName);

            var isDefault = groupSection.getBoolean("default");
            var permissions = groupSection.getStringList("permissions");
            var inheritance = groupSection.getStringList("inheritance");

            if (isDefault){
                if (hasDefault){
                    throw new GroupException("Multiple default groups found");
                }

                hasDefault = true;
            }

            var group = new Group(groupName, isDefault, Permission.valuesOf(permissions), new ArrayList<>());
            groupMap.put(group, inheritance);
        }

        if (!hasDefault){
            throw new GroupException("No default group found");
        }

        for (var groupEntrySet : groupMap.entrySet()){
            var group = groupEntrySet.getKey();
            var inheritedGroups = new ArrayList<Group>();
            for (var inheritedGroup: groupEntrySet.getValue()){
                groupMap.keySet().stream().filter(groupValue -> groupValue.name().equalsIgnoreCase(inheritedGroup)).findAny().ifPresent(inheritedGroups::add);
            }
            group.inheritedGroups().addAll(inheritedGroups);
        }

        return groupMap.keySet().stream().toList();
    }

    @Override
    public void updateGroup(Group group) {
        if (group.isDefault())
            updateDefaultTo(group);
        updatePermissions(group);
        updateInheritance(group);
    }

    @Override
    public void updateDefaultTo(Group group) {
        var groupsSection = config.getConfigurationSection("groups");
        var groupNames = groupsSection.getKeys(false);
        if (!groupNames.contains(group.name())){
            throw new GroupNotFoundException(group.name());
        }

        for (var groupName: groupNames){
            var groupSection = groupsSection.getConfigurationSection(groupName);
            groupSection.set("default", groupName.equalsIgnoreCase(group.name()));
        }
        save();
    }

    @Override
    public void updatePermissions(Group group) {
        var groupSection = config.getConfigurationSection(String.format("groups.%s", group.name()));
        if (groupSection == null){
            throw new GroupNotFoundException(group.name());
        }

        groupSection.set("permissions", group.permissions().stream().map(Permission::toString).toList());
        save();
    }

    @Override
    public void updateInheritance(Group group) {
        var groupSection = config.getConfigurationSection(String.format("groups.%s", group.name()));
        if (groupSection == null){
            throw new GroupNotFoundException(group.name());
        }

        groupSection.set("inheritance", group.getInheritedGroupsStringList());
        save();
    }
}
