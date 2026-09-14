package justjabka.justraces.showcase.registries;

import justjabka.justraces.api.JustRacesRegistries;
import justjabka.justraces.api.itemmodifiers.generic.BaseItemModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.modifiers.armor.*;
import justjabka.justraces.showcase.modifiers.food.*;

public class ItemModifiersRegistry {
    private static void registerModifiers() {
        // Armor
        registerModifier(new LeatherArmorItemModifier());
        registerModifier(new CopperArmorItemModifier());
        registerModifier(new ChainmailArmorItemModifier());
        registerModifier(new IronArmorItemModifier());
        registerModifier(new GoldenArmorItemModifier());

        // Food
        registerModifier(new SlimeBallFoodItemModifier());
        registerModifier(new MagmaCreamFoodItemModifier());
        registerModifier(new HoneyBottleFoodItemModifier());
        registerModifier(new BetterBerriesFoodItemModifier());
    }

    public static void register() {
        registerModifiers();

        JustRacesShowcase.LOGGER.info("Successfully registered {} item modifiers!", JustRacesRegistries.ITEM_MODIFIERS.keys().size());
    }

    private static void registerModifier(BaseItemModifier modifier) {
        JustRacesRegistries.ITEM_MODIFIERS.register(modifier.getKey(), modifier);
    }
}
