package justjabka.justraces.api.types.entry;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.types.Trigger;
import justjabka.justraces.api.types.TriggerCondition;

import java.util.Set;

public record AbilityEntry(BaseAbility ability, Trigger trigger, Set<TriggerCondition> conditions) {
    public static AbilityEntry ofDefault(BaseAbility ability) {
        return new AbilityEntry(
                ability,
                ability.getDefaultTrigger(),
                ability.getDefaultTriggerConditions()
        );
    }
}
