package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.WeltenRaces.Configs.Modifier.Food.GlowBerriesFoodModifierConfig;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;

@SuppressWarnings("UnstableApiUsage")
public class GlowBerriesFoodModifier extends BaseFoodModifier {
    public GlowBerriesFoodModifier(String id, GlowBerriesFoodModifierConfig config) {
        super(
                id,
                FoodProperties.food()
                        .nutrition(config.nutritionAmount)
                        .saturation(config.saturationAmount)
                        .build(),

                Consumable.consumable().build()
        );
    }
}
