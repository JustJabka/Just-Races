package justjabka.JustRaces.Types;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;

import java.util.Set;

public record AbilityBinding(BaseAbility ability, Trigger trigger, Set<TriggerCondition> conditions) {
    public static AbilityBinding ofDefault(BaseAbility ability) {
        return new AbilityBinding(
                ability,
                ability.getDefaultTrigger(),
                ability.getDefaultTriggerConditions()
        );
    }
}
