package justjabka.justraces.showcase.registries;

import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.modifiers.generic.BaseModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.modifiers.armor.*;
import justjabka.justraces.showcase.modifiers.food.*;

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
        registerModifier(new HoneyBottleFoodModifier());
        registerModifier(new BetterBerriesFoodModifier());
    }

    public static void register() {
        registerModifiers();

        JustRacesShowcase.LOGGER.info("Successfully registered {} item modifiers!", JustRacesRegistries.MODIFIERS.keys().size());
    }

    private static void registerModifier(BaseModifier modifier) {
        JustRacesRegistries.MODIFIERS.register(modifier.getKey(), modifier);
    }
}
