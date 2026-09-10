package justjabka.justraces.core.registries.impl;

import justjabka.justraces.api.interfaces.ReloadableRegistry;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.interfaces.configurable.generic.PluginConfigurable;

public class TraitsRegistry extends BaseRegistry<Trait> implements ReloadableRegistry {

    @Override
    public void reload() {
        storage.forEach((_, trait) -> {
            if (!(trait instanceof PluginConfigurable configurable)) return;
            configurable.reloadConfigFile();
        });
    }
}
