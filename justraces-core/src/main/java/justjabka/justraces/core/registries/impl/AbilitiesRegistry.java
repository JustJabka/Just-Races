package justjabka.justraces.core.registries.impl;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.registry.ReloadableRegistry;
import justjabka.justraces.api.common.configurable.PluginConfigurable;

public class AbilitiesRegistry extends BaseRegistry<BaseAbility> implements ReloadableRegistry {

    @Override
    public void reload() {
        storage.forEach((_, ability) -> {
            if (!(ability instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });
    }
}
