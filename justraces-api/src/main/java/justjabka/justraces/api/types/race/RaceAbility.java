package justjabka.justraces.api.types.race;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.types.Trigger;
import justjabka.justraces.api.types.TriggerCondition;

import java.util.Set;

public record RaceAbility(BaseAbility ability, Trigger trigger, Set<TriggerCondition> conditions) {
    public static RaceAbility ofDefault(BaseAbility ability) {
        return new RaceAbility(
                ability,
                ability.getDefaultTrigger(),
                ability.getDefaultTriggerConditions()
        );
    }
}
