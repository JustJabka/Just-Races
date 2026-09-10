package justjabka.justraces.core.registries.impl;

import justjabka.justraces.api.interfaces.ReloadableRegistry;
import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;
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
