package justjabka.justraces.api.modifiers.generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class BaseFoodModifier extends BaseModifier {
    public abstract @Nullable Consumer<FoodProperties.Builder> getFoodProperties();
    public abstract @Nullable Consumer<Consumable.Builder> getConsumable();
    public abstract @Nullable UseCooldown getUseCooldown();

    @Override
    public void apply(ItemStack item) {
        if (getFoodProperties() != null) mergeComponent(item, DataComponentTypes.FOOD, getFoodProperties(), FoodProperties.food());
        if (getConsumable() != null) mergeComponent(item, DataComponentTypes.CONSUMABLE, getConsumable(), Consumable.consumable());
        if (getUseCooldown() != null) item.setData(DataComponentTypes.USE_COOLDOWN, getUseCooldown());
    }

    @Override
    public void undo(ItemStack item) {
        if (getFoodProperties() != null) item.resetData(DataComponentTypes.FOOD);
        if (getConsumable() != null) item.resetData(DataComponentTypes.CONSUMABLE);
        if (getUseCooldown() != null) item.resetData(DataComponentTypes.USE_COOLDOWN);
    }
}
