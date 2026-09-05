package justjabka.JustRaces.Types;

import org.bukkit.entity.LivingEntity;

import java.util.Optional;

public record AbilityContext(
        Optional<LivingEntity> attacker,
        Optional<LivingEntity> victim
) {
    public static AbilityContext ofEmpty() {
        return new AbilityContext(Optional.empty(), Optional.empty());
    }

    public static AbilityContext ofVictim(LivingEntity victim) {
        return new AbilityContext(Optional.empty(), Optional.of(victim));
    }

    public static AbilityContext ofAttacker(LivingEntity attacker) {
        return new AbilityContext(Optional.of(attacker), Optional.empty());
    }
}
