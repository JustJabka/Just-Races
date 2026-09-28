package justjabka.justraces.showcase.modifiers.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.justraces.showcase.abilities.FrogTongueAbility;
import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.modifiers.food.generic.BaseFrogTongueTypeChangerFoodItemModifier;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class SlimeBallFoodItemModifier extends BaseFrogTongueTypeChangerFoodItemModifier {

    @Override
    public @NonNull NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/slime_ball");
    }

    @Override
    public FrogTongueAbility.TongueType getFrogTongueType() {
        return FrogTongueAbility.TongueType.SLIME;
    }

    @Override
    public Consumer<Consumable.Builder> consumable() {
        return builder -> builder
                .animation(ItemUseAnimation.EAT)
                .consumeSeconds(0.8f)
                .hasConsumeParticles(true);
    }

    @Override
    public Consumer<FoodProperties.Builder> foodProperties() {
        return builder -> builder
                .nutrition(3)
                .canAlwaysEat(true);
    }

    @Override
    public @Nullable UseCooldown useCooldown() {
        return null;
    }
}
