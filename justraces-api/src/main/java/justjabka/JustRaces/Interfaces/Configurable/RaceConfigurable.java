package justjabka.JustRaces.Interfaces.Configurable;

import justjabka.JustRaces.Definitions.RaceDefinition;
import justjabka.JustRaces.Interfaces.Configurable.Generic.Configurable;
import justjabka.JustRaces.Managers.RaceManager;
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
        return RaceManager.getRaceByKey(getKey());
    }
}
