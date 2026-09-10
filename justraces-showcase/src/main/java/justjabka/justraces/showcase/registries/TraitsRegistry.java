package justjabka.justraces.showcase.registries;

import justjabka.justraces.api.interfaces.Trait;
import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.traits.*;

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
