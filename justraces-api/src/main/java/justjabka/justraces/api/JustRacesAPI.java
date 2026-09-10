package justjabka.justraces.api;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.definitions.RaceDefinition;
import justjabka.justraces.api.interfaces.Registry;
import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import org.bukkit.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JustRacesAPI {
    private static Plugin INSTANCE;
    public static final String NAMESPACE = "justraces";
    private static Logger LOGGER = LoggerFactory.getLogger(NAMESPACE);

    public static Plugin getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("JustRaces API is not initialized!");
        }
        return INSTANCE;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void init(
            Plugin plugin,
            Logger logger,
            Registry<RaceDefinition> races,
            Registry<BaseAbility> abilities,
            Registry<Trait> traits,
            Registry<BaseModifier> modifiers
            ) {
        INSTANCE = plugin;
        LOGGER = logger;

        JustRacesRegistries.RACES = races;
        JustRacesRegistries.ABILITIES = abilities;
        JustRacesRegistries.TRAITS = traits;
        JustRacesRegistries.MODIFIERS = modifiers;
    }
}
