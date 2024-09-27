package io.github.cassiboy1203.staffmanagercore;

import com.google.inject.Singleton;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Singleton
public class StaffMode implements IStaffMode{
    private final List<UUID> playersInStaffMode;

    public StaffMode(){
        playersInStaffMode = new ArrayList<>();
    }

    @Override
    public boolean isInStaffMode(Player player) {
        return playersInStaffMode.contains(player.getUniqueId());
    }

    @Override
    public void setPlayerInStaffmode(Player player) {
        if (!playersInStaffMode.contains(player.getUniqueId())) {
            playersInStaffMode.add(player.getUniqueId());
        }
    }

    @Override
    public void removePlayerInStaffmode(Player player) {
        playersInStaffMode.remove(player.getUniqueId());
    }
}
