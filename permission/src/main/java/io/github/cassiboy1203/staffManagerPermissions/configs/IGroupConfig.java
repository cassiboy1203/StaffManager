package io.github.cassiboy1203.staffManagerPermissions.configs;

import io.github.cassiboy1203.staffManagerPermissions.records.Group;

import java.util.List;

public interface IGroupConfig extends ICustomConfig{
    List<Group> getGroups();
    void updateGroup(Group group);
    void updateDefaultTo(Group group);
    void updatePermissions(Group group);
    void updateInheritance(Group group);
}
