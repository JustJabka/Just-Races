package justjabka.WeltenRaces.Modifiers.Contents.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("UnstableApiUsage")
public class BaseFoodModifier extends BaseModifier {
    final FoodProperties foodProperties;
    final Consumable consumable;

    public BaseFoodModifier(
            NamespacedKey key,
            FoodProperties foodProperties,
            Consumable consumable
    ) {
        super(key);
        this.foodProperties = foodProperties;
        this.consumable = consumable;
    }

    @Override
    public void apply(ItemStack item) {
        item.setData(DataComponentTypes.FOOD, foodProperties);
        item.setData(DataComponentTypes.CONSUMABLE, consumable);
    }

    @Override
    public void undo(ItemStack item) {
        item.resetData(DataComponentTypes.FOOD);
        item.resetData(DataComponentTypes.CONSUMABLE);
    }
}
