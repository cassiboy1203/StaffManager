package io.github.cassiboy1203.staffmanagercore;

import com.google.inject.Guice;
import com.google.inject.Singleton;
import io.github.cassiboy1203.staffmanagercore.commands.ICommandRegistar;
import io.github.cassiboy1203.staffmanagercore.events.listerners.IListener;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class StaffManagerCore extends JavaPlugin {

    public static final String PERMISSION_BASE = "staffmanager";
    public static final String NAME = "StaffManagerCore";

    @Override
    public void onEnable() {

        saveDefaultConfig();

        var injector = Guice.createInjector(new StaffManagerCoreFactory(getConfig(),this));
        var listener = injector.getInstance(IListener.class);
        getServer().getPluginManager().registerEvents(listener, this);

        injector.getInstance(ICommandRegistar.class).registerCommands();
    }

    @Override
    public void onDisable() {

    }
}
