package io.github.cassiboy1203.staffmanagercore;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class Vanish implements IVanish{

    private final List<UUID> vanishedPlayers;
    private IStaffMode staffMode;
    private JavaPlugin plugin;

    public Vanish(){
        vanishedPlayers = new ArrayList<>();
    }

    @Inject
    public void setStaffMode(IStaffMode staffMode) {
        this.staffMode = staffMode;
    }

    @Inject
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
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
