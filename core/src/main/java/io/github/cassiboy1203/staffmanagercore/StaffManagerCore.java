package io.github.cassiboy1203.staffmanagercore;

import io.github.cassiboy1203.staffManagerLib.StaffManagerLib;
import io.github.cassiboy1203.staffManagerLib.annotations.Plugin;
import io.github.cassiboy1203.staffManagerLib.annotations.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Singleton
@Plugin
public final class StaffManagerCore extends JavaPlugin {

    public static final String PERMISSION_BASE = "staffmanager";
    public static final String NAME = "StaffManagerCore";

    @Override
    public void onEnable() {

        saveDefaultConfig();

        StaffManagerLib staffManagerLib = (StaffManagerLib) Bukkit.getPluginManager().getPlugin("StaffManagerLib");

        if (staffManagerLib == null) {
            Bukkit.getLogger().log(Level.SEVERE, "Missing dependency: StaffManagerLib");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        var injector = staffManagerLib.start(this);
    }

    @Override
    public void onDisable() {

    }
}
