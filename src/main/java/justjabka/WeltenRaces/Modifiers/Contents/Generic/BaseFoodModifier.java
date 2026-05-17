package justjabka.WeltenRaces.Modifiers.Contents.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import justjabka.WeltenRaces.Modifiers.ItemModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.WeltenRaces.Managers.ModifierManager.ITEM_MODIFIED_KEY;

@SuppressWarnings("UnstableApiUsage")
public class BaseFoodModifier implements ItemModifier {
    final NamespacedKey key;
    final FoodProperties foodProperties;
    final Consumable consumable;

    public BaseFoodModifier(
            NamespacedKey key,
            FoodProperties foodProperties,
            Consumable consumable
    ) {
        this.key = key;
        this.foodProperties = foodProperties;
        this.consumable = consumable;
    }

    @Override
    public void apply(ItemStack item) {
        // Apply Components
        item.setData(DataComponentTypes.FOOD, foodProperties);
        item.setData(DataComponentTypes.CONSUMABLE, consumable);

        // Add Marker
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, key.toString())
        );
    }

    @Override
    public void undo(ItemStack item) {
        item.resetData(DataComponentTypes.FOOD);
        item.resetData(DataComponentTypes.CONSUMABLE);

        // Remove Marker
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }
}
