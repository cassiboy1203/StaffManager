package io.github.cassiboy1203.staffManagerPermissions;

import com.google.inject.Guice;
import com.google.inject.Singleton;
import io.github.cassiboy1203.staffManagerPermissions.configs.IGroupConfig;
import org.bukkit.plugin.java.JavaPlugin;

@Singleton
public final class StaffManagerPermissions extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        var injector = Guice.createInjector(new StaffManagerPermissionsFactory(this, getConfig()));
        var groupConfig = injector.getInstance(IGroupConfig.class);
        groupConfig.setup();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
