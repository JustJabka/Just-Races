package justjabka.justraces.core.registries.impl;

import justjabka.justraces.api.common.registry.ReloadableRegistry;
import justjabka.justraces.api.common.configurable.PluginConfigurable;
import justjabka.justraces.api.modifiers.generic.BaseModifier;

public class ModifiersRegistry extends BaseRegistry<BaseModifier> implements ReloadableRegistry {

    @Override
    public void reload() {
        storage.forEach((_, modifier) -> {
            if (!(modifier instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });
    }
}
