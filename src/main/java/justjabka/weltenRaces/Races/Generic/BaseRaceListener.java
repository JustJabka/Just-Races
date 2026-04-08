package justjabka.weltenRaces.Races.Generic;

import io.papermc.paper.event.entity.EntityEquipmentChangedEvent;
import justjabka.weltenRaces.Managers.ArmorManager;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class BaseRaceListener implements Listener {
    @EventHandler
    public void onArmorChange(EntityEquipmentChangedEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;

        ArmorManager.updateArmorSet(player);
    }
}