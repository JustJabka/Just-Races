package justjabka.WeltenRaces.Listeners.Race;

import justjabka.WeltenRaces.Configs.Race.LizardRaceConfig;
import justjabka.WeltenRaces.DataProvider.DamageTypeTagKeysProvider;
import justjabka.WeltenRaces.DataProvider.RaceProvider;
import justjabka.WeltenRaces.Listeners.Generic.BaseRaceListener;
import org.bukkit.NamespacedKey;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
import java.util.Set;

public class LizardRaceListener extends BaseRaceListener {
    private final LizardRaceConfig config;

    private static final Collection<DamageType> lizardVulnerableTo = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.LIZARD_VULNERABLE_TO);
    private static final Set<EntityDamageEvent.DamageCause> lizardResistantTo = Set.of(
            EntityDamageEvent.DamageCause.POISON
    );

    public LizardRaceListener(LizardRaceConfig config) {
        this.config = config;
    }

    @Override
    public NamespacedKey getRaceKey() {
        return RaceProvider.LIZARD;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        if (!isRequiredRace(victim)) return;

        double damage = event.getDamage();
        DamageType damageType = event.getDamageSource().getDamageType();
        EntityDamageEvent.DamageCause damageCause = event.getCause();

        handleDamageCauses(event, damageType, damage, damageCause);
    }

    private void handleDamageCauses(EntityDamageEvent event, DamageType damageType, double damage, EntityDamageEvent.DamageCause damageCause) {
        if (lizardVulnerableTo.contains(damageType)) {
            event.setDamage(damage * config.vulnerableDamageMultiplier);
        } else if (lizardResistantTo.contains(damageCause)) {
            event.setDamage(damage * config.resistantDamageMultiplier);
        }
    }
}