package justjabka.weltenRaces.Races.Generic;

import justjabka.weltenRaces.Types.Race;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.weltenRaces.Managers.RaceManager.RACE_KEY;

public abstract class BaseRaceListener implements Listener {
    // Utils
    public String getRace(Player player) {
        PersistentDataContainer data = player.getPersistentDataContainer();
        String race = data.get(RACE_KEY, PersistentDataType.STRING);

        return race != null ? race : "none";
    }

    public boolean raceEquals(Player player, Race race) {
        return race.toString().equals(getRace(player));
    }
}