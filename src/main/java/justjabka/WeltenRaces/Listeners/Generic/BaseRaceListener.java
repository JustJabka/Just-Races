package justjabka.WeltenRaces.Listeners.Generic;

import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Managers.RaceManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.spongepowered.configurate.CommentedConfigurationNode;

public abstract class BaseRaceListener implements Listener {
    public abstract NamespacedKey getRaceKey();

    public CommentedConfigurationNode getConfig() {
        return RaceProvider.get(getRaceKey()).getConfig();
    }

    public boolean isRequiredRace(Player player) {
        return RaceManager.isRace(player, getRaceKey());
    }
}
