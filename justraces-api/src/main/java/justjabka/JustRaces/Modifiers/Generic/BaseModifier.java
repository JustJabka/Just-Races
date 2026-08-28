package justjabka.JustRaces.Modifiers.Generic;

import justjabka.JustRaces.Managers.ModifierManager;
import justjabka.JustRaces.Modifiers.ItemModifier;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class BaseModifier implements ItemModifier {
    private final NamespacedKey key;

    public BaseModifier(NamespacedKey key) {
        this.key = key;
    }

    @Override
    public NamespacedKey getKey() {
        return key;
    }

    public boolean isRequiredModifier(ItemStack item) {
        return ModifierManager.isModifiedWith(item, this);
    }

    @Override
    public abstract void apply(ItemStack item);

    @Override
    public abstract void undo(ItemStack item);
}
