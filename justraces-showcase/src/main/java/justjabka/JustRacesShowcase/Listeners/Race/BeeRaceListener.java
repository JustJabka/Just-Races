package justjabka.JustRacesShowcase.Listeners.Race;

import justjabka.JustRaces.Listeners.Generic.BaseRaceListener;
import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

public class BeeRaceListener extends BaseRaceListener {

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.BEE;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Bee)) return;

        if (!isRequiredRace(player)) return;

        event.setCancelled(true);
    }
}
