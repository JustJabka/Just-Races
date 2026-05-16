package justjabka.WeltenRaces.Listeners;

import justjabka.WeltenRaces.Configs.Race.LizardRaceConfig;
import justjabka.WeltenRaces.DataProvider.DamageTypeTagKeysProvider;
import justjabka.WeltenRaces.Managers.RaceManager;
import justjabka.WeltenRaces.Types.Race;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Collection;
import java.util.Set;

public class LizardRaceListener implements Listener {
    private final LizardRaceConfig config;

    private static final Collection<DamageType> lizardVulnerableTo = DamageTypeTagKeysProvider.getTagValues(DamageTypeTagKeysProvider.LIZARD_VULNERABLE_TO);
    private static final Set<EntityDamageEvent.DamageCause> lizardResistantTo = Set.of(
            EntityDamageEvent.DamageCause.POISON
    );

    public LizardRaceListener(LizardRaceConfig config) {
        this.config = config;
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player victim)) return;

        if (RaceManager.getRace(victim) != Race.LIZARD) return;

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