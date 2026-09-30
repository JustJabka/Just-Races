package justjabka.justraces.showcase.modifiers.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.justraces.api.itemmodifiers.generic.ConfigurableItemModifier;
import justjabka.justraces.api.itemmodifiers.generic.BaseFoodItemModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.jspecify.annotations.Nullable;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class BetterBerriesFoodItemModifier extends BaseFoodItemModifier implements ConfigurableItemModifier {

    @Override
    public @NonNull NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/better_berries");
    }

    @Override
    public @Nullable Consumer<FoodProperties.Builder> foodProperties() {
        return builder -> builder
                .nutrition(getConfigInt("nutrition"))
                .saturation(getConfigFloat("saturation"));
    }

    @Override
    public @Nullable Consumer<Consumable.Builder> consumable() {
        return null;
    }

    @Override
    public @Nullable UseCooldown useCooldown() {
        return null;
    }
}
