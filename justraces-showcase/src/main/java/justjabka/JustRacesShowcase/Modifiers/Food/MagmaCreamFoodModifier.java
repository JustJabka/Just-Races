package justjabka.JustRacesShowcase.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.JustRacesShowcase.Abilities.FrogTongueAbility;
import justjabka.JustRacesShowcase.JustRacesShowcase;
import justjabka.JustRacesShowcase.Modifiers.Food.Generic.BaseFrogTongueTypeChangerFoodModifier;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public class MagmaCreamFoodModifier extends BaseFrogTongueTypeChangerFoodModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "magma_cream_food");
    }

    @Override
    public FrogTongueAbility.TongueType getFrogTongueType() {
        return FrogTongueAbility.TongueType.MAGMA;
    }

    @Override
    public Consumable getConsumable() {
        return Consumable.consumable()
                .animation(ItemUseAnimation.EAT)
                .consumeSeconds(0.8f)
                .hasConsumeParticles(true)
                .build();
    }

    @Override
    public FoodProperties getFoodProperties() {
        return FoodProperties.food()
                .nutrition(3)
                .canAlwaysEat(true)
                .build();
    }

    @Override
    public @Nullable UseCooldown getUseCooldown() {
        return null;
    }
}
