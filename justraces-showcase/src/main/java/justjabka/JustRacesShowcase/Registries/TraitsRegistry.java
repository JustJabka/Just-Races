package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRaces.Interfaces.Trait;
import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Listeners.Trait.MagicVulnerabilityTraitListener;

public class TraitsRegistry {
    private static void registerTraits() {
        registerTrait(new MagicVulnerabilityTraitListener());
    }

    public static void register() {
        registerTraits();

        JustRacesShowcase.LOGGER.info("Successfully registered {} traits!", JustRacesRegistries.TRAITS.keys().size());
    }

    private static void registerTrait(Trait trait) {
        JustRacesRegistries.TRAITS.register(trait.getKey(), trait);
    }
}
