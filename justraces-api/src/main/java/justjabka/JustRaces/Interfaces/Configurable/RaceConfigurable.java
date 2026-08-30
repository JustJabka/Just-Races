package justjabka.JustRaces.Interfaces.Configurable;

import justjabka.JustRaces.Interfaces.Configurable.Generic.Configurable;
import justjabka.JustRaces.Managers.RaceManager;
import org.spongepowered.configurate.ConfigurationNode;

public interface RaceConfigurable extends Configurable {
    @Override
    default ConfigurationNode getConfigNode() {
        return RaceManager.getRaceByKey(getKey()).getConfig();
    }
}
