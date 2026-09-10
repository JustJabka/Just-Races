package justjabka.justraces.showcase.registries;

import justjabka.justraces.api.abilities.generic.BaseAbility;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.showcase.abilities.*;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.jetbrains.annotations.NotNull;

public class AbilitiesRegistry {
    private static void registerAbilities() {
        registerAbility(new DamageInversionAbility());
        registerAbility(new EcdysisAbility());
        registerAbility(new FrogTongueAbility());
        registerAbility(new SlimeTrailAbility());
        registerAbility(new AirBurstAbility());
        registerAbility(new PoisonousStingAbility());
        registerAbility(new DeepPocketsAbility());
    }

    public static void register() {
        registerAbilities();

        JustRacesShowcase.LOGGER.info("Successfully registered {} abilities!", JustRacesRegistries.ABILITIES.keys().size());
    }

    private static void registerAbility(@NotNull BaseAbility ability) {
        JustRacesRegistries.ABILITIES.register(ability.getKey(), ability);
    }
}
