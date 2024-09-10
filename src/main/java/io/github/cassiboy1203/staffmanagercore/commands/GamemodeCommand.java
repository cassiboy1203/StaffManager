package io.github.cassiboy1203.staffmanagercore.commands;

import com.google.inject.Inject;
import io.github.cassiboy1203.staffmanagercore.IStaffMode;
import io.github.cassiboy1203.staffmanagercore.StaffManagerCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GamemodeCommand implements ICommand{

    private static final String GAMEMODE_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, "gamemode");
    private static final String GAMEMODE_OTHER_PERMISSION = String.join(".", StaffManagerCore.PERMISSION_BASE, GAMEMODE_PERMISSION, "other");

    private IStaffMode staffMode;

    @Inject
    public void setStaffMode(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Override
    public String getCommandName() {
        return "gamemode";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(IStaffMode.GAMEMODE_PERMISSION)){
                return false;
            } else if (!staffMode.isInStaffMode(player) && !player.hasPermission(GAMEMODE_PERMISSION)){
                player.sendMessage(String.format("%sYou dont have the permission to change gamemode outside of staff mode.", ChatColor.RED));
                return true;
            }

            if (args.length > 0 && args.length < 3){
                if (!player.hasPermission(IStaffMode.GAMEMODE_OTHER_PERMISSION)){
                    player.sendMessage(String.format("%sYou dont have the permission to change another players gamemode.", ChatColor.RED));
                    return true;
                } else if (!staffMode.isInStaffMode(player) && !player.hasPermission(GAMEMODE_OTHER_PERMISSION)){
                    player.sendMessage(String.format("%sYou dont have the permission to change another players gamemode outside of staff mode.", ChatColor.RED));
                    return true;
                }
            } else if (args.length > 2){
                player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                player.sendMessage(String.format("%sUsage: /gamemode <gamemode> [player].", ChatColor.RED));
                return true;
            }

            switch (s){
                case "gmc":
                    if (args.length == 0) {
                        changeGamemode(player, "c");
                    } else if (args.length == 1) {
                        var otherPlayer = Bukkit.getPlayer(args[0]);
                        if (otherPlayer == null) {
                            player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[1]));
                            return true;
                        }
                        changeGamemode(otherPlayer, "c");
                        player.sendMessage(String.format("%sPlayer %s gamemode has been changed to creative.", ChatColor.YELLOW, args[0]));
                    } else {
                        player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                        player.sendMessage(String.format("%sUsage: /gmc [player].", ChatColor.RED));
                        return true;
                    }
                    break;
                case "gms":
                    if (args.length == 0) {
                        changeGamemode(player, "s");
                    } else if (args.length == 1) {
                        var otherPlayer = Bukkit.getPlayer(args[0]);
                        if (otherPlayer == null) {
                            player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[1]));
                            return true;
                        }
                        changeGamemode(otherPlayer, "s");
                        player.sendMessage(String.format("%sPlayer %s gamemode has been changed to survival.", ChatColor.YELLOW, args[0]));
                    } else {
                        player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                        player.sendMessage(String.format("%sUsage: /gms [player].", ChatColor.RED));
                        return true;
                    }
                    break;
                case "gmsp":
                    if (args.length == 0) {
                        changeGamemode(player, "sp");
                    } else if (args.length == 1) {
                        var otherPlayer = Bukkit.getPlayer(args[0]);
                        if (otherPlayer == null) {
                            player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[1]));
                            return true;
                        }
                        changeGamemode(otherPlayer, "sp");
                        player.sendMessage(String.format("%sPlayer %s gamemode has been changed to spectator.", ChatColor.YELLOW, args[0]));
                    } else {
                        player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                        player.sendMessage(String.format("%sUsage: /gmsp [player].", ChatColor.RED));
                        return true;
                    }
                    break;
                case "gma":
                    if (args.length == 0) {
                        changeGamemode(player, "a");
                    } else if (args.length == 1) {
                        var otherPlayer = Bukkit.getPlayer(args[0]);
                        if (otherPlayer == null) {
                            player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[1]));
                            return true;
                        }
                        changeGamemode(otherPlayer, "a");
                        player.sendMessage(String.format("%sPlayer %s gamemode has been changed to adventure.", ChatColor.YELLOW, args[0]));
                    } else {
                        player.sendMessage(String.format("%sToo many arguments.", ChatColor.RED));
                        player.sendMessage(String.format("%sUsage: /gma [player].", ChatColor.RED));
                        return true;
                    }
                    break;
                default:
                    if (args.length == 0) {
                        player.sendMessage(String.format("%sToo few arguments.", ChatColor.RED));
                        player.sendMessage(String.format("%sUsage: /gamemode <gamemode> [player].", ChatColor.RED));
                    } else if (args.length == 1) {
                        changeGamemode(player, args[0]);
                    } else {
                        var otherPlayer = Bukkit.getPlayer(args[1]);
                        if (otherPlayer == null) {
                            player.sendMessage(String.format("%sPlayer %s not found.", ChatColor.RED, args[1]));
                            return true;
                        }
                        changeGamemode(otherPlayer, args[0]);
                        player.sendMessage(String.format("%sPlayer %s gamemode has been changed to %s", ChatColor.YELLOW, args[1], args[0]));
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    private void changeGamemode(Player player, String gamemode){
        var changedGamemode = GameMode.SURVIVAL;
        switch (gamemode) {
            case "c":
            case "1":
            case "creative":
                changedGamemode = GameMode.CREATIVE;
                player.setGameMode(GameMode.CREATIVE);
                break;
            case "s":
            case "0":
            case "survival":
                changedGamemode = GameMode.SURVIVAL;
                player.setGameMode(GameMode.SURVIVAL);
                break;
            case "sp":
            case "4":
            case "spectator":
                changedGamemode = GameMode.SPECTATOR;
                player.setGameMode(GameMode.SPECTATOR);
                break;
            case "a":
            case "3":
            case "adventure":
                changedGamemode = GameMode.ADVENTURE;
                player.setGameMode(GameMode.ADVENTURE);
                break;
            default:
                player.sendMessage(String.format("%sUnknown gamemode %s.", ChatColor.RED, gamemode));
                return;
        }

        player.sendMessage(String.format("%sYour gamemode has been changed too %s", ChatColor.YELLOW, changedGamemode));
    }
}
