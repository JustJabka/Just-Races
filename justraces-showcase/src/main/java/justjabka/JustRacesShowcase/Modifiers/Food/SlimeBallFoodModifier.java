package justjabka.JustRacesShowcase.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.JustRacesShowcase.Abilities.FrogTongueAbility;
import justjabka.JustRacesShowcase.Modifiers.Food.Generic.BaseFrogTongueTypeChangerFoodModifier;
import org.bukkit.NamespacedKey;

@SuppressWarnings("UnstableApiUsage")
public class SlimeBallFoodModifier extends BaseFrogTongueTypeChangerFoodModifier {
    public SlimeBallFoodModifier(NamespacedKey key) {
        super(
                key,
                FoodProperties.food()
                        .nutrition(3)
                        .canAlwaysEat(true)
                        .build(),
                Consumable.consumable()
                        .animation(ItemUseAnimation.EAT)
                        .consumeSeconds(0.8f)
                        .hasConsumeParticles(true)
                        .build(),
                FrogTongueAbility.TongueType.SLIME
        );
    }
}
