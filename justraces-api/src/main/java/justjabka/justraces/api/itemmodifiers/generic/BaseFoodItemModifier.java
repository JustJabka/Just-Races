package justjabka.justraces.api.itemmodifiers.generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public abstract class BaseFoodItemModifier extends BaseItemModifier {
    public abstract @Nullable Consumer<FoodProperties.Builder> foodProperties();
    public abstract @Nullable Consumer<Consumable.Builder> consumable();
    public abstract @Nullable UseCooldown useCooldown();

    @Override
    public void apply(ItemStack item) {
        Consumer<FoodProperties.Builder> builderModifier = foodProperties();
        if (builderModifier != null) mergeComponent(item, DataComponentTypes.FOOD, builderModifier, FoodProperties.food());

        Consumer<Consumable.Builder> consumable = consumable();
        if (consumable != null) mergeComponent(item, DataComponentTypes.CONSUMABLE, consumable, Consumable.consumable());

        UseCooldown useCooldown = useCooldown();
        if (useCooldown != null) item.setData(DataComponentTypes.USE_COOLDOWN, useCooldown);
    }

    @Override
    public void undo(ItemStack item) {
        if (foodProperties() != null) item.resetData(DataComponentTypes.FOOD);
        if (consumable() != null) item.resetData(DataComponentTypes.CONSUMABLE);
        if (useCooldown() != null) item.resetData(DataComponentTypes.USE_COOLDOWN);
    }
}
