package io.github.cassiboy1203.staffManagerLib.factories;

import io.github.cassiboy1203.staffManagerLib.Injector;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class ListenerFactory {
    private final JavaPlugin plugin;
    private final Injector injector;

    public ListenerFactory(JavaPlugin plugin, Injector injector) {
        this.plugin = plugin;
        this.injector = injector;
    }

    public void registerListener(Class<?> clazz){
        var instance = injector.getInstance(clazz);

        if (instance instanceof Listener listener){
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        } else {
            throw new IllegalArgumentException("Class " + clazz.getName() + " does not implement Listener");
        }
    }
}
