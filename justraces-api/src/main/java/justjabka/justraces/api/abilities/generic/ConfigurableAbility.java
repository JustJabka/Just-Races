package justjabka.justraces.api.abilities.generic;

import justjabka.justraces.api.common.configurable.PluginConfigurable;

public interface ConfigurableAbility extends PluginConfigurable {

    default long getConfigCooldown() {
        return getConfigLong("cooldown");
    }

    default long getConfigDuration() {
        return getConfigLong("duration");
    }

    @Override
    default Category getCategory() {
        return Category.ABILITIES;
    }
}
