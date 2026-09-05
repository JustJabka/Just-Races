package justjabka.JustRaces.Modifiers.Generic;

import io.papermc.paper.datacomponent.BuildableDataComponent;
import io.papermc.paper.datacomponent.DataComponentBuilder;
import io.papermc.paper.datacomponent.DataComponentType;
import justjabka.JustRaces.Managers.ModifierManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public abstract class BaseModifier {
    public abstract NamespacedKey getKey();
    public abstract void apply(ItemStack item);
    public abstract void undo(ItemStack item);

    public boolean isRequiredModifier(ItemStack item) {
        return ModifierManager.isModifiedWith(item, this);
    }

    /**
     * Merges component without fully overwriting it
     * @param item Item whose component will be merged
     * @param type Type of the component
     * @param builderModifier Data that will be merged
     * @param fallback Fallback in case item doesn't have that component
     * @param <T> Component
     * @param <B> Component builder
     */
    @SuppressWarnings("NonExtendableApiUsage")
    public <T extends BuildableDataComponent<T, B>, B extends DataComponentBuilder<T>> void mergeComponent(
            ItemStack item,
            DataComponentType.Valued<T> type,
            Consumer<B> builderModifier,
            B fallback
    ) {
        T current = item.getData(type);
        T defaultData = item.getType().getDefaultData(type);

        B builder = (current != null) ? current.toBuilder()
                : (defaultData != null) ? defaultData.toBuilder() : fallback;

        builderModifier.accept(builder);

        item.setData(type, builder.build());
    }
}
