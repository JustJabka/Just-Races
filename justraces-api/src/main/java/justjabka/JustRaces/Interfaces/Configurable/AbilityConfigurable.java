package justjabka.JustRaces.Interfaces.Configurable;

import justjabka.JustRaces.Interfaces.Configurable.Generic.PluginConfigurable;

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
