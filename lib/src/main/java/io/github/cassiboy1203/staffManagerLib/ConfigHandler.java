package io.github.cassiboy1203.staffManagerLib;

import io.github.cassiboy1203.staffManagerLib.annotations.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;


public final class ConfigHandler {
    public static JavaPlugin plugin;

    private static File getFile(String fileName){
        return new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), fileName);
    }

    private static FileConfiguration getConfig(String fileName){
        FileConfiguration fileConfiguration;
        if (fileName.isEmpty()) {
            fileConfiguration = plugin.getConfig();
        } else {
            var file = getFile(fileName);
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        }

        return fileConfiguration;
    }

    public static void save(Object config) {

    }

    public static void load(Object config) {
        var fileName = config.getClass().getAnnotation(Config.class).value();
        var fileConfig = getConfig(fileName);

        loadFields(fileConfig, config);
    }

    private static void loadFields(ConfigurationSection configSection, Object instance){

    }
}
