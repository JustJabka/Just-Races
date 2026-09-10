package justjabka.justraces.showcase.modifiers.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import justjabka.justraces.showcase.abilities.FrogTongueAbility;
import justjabka.justraces.showcase.JustRacesShowcase;
import justjabka.justraces.showcase.modifiers.food.Generic.BaseFrogTongueTypeChangerFoodModifier;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class MagmaCreamFoodModifier extends BaseFrogTongueTypeChangerFoodModifier {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/magma_cream");
    }

    @Override
    public FrogTongueAbility.TongueType getFrogTongueType() {
        return FrogTongueAbility.TongueType.MAGMA;
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
