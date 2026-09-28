package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.traits.generic.BaseTraitListener;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Bee;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.jspecify.annotations.NonNull;

public class PassiveBeesTrait extends BaseTraitListener {

    @Override
    public @NonNull NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "passive_bees");
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTarget(EntityTargetLivingEntityEvent event) {
        if (!(event.getTarget() instanceof Player player)) return;
        if (!(event.getEntity() instanceof Bee)) return;

        if (!isRequiredTrait(player)) return;

        event.setCancelled(true);
    }
}
