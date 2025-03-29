package io.github.cassiboy1203.staffManagerLib.factories;

import io.github.cassiboy1203.staffManagerLib.ConfigHandler;
import io.github.cassiboy1203.staffManagerLib.Injector;
import io.github.cassiboy1203.staffManagerLib.annotations.config.Config;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public class ConfigFactory {
    private final JavaPlugin plugin;
    private final Injector injector;

    public ConfigFactory(JavaPlugin plugin, Injector injector) {
        this.plugin = plugin;
        this.injector = injector;

        ConfigHandler.plugin = plugin;
    }

    public Object registerConfig(Class<?> clazz){
        var instance = injector.getInstance(clazz);

        setup(clazz.getAnnotation(Config.class).value());
        ConfigHandler.load(instance);

        return instance;
    }

    private void setup(String fileName) {
        if (fileName.isEmpty())
            fileName = "config.yml";
        plugin.saveResource(fileName, false);
        var file = new File(Bukkit.getServer().getPluginManager().getPlugin(plugin.getName()).getDataFolder(), fileName);

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, String.format("Failed to create %s file", fileName), e);
            }
        }
    }
}
