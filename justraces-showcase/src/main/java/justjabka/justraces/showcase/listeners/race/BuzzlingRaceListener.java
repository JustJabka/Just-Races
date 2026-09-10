package justjabka.justraces.showcase.listeners.race;

import justjabka.justraces.api.listeners.generic.BaseRaceListener;
import justjabka.justraces.showcase.dataprovider.RaceProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class BuzzlingRaceListener extends BaseRaceListener {

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.BUZZLING;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Bee)) return;

        if (!isRequiredRace(player)) return;

        event.setCancelled(true);
    }
}
