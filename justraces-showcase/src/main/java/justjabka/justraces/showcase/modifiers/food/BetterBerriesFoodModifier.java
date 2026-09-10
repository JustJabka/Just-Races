package justjabka.justraces.showcase.modifiers.food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.justraces.api.interfaces.configurable.ItemModifierConfigurable;
import justjabka.justraces.api.modifiers.generic.BaseFoodModifier;
import justjabka.justraces.showcase.JustRacesShowcase;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BetterBerriesFoodModifier extends BaseFoodModifier implements ItemModifierConfigurable {

    @Override
    public NamespacedKey getKey() {
        return new NamespacedKey(JustRacesShowcase.NAMESPACE, "food/better_berries");
    }

    @Override
    public @Nullable Consumer<FoodProperties.Builder> getFoodProperties() {
        return builder -> builder
                .nutrition(getConfigInt("nutrition"))
                .saturation(getConfigFloat("saturation"));
    }

    @Override
    public @Nullable Consumer<Consumable.Builder> getConsumable() {
        return null;
    }

    @Override
    public @Nullable UseCooldown getUseCooldown() {
        return null;
    }
}
