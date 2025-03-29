package io.github.cassiboy1203.staffManagerLib;


import io.github.cassiboy1203.staffManagerLib.annotations.Singleton;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class StaffManagerLib extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
    }

    public Injector start(JavaPlugin plugin) {
        var injector =  new Injector(plugin);
        injector.start();

        return injector;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
