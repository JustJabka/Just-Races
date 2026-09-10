package justjabka.justraces.api.interfaces.configurable;

import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;

public interface AbilityConfigurable extends PluginConfigurable {

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
