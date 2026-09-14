package justjabka.justraces.api.common.entry;

import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import justjabka.justraces.api.abilities.generic.BaseAbility;

import java.util.Set;

public record AbilityEntry(BaseAbility ability, AbilityTrigger trigger, Set<AbilityTriggerCondition> conditions) {
    public static AbilityEntry ofDefault(BaseAbility ability) {
        return new AbilityEntry(
                ability,
                ability.getDefaultTrigger(),
                ability.getDefaultTriggerConditions()
        );
    }
}
