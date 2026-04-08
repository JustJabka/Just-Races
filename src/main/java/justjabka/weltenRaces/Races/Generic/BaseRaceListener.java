package justjabka.weltenRaces.Races.Generic;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.weltenRaces.Managers.ArmorManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.weltenRaces.Managers.RaceManager.RACE_KEY;

public abstract class BaseRaceListener implements Listener {
    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }

    // Utils
    public String getRace(Player player) {
        String race = player.getPersistentDataContainer().get(RACE_KEY, PersistentDataType.STRING);
        return race != null ? race : "none";
    }

    public boolean raceEquals(Player player, String raceId) {
        return raceId.equals(getRace(player));
    }
}