package justjabka.justraces.api.interfaces.configurable;

import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.interfaces.configurable.generic.Configurable;
import justjabka.justraces.api.managers.RaceManager;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationNode;

public interface RaceConfigurable extends Configurable {
    @Override
    default ConfigurationNode getConfigNode() {
        return getRaceByKey().getConfig();
    }

    @Override
    default void reloadConfigFile() {
        getRaceByKey().clearConfigCache();
    }

    private @NonNull RaceDefinition getRaceByKey() {
        return RaceManager.getByKey(getKey());
    }
}
