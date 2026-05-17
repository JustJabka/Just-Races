package justjabka.WeltenRaces.Modifiers.Contents.Generic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.Consumable;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.UseCooldown;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import static justjabka.WeltenRaces.Managers.ModifierManager.ITEM_MODIFIED_KEY;

@SuppressWarnings("UnstableApiUsage")
public class BaseFoodCooldownModifier extends BaseFoodModifier {
    private final UseCooldown useCooldown;

    public BaseFoodCooldownModifier(NamespacedKey key, FoodProperties foodProperties, Consumable consumable, UseCooldown useCooldown) {
        super(key, foodProperties, consumable);
        this.useCooldown = useCooldown;
    }

    @Override
    public void apply(ItemStack item) {
        // Apply Components
        item.setData(DataComponentTypes.FOOD, foodProperties);
        item.setData(DataComponentTypes.CONSUMABLE, consumable);
        item.setData(DataComponentTypes.USE_COOLDOWN, useCooldown);

        // Add Marker
        item.editPersistentDataContainer(pdc ->
                pdc.set(ITEM_MODIFIED_KEY, PersistentDataType.STRING, key.toString())
        );
    }

    @Override
    public void undo(ItemStack item) {
        item.resetData(DataComponentTypes.FOOD);
        item.resetData(DataComponentTypes.CONSUMABLE);
        item.resetData(DataComponentTypes.USE_COOLDOWN);

        // Remove Marker
        item.editPersistentDataContainer(pdc ->
                pdc.remove(ITEM_MODIFIED_KEY)
        );
    }
}
