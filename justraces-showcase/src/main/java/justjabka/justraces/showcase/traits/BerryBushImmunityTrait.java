package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.traits.generic.BaseTraitListener;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByBlockEvent;

public class BerryBushImmunityTrait extends BaseTraitListener {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "berry_bush_immunity");
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isRequiredTrait(player)) return;

        DamageType damageType = event.getDamageSource().getDamageType();
        if (!damageType.equals(DamageType.SWEET_BERRY_BUSH)) return;
        event.setCancelled(true);
    }
}
