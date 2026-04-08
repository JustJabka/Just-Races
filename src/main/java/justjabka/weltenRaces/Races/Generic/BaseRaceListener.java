package justjabka.weltenRaces.Races.Generic;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.weltenRaces.Manager.RaceManager.RACE_KEY;

public abstract class BaseRaceListener implements Listener {
    // Utils
    public String getRace(Player player) {
        String race = player.getPersistentDataContainer().get(RACE_KEY, PersistentDataType.STRING);
        return race != null ? race : "none";
    }

    public boolean raceEquals(Player player, String raceId) {
        return raceId.equals(getRace(player));
    }

    public boolean hasAnyArmor(Player player) {
        ItemStack[] equipment = player.getEquipment().getArmorContents();

        for (ItemStack item : equipment) {
            if (item == null) continue;
            if (item.isEmpty()) continue;

            return true;
        }
        return false;
    }
}