package justjabka.justraces.api.abilities.generic;

import com.google.gson.JsonParseException;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.abilities.AbilityTriggerCondition;
import justjabka.justraces.api.common.configurable.PluginConfigurable;

import java.util.*;

public interface ConfigurableAbility extends PluginConfigurable {

    default long getConfigCooldown() {
        return getConfigLong("cooldown");
    }

    default long getConfigDuration() {
        return getConfigLong("duration");
    }

    default AbilityTrigger getConfigTrigger() {
        final String triggerStr = getConfigString("trigger");
        if (triggerStr == null || triggerStr.isEmpty()) {
            return AbilityTrigger.CUSTOM;
        }

        try {
            return AbilityTrigger.valueOf(triggerStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown Ability Trigger %s".formatted(triggerStr), e);
        }
    }

    default Set<AbilityTriggerCondition> getConfigConditions() {
        List<String> conditionsStr = getConfigList(String.class, "conditions");
        if (conditionsStr == null || conditionsStr.isEmpty()) {
            return Collections.emptySet();
        }

        Set<AbilityTriggerCondition> conditions = EnumSet.noneOf(AbilityTriggerCondition.class);

        for (String element : conditionsStr) {
            if (element == null || element.isBlank()) continue;

            try {
                conditions.add(AbilityTriggerCondition.valueOf(element.toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Unknown Ability Trigger Condition %s".formatted(element), e);
            }
        }

        return Collections.unmodifiableSet(conditions);
    }

    @Override
    default Category category() {
        return Category.ABILITIES;
    }
}
