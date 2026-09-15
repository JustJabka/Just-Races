package justjabka.justraces.api;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.common.definition.RaceDefinition;
import justjabka.justraces.api.common.registry.Registry;
import justjabka.justraces.api.traits.generic.Trait;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
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
            Registry<BaseItemModifier> itemModifiers
            ) {
        INSTANCE = plugin;
        LOGGER = logger;

        JustRacesRegistries.RACES = races;
        JustRacesRegistries.ABILITIES = abilities;
        JustRacesRegistries.TRAITS = traits;
        JustRacesRegistries.ITEM_MODIFIERS = itemModifiers;
    }
}
