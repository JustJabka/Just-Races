package justjabka.JustRaces.Modifiers.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class BaseFoodCooldownModifier extends BaseFoodModifier {
    private final UseCooldown useCooldown;

    public BaseFoodCooldownModifier(NamespacedKey key, FoodProperties foodProperties, Consumable consumable, UseCooldown useCooldown) {
        super(key, foodProperties, consumable);
        this.useCooldown = useCooldown;
    }

    @Override
    public void apply(ItemStack item) {
        item.setData(DataComponentTypes.FOOD, foodProperties);
        item.setData(DataComponentTypes.CONSUMABLE, consumable);
        item.setData(DataComponentTypes.USE_COOLDOWN, useCooldown);
    }

    @Override
    public void undo(ItemStack item) {
        item.resetData(DataComponentTypes.FOOD);
        item.resetData(DataComponentTypes.CONSUMABLE);
        item.resetData(DataComponentTypes.USE_COOLDOWN);
    }
}
