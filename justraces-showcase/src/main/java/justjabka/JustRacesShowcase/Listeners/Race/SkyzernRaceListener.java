package justjabka.JustRacesShowcase.Listeners.Race;

import justjabka.JustRaces.Interfaces.Configurable.RaceConfigurable;
import justjabka.JustRaces.Listeners.Generic.BaseRaceListener;
import justjabka.JustRacesShowcase.DataProvider.RaceProvider;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class SkyzernRaceListener extends BaseRaceListener implements RaceConfigurable {

    @Override
    public NamespacedKey getKey() {
        return RaceProvider.SKYZERN;
    }

    @EventHandler
    public void applyCelestialOriginBonus(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof LivingEntity victim)) return;
        if (!(event.getDamager() instanceof Player attacker)) return;

        if (!isRequiredRace(attacker)) return;
        if (attacker.isOnGround()) return;

        float minAttackCooldown = getConfigFloat("air_damage_multiplier", "min_attack_cooldown");
        if (attacker.getAttackCooldown() < minAttackCooldown) return;

        double damage = event.getDamage();
        double damageBonus = damage * getConfigDouble("air_damage_multiplier", "bonus");
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
