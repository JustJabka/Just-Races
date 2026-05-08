package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;

@SuppressWarnings("UnstableApiUsage")
public class GlowBerriesFoodModifier extends BaseFoodModifier {
    public GlowBerriesFoodModifier(String id) {
        super(
                id,
                FoodProperties.food()
                        .nutrition(4 + 2)
                        .saturation(0.4f)
                        .build(),

                Consumable.consumable().build()
        );
    }
}
