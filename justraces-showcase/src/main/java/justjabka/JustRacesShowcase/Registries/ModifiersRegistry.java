package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Modifiers.ItemModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Modifiers.Armor.*;
import justjabka.JustRacesShowcase.Modifiers.Food.MagmaCreamFoodModifier;
import justjabka.JustRacesShowcase.Modifiers.Food.SlimeBallFoodModifier;

public class ModifiersRegistry {
    private static void registerModifiers() {
        // Armor
        registerModifier(new LeatherArmorModifier());
        registerModifier(new CopperArmorModifier());
        registerModifier(new ChainmailArmorModifier());
        registerModifier(new IronArmorModifier());
        registerModifier(new GoldenArmorModifier());

        // Food
        registerModifier(new SlimeBallFoodModifier());
        registerModifier(new MagmaCreamFoodModifier());
    }

    public static void register() {
        registerModifiers();

        JustRacesShowcase.LOGGER.info("Successfully registered {} item modifiers!", JustRacesRegistries.MODIFIERS.keys().size());
    }

    private static void registerModifier(ItemModifier  modifier) {
        JustRacesRegistries.MODIFIERS.register(modifier.getKey(), modifier);
    }
}
