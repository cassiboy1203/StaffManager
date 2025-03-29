package io.github.cassiboy1203.staffmanagercore.events.listerners;

import io.github.cassiboy1203.staffManagerLib.annotations.Inject;
import io.github.cassiboy1203.staffManagerLib.annotations.MCListener;
import io.github.cassiboy1203.staffmanagercore.IVanish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@MCListener
public class JoinEventListener implements Listener {

    private final IVanish vanish;

    @Inject
    public JoinEventListener(IVanish vanish){
        this.vanish = vanish;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        vanish.hideVanishedPlayers(player);
    }
}
