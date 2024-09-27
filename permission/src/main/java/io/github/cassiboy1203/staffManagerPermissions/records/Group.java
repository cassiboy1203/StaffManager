package io.github.cassiboy1203.staffManagerPermissions.records;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record Group(String name, boolean isDefault, @Nonnull List<Permission> permissions, List<Group> inheritedGroups) {
    public List<Permission> getPermissionsIncludingInherited(){
        var inheritedPermissions = new ArrayList<Permission>(permissions);

        for (var group : inheritedGroups) {
            for (var permission : group.getPermissionsIncludingInherited()) {
                if (!inheritedPermissions.contains(permission)) {
                    inheritedPermissions.add(permission);
                }
            }
        }

        return inheritedPermissions;
    }

    public List<String> getInheritedGroupsStringList(){
        var groupStringList = new ArrayList<String>();
        for (var group : inheritedGroups) {
            groupStringList.add(group.name);
        }

        return groupStringList;
    }
}
