package justjabka.JustRacesShowcase.Registries;

import justjabka.JustRaces.JustRacesRegistries;
import justjabka.JustRaces.Modifiers.ItemModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Modifiers.Armor.*;
import justjabka.JustRacesShowcase.Modifiers.Food.MagmaCreamFoodModifier;
import justjabka.JustRacesShowcase.Modifiers.Food.SlimeBallFoodModifier;
import org.bukkit.NamespacedKey;

import java.util.List;

public class ModifiersRegistry {
    public static void register(ConfigRegistry configs) {
        registerModifiers(configs);

        JustRacesShowcase.LOGGER.info("Successfully registered {} item modifiers!", JustRacesRegistries.MODIFIERS.keys().size());
    }

    private static void registerModifiers(ConfigRegistry configs) {
        // List off all unique modificator
        List<ItemModifier> modifiers = List.of(
                new LeatherArmorModifier(create("leather_armor"), configs.leatherArmorModifierConfig),
                new CopperArmorModifier(create("copper_armor"), configs.copperArmorModifierConfig),
                new ChainmailArmorModifier(create("chainmail_armor"), configs.chainmailArmorModifierConfig),
                new IronArmorModifier(create("iron_armor"), configs.ironArmorModifierConfig),
                new GoldenArmorModifier(create("golden_armor"), configs.goldenArmorModifierConfig),

                new SlimeBallFoodModifier(create("slime_ball")),
                new MagmaCreamFoodModifier(create("magma_cream"))
        );

        // Register modifiers globally
        for (ItemModifier modifier : modifiers) {
            JustRacesRegistries.MODIFIERS.register(modifier.getKey(), modifier);
        }
    }

    private static NamespacedKey create(String key) {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, key);
    }
}
