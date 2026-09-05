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

import java.util.function.Consumer;

public class SlimeBallFoodModifier extends BaseFrogTongueTypeChangerFoodModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/slime_ball");
    }

    @Override
    public FrogTongueAbility.TongueType getFrogTongueType() {
        return FrogTongueAbility.TongueType.SLIME;
    }

    @Override
    public Consumer<Consumable.Builder> getConsumable() {
        return builder -> builder
                .animation(ItemUseAnimation.EAT)
                .consumeSeconds(0.8f)
                .hasConsumeParticles(true);
    }

    @Override
    public Consumer<FoodProperties.Builder> getFoodProperties() {
        return builder -> builder
                .nutrition(3)
                .canAlwaysEat(true);
    }

    @Override
    public @Nullable UseCooldown getUseCooldown() {
        return null;
    }
}
