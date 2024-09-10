package io.github.cassiboy1203.staffmanagercore.inventory;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.cassiboy1203.staffmanagercore.StaffManagerCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

@Singleton
public class StaffInventory implements IStaffInventory {

    private FileConfiguration staffInventories;
    private File file;
    private FileConfiguration config;

    public StaffInventory() {
        setup();
    }

    @Inject
    public void setConfig(FileConfiguration config) {
        this.config = config;
    }

    private void setup(){
        file = new File(Bukkit.getServer().getPluginManager().getPlugin(StaffManagerCore.NAME).getDataFolder(), "staffInventory.yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Failed to create staff inventories file", e);
            }
        }

        staffInventories = YamlConfiguration.loadConfiguration(file);

        Bukkit.getLogger().log(Level.INFO, "Loaded staff inventories file");
    }


    @Override
    public boolean resetPlayerInventory(Player player) {
        var playerInventory = staffInventories.get(player.getUniqueId().toString());
        if (playerInventory == null) {
            player.getInventory().setContents(new ItemStack[]{});
        } else {
            player.getInventory().setContents((ItemStack[]) playerInventory);
        }
        return true;
    }

    @Override
    public boolean setPlayerInventory(Player player) {
        staffInventories.set(player.getUniqueId().toString(), player.getInventory().getContents());
        giveStaffItemsToPlayer(player);
        try {
            staffInventories.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to save staff inventory", e);
            return false;
        }

        return true;
    }

    private boolean giveStaffItemsToPlayer(Player player) {
        var inventory = config.getMapList("staff-mode.inventory");

        player.getInventory().setContents(new ItemStack[]{});

        for (Map item : inventory){
            var staffItem = StaffItem.fromMap((LinkedHashMap)item.get("item"));
            Bukkit.getLogger().log(Level.WARNING, staffItem.toString());
            var itemStack = new ItemStack(Objects.requireNonNull(staffItem.material()), staffItem.amount());
            var metadata = itemStack.getItemMeta();
            assert metadata != null;
            metadata.setDisplayName(staffItem.name());
            metadata.setLore(staffItem.description());
            itemStack.setItemMeta(metadata);

            player.getInventory().setItem(staffItem.slot(), itemStack);
        }
        return true;
    }
}
