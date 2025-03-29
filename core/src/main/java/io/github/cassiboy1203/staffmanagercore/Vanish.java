package io.github.cassiboy1203.staffmanagercore;

import io.github.cassiboy1203.staffManagerLib.annotations.Component;
import io.github.cassiboy1203.staffManagerLib.annotations.Inject;
import io.github.cassiboy1203.staffManagerLib.annotations.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
@Component
public class Vanish implements IVanish{

    private final List<UUID> vanishedPlayers;
    private final IStaffMode staffMode;
    private final JavaPlugin plugin;

    @Inject
    public Vanish(IStaffMode staffMode, JavaPlugin plugin) {
        this.staffMode = staffMode;
        this.plugin = plugin;
        vanishedPlayers = new ArrayList<>();
    }

    @Override
    public void vanish(Player player) {
        if (!vanishedPlayers.contains(player.getUniqueId())) {
            vanishedPlayers.add(player.getUniqueId());

            for (var person : Bukkit.getOnlinePlayers()){
                if (staffMode.isInStaffMode(person)){
                    if (!person.hasPermission(IStaffMode.SEE_VANISH_PERMISSION))
                        person.hidePlayer(plugin, player);
                } else {
                    if (!person.hasPermission(SEE_VANISH_PERMISSION))
                        person.hidePlayer(plugin, player);
                }
            }
            player.sendMessage(String.format("%sYou are now hidden", ChatColor.YELLOW));
        }
    }

    @Override
    public void unVanish(Player player) {
        vanishedPlayers.remove(player.getUniqueId());
        for (var person : Bukkit.getOnlinePlayers()){
            person.showPlayer(plugin, player);
        }
        player.sendMessage(String.format("%sYou are now shown", ChatColor.YELLOW));
    }

    @Override
    public void toggle(Player player) {
        if (vanishedPlayers.contains(player.getUniqueId())) {
            unVanish(player);
        } else {
            vanish(player);
        }
    }

    @Override
    public List<UUID> getVanishedPlayers() {
        return vanishedPlayers;
    }

    @Override
    public void hideVanishedPlayers(Player player) {
        for (var vanishedPlayer : vanishedPlayers){
            if (staffMode.isInStaffMode(player)){
                if (!player.hasPermission(IStaffMode.SEE_VANISH_PERMISSION))
                    player.hidePlayer(plugin, Bukkit.getPlayer(vanishedPlayer));
            } else {
                if (!player.hasPermission(SEE_VANISH_PERMISSION))
                    player.hidePlayer(plugin, Bukkit.getPlayer(vanishedPlayer));
            }
        }
    }
}
