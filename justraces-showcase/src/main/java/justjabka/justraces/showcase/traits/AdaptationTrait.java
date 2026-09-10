package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.events.race.PlayerRaceChangeEvent;
import justjabka.justraces.api.interfaces.configurable.TraitConfigurable;
import justjabka.justraces.api.listeners.generic.BaseTraitListener;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.tag.DamageTypeTags;

public class AdaptationTrait extends BaseTraitListener implements TraitConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "adaptation");
    }

    @EventHandler(ignoreCancelled = true)
    public void resetAdaptationsOnDeath(PlayerDeathEvent event) {
        resetAdaptations(event.getPlayer());
    }

    @EventHandler(ignoreCancelled = true)
    public void resetAdaptationsOnRaceChange(PlayerRaceChangeEvent event) {
        resetAdaptations(event.getPlayer());
    }

    private void resetAdaptations(Player player) {
        removeContainerData(player, getKey());
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageTaken(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!isRequiredTrait(player)) return;

        final double damage = event.getDamage();
        final DamageType damageType = event.getDamageSource().getDamageType();

        if (DamageTypeTags.BYPASSES_RESISTANCE.isTagged(damageType)) return;

        // Get Adaptations
        PersistentDataContainer adaptations = getContainerTagContainer(player, getKey());

        // Get Current Resist Value
        final float minPercent = getConfigFloat("min_percent");
        final float currentValue = adaptations.getOrDefault(damageType.getKey(), PersistentDataType.FLOAT, minPercent);

        if (currentValue > 0) {
            final double damageReduced = damage * (currentValue / 100.0);
            event.setDamage(damage - damageReduced);
        }

        float totalSum = 0.0f;
        for (NamespacedKey key : adaptations.getKeys()) {
            totalSum += adaptations.getOrDefault(key, PersistentDataType.FLOAT, 0.0f);
        }

        // Save new value
        final float newValue = Math.clamp(currentValue + getAdaptationAmount(damageType, adaptations, totalSum), minPercent, getMaxAdaptationPercent());
        adaptations.set(damageType.getKey(), PersistentDataType.FLOAT, newValue);

        setContainerTagContainer(player, getKey(), adaptations);
    }

    private float getAdaptationAmount(DamageType damageType, PersistentDataContainer adaptations, float totalSum) {
        float amount = getConfigFloat("percent_per_hit");

        if (totalSum + amount <= getMaxAdaptationPercent()) return amount;

        float overflow = (totalSum + amount) - getMaxAdaptationPercent();

        final NamespacedKey currentDamageTypeKey = damageType.getKey();

        for (NamespacedKey key : adaptations.getKeys()) {
            if (key.equals(currentDamageTypeKey)) continue;

            float value = adaptations.getOrDefault(key, PersistentDataType.FLOAT, 0.0f);
            if (value <= 0) continue;

            float deduction = Math.min(value, overflow);
            adaptations.set(key, PersistentDataType.FLOAT, value - deduction);
            overflow -= deduction;

            if (overflow <= 0) break;
        }

        return amount;
    }

    private float getMaxAdaptationPercent() {
        return getConfigFloat("max_percent");
    }
}
