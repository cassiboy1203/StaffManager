package io.github.cassiboy1203.staffManagerPermissions.configs;

import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public abstract class CustomConfig implements ICustomConfig{
    protected File file;
    protected FileConfiguration config;
    protected JavaPlugin plugin;
    protected String fileName;

    @Inject
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setup() {
        plugin.saveResource(fileName, false);

        file = new File(Bukkit.getServer().getPluginManager().getPlugin("StaffManagerPermissions").getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, String.format("Failed to create %s file", fileName), e);
            }
        }

        config = YamlConfiguration.loadConfiguration(file);
    }

    @Override
    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, String.format("Failed to save %s", fileName), e);
        }
    }

    @Override
    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
