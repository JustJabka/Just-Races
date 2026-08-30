package justjabka.JustRaces.Modifiers.Generic;

import justjabka.JustRaces.Managers.ModifierManager;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

public abstract class BaseModifier {
    public abstract NamespacedKey getKey();
    public abstract void apply(ItemStack item);
    public abstract void undo(ItemStack item);

    public boolean isRequiredModifier(ItemStack item) {
        return ModifierManager.isModifiedWith(item, this);
    }
}
