package justjabka.justraces.api.types;

import org.bukkit.entity.LivingEntity;

import java.util.Optional;

/**
 * Ability Context is <b>additional</b> context for the ability
 * that can be provided in the {@code BaseAbility#tryActivate(Player, AbilityContext)}
 * and used in {@code BaseAbility#onActivation(Player player, AbilityContext ctx)}
 */
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
