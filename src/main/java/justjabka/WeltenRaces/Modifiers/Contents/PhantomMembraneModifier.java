package justjabka.WeltenRaces.Modifiers.Contents;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.WeltenRaces.Modifiers.Contents.Generic.BaseFoodModifier;

@SuppressWarnings("UnstableApiUsage")
public class PhantomMembraneModifier extends BaseFoodModifier {
    public PhantomMembraneModifier(
            String id,
            FoodProperties foodProperties,
            Consumable consumable
    ) {
        super(id, foodProperties, consumable);
    }
}
