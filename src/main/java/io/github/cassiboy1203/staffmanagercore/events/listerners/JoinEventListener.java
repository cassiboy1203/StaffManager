package io.github.cassiboy1203.staffmanagercore.events.listerners;

import com.google.inject.Inject;
import io.github.cassiboy1203.staffmanagercore.IVanish;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinEventListener implements IListener{

    private IVanish vanish;

    @Inject
    public void setVanish(IVanish vanish) {
        this.vanish = vanish;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        var player = event.getPlayer();

        vanish.hideVanishedPlayers(player);
    }
}
