package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodCooldownModifier;

@SuppressWarnings("UnstableApiUsage")
public class MossFoodModifier extends BaseFoodCooldownModifier {
    public MossFoodModifier(String id) {
        super(
                id,
                FoodProperties.food()
                        .nutrition(0)
                        .saturation(0)
                        .canAlwaysEat(true)
                        .build(),

                Consumable.consumable().build(),
                UseCooldown.useCooldown(60).build()
        );
    }
}
