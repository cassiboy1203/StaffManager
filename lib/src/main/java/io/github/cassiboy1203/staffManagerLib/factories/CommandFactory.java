package io.github.cassiboy1203.staffManagerLib.factories;

import io.github.cassiboy1203.staffManagerLib.Injector;
import io.github.cassiboy1203.staffManagerLib.annotations.command.Alias;
import io.github.cassiboy1203.staffManagerLib.annotations.command.MCCommand;
import io.github.cassiboy1203.staffManagerLib.annotations.command.Subcommand;
import io.github.cassiboy1203.staffManagerLib.exceptions.InvalidCommandException;
import io.github.cassiboy1203.staffManagerLib.exceptions.methodNotSubcommandException;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CommandFactory {
    private final JavaPlugin plugin;
    private final Injector injector;

    public CommandFactory(JavaPlugin plugin, Injector injector) {
        this.plugin = plugin;
        this.injector = injector;
    }

    public void registerCommand(Class<?> clazz){
        var instance = injector.getInstance(clazz);

        CommandExecutor command;

        if (instance instanceof CommandExecutor){
            command = (CommandExecutor) instance;
        } else {

            command = new CommandExecutor() {

                @Override
                public boolean onCommand(CommandSender sender, Command command, String commandName, String[] args) {

                    Method defaultMethod = null;

                    try {
                        for (var method : clazz.getMethods()) {
                            if (method.isAnnotationPresent(Alias.class)) {
                                var alias = method.getAnnotation(Alias.class).value();

                                if (commandName.equals(alias)) {
                                    try {

                                        if (method.isAnnotationPresent(Subcommand.class)) {
                                            return runSubcommand(sender, command, args, method);
                                        }

                                        return (boolean) method.invoke(instance, sender, command, args);

                                    } catch (methodNotSubcommandException ignored) {
                                    }
                                } else if (alias.isEmpty()) {
                                    defaultMethod = method;
                                } else if (method.getName().equals("onCommand") && defaultMethod == null) {
                                    defaultMethod = method;
                                }
                            } else if (method.isAnnotationPresent(Subcommand.class)) {
                                try {
                                    return runSubcommand(sender, command, args, method);
                                } catch (methodNotSubcommandException ignored) {
                                }
                            }
                        }

                        if (defaultMethod != null) {
                            return (boolean) defaultMethod.invoke(instance, sender, command, args);
                        }

                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new InvalidCommandException();
                    }

                    return false;
                }

                private boolean runSubcommand(CommandSender sender, Command command, String[] args, Method method) throws InvocationTargetException, IllegalAccessException, methodNotSubcommandException {
                    var subcommand = method.getAnnotation(Subcommand.class).name();

                    if (args.length == 0)
                        throw new methodNotSubcommandException();

                    if (args[0].equals(subcommand)) {
                        return (boolean) method.invoke(instance, sender, command, Arrays.copyOfRange(args, 1, args.length));
                    }

                    throw new methodNotSubcommandException();
                }
            };
        }
        plugin.getCommand(clazz.getAnnotation(MCCommand.class).value()).setExecutor(command);
    }
}