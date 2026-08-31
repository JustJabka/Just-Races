package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRaces.Abilities.Generic.BaseAbility;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRacesShowcase.Abilities.*;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import org.jetbrains.annotations.NotNull;

public class AbilitiesRegistry {
    private static void registerAbilities() {
        registerAbility(new DamageInversionAbility());
        registerAbility(new EcdysisAbility());
        registerAbility(new FrogTongueAbility());
        registerAbility(new SlimeTrailAbility());
        registerAbility(new AirBurstAbility());
    }

    public static void register() {
        registerAbilities();

        JustRacesShowcase.LOGGER.info("Successfully registered {} abilities!", JustRacesRegistries.ABILITIES.keys().size());
    }

    private static void registerAbility(@NotNull BaseAbility ability) {
        JustRacesRegistries.ABILITIES.register(ability.getKey(), ability);
    }
}
