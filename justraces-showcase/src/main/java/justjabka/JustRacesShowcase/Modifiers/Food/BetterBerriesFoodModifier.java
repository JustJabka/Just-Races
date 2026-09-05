package justjabka.JustRacesShowcase.Modifiers.Food;

import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import justjabka.JustRaces.Interfaces.Configurable.ItemModifierConfigurable;
import justjabka.JustRaces.Modifiers.Generic.BaseFoodModifier;
import justjabka.JustRacesShowcase.JustRacesShowcase;
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
