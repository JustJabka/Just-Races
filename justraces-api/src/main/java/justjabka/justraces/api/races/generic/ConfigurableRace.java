package justjabka.justraces.api.races.generic;

import justjabka.justraces.api.common.configurable.Configurable;
import justjabka.justraces.api.managers.RaceManager;
import justjabka.justraces.api.races.RaceDefinition;
import org.jspecify.annotations.NonNull;
import org.spongepowered.configurate.ConfigurationNode;

public interface ConfigurableRace extends Configurable {
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
