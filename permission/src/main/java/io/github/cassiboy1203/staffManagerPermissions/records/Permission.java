package io.github.cassiboy1203.staffManagerPermissions.records;

import java.util.ArrayList;
import java.util.List;

public record Permission(String name, boolean value) {
    public static Permission valueOf(String name) {
        return new Permission(name.substring(1), name.charAt(0) != '-');
    }

    public static List<Permission> valuesOf(List<String> names) {
        var permissions = new ArrayList<Permission>();
        for (var name : names) {
            permissions.add(valueOf(name));
        }

        return permissions;
    }

    @Override
    public String toString() {
        return value ? "" : "-" + name;
    }
}
