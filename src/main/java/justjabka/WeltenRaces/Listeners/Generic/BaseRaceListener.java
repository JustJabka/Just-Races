package justjabka.WeltenRaces.Listeners.Generic;

import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public abstract class BaseRaceListener implements Listener {
    public abstract NamespacedKey getRaceKey();

    public boolean isRequiredRace(Player player) {
        return RaceManager.isRace(player, getRaceKey());
    }
}
