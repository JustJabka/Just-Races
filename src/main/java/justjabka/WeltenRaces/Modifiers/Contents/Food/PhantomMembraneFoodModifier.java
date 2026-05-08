package justjabka.WeltenRaces.Modifiers.Contents.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;

@SuppressWarnings("UnstableApiUsage")
public class PhantomMembraneFoodModifier extends BaseFoodModifier {
    public PhantomMembraneFoodModifier(String id) {
        super(
                id,
                FoodProperties.food()
                        .canAlwaysEat(true)
                        .nutrition(0)
                        .saturation(0)
                        .build(),

                Consumable.consumable()
                        .consumeSeconds(0.8f)
                        .animation(ItemUseAnimation.EAT)
                        .hasConsumeParticles(true)
                        .build()
        );
    }
}
