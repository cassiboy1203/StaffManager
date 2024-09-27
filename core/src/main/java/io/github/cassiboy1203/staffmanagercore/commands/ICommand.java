package io.github.cassiboy1203.staffmanagercore.commands;

import org.bukkit.command.CommandExecutor;

public interface ICommand extends CommandExecutor {
    String getCommandName();
}
