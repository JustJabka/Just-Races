package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Traits.*;

public class TraitsRegistry {
    private static void registerTraits() {
        registerTrait(new MagicVulnerabilityTrait());
        registerTrait(new AdrenalineRushTrait());
        registerTrait(new AdaptationTrait());
        registerTrait(new CelestialCombatantTrait());
        registerTrait(new BoundShellTrait());
    }

    public static void register() {
        registerTraits();

        JustRacesShowcase.LOGGER.info("Successfully registered {} traits!", JustRacesRegistries.TRAITS.keys().size());
    }

    private static void registerTrait(Trait trait) {
        JustRacesRegistries.TRAITS.register(trait.getKey(), trait);
    }
}
