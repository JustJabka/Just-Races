package justjabka.JustRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.JustRaces.Configs.Modifier.Food.GlowBerriesFoodModifierConfig;
import justjabka.JustRaces.Modifiers.Contents.Generic.BaseFoodModifier;
import org.bukkit.NamespacedKey;

@SuppressWarnings("UnstableApiUsage")
public class GlowBerriesFoodModifier extends BaseFoodModifier {
    public GlowBerriesFoodModifier(NamespacedKey key, GlowBerriesFoodModifierConfig config) {
        super(
                key,
                FoodProperties.food()
                        .nutrition(config.nutritionAmount)
                        .saturation(config.saturationAmount)
                        .build(),

                Consumable.consumable().build()
        );
    }
}
