package justjabka.JustRacesShowcase.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.JustRacesShowcase.Configs.Modifier.Food.SweetBerriesFoodModifierConfig;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodModifier;
import org.bukkit.NamespacedKey;

@SuppressWarnings("UnstableApiUsage")
public class SweetBerriesFoodModifier extends BaseFoodModifier {
    public SweetBerriesFoodModifier(NamespacedKey key, SweetBerriesFoodModifierConfig config) {
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
