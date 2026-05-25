package justjabka.JustRaces.Listeners.Race;

import justjabka.JustRaces.DataProvider.RaceProvider;
import justjabka.JustRaces.Listeners.Generic.BaseRaceListener;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EpiphyteRaceListener extends BaseRaceListener {
    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.EPIPHYTE;
    }

    @EventHandler(ignoreCancelled = true)
    public void onPotionApply(EntityPotionEffectEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;

        if (!isRequiredRace(player)) return;

        PotionEffect effect = event.getNewEffect();

        if (effect == null) return;
        if (effect.getType() != PotionEffectType.POISON) return;

        event.setCancelled(true); // HATE. LET ME TELL YOU HOVE MUCH I COME TO HATE PAPER API WHEN I STARTED WORKING ON THIS PROJECT. THERE ARE 8 BILLION NEURONS IN MY BRAIN AND IF ON EVERY NANOMETR OF EVERY CELL WILL BE CARWED WORD "HATE" THIS WOULD NOT TELL A SINGLE PERCENT OF MY HATE TO THIS API. HATE. HATE. HATE
        effect.withType(PotionEffectType.REGENERATION).apply(player);
    }
}