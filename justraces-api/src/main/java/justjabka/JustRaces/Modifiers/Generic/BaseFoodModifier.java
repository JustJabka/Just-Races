package justjabka.JustRaces.Modifiers.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseFoodModifier extends BaseModifier {
    public abstract Consumable getConsumable();
    public abstract FoodProperties getFoodProperties();
    public abstract @Nullable UseCooldown getUseCooldown();

    @Override
    public void apply(ItemStack item) {
        item.setData(DataComponentTypes.FOOD, getFoodProperties());
        item.setData(DataComponentTypes.CONSUMABLE, getConsumable());
        if (getUseCooldown() != null) item.setData(DataComponentTypes.USE_COOLDOWN, getUseCooldown());
    }

    @Override
    public void undo(ItemStack item) {
        item.resetData(DataComponentTypes.FOOD);
        item.resetData(DataComponentTypes.CONSUMABLE);
        if (getUseCooldown() != null) item.resetData(DataComponentTypes.USE_COOLDOWN);
    }
}
