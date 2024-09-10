package io.github.cassiboy1203.staffmanagercore;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import io.github.cassiboy1203.staffmanagercore.commands.*;
import io.github.cassiboy1203.staffmanagercore.events.listerners.IListener;
import io.github.cassiboy1203.staffmanagercore.events.listerners.JoinEventListener;
import io.github.cassiboy1203.staffmanagercore.inventory.IStaffInventory;
import io.github.cassiboy1203.staffmanagercore.inventory.StaffInventory;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class StaffManagerCoreFactory extends AbstractModule {

    private final FileConfiguration config;
    private final JavaPlugin plugin;

    public StaffManagerCoreFactory(FileConfiguration config, JavaPlugin plugin) {
        this.config = config;
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(IStaffMode.class).to(StaffMode.class);
        bind(IStaffInventory.class).toInstance(new StaffInventory());
        bind(FileConfiguration.class).toInstance(config);
        bind(JavaPlugin.class).toInstance(plugin);
        bind(IVanish.class).to(Vanish.class);
        bind(ICommandRegistar.class).to(CommandRegistar.class);


        Multibinder<ICommand> commandBinder = Multibinder.newSetBinder(binder(), ICommand.class);
        commandBinder.addBinding().to(StaffCommand.class);
        commandBinder.addBinding().to(GamemodeCommand.class);
        commandBinder.addBinding().to(FlyCommand.class);

        bind(IListener.class).to(JoinEventListener.class);
    }
}
