package justjabka.justraces.api.abilities.generic;

import com.google.gson.JsonParseException;
import justjabka.justraces.api.abilities.AbilityTrigger;
import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableAbility extends PluginConfigurable {

    default long getConfigCooldown() {
        return getConfigLong("cooldown");
    }

    default long getConfigDuration() {
        return getConfigLong("duration");
    }

    default AbilityTrigger getConfigTrigger() {
        final String triggerStr = getConfigString("trigger");

        try {
            return AbilityTrigger.valueOf(triggerStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new JsonParseException("Unknown Ability Trigger %s".formatted(triggerStr), e);
        }
    }

    @Override
    default Category getCategory() {
        return Category.ABILITIES;
    }
}
