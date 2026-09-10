package justjabka.justraces.showcase.traits;

import justjabka.justraces.api.interfaces.configurable.TraitConfigurable;
import justjabka.justraces.api.listeners.generic.BaseTraitListener;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CelestialCombatantTrait extends BaseTraitListener implements TraitConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "celestial_combatant");
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!isRequiredTrait(attacker)) return;
        if (attacker.isOnGround()) return;

        float minAttackCooldown = getConfigFloat("min_attack_cooldown");
        if (attacker.getAttackCooldown() < minAttackCooldown) return;

        double damage = event.getDamage();
        double damageBonus = damage * getConfigDouble("bonus");
        event.setDamage(damage + damageBonus);

        victim.getWorld().spawnParticle(
                Particle.SMALL_GUST,
                victim.getEyeLocation().subtract(0, 0.5, 0),
                3,
                0.25,
                0.5,
                0.25
        );
    }
}
