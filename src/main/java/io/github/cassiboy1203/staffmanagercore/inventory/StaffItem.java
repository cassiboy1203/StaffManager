package io.github.cassiboy1203.staffmanagercore.inventory;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public record StaffItem(int slot, Material material, int amount, String name, List<String> description) {

    public static StaffItem fromMap(HashMap map){
        var slot = (int) map.get("slot");
        var item = (HashMap) map.get("item");
        var material = Material.getMaterial((String) item.get("item"));
        var amount = (int) item.get("amount");
        var name = (String) item.get("name");
        var description = (List<String>)item.get("description");

        return new StaffItem(slot, material, amount, name, description);
    }
}
