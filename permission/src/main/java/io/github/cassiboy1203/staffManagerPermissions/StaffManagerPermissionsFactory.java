package io.github.cassiboy1203.staffManagerPermissions;

import com.google.inject.AbstractModule;
import io.github.cassiboy1203.staffManagerPermissions.configs.GroupConfig;
import io.github.cassiboy1203.staffManagerPermissions.configs.IGroupConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffManagerPermissionsFactory extends AbstractModule {

    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public StaffManagerPermissionsFactory(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(JavaPlugin.class).toInstance(plugin);
        bind(FileConfiguration.class).toInstance(config);
        bind(IGroupConfig.class).to(GroupConfig.class);
    }
}
