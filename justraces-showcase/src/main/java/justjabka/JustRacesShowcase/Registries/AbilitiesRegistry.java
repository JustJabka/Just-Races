package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRacesShowcase.Abilities.*;
import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRaces.JustRacesRegistries;
import org.jetbrains.annotations.NotNull;

public class AbilitiesRegistry {
    private static void registerAbilities(ConfigRegistry configs) {
        registerAbility(new DamageInversionAbility(configs.damageInversionAbilityConfig));
        registerAbility(new EcdysisAbility(configs.ecdysisAbilityConfig));
        registerAbility(new FrogTongueAbility());
    }

    public static void register(ConfigRegistry configs) {
        registerAbilities(configs);

        JustRacesShowcase.LOGGER.info("Successfully registered {} abilities!", JustRacesRegistries.ABILITIES.keys().size());
    }

    private static void registerAbility(@NotNull BaseAbility ability) {
        JustRacesRegistries.ABILITIES.register(ability.getKey(), ability);
    }
}
