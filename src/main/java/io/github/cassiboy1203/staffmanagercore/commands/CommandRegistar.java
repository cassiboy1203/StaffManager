package io.github.cassiboy1203.staffmanagercore.commands;

import com.google.inject.Inject;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public class CommandRegistar implements ICommandRegistar{

    private JavaPlugin plugin;
    private Set<ICommand> commands;

    @Inject
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Inject
    public void setCommands(Set<ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public void registerCommands() {
        for (var command : commands) {
            plugin.getCommand(command.getCommandName()).setExecutor(command);
        }
    }
}
